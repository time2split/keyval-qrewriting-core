package insomnia.qrewriting.thread;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import insomnia.qrewriting.context.HasContext;

/**
 * @author zuri
 */
public interface QThread extends Callable<Collection<QThreadResult>>, HasContext
{
	/**
	 * A callback called after the execution of the thread.
	 * 
	 * @param callback
	 */
	void setCallback(Consumer<Collection<QThreadResult>> callback);
}
