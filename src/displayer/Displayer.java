package displayer;

import java.io.OutputStream;

abstract public class Displayer implements Displayable
{
	private OutputStream	out	= System.out;

	public void setOut(OutputStream o)
	{
		out = o;
	}

	public OutputStream getOut()
	{
		return out;
	}
}
