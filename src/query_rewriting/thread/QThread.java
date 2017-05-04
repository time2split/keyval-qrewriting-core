package query_rewriting.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import numeric.Interval;
import query_rewriting.code.Code;
import query_rewriting.code.ContextManager;
import query_rewriting.code.Encoding;
import query_rewriting.qpu.QPUSimple;
import query_rewriting.query.Query;
import builder.BuilderData;
import builder.BuilderDataFactory;
import builder.BuilderException;

/**
 * Permet de calculer les réécritures d'une requête (par intervalle ou codes
 * directs)
 * 
 * @author zuri
 * 
 */
public class QThread implements Callable<ArrayList<QThreadResult>>
{
	private Collection<Code>	codes;
	private Interval			interval;
	private Query				query;
	private ContextManager		contexts;
	private Encoding			encoding;
	private BuilderDataFactory	builderDataFactory;

	public QThread(Query q, Interval i, Encoding e, ContextManager cm)
	{
		setQuery(q);
		setCodes(i);
		setEncoding(e);
		setContexts(cm);
	}

	public void setBuilderDataFactory(BuilderDataFactory b)
	{
		builderDataFactory = b;
	}

	private void setContexts(ContextManager cm)
	{
		contexts = cm;
	}

	private void setEncoding(Encoding e)
	{
		encoding = e;
	}

	public void setQuery(Query q)
	{
		query = q;
	}

	public void setCodes(Collection<Code> c)
	{
		interval = null;
		codes = c;
	}

	public void setCodes(Interval i)
	{
		interval = i;
		codes = null;
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
		ArrayList<Query> qpuRes = new QPUSimple(query, codes, encoding, contexts).process();
		ArrayList<QThreadResult> ret = new ArrayList<>(qpuRes.size());

		if (builderDataFactory == null)
		{
			for (Query q : qpuRes)
			{
				ret.add(new QThreadResult(q, null));
			}
		}
		else
		{
			BuilderData b = builderDataFactory.create();

			for (Query q : qpuRes)
			{
				b.setData(q);
				ret.add(new QThreadResult(q, b.newBuild()));
			}
		}
		return ret;
	}
}
