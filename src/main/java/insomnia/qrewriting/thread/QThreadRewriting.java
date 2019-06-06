package insomnia.qrewriting.thread;

import java.util.ArrayList;
import java.util.Collection;

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

	public QThreadRewriting(Context context, Query q, Interval i, Encoding e)
	{
		setContext(context);
		setQuery(q);
		setInterval(i);
		setEncoding(e);
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

		for (Query q : qpuRes)
			ret.add(new QThreadResult(q, null));

		if (callback != null)
			callback.accept(ret);

		return ret;
	}
}
