package insomnia.qrewriting.node;

public class NodeValueLiteral extends NodeValue
{
	private String	s;

	public NodeValueLiteral(String ss)
	{
		s = ss;
	}

	public String getLiteral()
	{
		return s;
	}

	@Override
	public boolean isLiteral()
	{
		return true;
	}

	@Override
	public Object getValue()
	{
		return getLiteral();
	}

	@Override
	public String toString()
	{
		return s;
	}

	@Override
	public NodeValueLiteral clone()
	{
		return new NodeValueLiteral(s);
	}
}
