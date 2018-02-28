package insomnia.qrewriting.generator;

import insomnia.qrewriting.code.Context;
import insomnia.qrewriting.query.node.Node;

/**
 * Association d'un noeud à son contexte de codage
 * 
 * @author zuri
 * 
 */
public class NodeContext
{
	private Node	n;
	private Context	c;

	public NodeContext(Node nn, Context cc)
	{
		n = nn;
		c = cc;
	}

	public Node getNode()
	{
		return n;
	}

	public Context getContext()
	{
		return c;
	}

	@Override
	public String toString()
	{
		return "(" + n + "" + c + ")";
	}
}