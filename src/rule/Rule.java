package rule;

abstract class Rule
implements Cloneable
{
	protected String from;
	protected String to;
	
	
	@Override
	abstract public Object clone();
	
	
	public Rule(String from)
	{
		this.from = from;
	}
	
	
	public Rule(String from, String to)
	{
		this.from = from;
		this.to   = to;
	}
	
	
	public Rule(Rule r)
	{
		this.from = new String(r.from);
		this.to   = new String(r.to);
	}
	
	
	public String getFrom()
	{
		return this.from;
	}
	
	
	public String getTo()
	{
		return this.to;
	}

	
	public int cmp(Rule r)
	{
		return from.equals(r.from) && to.equals(r.to) ? 0 : 1;
	}
	
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Rule))
			return false;
		
		return this.cmp((Rule)o) == 0;
	}
	
	
	@Override
	public int hashCode()
	{
		return from.hashCode() + to .hashCode();
	}
}
