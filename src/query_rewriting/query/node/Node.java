package query_rewriting.query.node;

import query_rewriting.query.Label;

/**
 * Noeud d'une requête
 * 
 * @author zuri
 * 
 */
public class Node implements Cloneable
{
	private Label		label;
	private NodeChilds	childs	= new NodeChilds();
	private NodeValue	value;
	private int			id		= 0;

	public Node()
	{
		label = new Label("");
	}

	public Node(Label l)
	{
		label = l;
	}

	/**
	 * Est une feuille
	 * 
	 * @return true|false
	 */
	public boolean isLeaf()
	{
		return childs.isEmpty();
	}

	public void setLabel(Label l)
	{
		label = l;
	}

	public void setValue(NodeValue v)
	{
		value = v;
	}

	public NodeValue getValue()
	{
		return value;
	}

	public Label getLabel()
	{
		return label;
	}

	public NodeChilds getChilds()
	{
		return childs;
	}

	public void setId(int i)
	{
		id = i;
	}

	public int getId()
	{
		return id;
	}

	@Override
	public String toString()
	{
		boolean doonce = false;
		String ret = "" + label + "(" + id + ")";

		if (value != null)
			ret += "(" + value + ")";

		ret += " => {";

		for (Node c : childs.getChilds())
		{
			if (doonce)
				ret += ", ";

			ret += c;
			doonce = true;
		}
		return ret + "}";
	}

	@Override
	public Node clone()
	{
		Node n = new Node();
		n.setLabel(label);
		n.setId(id);

		if (value != null)
			n.setValue(value.clone());

		for (Node tmp : childs.getChilds())
		{
			n.childs.add(tmp.clone());
		}
		return n;
	}
}
