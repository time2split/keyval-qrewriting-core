package insomnia.qrewritingnorl1.node;

import java.util.ArrayList;

import insomnia.qrewritingnorl1.query_rewriting.query.Label;

/**
 * Enfants d'un noeud
 * 
 * @author zuri
 * 
 */
public class NodeChilds extends ArrayList<Node> implements Cloneable
{
	private static final long serialVersionUID = 1L;

	public NodeChilds(int nb)
	{
		super(nb);
	}

	public NodeChilds(NodeChilds childs)
	{
		for (Node tmp : childs)
		{
			add(new Node(tmp));
		}
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

	@Override
	public NodeChilds clone()
	{
		return new NodeChilds(this);
	}
}
