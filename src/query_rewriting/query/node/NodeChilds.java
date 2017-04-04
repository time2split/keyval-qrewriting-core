package query_rewriting.query.node;

import java.util.ArrayList;

import query_rewriting.query.Label;

public class NodeChilds
{
	private ArrayList<Node>	childs;

	public NodeChilds(int nb)
	{
		childs = new ArrayList<Node>(nb);
	}

	public NodeChilds()
	{
		childs = new ArrayList<Node>();
	}

	public ArrayList<Label> getChildsLabel()
	{
		ArrayList<Label> ret = new ArrayList<Label>();

		for (Node n : childs)
		{
			ret.add(n.getLabel());
		}
		return ret;
	}

	public ArrayList<Node> getChilds()
	{
		return childs;
	}

	public void add(Node n)
	{
		childs.add(n);
	}

	public void add(Label l)
	{
		add(new Node(l));
	}

	public void add(Label l, Node n)
	{
		n.setLabel(l);
		add(n);
	}

	@Override
	public String toString()
	{
		return childs.toString();
	}
}
