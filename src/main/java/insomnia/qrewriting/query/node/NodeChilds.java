package insomnia.qrewriting.query.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import insomnia.qrewriting.query.Label;

/**
 * Enfants d'un noeud
 * 
 * @author zuri
 * 
 */
public class NodeChilds implements Iterable<Node>
{
	ArrayList<Node> childs;

	protected NodeChilds(int nb)
	{
		childs = new ArrayList<>(nb);
	}

	public NodeChilds copy(Node parent)
	{
		NodeChilds ret = new NodeChilds(childs.size());

		for (Node tmp : childs)
		{
			Node nn = new Node(tmp);
			nn.setParent(parent);
			ret.childs.add(nn);
		}
		return ret;
	}

	public NodeChilds()
	{
		childs = new ArrayList<>();
	}

	// ============================================================
	// PROTECTED

//	protected void setChildsParent(Node parent)
//	{
//		for (Node n : this)
//			n.setParent(parent);
//	}

//	protected void deleteChild(int id)
//	{
//		final int size = size();
//
//		for (int i = 0; i < size; i++)
//		{
//			Node n = childs.get(i);
//
//			if (n.getId() == id)
//			{
//				childs.remove(i);
//				return;
//			}
//		}
//	}

	protected void add(Node... nodes)
	{
		for (Node n : nodes)
			childs.add(n);
	}

	protected NodeChilds addMe(Node... nodes)
	{
		add(nodes);
		return this;
	}

	// ============================================================

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
		HashMap<Label, Integer> ret = new HashMap<>(this.size() * 2);

		for (Node n : childs)
		{
			Label l = n.getLabel();

			if (ret.containsKey(l))
			{
				ret.put(l, ret.get(l) + 1);
			}
			else
			{
				ret.put(l, 1);
			}
		}
		return ret;
	}

	public Node[] getChilds()
	{
		return childs.toArray(new Node[0]);
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

			if (cl == l || cl.equals(l))
				return c;
		}
		return null;
	}

	// =======================================================

	public int size()
	{
		return childs.size();
	}

	@Override
	public Iterator<Node> iterator()
	{
		return childs.iterator();
	}
}
