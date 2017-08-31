package insomnia.qrewritingnorl1.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import insomnia.writer.Writer;
import insomnia.writer.WriterException;

public class JsonWriter extends Writer
{
	private JsonOptions	options		= new JsonOptions();
	private String		charTabs	= "\t";
	private String		charEol		= "\n";

	public JsonWriter()
	{
		super();
	}

	public JsonWriter(OutputStream d)
	{
		super(d);
	}

	public JsonWriter(File f) throws FileNotFoundException
	{
		super(f);
	}

	// =========================================================================

	public JsonOptions getOptions()
	{
		return options;
	}

	public void setEol(String s)
	{
		charEol = s;
	}

	public void setTab(String s)
	{
		charTabs = s;
	}

	// =========================================================================

//	@Override
	public void write(Json json) throws WriterException, IOException
	{
		s_write(json.getDocument(), 0);
	}

	@Override
	public void write(Object o) throws WriterException, IOException
	{
		if (!(o instanceof Json))
			throw new WriterException("Document Json attendu");

		write((Json) o);
	}

	// =========================================================================

	private void s_write(Element e, int level) throws IOException
	{
		String tabs;
		String tabs1;
		String eol;
		final boolean option_compact = options.getCompact();

		if (!option_compact)
		{
			tabs = String.join("", Collections.nCopies(level, charTabs));
			tabs1 = tabs + charTabs;
			eol = charEol;
		}
		else
		{
			eol = "";
			tabs = "";
			tabs1 = "";
		}

		OutputStream dest = getDestination();

		if (e instanceof ElementObject)
		{
			HashMap<String, Element> objects = ((ElementObject) e).getObject();
			boolean doonce = false;

			dest.write(('{' + eol).getBytes());

			for (String k : objects.keySet())
			{
				Element c = objects.get(k);
				dest.write(tabs1.getBytes());

				if (doonce)
				{
					dest.write(',');
				}
				else
					doonce = true;

				dest.write(('"' + k + "\" : ").getBytes());
				s_write(c, level + 1);
			}
			dest.write(tabs.getBytes());
			dest.write(('}' + eol).getBytes());
		}
		else if (e instanceof ElementArray)
		{
			ArrayList<Element> objects = ((ElementArray) e).getArray();
			boolean doonce = false;

			dest.write(('[' + eol).getBytes());

			for (Element c : objects)
			{
				dest.write(tabs1.getBytes());

				if (doonce)
				{
					dest.write(',');
				}
				else
					doonce = true;

				s_write(c, level + 1);
			}
			dest.write(tabs.getBytes());
			dest.write((']' + eol).getBytes());
		}
		// else if (e instanceof ElementString)
		// {
		// dest.write(("\"" + ((ElementString) e).getString() + "\"" +
		// eol).getBytes());
		// }
		else
		{
			dest.write((e + eol).getBytes());
		}
	}
}
