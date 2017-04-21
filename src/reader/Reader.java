package reader;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Permet la lectue d'un flux quelconque
 * 
 * @author zuri
 * 
 */
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

	public Reader(File f) throws FileNotFoundException
	{
		setSource(new BufferedInputStream(new FileInputStream(f)));
		closeStream = true;
	}

	@Override
	public void finalize()
	{
		try
		{
			close();
		}
		catch (IOException e)
		{
			System.err.println("Impossible de fermer la ressource : "
					+ e.getMessage());
		}
	}

	// =========================================================================

	abstract public Object read() throws ReaderException, IOException;

	// =========================================================================

	public void close() throws IOException
	{
		if (!closeStream)
			return;

		source.close();
		closeStream = false;
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
		closeStream = true;
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
