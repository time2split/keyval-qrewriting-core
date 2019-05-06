package insomnia.qrewriting.database;

import java.util.Properties;

import insomnia.qrewriting.context.Context;

public abstract class AbstractDriver implements Driver
{
	private Context    context;
	private Properties options;

	protected void setContext(Context context)
	{
		this.context = context;
	}

	@Override
	public Context getContext()
	{
		return context;
	}

	protected void setOptions(Properties options)
	{
		this.options = options;
	}

	@Override
	public Object getOption(String name)
	{
		return options.get(name);
	}

	@Override
	public Object getOption(String name, Object def)
	{
		return options.getOrDefault(name, def);
	}

	@Override
	public void load(Context context, Properties options) throws DriverException
	{
		setContext(context);
		setOptions(options);
	}

	@Override
	public void unload() throws DriverException
	{
	}
}
