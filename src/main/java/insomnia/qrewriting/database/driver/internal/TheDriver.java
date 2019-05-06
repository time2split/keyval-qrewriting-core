package insomnia.qrewriting.database.driver.internal;

import insomnia.qrewriting.database.AbstractDriver;
import insomnia.qrewriting.database.DriverException;
import insomnia.qrewriting.database.driver.DriverQueryBuilder;
import insomnia.qrewriting.database.driver.DriverQueryManager;

public class TheDriver extends AbstractDriver
{
	@Override
	public DriverQueryBuilder getAQueryBuilder() throws DriverException
	{
		return new MyQueryBuilder(this);
	}

	@Override
	public DriverQueryManager getAQueryManager() throws DriverException
	{
		return new MyQueryManager(this);
	}
}
