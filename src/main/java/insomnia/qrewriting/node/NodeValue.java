package insomnia.qrewriting.node;

/**
 * Valeur d'une noeud
 * 
 * @author zuri
 * 
 */
abstract public class NodeValue implements Cloneable
{
	abstract public Object getValue();

	public boolean isString()
	{
		return false;
	}

	public boolean isNumber()
	{
		return false;
	}

	public boolean isLiteral()
	{
		return false;
	}

	@Override
	public String toString()
	{
		return getValue().toString();
	}

	@Override
	abstract public NodeValue clone();
}
