package insomnia.qrewritingnorl1.node;

/**
 * Valeur 'exists' (pour les feuilles)
 * 
 * @author zuri
 * 
 */
public class NodeValueExists extends NodeValue
{
	boolean	exists	= true;

	public NodeValueExists()
	{

	}

	public NodeValueExists(boolean e)
	{
		exists = e;
	}

	public boolean exists()
	{
		return exists;
	}

	@Override
	public Object getValue()
	{
		return exists;
	}

	@Override
	public String toString()
	{
		return exists ? "exists" : "noexists";
	}

	@Override
	public NodeValueExists clone()
	{
		return new NodeValueExists(exists);
	}
}
