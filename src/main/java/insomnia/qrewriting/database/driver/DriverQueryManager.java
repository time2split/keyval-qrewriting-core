package insomnia.qrewriting.database.driver;

import java.util.Collection;

import insomnia.qrewriting.database.Driver;
import insomnia.qrewriting.query.Query;
import insomnia.qrewriting.query.QueryManager;

public abstract class DriverQueryManager extends QueryManager
{
	Driver driver;

	public DriverQueryManager()
	{
		super();
	}

	public DriverQueryManager(Query... queries)
	{
		super(queries);
	}

	public DriverQueryManager(Collection<Query> queries)
	{
		super(queries);
	}

	public void setDriver(Driver d)
	{
		driver = d;
	}

	public Driver getDriver()
	{
		return driver;
	}
}
