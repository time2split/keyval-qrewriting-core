package numeric;

import java.util.ArrayList;

/**
 * Intervalle numérique
 * 
 * @author zuri
 * 
 */
public class Interval
{
	/**
	 * La classe garantie que a <= b
	 */
	// TODO: Généric
	long	a;
	long	b;

	public Interval(long aa, long bb)
	{
		if (aa < bb)
		{
			a = aa;
			b = bb;
			return;
		}
		a = bb;
		b = aa;
	}

	public long geta()
	{
		return a;
	}

	public long getb()
	{
		return b;
	}

	public long diff()
	{
		return b - a;
	}

	public long size()
	{
		return diff() + 1;
	}

	/**
	 * Découpe un intervalle en $number sous intervalles. Le dernier intervalle
	 * se voit ajouté les objets restants
	 * 
	 * @param number
	 * @return
	 */
	public ArrayList<Interval> cutByNumberOfIntervals(int number)
	{
		ArrayList<Interval> ret = new ArrayList<>(number);
		long size = size() / number;
		long rest = size() % number;
		long s = a;
		long end = a + size - 1;

		if (rest > 0)
			number--;

		while (number-- > 0)
		{
			ret.add(new Interval(s, end));
			s = end + 1;
			end = s + size - 1;
		}

		if (rest > 0)
		{
			end += rest;
			ret.add(new Interval(s, end));
		}
		return ret;
	}

	@Override
	public String toString()
	{
		return "[" + a + "," + b + "]";
	}
}
