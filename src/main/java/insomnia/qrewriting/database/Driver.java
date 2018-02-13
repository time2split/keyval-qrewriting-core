package insomnia.qrewriting.database;

public abstract class Driver
{
	public abstract void load() throws Exception;

	public abstract void unload() throws Exception;
}
