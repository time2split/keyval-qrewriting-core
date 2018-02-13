package insomnia.qrewriting.query_rewriting.generator;

import insomnia.qrewriting.node.Node;
import insomnia.qrewriting.query_rewriting.code.Context;

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
	private boolean	existRule;

	public NodeContext(Node nn, Context cc, boolean erule)
	{
		n = nn;
		c = cc;
		existRule = erule;
	}

	public Node getNode()
	{
		return n;
	}

	public Context getContext()
	{
		return c;
	}

	public boolean isForallRule()
	{
		return !existRule;
	}

	public boolean isExistRule()
	{
		return existRule;
	}

	@Override
	public String toString()
	{
		return "(" + n + "" + c + ")";
	}
}