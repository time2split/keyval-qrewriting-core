package query_rewriting.query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import query_rewriting.query.node.Node;

public class Query implements Cloneable
{
	private Node	root;

	public Query()
	{

	}

	@Override
	public Object clone()
	{
		Query q = new Query();
		q.setRoot((Node) root.clone());
		return q;
	}

	public void setRoot(Node r)
	{
		root = r;
	}

	public Node getRoot()
	{
		return root;
	}

	@Override
	public String toString()
	{
		return "Query(" + root + ")";
	}

	public boolean isUnfolded()
	{
		for (Node n : getNodes())
		{
			if (n.getLabel().size() > 1)
				return false;
		}
		return true;
	}

	public Set<String> getAllKeys()
	{
		Set<String> ret = new HashSet<String>();

		for (Node n : getNodes())
		{
			Label l = n.getLabel();

			if (l != null)
			{
				for (String k : l)
					ret.add(k);
			}
		}
		return ret;
	}

	/**
	 * Retourne tous les noeuds sauf la racine
	 * 
	 * @return
	 */
	public ArrayList<Node> getNodes()
	{
		ArrayList<Node> ret = new ArrayList<Node>();
		s_getNodes(ret, root);
		ret.remove(root);
		return ret;
	}

	/**
	 * Récupère un noeud par son identifiant
	 * 
	 * @return
	 */
	public Node getNode(int id)
	{
		for (Node n : getNodes())
		{
			if (n.getId() == id)
				return n;
		}
		return null;
	}

	private void s_getNodes(ArrayList<Node> ret, Node n)
	{
		ret.add(n);

		for (Node c : n.getChilds().getChilds())
			s_getNodes(ret, c);
	}
}
