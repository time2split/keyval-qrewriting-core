package insomnia.qrewritingnorl1.query_building.mongo;

import java.util.ArrayList;

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

	@Override
	public void build() throws JsonBuilderException
	{
		Query query = getQuery();

		if (!query.isUnfolded())
			throw new JsonBuilderException("La Query doit être dépliée");

		ElementObject root = new ElementObject();
		Json doc = getJson();
		ElementArray eand = new ElementArray();

		doc.setDocument(root);
		root.getObject().put("$and", eand);

		ArrayList<BuildResult> buildResults = new ArrayList<>();
		ArrayList<Element> array = eand.getArray();

		p_prebuild(query.getRoot(), "", buildResults);

		for (BuildResult res : buildResults)
		{
			ElementObject eobj = new ElementObject();
			array.add(eobj);
			eobj.getObject().put(res.path, res.element);
		}
	}

	private void p_prebuild(Node n, String label, ArrayList<BuildResult> ret)
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
					new_e = new ElementString(
						((NodeValueString) v).getString());
				}
				else if (v.isLiteral())
				{
					new_e = new ElementLiteral(
						((NodeValueLiteral) v).toString());
				}
				else if (v.isNumber())
				{
					new_e = new ElementNumber(
						((NodeValueNumber) v).getNumber());
				}
				else
					throw new JsonBuilderException(
						"Query Element '" + v + "' non pris en charge");
				ret.add(new BuildResult(tlabel, new_e));
			}
			else
			{
				p_prebuild(child, tlabel, ret);
			}
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
}