package insomnia.qrewriting.database.driver.internal;

import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collection;

import org.apache.commons.io.output.WriterOutputStream;

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
	public void writeStrFormat(Writer writer, Query query) throws Exception
	{
		OutputStream buffer = new WriterOutputStream(writer,Charset.defaultCharset());

		try (JsonWriter jsonWriter = new JsonWriter(buffer);)
		{
			JsonBuilder_query jsonBuilder = new JsonBuilder_query();

			{
				final boolean compactPrint = getDriver()
						.getOption("json.prettyPrint", "false").equals("false");
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
		Node root = new Node();
		ret.setRoot(root);

		for (Query q : queries)
		{
			Node tmp = new Node().setLabelMe(new Label("$or"));
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
