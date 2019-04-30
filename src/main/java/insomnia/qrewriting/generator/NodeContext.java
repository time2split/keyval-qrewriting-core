package insomnia.qrewriting.generator;

import insomnia.qrewriting.code.CodeContext;
import insomnia.qrewriting.query.node.Node;

/**
 * Association d'un noeud à son contexte de codage
 * 
 * @author zuri
 */
public class NodeContext
{
	private int         nodeId;
	private CodeContext c;

	public NodeContext(Node nn, CodeContext cc)
	{
		nodeId = nn.getId();
		c      = cc;
	}

	public int getNodeId()
	{
		return nodeId;
	}

	public CodeContext getContext()
	{
		return c;
	}

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + "(" + nodeId + ":" + c + ")";
	}
}