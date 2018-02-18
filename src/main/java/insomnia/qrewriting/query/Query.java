package insomnia.qrewriting.query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import insomnia.qrewriting.query.node.Node;

/**
 * Une requête
 * 
 * @author zuri
 * 
 */
public class Query implements Cloneable
{
	private Node root;

	public Query()
	{

	}

	/**
	 * Constructeur par copie
	 * 
	 * @param q
	 */
	public Query(Query q)
	{
		if (q.root != null)
			setRoot(q.root.clone());
	}

	public Query(Node root)
	{
		setRoot(root);
	}

	@Override
	public Query clone()
	{
		return new Query(this);
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

	/**
	 * 
	 * @return Toutes les clés de la requête (labels)
	 */
	public Set<String> getAllKeys()
	{
		Set<String> ret = new HashSet<>();

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
	 * 
	 * @return Tous les noeuds sauf la racine
	 */
	public ArrayList<Node> getNodes()
	{
		ArrayList<Node> ret = new ArrayList<>();
		s_getNodes(ret, root);
		ret.remove(root);
		return ret;
	}

	/**
	 * 
	 * @return Le noeud dont l'identifiant est $id
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
