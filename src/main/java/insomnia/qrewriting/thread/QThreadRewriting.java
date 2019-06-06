package insomnia.qrewriting.thread;

import java.util.ArrayList;
import java.util.Collection;

import insomnia.builder.BuilderData;
import insomnia.builder.BuilderDataFactory;
import insomnia.builder.BuilderException;
import insomnia.numeric.Interval;
import insomnia.qrewriting.code.Encoding;
import insomnia.qrewriting.context.Context;
import insomnia.qrewriting.qpu.QPUSimple;
import insomnia.qrewriting.query.Query;

/**
 * Permet de calculer les réécritures d'une requête (par intervalle ou codes directs)
 * 
 * @author zuri
 */
public class QThreadRewriting extends AbstractQThread
{
	private Context  context;
	private Interval interval;
	private Query    query;
	private Encoding encoding;

	private BuilderDataFactory<Object, Query> builderDataFactory;

	public QThreadRewriting(Context context, Query q, Interval i, Encoding e)
	{
		setContext(context);
		setQuery(q);
		setInterval(i);
		setEncoding(e);
	}

	public void setBuilderDataFactory(BuilderDataFactory<Object, Query> b)
	{
		builderDataFactory = b;
	}

	private void setEncoding(Encoding e)
	{
		encoding = e;
	}

	protected void setContext(Context context)
	{
		this.context = context;
	}
	
	public void setInterval(Interval i)
	{
		interval = i;
	}

	@Override
	public Context getContext()
	{
		return context;
	}

	public void setQuery(Query q)
	{
		query = q;
	}

	/**
	 * Calcul les codes, $query n'est pas ajouté au résultat
	 * 
	 * @throws BuilderException
	 */
	@Override
	public Collection<QThreadResult> call() throws BuilderException
	{
		ArrayList<Query>         qpuRes = new QPUSimple(context, query, interval, encoding).process();
		ArrayList<QThreadResult> ret    = new ArrayList<>(qpuRes.size());

		if (builderDataFactory == null)
		{
			for (Query q : qpuRes)
				ret.add(new QThreadResult(q, null));
		}
		else
		{
			BuilderData<Object, Query> b = builderDataFactory.create();

			for (Query q : qpuRes)
			{
				b.setData(q);
				ret.add(new QThreadResult(q, b.newBuild()));
			}
		}

		if (callback != null)
			callback.accept(ret);

		return ret;
	}
}
