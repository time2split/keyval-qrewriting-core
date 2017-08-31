package insomnia.qrewritingnorl1.query_rewriting.code;

import java.util.ArrayList;

import insomnia.numeric.Interval;
import insomnia.qrewritingnorl1.query_rewriting.generator.NodeContext;

/**
 * Encodage : signification des Codes. Suit la même indexation que les Codes.
 * 
 * @author zuri
 * 
 */
public class Encoding extends ArrayList<NodeContext>
{
	private static final long	serialVersionUID	= 1L;

	/**
	 * @return L'intervalle de tous les codes possibles
	 */
	public Interval generateCodeInterval()
	{
		return new Interval(0, getTotalNbStates() - 1);
	}

	/**
	 * 
	 * @param c
	 *            Représentation d'un code
	 * @return Le code dont la représentation numérique est $c
	 */
	public Code getCodeFrom(int c)
	{
		final Code ret = new Code(size());
		final int size = size();
		int num = c;
		int rest;

		for (int i = size - 1; i >= 0; i--)
		{
			NodeContext nc = this.get(i);
			Context ctx = nc.getContext();
			final int base = ctx.size();
			rest = num % base;
			num /= base;
			ret.setCode(i, rest);
		}
		return ret;
	}

	/**
	 * 
	 * @param codes
	 *            Intervalle des codes à générer
	 * @return L'ensemble des codes de l'intervalle
	 */
	public ArrayList<Code> generateAllCodes(Interval codes)
	{
		final ArrayList<Code> ret = new ArrayList<>((int) codes.size());
		final int c = (int) codes.getb();
		int i = (int) codes.geta();

		for (; i <= c; i++)
		{
			ret.add(getCodeFrom(i));
		}
		return ret;
	}

	/**
	 * @return tous les codes possibles
	 */
	public ArrayList<Code> generateAllCodes()
	{
		final int totalNbState = getTotalNbStates();
		final int nbState = size();
		final ArrayList<Code> ret = new ArrayList<>(totalNbState);
		final Code codes[] = new Code[totalNbState];

		for (int i = 0; i < totalNbState; i++)
		{
			codes[i] = new Code(nbState);
			ret.add(codes[i]);
		}
		int restNbState = totalNbState;

		for (int pos = 0; pos < nbState; pos++)
		{
			int currNbState = this.get(pos).getContext().size();
			int state = 0;

			for (int i = 0; i < totalNbState;)
			{
				int window = restNbState / currNbState;

				for (int w = 0; w < window; w++, i++)
				{
					codes[i].setCode(pos, state);
				}
				state = (state + 1) % currNbState;
			}
			restNbState /= currNbState;
		}
		return ret;
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