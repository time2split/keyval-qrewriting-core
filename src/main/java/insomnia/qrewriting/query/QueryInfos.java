package insomnia.qrewriting.query;

import java.util.ArrayList;

import insomnia.qrewriting.query.node.Node;

public class QueryInfos implements Cloneable
{
	protected Query	query;

	/**
	 * Tous les noeuds de la requête
	 */
	ArrayList<Node>	nodes	= new ArrayList<>(100);
	Node[]			trees;
	Node[]			paths;
	int				nextId	= 0;

	public QueryInfos()
	{

	}

	public QueryInfos(QueryInfos infos)
	{

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

	// public Query getQuery()
	// {
	// return query;
	// }

	// ==========================================================

	public void addNode(Node... nodes)
	{
		for (Node n : nodes)
			addNode(n);
	}

	public void addNode(Node n)
	{
		trees = null;
		paths = null;
		nodes.add(n);
		n.setId(nextId());
		
		for (Node desc : n.getDescendants())
		{
			nodes.add(desc);
			desc.setId(nextId());
		}
	}

	// public List<Node> getNodes()
	// {
	// return nodes;
	// }

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

	// ==========================================================

	@Override
	public QueryInfos clone()
	{
		return new QueryInfos((QueryInfos) this);
	}
}
