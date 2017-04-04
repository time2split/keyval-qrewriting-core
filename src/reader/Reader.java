package reader;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public abstract class Reader
{
	protected InputStream	source;
	private boolean			closeStream	= false;
	private int				buffLen		= 4096;

	public Reader()
	{
		setSource(System.in);
	}

	public Reader(InputStream s)
	{
		setSource(s);
	}

	public Reader(String s)
	{
		setSource(s);
	}

	public Reader(File f)
	{
		try
		{
			setSource(new BufferedInputStream(new FileInputStream(f)));
			closeStream = true;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	// =========================================================================

	// abstract public Object read();

	// =========================================================================

	public void close() throws IOException
	{
		if (!closeStream)
			return;

		source.close();
		closeStream = false;
	}

	public void closeNoe()
	{
		try
		{
			close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	// =========================================================================

	public void setSource(InputStream stream)
	{
		closeNoe();
		source = stream;
	}

	public void setSource(String s)
	{
		closeNoe();
		setSource(new ByteArrayInputStream(s.getBytes()));
	}

	public void setSource(File f)
	{
		closeNoe();
		try
		{
			setSource(new BufferedInputStream(new FileInputStream(f)));
			closeStream = true;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public InputStream getSource()
	{
		return source;
	}

	// =========================================================================

	public String getContents()
	{
		String ret = "";
		byte buff[] = new byte[buffLen];

		try
		{
			while (-1 != (source.read(buff)))
			{
				ret += new String(buff);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return ret;
	}
}
