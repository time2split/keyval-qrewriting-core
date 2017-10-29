package insomnia.qrewritingnorl1.query_rewriting.thread;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import insomnia.builder.BuilderDataFactory;
import insomnia.numeric.Interval;
import insomnia.qrewritingnorl1.query_rewriting.code.Encoding;
import insomnia.qrewritingnorl1.query_rewriting.query.Query;

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
	private Encoding					encoding;
	private ExecutorService				executor;
	private ArrayList<QThreadResult>	result;
	private int							modeData;
	private Mode						mode	= Mode.AUTO;

	public QThreadManager(Query q, Encoding e)
	{
		super();
		setQuery(q);
		setEncoding(e);
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

	public ArrayList<QThreadResult> compute()
			throws InterruptedException, ExecutionException
	{
		return compute((BuilderDataFactory) null);
	}

	public ArrayList<QThreadResult> compute(ExecutorService exec)
			throws InterruptedException, ExecutionException
	{
		return compute(null, exec);
	}

	public ArrayList<QThreadResult> compute(BuilderDataFactory factory)
			throws InterruptedException, ExecutionException
	{
		return compute(factory, Executors.newCachedThreadPool());
	}

	public ArrayList<QThreadResult> compute(BuilderDataFactory factory, ExecutorService exec)
			throws InterruptedException, ExecutionException
	{
		int nbThread;
		ArrayList<Interval> intervals;
		result = new ArrayList<>(encoding.size());
		executor = exec;

		switch (mode)
		{
		case NB_THREAD:
			nbThread = modeData;
			intervals = encoding.generateCodeInterval()
					.cutByNumberOfIntervals(nbThread);
			break;

		case SIZEOF_THREAD:
			intervals = encoding.generateCodeInterval()
					.cutBySizeOfIntervals(modeData);
			nbThread = intervals.size();
			break;

		// TODO: définir un meilleur comportement
		default:
			// case AUTO:
			intervals = encoding.generateCodeInterval()
					.cutBySizeOfIntervals(1000);
			nbThread = intervals.size();
			break;
		}
		ArrayList<Future<ArrayList<QThreadResult>>> loaded = new ArrayList<>(
			nbThread);

		for (Interval i : intervals)
		{
			QThread th = new QThread(query, i, encoding);
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
