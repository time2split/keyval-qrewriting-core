package insomnia.qrewriting.database;

import java.util.Properties;

import insomnia.qrewriting.context.Context;
import insomnia.qrewriting.context.HasContext;
import insomnia.qrewriting.database.driver.DriverQueryBuilder;
import insomnia.qrewriting.database.driver.DriverQueryManager;

public interface Driver extends HasContext
{
	void load(Context context, Properties options) throws DriverException;

	void unload() throws DriverException;

	DriverQueryBuilder getAQueryBuilder() throws DriverException;

	DriverQueryManager getAQueryManager() throws DriverException;

	Object getOption(String name);

	Object getOption(String name, Object def);
}
