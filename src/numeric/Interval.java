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
	public static final int	OPTION_NULL			= 0;
	public static final int	OPTION_HOMOGENEOUS	= 1;

	/**
	 * La classe garantie que a <= b
	 */
	// TODO: Généric
	long					a;
	long					b;

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

	public ArrayList<Interval> cutByNumberOfIntervals(int number)
	{
		return cutByNumberOfIntervals(number, OPTION_NULL);
	}

	/**
	 * Découpe un intervalle en $number sous intervalles.
	 * 
	 * @param number
	 * @param option
	 *            OPTION_NULL|OPTION_HOMOGENEOUS
	 *            <ul>
	 *            <li>OPTION_NULL : les éléments restants sont ajoutés dans le
	 *            dernier Interval</li>
	 *            <li>OPTION_HOMOGENEOUS : les $reste premiers intervalles se
	 *            voient ajouté 1 élément supplémentaire</li>
	 *            </ul>
	 * @return $number Interval.
	 *         Si $number > size() un tableau vide est
	 *         retourné
	 */
	public ArrayList<Interval> cutByNumberOfIntervals(final int number,
			final int option)
	{
		ArrayList<Interval> ret = new ArrayList<>(number);

		if (number > size())
			return ret;

		long size = size() / number;
		long rest = size() % number;
		long s = a;
		long end = a + size - 1;
		boolean option_rest = (option & OPTION_HOMOGENEOUS) != 0;
		int nb = number;

		if (rest > 0)
			nb--;

		while (nb-- > 0)
		{
			ret.add(new Interval(s, end));
			s = end + 1;
			end = s + size - 1;
		}

		if (rest > 0)
		{
			if (option_rest)
			{
				ret.add(new Interval(s, end));

				final int offset = number - (int) rest;

				for (int i = 0; i < rest - 1; i++)
				{
					final int pos = offset + i;
					ret.get(pos).b += i + 1;
					ret.get(pos + 1).a += i + 1;
				}
				ret.get(number - 1).b += rest;
			}
			else
			{
				end += rest;
				ret.add(new Interval(s, end));
			}
		}
		return ret;
	}

	/**
	 * 
	 * @param size La taille d'un Interval
	 * @return Une liste d'Interval de taille $size
	 */
	public ArrayList<Interval> cutBySizeOfIntervals(long size)
	{
		if (size < 1)
			return new ArrayList<>();

		int number = (int) (size() / size);
		final int rest = (int) (size() % size);
		long s = a;
		long end = a + size - 1;

		ArrayList<Interval> ret = new ArrayList<>(number + (rest > 0 ? 1 : 0));

		while (number-- > 0)
		{
			ret.add(new Interval(s, end));
			s = end + 1;
			end = s + size - 1;
		}

		if (rest > 0)
		{
			end -= size - rest;
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
