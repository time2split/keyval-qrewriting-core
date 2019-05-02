package insomnia.qrewriting.query;

import insomnia.builder.Builder;

/**
 * Permet de construire une requête
 * 
 * @author zuri
 * 
 */
abstract public class QueryBuilder extends Builder<Query>
{
	public QueryBuilder()
	{

	}

	public QueryBuilder(Query q)
	{
		setQuery(q);
	}

	public void setQuery(Query q)
	{
		setBuilded(q);
	}

	final public Query getQuery()
	{
		return getBuilded();
	}

	@Override
	abstract public void build() throws QueryBuilderException;

	@Override
	abstract public Query newBuild() throws QueryBuilderException;
}
