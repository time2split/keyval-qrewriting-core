package insomnia.qrewriting.query.node;

public class NodeValueFantom extends NodeValue
{
	static NodeValueFantom instance = new NodeValueFantom();

	private NodeValueFantom()
	{
	}

	static public NodeValueFantom getInstance()
	{
		return instance;
	}

	public Object getValue()
	{
		return null;
	}

	@Override
	public String toString()
	{
		return "@Fantom";
	}

	public NodeValueFantom clone()
	{
		return instance;
	}
}
