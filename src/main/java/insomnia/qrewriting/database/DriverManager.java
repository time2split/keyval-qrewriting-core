package insomnia.qrewriting.database;

import java.util.HashMap;

/**
 * Gestion des drivers de base de données (récupération, stockage)
 * 
 * @author zuri
 *
 */
public abstract class DriverManager
{
	private HashMap<String, Driver> drivers;

	/**
	 * 
	 * @param driverName
	 *            Nom du driver
	 * @return null|Driver null si driver non trouvé
	 * @throws Exception
	 */
	abstract public Driver getDriver(String driverName) throws Exception;

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
