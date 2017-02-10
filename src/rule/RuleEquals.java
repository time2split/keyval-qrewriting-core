package rule;


public class RuleEquals extends Rule
{
	
	public RuleEquals(RuleEquals r)
	{
		super((Rule)r);
	}
	
	
	public RuleEquals(Rule r)
	{
		super(r);
	}
	

	public RuleEquals(String from)
	{
		super(from);
	}
	
	
	public RuleEquals(String from, String to)
	{
		super(from, to);
	}

	
	@Override
	public Object clone()
	{
		return new RuleEquals(this);
	}
	
	
	@Override
	public String toString()
	{
		return this.from + "-> " + this.to;
	}
}
