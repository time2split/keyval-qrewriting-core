package insomnia.qrewritingnorl1.query_building.mongo;

import java.util.ArrayList;
import java.util.HashMap;

import insomnia.json.Element;
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
import insomnia.qrewritingnorl1.query_rewriting.query.Label;
import insomnia.qrewritingnorl1.query_rewriting.query.Query;

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

	/**
	 * Fusionne les enfants ayant le même label (supprime les exists node)
	 */
	private void merge(Node n)
	{
		ArrayList<Label> labels = n.getChilds().getChildsLabel();

		for (Label label : labels)
		{
			// System.out.println(labels);
			ArrayList<Node> childs = n.getChilds().getChilds(label);
			int c = childs.size();
			int i = c - 1;

			// Plusieurs noeuds avec même label
			while (c > 1)
			{
				Node nn = childs.get(i--);

				// On supprime les noeuds d'existence
				if (nn.getValue() instanceof NodeValueExists)
				{
					n.getChilds().deleteChild(nn.getId());
					c--;
				}
			}
		}

		for (Node nn : n.getChilds().getChilds())
		{
			merge(nn);
		}
	}

	@Override
	public void build() throws JsonBuilderException
	{
		Query query = getQuery();

		if (!query.isUnfolded())
			throw new JsonBuilderException("La Query doit être dépliée");

		// System.out.println(query);
		merge(query.getRoot());
		// System.out.println(query);
		final ElementObject e_new = new ElementObject();
		Json doc = getJson();
		doc.setDocument(e_new);
		p_build(query.getRoot(), e_new, "");
	}

	private void p_build(Node n, ElementObject json_e, String label)
			throws JsonBuilderException
	{
		HashMap<String, Element> map = json_e.getObject();

		for (Node child : n.getChilds())
		{
			final int nbChilds = child.getChilds().size();
			String k = child.getLabel().get();
			NodeValue v = child.getValue();
			String tlabel;

			if (label.isEmpty())
				tlabel = k;
			else
				tlabel = label + "." + k;

			// Feuille
			if (nbChilds == 0)
			{
				Element new_e;

				if (v instanceof NodeValueExists)
				{
					new_e = new ElementLiteral(ElementLiteral.Literal.TRUE);
					ElementObject exists = new ElementObject();
					exists.getObject().put("$exists", new_e);
					map.put(tlabel, exists);
				}
				else
				{
					if (v instanceof NodeValueString)
					{
						new_e = new ElementString(
							((NodeValueString) v).getString());
					}
					else if (v instanceof NodeValueLiteral)
					{
						new_e = new ElementLiteral(
							((NodeValueLiteral) v).toString());
					}
					else if (v instanceof NodeValueNumber)
					{
						new_e = new ElementNumber(
							((NodeValueNumber) v).getNumber());
					}
					else
						throw new JsonBuilderException(
							"Query Element '" + v + "' non pris en charge");

					map.put(tlabel, new_e);
				}
			}
			else
			{
				// ElementObject eMatch = new ElementObject();
				// ElementObject new_e = new ElementObject(eMatch);
				// eMatch.getObject().put("$elemMatch", new_e);
				// map.put(k, eMatch);
				// p_build(child, new_e);

				p_build(child, json_e, tlabel);
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