package insomnia.qrewritingnorl1.query_rewriting.rule;

/**
 * exists-rules
 * @author zuri
 *
 */
public class RuleExists extends Rule
{
	public RuleExists(String hh, String cc)
	{
		super(hh, cc);
	}

	
	@Override
	public String toString()
	{
		return h + " => ?" + c;
	}
}
