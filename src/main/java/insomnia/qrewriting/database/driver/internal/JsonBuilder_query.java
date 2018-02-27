package insomnia.qrewriting.database.driver.internal;

import java.util.HashMap;
import java.util.Map;

import insomnia.json.Element;
import insomnia.json.ElementArray;
import insomnia.json.ElementLiteral;
import insomnia.json.ElementNumber;
import insomnia.json.ElementObject;
import insomnia.json.ElementString;
import insomnia.json.Json;
import insomnia.json.JsonBuilder;
import insomnia.json.JsonBuilderException;
import insomnia.qrewriting.query.Label;
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

		Map<Label, Integer> labelCount = childs.getChildsLabelCount();
		HashMap<Label, ElementArray> labelElementArray = new HashMap<>(
			childs.size());

		for (Node ncur : childs)
		{
			NodeValue vcur = ncur.getValue();
			Label lcur = ncur.getLabel();

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
			else if (labelCount.get(lcur) == 1)
			{
				newVal = new ElementObject();
				makeJson((ElementObject) newVal, ncur);
			}
			// > 1
			else
			{
				ElementArray earray = labelElementArray.get(lcur);

				if (earray == null)
				{
					earray = new ElementArray();
					labelElementArray.put(lcur, earray);
					jsonObjects.put(lcur.get(), earray);
				}
				newVal = new ElementObject();
				earray.getArray().add(newVal);
				makeJson((ElementObject) newVal, ncur);
				newVal = null;
			}

			if (newVal != null)
				jsonObjects.put(lcur.get(), newVal);
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
