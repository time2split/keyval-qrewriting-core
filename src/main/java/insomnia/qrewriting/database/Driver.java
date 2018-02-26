package insomnia.qrewriting.database;

import java.util.Properties;

import insomnia.qrewriting.database.driver.DriverQueryBuilder;
import insomnia.qrewriting.database.driver.DriverQueryManager;

public abstract class Driver
{
	private Properties options;

	public void load() throws Exception
	{

	}

	public void unload() throws Exception
	{

	}

	public abstract Class<? extends DriverQueryBuilder> getQueryBuilderClass();

	public abstract Class<? extends DriverQueryManager> getQueryManagerClass();

	public DriverQueryBuilder getAQueryBuilder(Object... params)
			throws Exception
	{
		DriverQueryBuilder ret = getQueryBuilderClass().getConstructor()
				.newInstance(this, params);
		ret.setDriver(this);
		return ret;
	}

	public DriverQueryManager getAQueryManager(Object... params)
			throws Exception
	{
		DriverQueryManager ret = getQueryManagerClass().getConstructor()
				.newInstance(params);
		ret.setDriver(this);
		return ret;
	}

	final public void setOptions(Properties options)
	{
		this.options = options;
	}

	/**
	 * Retourne une option
	 * 
	 * @param name
	 * @return null si non trouvé
	 */
	final public Object getOption(String name)
	{
		return options.getProperty(name);
	}

	final public Object getOption(String name, String def)
	{
		return options.getProperty(name, def);
	}
}
