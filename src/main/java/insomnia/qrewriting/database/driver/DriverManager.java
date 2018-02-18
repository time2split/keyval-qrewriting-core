package insomnia.qrewriting.database.driver;

import java.util.HashMap;

import insomnia.qrewriting.database.Driver;

/**
 * Gestion des drivers de base de données (récupération, stockage)
 * 
 * @author zuri
 *
 */
public abstract class DriverManager
{
	protected HashMap<String, Driver> drivers = new HashMap<>();

	/**
	 * 
	 * @param driverName
	 *            Nom du driver
	 * @return null|Driver null si driver non trouvé
	 * @throws Exception
	 */
	abstract public Driver getDriver(String driverName)
			throws ClassNotFoundException, Exception;

	/**
	 * 
	 * @param driverName
	 *            nom du driver
	 * @return null|Driver null si le driver n'existait pas
	 */
	public Driver forgetDriver(String driverName) throws Exception
	{
		Driver driver = drivers.remove(driverName);

		if (driver == null)
			return null;

		driver.unload();
		return driver;
	}
}
