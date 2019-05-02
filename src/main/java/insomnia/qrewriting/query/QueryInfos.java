package insomnia.qrewriting.query;

import java.util.ArrayList;
import java.util.List;

import insomnia.qrewriting.query.node.Node;

public final class QueryInfos
{
//	Node[]			trees;
//	Node[]			paths;

	/**
	 * Tous les noeuds de la requête
	 */
	List<Node> nodes;

	/**
	 * L'identifiant d'arc suivant
	 */
	int nextId;

	public QueryInfos()
	{
		nodes  = new ArrayList<>();
		nextId = 0;
	}

	public QueryInfos(QueryInfos infos)
	{
		this();
		nextId = infos.nextId;
	}

	// ==========================================================

	public int nextId()
	{
		return nextId++;
	}

	// ==========================================================

	public void addNode(boolean generateNodeId, Node... nodes)
	{
		for (Node n : nodes)
			addOneNode(n, generateNodeId);
	}

	private void addOneNode(Node n, boolean changeId)
	{
//		trees = null;
//		paths = null;
		nodes.add(n);

		if (changeId)
			n.setId(nextId());

		for (Node desc : n.getDescendants())
		{
			nodes.add(desc);

			if (changeId)
				desc.setId(nextId());
		}
	}

	public List<Node> getNodes()
	{
		return nodes;
	}

//	public Node[] getTrees()
//	{
//		if (trees == null)
//			trees = query.s_getTrees();
//
//		return Arrays.copyOf(trees, trees.length);
//	}
//
//	public Node[] getPaths()
//	{
//		if (paths == null)
//			paths = query.getPaths();
//
//		return Arrays.copyOf(paths, paths.length);
//	}
}
