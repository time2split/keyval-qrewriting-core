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
import insomnia.qrewriting.qpu.QPUSimple;
import insomnia.qrewriting.query.Query;

/**
 * @author zuri
 */
public class QThreadQueriesManager extends AbstractQThreadManager
{
	private Encoding encoding;

	public QThreadQueriesManager(Context context, Query query, Encoding encoding)
	{
		super();
		setQuery(query);
		setEncoding(encoding);
		setContext(context);
	}

	private void setEncoding(Encoding encoding)
	{
		this.encoding = encoding;
	}

	@Override
	public ArrayList<QThreadResult> compute(ExecutorService executor) throws InterruptedException, ExecutionException
	{
		List<Interval> intervals = computeIntervals(encoding.generateCodeInterval());
		ArrayList<QThreadResult> result = new ArrayList<>(encoding.size());
		Collection<Future<Collection<QThreadResult>>> loaded = new ArrayList<>(intervals.size());

		for (Interval i : intervals)
		{
			ArrayList<Query> qpuRes = new QPUSimple(getContext(), getQuery(), i, encoding).process();

			QThread th = new QThreadQueries(getContext(), qpuRes);
			th.setCallback(getThreadCallback());
			loaded.add(executor.submit(th));
		}

		for (Future<Collection<QThreadResult>> ft : loaded)
			result.addAll(ft.get());

		executor.shutdown();
		return result;
	}
}
