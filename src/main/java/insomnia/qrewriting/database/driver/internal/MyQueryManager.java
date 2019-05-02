package insomnia.qrewriting.database.driver.internal;

import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;

import org.apache.commons.io.output.WriterOutputStream;

import insomnia.json.JsonWriter;
import insomnia.qrewriting.database.Driver;
import insomnia.qrewriting.database.driver.DriverQueryManager;
import insomnia.qrewriting.query.Query;

public class MyQueryManager extends DriverQueryManager
{
	public MyQueryManager(Driver driver)
	{
		super(driver);
	}

	@Override
	public void writeStrFormat(Writer writer, Query query) throws Exception
	{
		OutputStream buffer = new WriterOutputStream(writer, Charset.defaultCharset());

		try (JsonWriter jsonWriter = new JsonWriter(buffer);)
		{
			JsonBuilder_query jsonBuilder = new JsonBuilder_query(getDriver().getContext());
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

//	@Override
	public Query merge(Query... queries)
	{
		// TODO: implement it
//		Context context = getDriver().getContext();
//		
//		Query ret = new DefaultQuery();
//		
//
//		for (Query q : queries)
//		{
//			Node tmp = new Node(context.getLabelFactory().from("$or"));
//			tmp.addChild(q.getChilds().getChilds());
//			q.addChild(tmp);
//		}
//		return ret;
		return null;
	}

	@Override
	public boolean canMerge(Query... queries)
	{
		return !true;
	}

}
