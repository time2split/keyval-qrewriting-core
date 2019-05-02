package insomnia.qrewriting.query.node;

public class NodeValuePhantom extends NodeValue
{
	static NodeValuePhantom instance = new NodeValuePhantom();

	private NodeValuePhantom()
	{
	}

	static public NodeValuePhantom getInstance()
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
		return "@Phantom";
	}

	public NodeValuePhantom clone()
	{
		return instance;
	}
}
