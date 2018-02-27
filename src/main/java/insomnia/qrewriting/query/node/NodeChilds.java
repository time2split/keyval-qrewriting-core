package insomnia.qrewriting.query.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import insomnia.qrewriting.query.Label;

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

	public void deleteChild(int id)
	{
		for (int i = 0; i < this.size(); i++)
		{
			Node n = this.get(i);

			if (n.getId() == id)
			{
				this.remove(i);
				return;
			}
		}
	}

	public Label[] getChildsLabel()
	{
		HashSet<Label> ret = new HashSet<>(this.size() * 2);

		for (Node n : this)
		{
			ret.add(n.getLabel());
		}
		return ret.toArray(new Label[0]);
	}

	public Map<Label, Integer> getChildsLabelCount()
	{
		HashMap<Label,Integer> ret = new HashMap<>(this.size() * 2);

		for (Node n : this)
		{
			Label l = n.getLabel();
			
			if(ret.containsKey(l))
			{
				ret.put(l,ret.get(l) +1 );
			}
			else {
				ret.put(l, 1);
			}
		}
		return ret;
	}

	public Node[] getChilds()
	{
		return this.toArray(new Node[0]);
	}

	public ArrayList<Node> getMultipleChilds()
	{
		ArrayList<Node> ret = new ArrayList<>(this.size());
		ArrayList<Node> unique = getUniqueChilds();

		for (Node c : getChilds())
		{
			if (!unique.contains(c))
			{
				ret.add(c);
			}
		}
		return ret;
	}

	public ArrayList<Node> getUniqueChilds()
	{
		ArrayList<Node> ret = new ArrayList<>(this.size());
		HashMap<Label, ArrayList<Node>> buff = new HashMap<>();

		for (Node c : getChilds())
		{
			ArrayList<Node> multi;
			Label label = c.getLabel();

			if (buff.containsKey(label))
			{
				multi = buff.get(label);
			}
			else
			{
				multi = getChildsList(label);
				buff.put(label, multi);
			}

			if (multi.size() <= 1 || !multi.contains(c))
				ret.add(c);
		}
		return ret;
	}

	public Node[] getChilds(Label l)
	{
		return getChildsList(l).toArray(new Node[0]);
	}

	private ArrayList<Node> getChildsList(Label l)
	{
		ArrayList<Node> ret = new ArrayList<>(this.size());

		for (Node c : getChilds())
		{
			Label cl = c.getLabel();

			if (cl.equals(l))
				ret.add(c);
		}
		return ret;
	}

	public Node getChild(Label l)
	{
		for (Node c : getChilds())
		{
			Label cl = c.getLabel();

			if (cl.equals(l))
				return c;
		}
		return null;
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
