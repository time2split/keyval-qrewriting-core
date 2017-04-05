package query_rewriting.query.node;

import java.util.ArrayList;

import query_rewriting.query.Label;

/**
 * Enfants d'un noeud
 * 
 * @author zuri
 * 
 */
public class NodeChilds extends ArrayList<Node>
{
	private static final long	serialVersionUID	= 1L;

	public NodeChilds(int nb)
	{
		super(nb);
	}

	public NodeChilds()
	{
		super();
	}

	public ArrayList<Label> getChildsLabel()
	{
		ArrayList<Label> ret = new ArrayList<>();

		for (Node n : this)
		{
			ret.add(n.getLabel());
		}
		return ret;
	}

	public ArrayList<Node> getChilds()
	{
		return this;
	}

	public boolean add(Label l)
	{
		return add(new Node(l));
	}

	public boolean add(Label l, Node n)
	{
		n.setLabel(l);
		return add(n);
	}
}
