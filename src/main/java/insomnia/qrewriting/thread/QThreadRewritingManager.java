package insomnia.qrewriting.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import insomnia.numeric.Interval;
import insomnia.qrewriting.code.Encoding;
import insomnia.qrewriting.context.Context;
import insomnia.qrewriting.query.Query;

/**
 * @author zuri
 */
public class QThreadRewritingManager extends AbstractQThreadManager
{
	private Encoding                 encoding;
	private ExecutorService          executor;
	private ArrayList<QThreadResult> result;

	public QThreadRewritingManager(Context context, Query q, Encoding e)
	{
		super();
		setQuery(q);
		setEncoding(e);
		setContext(context);
	}

	private void setEncoding(Encoding e)
	{
		encoding = e;
	}

	@Override
	public ArrayList<QThreadResult> compute(ExecutorService exec) throws InterruptedException, ExecutionException
	{
		List<Interval> intervals = computeIntervals(encoding.generateCodeInterval());
		result   = new ArrayList<>(encoding.size());
		executor = exec;
		Collection<Future<Collection<QThreadResult>>> loaded = new ArrayList<>(intervals.size());

		for (Interval i : intervals)
		{
			QThread th = new QThreadRewriting(getContext(), getQuery(), i, encoding);
			th.setCallback(getThreadCallback());
			loaded.add(executor.submit(th));
		}

		for (Future<Collection<QThreadResult>> ft : loaded)
			result.addAll(ft.get());

		executor.shutdown();
		return result;
	}
}
