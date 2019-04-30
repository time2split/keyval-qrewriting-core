package insomnia.qrewriting.database.driver;

import insomnia.qrewriting.database.Driver;
import insomnia.qrewriting.query.QueryManager;

public abstract class DriverQueryManager extends QueryManager
{
	Driver driver;

	public DriverQueryManager(Driver driver)
	{
		setDriver(driver);
	}

	private void setDriver(Driver d)
	{
		driver = d;
	}

	public Driver getDriver()
	{
		return driver;
	}
}
