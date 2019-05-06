package insomnia.qrewriting.database.driver.internal;

import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;

import org.apache.commons.io.output.WriterOutputStream;

import insomnia.builder.BuilderException;
import insomnia.json.JsonWriter;
import insomnia.qrewriting.context.Context;
import insomnia.qrewriting.database.Driver;
import insomnia.qrewriting.database.driver.DriverQueryManager;
import insomnia.qrewriting.query.DefaultQuery;
import insomnia.qrewriting.query.Label;
import insomnia.qrewriting.query.Query;
import insomnia.qrewriting.query.node.NodeBuilder;

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
		try
		{
			Context context = getDriver().getContext();

			Query       ret      = new DefaultQuery();
			NodeBuilder nbuilder = new NodeBuilder(ret.getRoot());
			Label       orLabel  = context.getLabelFactory().from("$or");

			for (Query q : queries)
				nbuilder.child().setLabel(orLabel).addChild(false, q.getRoot().getChilds().getChilds()).end();

			return ret;
		}
		catch (BuilderException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean canMerge(Query... queries)
	{
		return true;
	}

}
