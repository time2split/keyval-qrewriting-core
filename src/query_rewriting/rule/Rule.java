package query_rewriting.rule;

import query_rewriting.query.Label;

/**
 * Règle
 * 
 * @author zuri
 */
public abstract class Rule
{
	// Seul le premier élément de Label sera utilisé (utile pour le futur ?)
	protected Label	h;
	protected Label	c;

	public Rule(String hh, String cc)
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

	public void setHypothesis(String hh)
	{
		h = new Label(hh);
	}

	public void setConclusion(String cc)
	{
		c = new Label(cc);
	}

	// public Label getHypothesisLabel(Label hh)
	// {
	// return h;
	// }
	//
	// public Label getConclusionLabel(Label cc)
	// {
	// return c;
	// }

	public String getHypothesis()
	{
		return h.get(0);
	}

	public String getConclusion()
	{
		return c.get(0);
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
	public String toString()
	{
		return h + " => " + c;
	}
}
