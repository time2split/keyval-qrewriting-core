package query_rewriting.query;

/**
 * Permet de construire une requête
 * 
 * @author zuri
 * 
 */
abstract public class QueryBuilder
{
	protected Query	query;

	public QueryBuilder()
	{

	}

	public QueryBuilder(Query q)
	{
		setQuery(q);
	}

	public void setQuery(Query q)
	{
		query = q;
	}

	abstract public void build() throws QueryBuilderException;
}
