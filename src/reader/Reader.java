package reader;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Permet la lecture d'objets depuis un flux quelconque
 * 
 * @author zuri
 * 
 */
public abstract class Reader implements Closeable
{
	private InputStream	source;
	private int			buffLen	= 4096;

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

	public Reader(File f) throws FileNotFoundException
	{
		setSource(new BufferedInputStream(new FileInputStream(f)));
	}

	@Override
	public void finalize() throws IOException
	{
		close();
	}

	// =========================================================================

	abstract public Object read() throws ReaderException, IOException;

	public Object nextRead() throws ReaderException, IOException
	{
		return null;
	}

	// =========================================================================

	@Override
	public void close() throws IOException
	{
		source.close();
	}

	// =========================================================================

	public void setSource(InputStream stream)
	{
		source = stream;
	}

	public void setSource(String s)
	{
		setSource(new ByteArrayInputStream(s.getBytes()));
	}

	public void setSource(File f) throws FileNotFoundException
	{
		setSource(new BufferedInputStream(new FileInputStream(f)));
	}

	public InputStream getSource()
	{
		return source;
	}

	// =========================================================================

	/**
	 * Lecture jusqu'à épuisement de la source
	 * 
	 * @return
	 */
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
