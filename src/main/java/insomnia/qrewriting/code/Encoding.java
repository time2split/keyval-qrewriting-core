package insomnia.qrewriting.code;

import java.util.ArrayList;

import insomnia.numeric.Interval;
import insomnia.qrewriting.generator.NodeContext;

/**
 * Encodage : signification des Codes. Suit la même indexation que les Codes.
 * 
 * @author zuri
 */
public class Encoding extends ArrayList<NodeContext>
{
	private static final long serialVersionUID = 1L;

	/**
	 * @return L'intervalle de tous les codes possibles
	 */
	public Interval generateCodeInterval()
	{
		return new Interval(0, getTotalNbStates() - 1);
	}

	public void add(Code code, int val)
	{
		int[] multiBase = getMultiBase();
		int   num       = val;
		int   rest;

		for (int i = multiBase.length - 1; i >= 0; i--)
		{
			final int base = multiBase[i];
			num  += code.getCode(i);
			rest  = num % base;
			num  /= base;
			code.setCode(i, rest);

			if (num == 0)
				break;
		}
	}

	public int[] getMultiBase()
	{
		final int size  = this.size();
		int       ret[] = new int[size];

		for (int i = 0; i < size; i++)
			ret[i] = get(i).getContext().size();

		return ret;
	}

	/**
	 * Calcule pour chaque position d'un code de l'encodage le format
	 * d'affichage, c-à-d le nombre de chiffres à afficher pour chaque code
	 * 
	 * @return
	 */
	public int[] getCodeFormat()
	{
		final int size     = this.size();
		int       format[] = new int[size];

		for (int i = 0; i < size; i++)
		{
			CodeContext c        = this.get(i).getContext();
			final int   nbStates = c.size() - 1;

			if (nbStates > 0)
				format[i] = (int) (Math.floor(Math.log10(nbStates))) + 1;
			else
				format[i] = 1;
		}
		return format;
	}

	/**
	 * @param c
	 *          Représentation d'un code
	 * @return Le code dont la représentation numérique est $c
	 */
	public Code getCodeFrom(int c)
	{
		final Code ret  = new Code(size(), this);
		final int  size = size();
		int        num  = c;
		int        rest;

		for (int i = size - 1; i >= 0; i--)
		{
			NodeContext nc   = this.get(i);
			CodeContext ctx  = nc.getContext();
			final int   base = ctx.size();
			rest  = num % base;
			num  /= base;
			ret.setCode(i, rest);

			if (num == 0)
				break;
		}
		return ret;
	}

	/**
	 * @param codes
	 *              Intervalle des codes à générer
	 * @return L'ensemble des codes de l'intervalle
	 * @deprecated
	 */
	public Code[] generateAllCodes(Interval codes)
	{
		final Code[] ret = new Code[(int) codes.size()];
		final int    c   = (int) codes.getb();
		int          i   = (int) codes.geta();

		for (int o = 0; i <= c; i++, o++)
		{
			ret[o] = getCodeFrom(i);
		}
		return ret;
	}

	/**
	 * @return tous les codes possibles
	 * @deprecated
	 */
	public Code[] generateAllCodes()
	{
		final int  totalNbState = getTotalNbStates();
		final Code codes[]      = new Code[totalNbState];

		for (int i = 0; i < totalNbState; i++)
		{
			codes[i] = getCodeFrom(i);
		}
		return codes;
	}

	/**
	 * @return le nombre de codes possibles
	 */
	public int getTotalNbStates()
	{
		int ret = 1;

		for (NodeContext nc : this)
		{
			ret *= nc.getContext().size();
		}
		return ret;
	}
}