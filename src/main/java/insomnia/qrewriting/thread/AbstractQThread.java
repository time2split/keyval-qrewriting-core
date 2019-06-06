package insomnia.qrewriting.thread;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * @author zuri
 */
abstract public class AbstractQThread implements QThread
{
	protected Consumer<Collection<QThreadResult>> callback;

	public void setCallback(Consumer<Collection<QThreadResult>> callback)
	{
		this.callback = callback;
	}
}
