package insomnia.qrewritingnorl1.node;

import insomnia.qrewritingnorl1.query_rewriting.query.Label;

/**
 * Noeud d'une requête
 * 
 * @author zuri
 * 
 */
public class Node implements Cloneable
{
	private Label		label;
	private NodeChilds	childs;
	private NodeValue	value;
	private int			id	= 0;

	public Node()
	{
		childs = new NodeChilds();
		label = new Label("");
	}

	public Node(Node n)
	{
		setLabel(n.label);
		setId(n.id);
		childs = new NodeChilds(n.childs);

		if (n.value != null)
			setValue(n.value.clone());
	}

	public Node(Label l)
	{
		childs = new NodeChilds();
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
		return new Node(this);
	}
}
