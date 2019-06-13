package insomnia.qrewriting.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import insomnia.builder.BuilderException;
import insomnia.qrewriting.context.Context;
import insomnia.qrewriting.query.Query;

/**
 * A thread which take a set of queries as input and apply its callback on them.
 * 
 * @author zuri
 */
public class QThreadQueries extends AbstractQThread
{
	private Context     context;
	private List<Query> queries;

	public QThreadQueries(Context context, Collection<Query> queries)
	{
		setContext(context);
		setQueries(queries);
	}

	private void setContext(Context context)
	{
		this.context = context;
	}

	@Override
	public Context getContext()
	{
		return context;
	}

	private void setQueries(Collection<Query> queries)
	{
		this.queries = new ArrayList<Query>(queries);
	}

	@Override
	public Collection<QThreadResult> call() throws BuilderException
	{
		ArrayList<QThreadResult> ret = new ArrayList<>(queries.size());

		for (Query q : queries)
			ret.add(new QThreadResult(q, null));

		callback(ret);
		return ret;
	}
}
