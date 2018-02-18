package insomnia.qrewriting.query_rewriting.query;

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
		setQueries((Query[]) queries.toArray());
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

	// public void writeFormat(Writer write) throws IOException
	// {
	// write.write(getStrFormat());
	// }

	abstract public String[] getStrFormat();
}
