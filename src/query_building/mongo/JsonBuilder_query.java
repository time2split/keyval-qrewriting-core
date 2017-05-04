package query_building.mongo;

import java.util.HashMap;

import json.Element;
import json.ElementLiteral;
import json.ElementObject;
import json.ElementString;
import json.Json;
import json.JsonBuilder;
import json.JsonBuilderException;
import query_rewriting.query.Query;
import query_rewriting.query.node.Node;
import query_rewriting.query.node.NodeValue;
import query_rewriting.query.node.NodeValueExists;
import query_rewriting.query.node.NodeValueString;

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

		final ElementObject e_new = new ElementObject();
		Json doc = getJson();
		doc.setDocument(e_new);
		p_build(query.getRoot(), e_new);
	}

	private void p_build(Node n, ElementObject json_e)
			throws JsonBuilderException
	{
		HashMap<String, Element> map = json_e.getObject();

		for (Node child : n.getChilds())
		{
			final int nbChilds = child.getChilds().size();
			String k = child.getLabel().get(0);
			NodeValue v = child.getValue();

			// Feuille
			if (nbChilds == 0)
			{
				Element new_e;

				if (v instanceof NodeValueExists)
					new_e = new ElementLiteral(ElementLiteral.Literal.TRUE);
				else if (v instanceof NodeValueString)
					new_e = new ElementString(((NodeValueString) v).getString());
				else
					throw new JsonBuilderException("Query Element '" + v
							+ "' non pris en charge");

				ElementObject exists = new ElementObject();
				exists.getObject().put("$exists", new_e);
				map.put(k, exists);
			}
			else if (nbChilds == 1)
			{
				ElementObject new_e = new ElementObject();
				map.put(k, new_e);
				p_build(child, new_e);
			}
			else
			{
				ElementObject eMatch = new ElementObject();
				ElementObject new_e = new ElementObject(eMatch);
				eMatch.getObject().put("$elemMatch", new_e);
				map.put(k, eMatch);
				p_build(child, new_e);
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