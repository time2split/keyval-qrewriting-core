package rule;


public class RuleExists extends Rule
{
	
	public RuleExists(RuleExists r)
	{
		super((Rule)r);
	}
	
	
	public RuleExists(Rule r)
	{
		super(r);
	}
	

	public RuleExists(String from)
	{
		super(from);
	}
	
	
	public RuleExists(String from, String to)
	{
		super(from, to);
	}

	
	@Override
	public Object clone()
	{
		return new RuleExists(this);
	}
	
	
	@Override
	public String toString()
	{
		return this.from + "-> ?? " + this.to;
	}
}
