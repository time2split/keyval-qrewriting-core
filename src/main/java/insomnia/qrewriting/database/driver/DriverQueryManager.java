package insomnia.qrewriting.database.driver;

import java.util.Collection;

import insomnia.qrewriting.query.Query;
import insomnia.qrewriting.query.QueryManager;

public abstract class DriverQueryManager extends QueryManager
{

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
}
