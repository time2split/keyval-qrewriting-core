package insomnia.qrewriting.database.driver.internal;

import insomnia.qrewriting.database.Driver;

public class TheDriver extends Driver
{

	@Override
	public void load() throws Exception
	{
		
	}

	@Override
	public void unload() throws Exception
	{
		
	}

	@Override
	public Class<?> getQueryBuilderClass()
	{
		return MyQueryBuilder.class;
	}

	@Override
	public Class<?> getQueryManagerClass()
	{
		return MyQueryManager.class;
	}

}
