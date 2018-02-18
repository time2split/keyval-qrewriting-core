package insomnia.qrewriting.database.driver.internal;

import java.util.HashMap;

import insomnia.json.Element;
import insomnia.json.ElementLiteral;
import insomnia.json.ElementNumber;
import insomnia.json.ElementObject;
import insomnia.json.ElementString;
import insomnia.json.Json;
import insomnia.json.JsonBuilder;
import insomnia.json.JsonBuilderException;
import insomnia.qrewriting.query.Query;
import insomnia.qrewriting.query.node.Node;
import insomnia.qrewriting.query.node.NodeChilds;
import insomnia.qrewriting.query.node.NodeValue;
import insomnia.qrewriting.query.node.NodeValueExists;
import insomnia.qrewriting.query.node.NodeValueLiteral;
import insomnia.qrewriting.query.node.NodeValueNumber;
import insomnia.qrewriting.query.node.NodeValueString;

public class JsonBuilder_query extends JsonBuilder
{
	Query query;

	public JsonBuilder_query()
	{
		super();
	}

	public JsonBuilder_query(Json j)
	{
		super(j);
	}

	public JsonBuilder_query(Json j, Query q)
	{
		super(j);
		setQuery(q);
	}

	public void setQuery(Query q)
	{
		query = q;
	}

	@Override
	public void build() throws JsonBuilderException
	{
		Json json = getJson();
		ElementObject root = new ElementObject();
		json.setDocument(root);
		Node queryRoot = query.getRoot();

		if (queryRoot.isLeaf())
		{
			throw new JsonBuilderException("Bad query structure for " + query);
		}
		makeJson(root, queryRoot);
	}

	private void makeJson(ElementObject jsonObject, Node n)
			throws JsonBuilderException
	{
		HashMap<String, Element> jsonObjects = jsonObject.getObject();

		NodeChilds childs = n.getChilds();
		Element newVal;

		for (Node ncur : childs)
		{
			NodeValue vcur = ncur.getValue();

			if (ncur.isLeaf())
			{
				if (vcur instanceof NodeValueNumber)
				{
					newVal = new ElementNumber(
						((NodeValueNumber) vcur).getNumber());
				}
				else if (vcur instanceof NodeValueString)
				{
					newVal = new ElementString(
						((NodeValueString) vcur).getString());
				}
				else if (vcur instanceof NodeValueLiteral)
				{
					newVal = new ElementLiteral(
						((NodeValueLiteral) vcur).getLiteral());
				}
				else if (vcur instanceof NodeValueExists)
				{
					newVal = new ElementLiteral(ElementLiteral.Literal.TRUE);
				}
				else
				{
					throw new JsonBuilderException("Cannot make value " + vcur);
				}
			}
			else
			{
				newVal = new ElementObject();
				makeJson((ElementObject) newVal, ncur);
			}
			jsonObjects.put(ncur.getLabel().get(), newVal);
		}
	}

	@Override
	public Json newBuild() throws JsonBuilderException
	{
		Json json = new Json();
		setJson(json);
		build();
		return json;
	}
}
