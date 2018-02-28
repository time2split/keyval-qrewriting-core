package insomnia.qrewriting.database.driver.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import insomnia.json.Json;
import insomnia.json.JsonWriter;
import insomnia.qrewriting.database.driver.DriverQueryManager;
import insomnia.qrewriting.query.Label;
import insomnia.qrewriting.query.Query;
import insomnia.qrewriting.query.node.Node;

public class MyQueryManager extends DriverQueryManager
{

	public MyQueryManager()
	{
		super();
	}

	public MyQueryManager(Query... queries)
	{
		super(queries);
	}

	public MyQueryManager(Collection<Query> queries)
	{
		super(queries);
	}

	@Override
	public String[] getStrFormat(Query... queries) throws Exception
	{
		ArrayList<String> formats = new ArrayList<>(queries.length);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		try (JsonWriter jsonWriter = new JsonWriter(buffer);)
		{
			JsonBuilder_query jsonBuilder = new JsonBuilder_query();

			{
				final boolean compactPrint = getDriver()
						.getOption("json.prettyPrint", "false").equals("false");
				jsonWriter.getOptions().setCompact(compactPrint);
			}
			for (Query q : queries)
			{
				System.out.println(q);
				jsonBuilder.setQuery(q);
				Json json;

				// try
				// {
				json = jsonBuilder.newBuild();
				jsonWriter.write(json);
				formats.add(buffer.toString());
				// }
				// catch (Exception e)
				// {
				// formats.add(e.getMessage());
				// }
				buffer.reset();
			}
		}
		catch (IOException e)
		{
			throw e;
		}
		return formats.toArray(new String[0]);
	}

	@Override
	public Query merge(Query... queries)
	{
		Query ret = new Query();
		Node root = new Node();
		ret.setRoot(root);

		for (Query q : queries)
		{
			Node tmp = new Node(new Label("$or"));
			tmp.addChild(q.getRoot().getChilds().getChilds());
			root.addChild(tmp);
		}
		return ret;
	}

	@Override
	public boolean canMerge(Query... queries)
	{
		return true;
	}

}
