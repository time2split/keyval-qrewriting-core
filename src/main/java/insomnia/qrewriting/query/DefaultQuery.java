package insomnia.qrewriting.query;

import insomnia.qrewriting.query.node.DefaultNode;
import insomnia.qrewriting.query.node.Node;

/**
 * Noeud représentant une requête.
 * Ce noeud à la particularité de définir un objet NodeInfos qui sera accessible par tous les noeuds de la requête.
 * 
 * @author zuri
 */
public class DefaultQuery implements Query
{
	private Node       root;
	private QueryInfos infos;

	private void initInfos()
	{
		infos = new QueryInfos();
		setInfos(infos);
	}

	public DefaultQuery()
	{
		this(new DefaultNode());
	}

	public DefaultQuery(Query q)
	{
		setInfos(new QueryInfos(q.getInfos()));

		Node root = new DefaultNode(this, q.getRoot());

		setRoot(root);
		addNode(false, root);
	}

	private DefaultQuery(Node root)
	{
		initInfos();
		setRoot(root);
		root.setQuery(this);
		addNode(true, root);
	}

	// =========================================================================

	@Override
	public Node getRoot()
	{
		return root;
	}

	@Override
	public QueryInfos getInfos()
	{
		return infos;
	}

	// =========================================================================

	public void setRoot(Node root)
	{
		this.root = root;
	}

	private void setInfos(QueryInfos infos)
	{
		this.infos = infos;
	}

	// =========================================================================

	@Override
	public void addNode(boolean generateNodeId, Node... nodes)
	{
		infos.addNode(generateNodeId, nodes);

		for (Node node : nodes)
			node.setQuery(this);
	}

	@Override
	public String toString()
	{
		return "Query[" + root.getNbOfDescendants() + "](" + root.childsToString() + ")";
	}

	// =========================================================================

//	@Override
//	public Node[] getPaths()
//	{
//		if (infos.paths == null)
//			infos.paths = super.getPaths();
//
//		return infos.paths;
//	}
//
//	@Override
//	public Node[] getTrees()
//	{
//		if (infos.trees == null)
//			infos.trees = super.getTrees();
//
//		return infos.trees;
//	}
}
