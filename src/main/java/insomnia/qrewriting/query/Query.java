package insomnia.qrewriting.query;

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

	// =========================================================================

	/**
	 * @see Node#isUnfolded()
	 */
	public boolean isUnfolded()
	{
		return root.isUnfolded();
	}

	/**
	 * @see Node#getAllKeys()
	 */
	public Set<String> getAllKeys()
	{
		return root.getAllKeys();
	}

	/**
	 * @see Node#getNodes()
	 */
	public Node[] getNodes()
	{
		return root.getNodes();
	}

	/**
	 * @see Node#getNode(int)
	 */
	public Node getNode(int id)
	{
		return root.getNode(id);
	}

	/**
	 * Retourne le nombre de noeuds de la requête
	 */
	public int getNbNodes()
	{
		return root.getNbOfDescendant();
	}
}
