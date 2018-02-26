package insomnia.qrewriting.database.driver;

import java.io.Reader;

import insomnia.qrewriting.database.Driver;
import insomnia.qrewriting.query.Query;
import insomnia.qrewriting.query.QueryBuilder;

/**
 * Le DriverQueryBuilder doit obligatoirement construire une Query à partir
 * d'une source externe Reader
 * 
 * @author zuri
 *
 */
public abstract class DriverQueryBuilder extends QueryBuilder
{
	Reader	reader;
	Driver	driver;

	public DriverQueryBuilder()
	{
		super();
	}

	public DriverQueryBuilder(Reader r)
	{
		super();
		setReader(r);
	}

	public DriverQueryBuilder(Query q, Reader r)
	{
		super(q);
		setReader(r);
	}

	public void setReader(Reader r)
	{
		reader = r;
	}

	public Reader getReader()
	{
		return reader;
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
