package query_rewriting.generator;

import query_rewriting.code.Context;
import query_rewriting.query.node.Node;

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