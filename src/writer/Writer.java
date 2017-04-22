package writer;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import reader.WriterException;

/**
 * Permet l'écriture d'objets sur un flux quelconque
 * 
 * @author zuri
 * 
 */
abstract public class Writer implements Closeable, Flushable
{
	private OutputStream	destination;

	public Writer()
	{
		setDestination(System.out);
	}

	public Writer(OutputStream d)
	{
		setDestination(d);
	}

	public Writer(File f) throws FileNotFoundException
	{
		setDestination(f);
	}

	@Override
	public void finalize() throws IOException
	{
		close();
	}

	// =========================================================================

	abstract public void write(Object o) throws WriterException, IOException;

	public void write(Collection<Object> co) throws WriterException,
			IOException
	{
		for (Object o : co)
			write(o);
	}

	// =========================================================================

	@Override
	public void close() throws IOException
	{
		destination.close();
	}

	// =========================================================================

	public void setDestination(OutputStream d)
	{
		destination = d;
	}

	public void setDestination(File f) throws FileNotFoundException
	{
		setDestination(new BufferedOutputStream(new FileOutputStream(f)));
	}

	public OutputStream getDestination()
	{
		return destination;
	}

	// =========================================================================

	@Override
	public void flush() throws IOException
	{
		destination.flush();
	}
}