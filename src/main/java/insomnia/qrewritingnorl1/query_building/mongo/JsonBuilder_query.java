package insomnia.qrewritingnorl1.query_building.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import insomnia.json.Element;
import insomnia.json.ElementArray;
import insomnia.json.ElementLiteral;
import insomnia.json.ElementNumber;
import insomnia.json.ElementObject;
import insomnia.json.ElementString;
import insomnia.json.Json;
import insomnia.json.JsonBuilder;
import insomnia.json.JsonBuilderException;
import insomnia.qrewritingnorl1.node.Node;
import insomnia.qrewritingnorl1.node.NodeValue;
import insomnia.qrewritingnorl1.node.NodeValueExists;
import insomnia.qrewritingnorl1.node.NodeValueLiteral;
import insomnia.qrewritingnorl1.node.NodeValueNumber;
import insomnia.qrewritingnorl1.node.NodeValueString;
import insomnia.qrewritingnorl1.query_rewriting.query.Query;

class BuildResult
{
	public String	path;
	public Element	element;

	public BuildResult(String p, Element e)
	{
		path = p;
		element = e;
	}
}

public class JsonBuilder_query extends JsonBuilder
{
	public enum MODE
	{
		ELEMMATCH, DOT
	};

	private MODE mode = MODE.ELEMMATCH;

	public JsonBuilder_query()
	{

	}

	public JsonBuilder_query(Query q)
	{
		setQuery(q);
	}

	public void setQuery(Query q)
	{
		setData(q);
	}

	public Query getQuery()
	{
		return (Query) getData();
	}
	
	public void setMode (MODE mode)
	{
		this.mode = mode;
	}

	@Override
	public void build() throws JsonBuilderException
	{
		Query query = getQuery();

		if (!query.isUnfolded())
			throw new JsonBuilderException("La Query doit être dépliée");

		switch (mode)
		{
		case DOT:
			buildDOT();
			return;
		case ELEMMATCH:
			buildELEMMATCH();
			return;
		}
	}

	@Override
	public Json newBuild() throws JsonBuilderException
	{
		Json ret = new Json();
		setJson(ret);
		build();
		return ret;
	}

	// ===============================================================
	// EMATCH
	// ===============================================================

	private Element fromValue(NodeValue v) throws JsonBuilderException
	{
		Element new_e;

		if (v instanceof NodeValueExists)
		{
			new_e = new ElementLiteral(ElementLiteral.Literal.TRUE);
			ElementObject exists = new ElementObject();
			exists.getObject().put("$exists", new_e);
			new_e = exists;
		}
		else if (v.isString())
		{
			new_e = new ElementString(((NodeValueString) v).getString());
		}
		else if (v.isLiteral())
		{
			new_e = new ElementLiteral(((NodeValueLiteral) v).toString());
		}
		else if (v.isNumber())
		{
			new_e = new ElementNumber(((NodeValueNumber) v).getNumber());
		}
		else
		{
			throw new JsonBuilderException(
				"Query Element '" + v + "' non pris en charge");
		}
		return new_e;
	}

	public void buildELEMMATCH() throws JsonBuilderException
	{
		Query query = getQuery();
		Json doc = getJson();
		ElementObject root = new ElementObject();
		doc.setDocument(root);
		p_buildELEMMATCH(query.getRoot(), root);
	}

	private void p_buildELEMMATCH(Node n, ElementObject json_e)
			throws JsonBuilderException
	{
		HashMap<String, Element> map = json_e.getObject();
		boolean withEAnd = !n.getChilds().getMultipleChilds().isEmpty();
		ArrayList<Element> array = null;
		
		if (withEAnd)
		{
			ElementArray json_array = new ElementArray();
			map.put("$and", json_array);
			array = json_array.getArray();
		}

		for (Node child : n.getChilds().getChilds())
		{
			final int nbChilds = child.getChilds().size();
			NodeValue v = child.getValue();
			String label = child.getLabel().get();

			// Feuille
			if (nbChilds == 0)
			{
				Element val = fromValue(v);

				// Normalement impossible
				if (withEAnd)
				{
					ElementObject base = new ElementObject();
					base.getObject().put(label, val);
					array.add(base);
				}
				else
				{
					map.put(label, val);
				}
			}
			else
			{
				ElementObject ematch = new ElementObject();
				ElementObject new_e = new ElementObject();
				new_e.getObject().put("$elemMatch", ematch);

				if (withEAnd)
				{
					ElementObject base = new ElementObject();
					base.getObject().put(label, new_e);
					array.add(base);
				}
				else
				{
					map.put(label, new_e);
				}
				p_buildELEMMATCH(child, ematch);
			}
		}
	}

	// ===============================================================
	// DOT
	// ===============================================================

	public void buildDOT() throws JsonBuilderException
	{
		Query query = getQuery();
		ElementObject root = new ElementObject();
		Json doc = getJson();

		doc.setDocument(root);

		ArrayList<BuildResult> buildResults = new ArrayList<>();
		boolean withEAnd = false;

		p_prebuildDOT(query.getRoot(), "", buildResults);
		{
			HashSet<String> set = new HashSet();

			for (BuildResult res : buildResults)
			{
				String path = res.path;

				if (set.contains(path))
				{
					withEAnd = true;
					break;
				}
				set.add(path);
			}
		}

		// Des labels en commun existent
		if (withEAnd)
		{
			ElementArray eand = new ElementArray();
			ArrayList<Element> array = eand.getArray();
			root.getObject().put("$and", eand);

			for (BuildResult res : buildResults)
			{
				ElementObject eobj = new ElementObject();
				array.add(eobj);
				eobj.getObject().put(res.path, res.element);
			}
		}
		else
		{
			HashMap<String, Element> map = root.getObject();

			for (BuildResult res : buildResults)
			{
				map.put(res.path, res.element);
			}
		}
	}

	private void p_prebuildDOT(Node n, String label, ArrayList<BuildResult> ret)
			throws JsonBuilderException
	{
		String tlabel;

		for (Node child : n.getChilds().getChilds())
		{
			final int nbChilds = child.getChilds().size();
			String k = child.getLabel().get();
			NodeValue v = child.getValue();
			tlabel = label.isEmpty() ? k : label + "." + k;

			// Feuille
			if (nbChilds == 0)
			{
				Element new_e = fromValue(v);
				ret.add(new BuildResult(tlabel, new_e));
			}
			else
			{
				p_prebuildDOT(child, tlabel, ret);
			}
		}
	}
}