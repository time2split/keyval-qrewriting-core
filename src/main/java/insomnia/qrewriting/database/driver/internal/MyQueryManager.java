package insomnia.qrewriting.database.driver.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import insomnia.json.Json;
import insomnia.json.JsonWriter;
import insomnia.qrewriting.database.driver.DriverQueryManager;
import insomnia.qrewriting.database.driver.internal.query.MyNodeOperation;
import insomnia.qrewriting.query.Query;
import insomnia.qrewriting.query.node.Node;
import insomnia.qrewriting.query.node.NodeChilds;

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
	public String[] getStrFormat()
	{
		ArrayList<String> formats = new ArrayList<>(queries.length);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		JsonWriter jsonWriter = new JsonWriter(buffer);
		JsonBuilder_query jsonBuilder = new JsonBuilder_query();
		
		{
			final boolean compactPrint = getDriver().getOption("json.prettyPrint","false").equals("false");
			jsonWriter.getOptions().setCompact(compactPrint);
		}
		
		for (Query q : queries)
		{
			jsonBuilder.setQuery(q);
			Json json;

			try
			{
				json = jsonBuilder.newBuild();
				jsonWriter.write(json);
				formats.add(buffer.toString());
			}
			catch (Exception e)
			{
				formats.add(e.getMessage());
			}
			buffer.reset();
		}

		try
		{
			jsonWriter.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return formats.toArray(new String[0]);
	}

	@Override
	public Query merge(Query... queries)
	{
		Query ret = new Query();
		NodeChilds rootChilds;
		{
			Node root = new MyNodeOperation(MyNodeOperation.Operation.Or);
			ret.setRoot(root);
			rootChilds = root.getChilds();
		}
		
		for (Query q : queries)
		{
			rootChilds.add(q.getRoot());
		}
		return ret;
	}

	@Override
	public boolean canMerge(Query... queries)
	{
		return true;
	}

}
