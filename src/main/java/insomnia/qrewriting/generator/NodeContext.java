package insomnia.qrewriting.generator;

import insomnia.qrewriting.code.CodeContext;
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
	private CodeContext	c;

	public NodeContext(Node nn, CodeContext cc)
	{
		n = nn;
		c = cc;
	}

	public Node getNode()
	{
		return n;
	}

	public CodeContext getContext()
	{
		return c;
	}

	@Override
	public String toString()
	{
		return "(" + n + "" + c + ")";
	}
}