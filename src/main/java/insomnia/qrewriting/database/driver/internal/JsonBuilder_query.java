package insomnia.qrewriting.database.driver.internal;

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
import insomnia.qrewriting.context.Context;
import insomnia.qrewriting.query.Label;
import insomnia.qrewriting.query.Query;
import insomnia.qrewriting.query.node.Node;
import insomnia.qrewriting.query.node.NodeChilds;
import insomnia.qrewriting.query.node.NodeValue;
import insomnia.qrewriting.query.node.NodeValueExists;
import insomnia.qrewriting.query.node.NodeValueLiteral;
import insomnia.qrewriting.query.node.NodeValueNumber;
import insomnia.qrewriting.query.node.NodeValuePhantom;
import insomnia.qrewriting.query.node.NodeValueString;

public class JsonBuilder_query extends JsonBuilder
{
	Context context;
	Query   query;

	public JsonBuilder_query(Context context)
	{
		super();
		setContext(context);
	}

	public JsonBuilder_query(Context context, Json j)
	{
		super(j);
		setContext(context);
	}

	public JsonBuilder_query(Context context, Json j, Query q)
	{
		super(j);
		setContext(context);
		setQuery(q);
		setJson(j);
	}

	public void setQuery(Query query)
	{
		this.query = query;
	}

	private void setContext(Context context)
	{
		this.context = context;
	}

	@Override
	public void build() throws JsonBuilderException
	{
		Json json = getJson();

		if (query.getRoot().isLeaf())
		{
			throw new JsonBuilderException("Bad query structure for " + query);
		}
		//TODO: del the root node ?
		json.setDocument(makeJson(query.getRoot()));
	}

	private Element makeJson(Node node) throws JsonBuilderException
	{
		Element    ret;
		Element    newVal;
		NodeChilds childs = node.getChilds();

		Map<Label, Integer> labelCount = childs.getChildsLabelCount();

		// On vérifie les labels vides
		if (labelCount.get(context.getLabelFactory().emptyLabel()) != null)
		{
			boolean haveOthers = labelCount.size() > 1;

			if (haveOthers)
				throw new JsonBuilderException("Bad structure of " + node);

			ret = new ElementArray();
		}
		else
			ret = new ElementObject();

		for (Node ncur : node)
		{
			NodeValue vcur = ncur.getValue();
			Label     lcur = ncur.getLabel();

			if (ncur.isLeaf())
			{
				if (vcur instanceof NodeValueNumber)
				{
					newVal = new ElementNumber(((NodeValueNumber) vcur).getNumber());
				}
				else if (vcur instanceof NodeValueString)
				{
					newVal = new ElementString(((NodeValueString) vcur).getString());
				}
				else if (vcur instanceof NodeValueLiteral)
				{
					newVal = new ElementLiteral(((NodeValueLiteral) vcur).getLiteral());
				}
				else if (vcur instanceof NodeValueExists)
				{
					newVal = new ElementLiteral(ElementLiteral.Literal.TRUE);
				}
				else if (vcur instanceof NodeValuePhantom)
				{
					newVal = null;
				}
				else
				{
					throw new JsonBuilderException("Cannot make value " + vcur + " of " + ncur);
				}
			}
			else
			{
				newVal = makeJson(ncur);
			}

			if (newVal != null)
				ret.add(newVal, lcur.get());
		}
		return ret;
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
