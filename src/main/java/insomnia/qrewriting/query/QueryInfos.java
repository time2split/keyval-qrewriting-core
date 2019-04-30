package insomnia.qrewriting.query;

import java.util.ArrayList;
import java.util.List;

import insomnia.qrewriting.query.node.Node;

public class QueryInfos
{
	private Query query;

	/**
	 * Tous les noeuds de la requête
	 */
	List<Node> nodes;
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

	public void setQuery(Query q)
	{
		query = q;
	}

	public int nextId()
	{
		return nextId++;
	}

	public Query getQuery()
	{
		return query;
	}

	// ==========================================================

	public void addNode(Node... nodes)
	{
		for (Node n : nodes)
			addNode(n);
	}

	public void addNode(Node n)
	{
		addNode(n, true);
	}

	public void addNode(Node n, boolean changeId)
	{
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

	public void addQuery(Query q)
	{
		addNode(q, false);
		nodes.remove(0);
	}

	// public Node[] getTrees()
	// {
	// if (trees == null)
	// trees = query.s_getTrees();
	//
	// return Arrays.copyOf(trees,trees.length);
	// }
	//
	// public Node[] getPaths()
	// {
	// if (paths == null)
	// paths = query.getPaths();
	//
	// return Arrays.copyOf(paths,paths.length);
	// }
}
