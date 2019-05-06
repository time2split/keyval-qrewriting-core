package insomnia.qrewriting.database.driver;

import java.util.HashMap;
import java.util.Properties;

import insomnia.qrewriting.context.Context;
import insomnia.qrewriting.database.Driver;
import insomnia.qrewriting.database.DriverException;

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
	 * @param options Options à envoyer au driver
	 * @param context Le contexte que le driver devra considérer
	 * @return null|Driver null si driver non trouvé*
	 * @throws Exception
	 */
	abstract public Driver getDriver(String driverName, Properties options, Context context)
			throws ClassNotFoundException, Exception;

	/**
	 * 
	 * @param driverName
	 *            nom du driver
	 * @return null|Driver null si le driver n'existait pas
	 */
	public Driver forgetDriver(String driverName) throws DriverException
	{
		Driver driver = drivers.remove(driverName);

		if (driver == null)
			return null;

		driver.unload();
		return driver;
	}
}
