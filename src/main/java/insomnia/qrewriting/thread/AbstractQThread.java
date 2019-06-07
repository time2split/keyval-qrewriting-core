package insomnia.qrewriting.thread;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * @author zuri
 */
abstract public class AbstractQThread implements QThread
{
	private Consumer<Collection<QThreadResult>> callback;

	public void setCallback(Consumer<Collection<QThreadResult>> callback)
	{
		this.callback = callback;
	}

	protected void callback(Collection<QThreadResult> results)
	{
		if (callback != null)
			callback.accept(results);
	}
}
