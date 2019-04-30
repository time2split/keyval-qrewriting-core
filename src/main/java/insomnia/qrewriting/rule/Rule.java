package insomnia.qrewriting.rule;

import insomnia.qrewriting.query.Label;

/**
 * Règle
 * 
 * @author zuri
 */
public abstract class Rule
{
	// Seul le premier élément de Label sera utilisé (utile pour le futur ?)
	protected Label h;
	protected Label c;

	public Rule(Label hh, Label cc)
	{
		setHypothesis(hh);
		setConclusion(cc);
	}

	public void setHypothesis(Label hh)
	{
		h = hh;
	}

	public void setConclusion(Label cc)
	{
		c = cc;
	}

	public String getHypothesis()
	{
		return h.get();
	}

	public String getConclusion()
	{
		return c.get();
	}

	@Override
	public boolean equals(Object b)
	{
		if (!(b instanceof Rule))
			return false;

		Rule bb = (Rule) b;
		return h.equals(bb.h) && c.equals(bb.c);
	}

	@Override
	public int hashCode()
	{
		return h.hashCode() + 12 * c.hashCode();
	}

	@Override
	public String toString()
	{
		return h + " => " + c;
	}
}
