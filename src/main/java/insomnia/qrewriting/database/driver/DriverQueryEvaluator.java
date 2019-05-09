package insomnia.qrewriting.database.driver;

import insomnia.qrewriting.database.Driver;
import insomnia.qrewriting.database.DriverException;
import insomnia.qrewriting.query.Query;

public interface DriverQueryEvaluator
{
	Driver getDriver();

	/**
	 * Evaluate the queries and return the result as a set of Query (for now a query can represent a data).
	 * 
	 * @param queries The queries to evaluate
	 * @return A set of data represented by queries.
	 */
	Cursor evaluate(Query... queries) throws DriverException;

	/**
	 * Réponse à une requête
	 */
	static interface Cursor extends Iterable<Query>
	{
		/**
		 * Number of results
		 * @return
		 */
		long size();
	}
}
