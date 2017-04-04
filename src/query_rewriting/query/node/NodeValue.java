package query_rewriting.query.node;

abstract public class NodeValue implements Cloneable
{
	abstract public Object getValue();

	public boolean isString()
	{
		return false;
	}

	@Override
	public String toString()
	{
		return getValue().toString();
	}

	@Override
	abstract public Object clone();
}
