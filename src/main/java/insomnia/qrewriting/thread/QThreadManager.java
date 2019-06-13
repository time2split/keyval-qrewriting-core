package insomnia.qrewriting.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import insomnia.numeric.Interval;
import insomnia.qrewriting.context.HasContext;

public interface QThreadManager extends HasContext
{
	void setThreadCallback(Consumer<Collection<QThreadResult>> callback);

	void setMode_sizeOfThread(int nb);

	void setMode_nbThreads(int nb);

	/**
	 * Get the intervals such as each correspond to the input of a thread.
	 */
	List<Interval> computeIntervals(Interval queriesInterval);

	ArrayList<QThreadResult> compute(ExecutorService exec) throws InterruptedException, ExecutionException;

	// =========================================================================

	default ArrayList<QThreadResult> compute() throws InterruptedException, ExecutionException
	{
		return compute(Executors.newCachedThreadPool());
	}
}
