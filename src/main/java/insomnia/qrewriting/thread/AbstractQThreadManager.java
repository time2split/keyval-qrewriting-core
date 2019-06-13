package insomnia.qrewriting.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import insomnia.numeric.Interval;
import insomnia.qrewriting.context.Context;
import insomnia.qrewriting.context.HasContext;
import insomnia.qrewriting.query.Query;

/**
 * @author zuri
 */
public abstract class AbstractQThreadManager implements QThreadManager, HasContext
{
	/**
	 * Computation mode for threads configuration.
	 * <ul>
	 * <li>NB_THREAD : the number of threads on which we want to compute</li>
	 * <li>SIZEOF_THREAD : The maximum number of input a thread can support</li>
	 * <li>AUTO : automatic configuration</li>
	 * </ul>
	 * 
	 * @author zuri
	 */
	protected enum Mode
	{
		NB_THREAD, SIZEOF_THREAD, AUTO
	};

	private Context context;
	private Query   query;
	private Mode    mode = Mode.AUTO;
	private int     modeData;

	private Consumer<Collection<QThreadResult>> callback;

	/**
	 * A callback to be apply inside a thread on its result set.
	 */
	@Override
	public void setThreadCallback(Consumer<Collection<QThreadResult>> callback)
	{
		this.callback = callback;
	}

	protected Consumer<Collection<QThreadResult>> getThreadCallback()
	{
		return callback;
	}

	protected void setContext(Context context)
	{
		this.context = context;
	}

	protected void setQuery(Query q)
	{
		query = q;
	}

	protected Query getQuery()
	{
		return query;
	}

	@Override
	public Context getContext()
	{
		return context;
	}

	@Override
	public void setMode_sizeOfThread(int nb)
	{
		mode     = Mode.SIZEOF_THREAD;
		modeData = nb;
	}

	@Override
	public void setMode_nbThreads(int nb)
	{
		mode     = Mode.NB_THREAD;
		modeData = nb;
	}

	protected Mode getMode()
	{
		return mode;
	}

	protected int getModeData()
	{
		return modeData;
	}

	@Override
	public List<Interval> computeIntervals(Interval queriesInterval)
	{
		List<Interval> intervals;

		switch (mode)
		{
		case NB_THREAD:
		{
			int      nbThreads       = modeData;
			Interval initialInterval = queriesInterval;
			intervals = initialInterval.cutByNumberOfIntervals(nbThreads);

			if (intervals.isEmpty())
			{
				nbThreads = 1;
				intervals = new ArrayList<>();
				intervals.add(initialInterval);
			}
			break;
		}

		case SIZEOF_THREAD:
			intervals = queriesInterval.cutBySizeOfIntervals(modeData);
			break;

		// TODO: define a better approach
		case AUTO:
		default:
			intervals = queriesInterval.cutBySizeOfIntervals(1000);
			break;
		}
		return intervals;
	}
}
