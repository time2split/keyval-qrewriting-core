package insomnia.qrewriting.database.driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import insomnia.qrewriting.context.Context;
import insomnia.qrewriting.database.Driver;

public class DriverManagerStandard extends DriverManager
{
	ArrayList<String>				driversDirs;
	HashMap<String, Class<Driver>>	driverClasses;

	public DriverManagerStandard(String... dirs)
	{
		driverClasses = new HashMap<>();
		driversDirs = new ArrayList<>(dirs.length);

		for (String dir : dirs)
		{
			driversDirs.add(dir);
		}
	}

	public void bindClass(String driverName, Class<Driver> cls)
	{
		driverClasses.put(driverName, cls);
	}

	/**
	 * {@inheritDoc}
	 */
	 @Override
	public Driver getDriver(String driverName, Properties options, Context context)
			throws Exception
	{
		Class<Driver> cls = driverClasses.get(driverName);

		if (cls != null)
		{
			Driver driver = cls.getDeclaredConstructor().newInstance();
			driver.load(context);
			return driver;
		}
		return null;
	}

}
