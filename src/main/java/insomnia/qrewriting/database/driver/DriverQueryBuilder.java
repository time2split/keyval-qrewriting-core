package insomnia.qrewriting.database.driver;

import java.io.Reader;

import insomnia.qrewriting.database.Driver;
import insomnia.qrewriting.query.QueryBuilder;

/**
 * Le DriverQueryBuilder doit obligatoirement construire une Query à partir
 * d'une source externe Reader
 * 
 * @author zuri
 */
public abstract class DriverQueryBuilder extends QueryBuilder
{
	Reader  reader;
	Driver  driver;

	public DriverQueryBuilder(Driver driver)
	{
		super();
		setDriver(driver);
	}

	public void setReader(Reader r)
	{
		reader = r;
	}

	public Reader getReader()
	{
		return reader;
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
