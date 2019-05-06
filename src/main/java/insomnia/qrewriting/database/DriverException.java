package insomnia.qrewriting.database;

public class DriverException extends Exception
{
	private static final long serialVersionUID = 1L;

	public DriverException(String m)
	{
		super(m);
	}

	public DriverException(Exception e)
	{
		super(e);
	}
}
