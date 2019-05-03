package insomnia.qrewriting.database.driver.internal;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import insomnia.json.Element;
import insomnia.json.ElementLiteral;
import insomnia.json.ElementLiteral.Literal;
import insomnia.json.ElementNumber;
import insomnia.json.ElementObject;
import insomnia.json.ElementString;
import insomnia.json.Json;
import insomnia.json.JsonReader;
import insomnia.qrewriting.database.Driver;
import insomnia.qrewriting.database.driver.DriverQueryBuilder;
import insomnia.qrewriting.query.DefaultQuery;
import insomnia.qrewriting.query.Query;
import insomnia.qrewriting.query.QueryBuilderException;
import insomnia.qrewriting.query.node.NodeBuilder;
import insomnia.qrewriting.query.node.NodeValueExists;
import insomnia.qrewriting.query.node.NodeValueLiteral;
import insomnia.qrewriting.query.node.NodeValueNumber;
import insomnia.qrewriting.query.node.NodeValueString;

public class MyQueryBuilder extends DriverQueryBuilder
{
	private static final String[] sep = { Pattern.quote(".") };
	private Pattern               sepPattern;

	public MyQueryBuilder(Driver driver)
	{
		super(driver);
		StringBuilder sbuilder = new StringBuilder();

		for (String s : sep)
		{

			if (sbuilder.length() > 0)
				sbuilder.append('|');

			sbuilder.append(s);
		}
		sepPattern = Pattern.compile(sbuilder.toString());
	}

	@Override
	public void build() throws QueryBuilderException
	{
		Reader r = getReader();

		try (JsonReader jsonReader = new JsonReader(IOUtils.toString(r));)
		{
			Json doc = jsonReader.read();

			if (!doc.getDocument().isObject())
				throw new Exception("The base document of a query must be an object");

			NodeBuilder nbuilder = new NodeBuilder(getQuery().getRoot());
			nbuilder.setLabel(getDriver().getContext().getLabelFactory().from("@root"));
			makeTheQuery(nbuilder, doc.getDocument());
			nbuilder.build();
		}
		catch (Exception e)
		{
			throw new QueryBuilderException(e);
		}
	}

	@Override
	public Query newBuild() throws QueryBuilderException
	{
		Query query = new DefaultQuery();
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
	private void makeTheQuery(NodeBuilder nbuilder, Element jsonE) throws Exception
	{
		HashMap<String, Element> objects      = ((ElementObject) jsonE).getObject();
		int                      nbOfElements = objects.size();

		for (Entry<String, Element> entry : objects.entrySet())
		{
			int nbEnd = 1;
			String  key = entry.getKey();
			Element val = entry.getValue();
			nbuilder.child();

			switch (key)
			{
			case "$exists":

				if (!val.isLiteral())
				{
					Literal sval = ((ElementLiteral) val).getLiteral();

					if (sval != Literal.TRUE)
						throw new Exception("$exists operator must be a true literal");
				}

				if (nbOfElements > 1)
				{
					throw new Exception("Operator $exists must be alone");
				}
				nbuilder.setValue(new NodeValueExists());
				break;

			default:
			{
				checkKeyCut:
				{
					String[] cut = sepPattern.split(key, -1);

					if (cut.length == 1)
						break checkKeyCut;

					key   = cut[cut.length-1];
					nbEnd = cut.length;

					for (int i = 0; i < cut.length - 1; i++)
						nbuilder.setLabel(getDriver().getContext().getLabelFactory().from(cut[i])).child();
				}

				if (val.isObject())
				{
					makeTheQuery(nbuilder, val);
				}
				else if (val.isLiteral())
				{
					nbuilder.setValue(new NodeValueLiteral(((ElementLiteral) val).getLiteral().toString()));
				}
				else if (val.isNumber())
				{
					nbuilder.setValue(new NodeValueNumber(((ElementNumber) val).getNumber()));
				}
				else if (val.isString())
				{
					nbuilder.setValue(new NodeValueString(((ElementString) val).getString()));
				}
				else
				{
					throw new Exception("Internal QueryBuilder cannot take the val " + val);
				}
			}
			}
				nbuilder.setLabel(getDriver().getContext().getLabelFactory().from(key));

			while (nbEnd-- > 0)
				nbuilder.end();
		}
	}
}
