package insomnia.qrewriting.query;

import java.lang.reflect.InvocationTargetException;

import insomnia.builder.Builder;
import insomnia.builder.BuilderException;
import insomnia.qrewriting.query.Query;
import insomnia.qrewriting.query.node.NodeBuilder;

public class QueryBuilder extends Builder<Query>
{
	private Class<? extends Query> queryClass;

	protected QueryBuilder()
	{

	}

	public QueryBuilder(Class<? extends Query> queryClass)
	{
		super();
		setQueryClass(queryClass);
	}

	public QueryBuilder(Query builded)
	{
		super(builded);
		setQueryClass(builded.getClass());
	}

	protected void setQueryClass(Class<? extends Query> queryClass)
	{
		this.queryClass = queryClass;
	}

	// ========================================================================

	public static Query newQuery(Class<? extends Query> queryClass) throws BuilderException
	{
		try
		{
			return queryClass.getDeclaredConstructor().newInstance();
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			throw new BuilderException(e);
		}
	}

	private Query newQuery() throws BuilderException
	{
		return newQuery(queryClass);
	}

	@Override
	public void build() throws BuilderException
	{

	}

	@Override
	public Query newBuild() throws BuilderException
	{
		Query build = newQuery();
		setBuilded(build);
		build();
		return build;
	}

	public NodeBuilder getRootNodeFactory()
	{
		return new NodeBuilder(getBuilded().getRoot());
	}
}
