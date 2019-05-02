package insomnia.qrewriting.query.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import insomnia.builder.BuilderException;
import insomnia.qrewriting.query.Label;
import insomnia.qrewriting.query.Query;

/**
 * Childs of a node.
 * 
 * @author zuri
 */
public class NodeChilds implements Iterable<Node>
{
	ArrayList<Node> childs;

	protected NodeChilds(int nb)
	{
		childs = new ArrayList<>(nb);
	}

	public NodeChilds()
	{
		childs = new ArrayList<>();
	}

	public NodeChilds copy(Query query, Node parent)
	{
		NodeChilds  ret      = new NodeChilds(childs.size());
		NodeBuilder nbuilder = new NodeBuilder(parent.getClass());
		nbuilder.setTheQuery(query);
		nbuilder.generateNodeId(false);

		try
		{
			for (Node tmp : childs)
			{
				nbuilder.root(tmp).setParent(parent).build();
				ret.childs.add(nbuilder.getNode());
			}
		}
		catch (BuilderException e)
		{
			throw new RuntimeException(e);
		}
		return ret;
	}

	// ============================================================
	// PROTECTED

	protected void add(Node... nodes)
	{
		for (Node n : nodes)
			childs.add(n);
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
		ArrayList<Node> ret    = new ArrayList<>(this.size());
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
		ArrayList<Node>                 ret  = new ArrayList<>(this.size());
		HashMap<Label, ArrayList<Node>> buff = new HashMap<>();

		for (Node c : getChilds())
		{
			ArrayList<Node> multi;
			Label           label = c.getLabel();

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
