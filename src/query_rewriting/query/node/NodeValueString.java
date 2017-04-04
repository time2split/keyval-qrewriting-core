package query_rewriting.query.node;

public class NodeValueString extends NodeValue
{
	private String	s;

	public NodeValueString(String ss)
	{
		s = ss;
	}

	String getString()
	{
		return s;
	}

	@Override
	public boolean isString()
	{
		return true;
	}

	@Override
	public Object getValue()
	{
		return getString();
	}

	@Override
	public String toString()
	{
		return "'" + s + "'";
	}

	@Override
	public Object clone()
	{
		return new NodeValueString(s);
	}
}
