package insomnia.qrewriting.database.driver.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import insomnia.json.Json;
import insomnia.json.JsonWriter;
import insomnia.qrewriting.database.driver.DriverQueryManager;
import insomnia.qrewriting.query_rewriting.query.Query;

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

}
