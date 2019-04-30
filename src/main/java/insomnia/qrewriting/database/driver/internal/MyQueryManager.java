package insomnia.qrewriting.database.driver.internal;

import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;

import org.apache.commons.io.output.WriterOutputStream;

import insomnia.json.JsonWriter;
import insomnia.qrewriting.context.Context;
import insomnia.qrewriting.database.Driver;
import insomnia.qrewriting.database.driver.DriverQueryManager;
import insomnia.qrewriting.query.Query;
import insomnia.qrewriting.query.node.Node;

public class MyQueryManager extends DriverQueryManager
{
	private final Context context;

	public MyQueryManager(Driver driver)
	{
		super(driver);
		context = getDriver().getContext();
	}

	@Override
	public void writeStrFormat(Writer writer, Query query) throws Exception
	{
		OutputStream buffer = new WriterOutputStream(writer, Charset.defaultCharset());

		try (JsonWriter jsonWriter = new JsonWriter(buffer);)
		{
			JsonBuilder_query jsonBuilder = new JsonBuilder_query(context);

			{
				final boolean compactPrint = getDriver().getOption("json.prettyPrint", "false").equals("false");
				jsonWriter.getOptions().setCompact(compactPrint);
			}
			jsonBuilder.setQuery(query);
			jsonWriter.write(jsonBuilder.newBuild());
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	@Override
	public Query merge(Query... queries)
	{
		Query ret = new Query();

		for (Query q : queries)
		{
			Node tmp = new Node(context.getLabelFactory().from("$or"));
			tmp.addChild(q.getChilds().getChilds());
			q.addChild(tmp);
		}
		return ret;
	}

	@Override
	public boolean canMerge(Query... queries)
	{
		return true;
	}

}
