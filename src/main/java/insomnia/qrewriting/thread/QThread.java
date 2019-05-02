package insomnia.qrewriting.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import insomnia.builder.BuilderData;
import insomnia.builder.BuilderDataFactory;
import insomnia.builder.BuilderException;
import insomnia.numeric.Interval;
import insomnia.qrewriting.code.Code;
import insomnia.qrewriting.code.Encoding;
import insomnia.qrewriting.context.Context;
import insomnia.qrewriting.context.HasContext;
import insomnia.qrewriting.qpu.QPUSimple;
import insomnia.qrewriting.query.Query;

/**
 * Permet de calculer les réécritures d'une requête (par intervalle ou codes
 * directs)
 * 
 * @author zuri
 */
public class QThread implements Callable<ArrayList<QThreadResult>>, HasContext
{
	private Context            context;
	private Code[]             codes;
	private Interval           interval;
	private Query              query;
	private Encoding           encoding;
	private BuilderDataFactory<Object,Query> builderDataFactory;

	public QThread(Context context, Query q, Interval i, Encoding e)
	{
		setContext(context);
		setQuery(q);
		setCodes(i);
		setEncoding(e);
	}

	public void setBuilderDataFactory(BuilderDataFactory<Object,Query> b)
	{
		builderDataFactory = b;
	}

	private void setEncoding(Encoding e)
	{
		encoding = e;
	}

	@Override
	public void setContext(Context context)
	{
		this.context = context;
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

	public void setCodes(Collection<Code> c)
	{
		interval = null;
		codes    = c.toArray(new Code[0]);
	}

	public void setCodes(Interval i)
	{
		interval = i;
		codes    = null;
	}

	private void s_computeCodes()
	{
		if (codes != null)
			return;

		codes = encoding.generateAllCodes(interval);
	}

	/**
	 * Calcul les codes, $query n'est pas ajouté au résultat
	 * 
	 * @throws BuilderException
	 */
	@Override
	public ArrayList<QThreadResult> call() throws BuilderException
	{
		// System.out.println(Thread.currentThread().getName() + " : " +
		// interval);
		s_computeCodes();
		ArrayList<Query>         qpuRes = new QPUSimple(context, query, codes, encoding).process();
		ArrayList<QThreadResult> ret    = new ArrayList<>(qpuRes.size());

		if (builderDataFactory == null)
		{
			for (Query q : qpuRes)
			{
				ret.add(new QThreadResult(q, null));
			}
		}
		else
		{
			BuilderData<Object,Query> b = builderDataFactory.create();

			for (Query q : qpuRes)
			{
				b.setData(q);
				ret.add(new QThreadResult(q, b.newBuild()));
			}
		}
		return ret;
	}
}
