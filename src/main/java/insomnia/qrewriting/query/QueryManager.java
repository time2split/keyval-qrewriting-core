package insomnia.qrewriting.query;

import java.util.ArrayList;
import java.util.Arrays;
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

	final public void setQueries(Collection<? extends Query> queries)
	{
		this.queries = queries.toArray(new Query[0]);
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

	public Query merge(Collection<? extends Query> queries)
	{
		return merge(queries.toArray(new Query[0]));
	}

	public boolean canMerge(Collection<? extends Query> queries)
	{
		return canMerge(queries.toArray(new Query[0]));
	}

	public boolean canMerge()
	{
		return canMerge(queries);
	}

	public Query[] mergeByNumberOfQueries(int nbofQueries, Collection<? extends Query> queries)
	{
		return mergeByNumberOfQueries(nbofQueries, queries.toArray(new Query[0]));
	}

	public Query[] mergeBySizeOfQueries(int nbofQueries, Collection<? extends Query> queries)
	{
		return mergeBySizeOfQueries(nbofQueries, queries.toArray(new Query[0]));
	}

	public Query[] mergeByNumberOfQueries(int nbofQueries, Query... queries)
	{
		final int sizeofQueries = queries.length / nbofQueries;
		final int oneMore = (queries.length % nbofQueries) != 0 ? 1 : 0;
		return mergeBySizeOfQueries(sizeofQueries + oneMore, queries);
	}

	public Query[] mergeBySizeOfQueries(int sizeofQueries, Query... queries)
	{
		ArrayList<Query> ret = new ArrayList<>(
			(sizeofQueries / queries.length) + 1);

		final int c = queries.length;

		for (int i = 0; i != c;)
		{
			final int size;

			if (c - i < sizeofQueries)
				size = c - i;
			else
				size = sizeofQueries;

			ret.add(merge(Arrays.copyOfRange(queries, i, i + size)));
			i += size;
		}
		return ret.toArray(new Query[0]);
	}

	public Query[] mergeByNumberOfQueries(int nbofQueries)
	{
		return this.mergeByNumberOfQueries(nbofQueries, queries);
	}

	public Query[] mergeBySizeOfQueries(int sizeofQueries)
	{
		return this.mergeBySizeOfQueries(sizeofQueries, queries);
	}
}
