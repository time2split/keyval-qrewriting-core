package query_rewriting.thread;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import numeric.Interval;
import query_rewriting.code.ContextManager;
import query_rewriting.code.Encoding;
import query_rewriting.query.Query;
import builder.BuilderDataFactory;

/**
 * Va créer des threads en fonction des codes de $encoding
 * 
 * @author zuri
 * 
 */
public class QThreadManager
{
	/**
	 * Mode de fonctionnement du gestionnaire
	 * <ul>
	 * <li>NB_THREAD : spécifie le nombre de thread sur lesquels partager le
	 * calcul</li>
	 * <li>SIZEOF_THREAD : spécifie la taille maximale d'un thread, le nombre de
	 * threads sera calculé en conséquence</li>
	 * <li>AUTO : tente de déterminer seul la configuration</li>
	 * </ul>
	 * 
	 * @author zuri
	 * 
	 */
	private enum Mode
	{
		NB_THREAD, SIZEOF_THREAD, AUTO
	};

	private Query						query;
	private ContextManager				contexts;
	private Encoding					encoding;
	private ExecutorService				executor;
	private ArrayList<QThreadResult>	result;
	private int							modeData;
	private Mode						mode	= Mode.AUTO;

	public QThreadManager(Query q, Encoding e, ContextManager cm)
	{
		super();
		setQuery(q);
		setEncoding(e);
		setContexts(cm);
	}

	public void setMode_sizeOfThread(int nb)
	{
		mode = Mode.SIZEOF_THREAD;
		modeData = nb;
	}

	public void setMode_nbThread(int nb)
	{
		mode = Mode.NB_THREAD;
		modeData = nb;
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

	public ExecutorService getExecutor()
	{
		return executor;
	}

	public ArrayList<QThreadResult> getResult()
	{
		return result;
	}

	public ArrayList<QThreadResult> compute() throws InterruptedException,
			ExecutionException
	{
		return compute(null);
	}

	public ArrayList<QThreadResult> compute(BuilderDataFactory factory)
			throws InterruptedException, ExecutionException
	{
		int nbThread;
		ArrayList<Interval> intervals;
		result = new ArrayList<>(encoding.size());
		executor = Executors.newCachedThreadPool();

		switch (mode)
		{
		case NB_THREAD:
			nbThread = modeData;
			intervals = encoding.generateCodeInterval().cutByNumberOfIntervals(nbThread);
			break;

		case SIZEOF_THREAD:
			intervals = encoding.generateCodeInterval().cutBySizeOfIntervals(modeData);
			nbThread = intervals.size();
			break;

		// TODO: définir un meilleur comportement
		default:
			// case AUTO:
			intervals = encoding.generateCodeInterval().cutBySizeOfIntervals(1000);
			nbThread = intervals.size();
			break;
		}
		ArrayList<Future<ArrayList<QThreadResult>>> loaded = new ArrayList<>(nbThread);

		for (Interval i : intervals)
		{
			QThread th = new QThread(query, i, encoding, contexts);
			th.setBuilderDataFactory(factory);
			loaded.add(executor.submit(th));
		}

		for (Future<ArrayList<QThreadResult>> ft : loaded)
		{
			result.addAll(ft.get());
		}
		executor.shutdown();
		return result;
	}
}
