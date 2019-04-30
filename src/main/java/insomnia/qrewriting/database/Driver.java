package insomnia.qrewriting.database;

import java.util.Properties;

import insomnia.qrewriting.context.Context;
import insomnia.qrewriting.context.HasContext;
import insomnia.qrewriting.database.driver.DriverQueryBuilder;
import insomnia.qrewriting.database.driver.DriverQueryManager;

public abstract class Driver implements HasContext
{
	private Context    context;
	private Properties options;

	public void load(Context context) throws Exception
	{
		this.context = context;
	}

	public void unload() throws Exception
	{

	}

	@Override
	public Context getContext()
	{
		return context;
	}

	@Override
	public void setContext(Context context)
	{
		this.context = context;
	}

	protected abstract Class<? extends DriverQueryBuilder> getQueryBuilderClass();

	protected abstract Class<? extends DriverQueryManager> getQueryManagerClass();

	public DriverQueryBuilder getAQueryBuilder() throws Exception
	{
		DriverQueryBuilder ret = getQueryBuilderClass().getConstructor(Driver.class).newInstance(this);
		return ret;
	}

	public DriverQueryManager getAQueryManager() throws Exception
	{
		DriverQueryManager ret = getQueryManagerClass().getConstructor(Driver.class).newInstance(this);
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
