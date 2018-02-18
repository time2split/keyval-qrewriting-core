package insomnia.qrewriting.database.driver.internal;

import java.io.Reader;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

import insomnia.json.Element;
import insomnia.json.ElementLiteral;
import insomnia.json.ElementLiteral.Literal;
import insomnia.json.ElementNumber;
import insomnia.json.ElementObject;
import insomnia.json.ElementString;
import insomnia.json.Json;
import insomnia.json.JsonReader;
import insomnia.qrewriting.database.driver.DriverQueryBuilder;
import insomnia.qrewriting.node.Node;
import insomnia.qrewriting.node.NodeChilds;
import insomnia.qrewriting.node.NodeValueExists;
import insomnia.qrewriting.node.NodeValueLiteral;
import insomnia.qrewriting.node.NodeValueNumber;
import insomnia.qrewriting.node.NodeValueString;
import insomnia.qrewriting.query_rewriting.query.Label;
import insomnia.qrewriting.query_rewriting.query.Query;
import insomnia.qrewriting.query_rewriting.query.QueryBuilderException;

public class MyQueryBuilder extends DriverQueryBuilder
{
	private int nodeId = 0;

	@Override
	public void build() throws QueryBuilderException
	{
		Reader r = getReader();

		try (JsonReader jsonReader = new JsonReader(IOUtils.toString(r));)
		{
			Json doc = jsonReader.read();
			Node root = new Node();
			root.setId(nodeId++);

			if (!doc.getDocument().isObject())
				throw new Exception(
					"The base document of a query must be an object");

			makeTheQuery(root, doc.getDocument());
			getQuery().setRoot(root);
		}
		catch (Exception e)
		{
			throw new QueryBuilderException(e.getMessage());
		}
	}

	@Override
	public Query newBuild() throws QueryBuilderException
	{
		Query query = new Query();
		setQuery(query);
		build();
		return query;
	}

	/**
	 * Construit la requête récursivement à partir des éléments Json
	 * 
	 * @param node
	 * @param jsonE
	 * @throws Exception
	 */
	private void makeTheQuery(Node node, Element jsonE) throws Exception
	{

		HashMap<String, Element> objects = ((ElementObject) jsonE).getObject();
		int nbOfElements = objects.size();
		NodeChilds childs = node.getChilds();

		for (String key : objects.keySet())
		{
			Element val = objects.get(key);
			Node newNode = new Node();
				newNode.setId(nodeId++);

			switch (key)
			{
			case "$exists":

				if (!val.isLiteral())
				{
					Literal sval = ((ElementLiteral) val).getLiteral();

					if (sval != Literal.TRUE)
						throw new Exception(
							"$exists operator must be a true literal");
				}

				if (nbOfElements > 1)
				{
					throw new Exception("Operator $exists must be alone");
				}
				newNode.setValue(new NodeValueExists());
				break;

			default:
			{
				if (val.isObject())
				{
					makeTheQuery(newNode, val);
				}
				else if (val.isLiteral())
				{
					newNode.setValue(new NodeValueLiteral(
						((ElementLiteral) val).getLiteral().toString()));
				}
				else if (val.isNumber())
				{
					newNode.setValue(
						new NodeValueNumber(((ElementNumber) val).getNumber()));
				}
				else if (val.isString())
				{
					newNode.setValue(
						new NodeValueString(((ElementString) val).getString()));
				}
				else
				{
					throw new Exception(
						"Internal QueryBuilder cannot take the val " + val);
				}
			}
			}
			childs.add(new Label(key), newNode);
		}
	}
}
