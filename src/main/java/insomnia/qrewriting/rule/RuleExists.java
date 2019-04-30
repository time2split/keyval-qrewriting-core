package insomnia.qrewriting.rule;

import insomnia.qrewriting.query.Label;

/**
 * exists-rules
 * 
 * @author zuri
 */
public class RuleExists extends Rule
{
	public RuleExists(Label hh, Label cc)
	{
		super(hh, cc);
	}

	@Override
	public String toString()
	{
		return h + " => ?" + c;
	}
}
