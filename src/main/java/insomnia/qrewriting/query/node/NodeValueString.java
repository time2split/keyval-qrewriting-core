package insomnia.qrewriting.query.node;

public class NodeValueString extends NodeValue
{
	private String	s;

	public NodeValueString(String ss)
	{
		s = ss;
	}

	public String getString()
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
	public NodeValueString clone()
	{
		return new NodeValueString(s);
	}
}
