package insomnia.qrewriting.query;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Gestion de requêtes
 * 
 * @author zuri
 */
public abstract class QueryManager
{
	protected Query[] queries;

	public QueryManager()
	{

	}

	public QueryManager(Collection<Query> queries)
	{
		setQueries(queries.toArray(new Query[0]));
	}

	public QueryManager(Query... queries)
	{
		setQueries(queries);
	}

	final public void setQueries(Query... queries)
	{
		this.queries = queries;
	}

	public Query[] getQueries()
	{
		return queries;
	}

	abstract public String[] getStrFormat();

	/**
	 * Fusionne plusieurs requêtes entre elles si c'est possible
	 * 
	 * @param queries
	 * @return La requête résultante ou null si impossibilité de fusion
	 */
	abstract public Query merge(Query... queries);

	abstract public boolean canMerge(Query... queries);

	public Query merge(Collection<Query> queries)
	{
		return merge(queries.toArray(new Query[0]));
	}

	public boolean canMerge(Collection<Query> queries)
	{
		return canMerge(queries.toArray(new Query[0]));
	}

	public Query[] mergeByNumberOfQueries(int nbofQueries, Query... queries)
	{
		ArrayList<Query> ret = new ArrayList<>();
		// TODO
		return ret.toArray(new Query[0]);
	}

	public Query[] mergeBySizeOfQueries(int sizeofQueries, Query... queries)
	{
		ArrayList<Query> ret = new ArrayList<>();
		// TODO
		return ret.toArray(new Query[0]);
	}

	public Query[] mergeAllByNumberOfQueries(int nbofQueries)
	{
		return this.mergeByNumberOfQueries(nbofQueries, queries);
	}

	public Query[] mergeAllBySizeOfQueries(int sizeofQueries)
	{
		return this.mergeBySizeOfQueries(sizeofQueries, queries);
	}
}
