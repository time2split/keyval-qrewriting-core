package numeric;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Permet de convertir des entiers en différentes bases
 * 
 * @author zuri
 * 
 */
public class Numeric
{
	int	n;

	public Numeric(int nn)
	{
		n = nn;
	}

	public String toMultiBase(int b)
	{
		int bases[] = { b };
		return toMultiBase(bases);
	}

	public String toMultiBase(int[] c)
	{
		ArrayList<Integer> args = new ArrayList<>(c.length);

		for (int i : c)
			args.add(i);

		return toMultiBase(args);
	}

	public String toMultiBase(Collection<Integer> c)
	{
		String ret = "";
		long num = n;

		for (Integer ob : c)
		{
			int b = ob.intValue();
			long r;

			if (b == 0)
				r = 0;
			else
			{
				r = num % b;
				num /= b;
			}
			ret = Long.toString(r) + ret;
		}
		return ret;
	}

	public String toBase(int b)
	{
		String ret = "";
		long num = n;

		while (true)
		{
			long r;

			if (b == 0)
				r = 0;
			else
			{
				r = num % b;
				num /= b;
			}
			ret = Long.toString(r) + ret;

			if (num == 0)
				return ret;
		}
	}
}
