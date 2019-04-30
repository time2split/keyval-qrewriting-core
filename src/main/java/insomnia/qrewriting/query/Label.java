package insomnia.qrewriting.query;

import java.util.Collection;

/**
 * Interface représentant un ensemble de labels d'un arc d'une requête.
 * 
 * @author zuri
 */
public interface Label extends Collection<String>
{
	public default String[] getLabels()
	{
		return toArray(new String[0]);
	}

	public default String get()
	{
		return get(0);
	}

	public default String get(int i)
	{
		return getLabels()[i];
	}
}
