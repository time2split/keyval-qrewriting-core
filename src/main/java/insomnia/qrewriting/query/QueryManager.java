package insomnia.qrewriting.query;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.output.StringBuilderWriter;

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

	private int getWriterCapacity(Query q, int nb)
	{
		return 512 * q.getRoot().getNbOfDescendants() * nb;
	}

	protected String getStrFormat(Query query, StringBuilderWriter writer)
			throws Exception
	{
		writeStrFormat(writer, query);
		return writer.getBuilder().toString();
	}

	public String getStrFormat(Query query) throws Exception
	{
		StringBuilderWriter writer = new StringBuilderWriter(
			getWriterCapacity(query, 1));
		return getStrFormat(query, writer);
	}

	public String[] getStrFormat(Query... queries) throws Exception
	{
		String[] ret = new String[queries.length];
		StringBuilderWriter writer = new StringBuilderWriter(
			getWriterCapacity(queries[0], queries.length));
		int i = 0;

		for (Query q : queries)
		{
			ret[i] = getStrFormat(q, writer);
			writer.getBuilder().setLength(0);
		}
		return ret;
	}

	public String[] getStrFormat() throws Exception
	{
		return getStrFormat(queries);
	}

	abstract public void writeStrFormat(Writer writer, Query query)
			throws Exception;

	public void writeStrFormat(Writer writer, Query... queries) throws Exception
	{
		for (Query q : queries)
			writeStrFormat(writer, q);
	}

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
		return mergeByNumberOfQueries(nbofQueries,
			queries.toArray(new Query[0]));
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
		List<Query> ret = new ArrayList<>();

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
