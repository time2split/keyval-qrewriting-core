package insomnia.qrewriting.query;

import java.util.Collection;
import java.util.List;

import insomnia.qrewriting.query.node.Node;

/**
 * Noeud représentant une requête Ce noeud à la particularité de définir un
 * nouvel objet NodeInfos et de se baser sur celui-ci pour ces calculs
 * 
 * @author zuri
 * 
 */
public class Query extends Node
{

	private void initInfos()
	{
		infos = new QueryInfos();
		setLabel((Label) null);
		setInfos(infos);
		infos.setQuery(this);
	}

	public Query()
	{
		super();
		initInfos();
		setId(infos.nextId());
	}

	public Query(Query q)
	{
		super(q);
		infos = q.infos.clone();
		s_getNodes(infos.nodes, q);
		infos.nodes.remove(0);
	}
	
	// =========================================================================
	
	
	
	// =========================================================================

	@Override
	public Query addChildMe(Collection<Node> childs)
	{
		super.addChild(childs);
		return this;
	}

	@Override
	public Query addChildMe(Node... childs)
	{
		super.addChild(childs);
		return this;
	}

	@Override
	public Query newChildMe()
	{
		return addChildMe(new Node());
	}

	public Query copy(Query q)
	{
		Query ret = new Query();
		infos = new QueryInfos(q.infos);
		ret.setInfos(infos);
		ret.infos.setQuery(this);
		return ret;
	}

//	@Override
//	public Query clone()
//	{
//		return new Query(this);
//	}

	@Override
	public String toString()
	{
		return "Query[" + getNbOfDescendants() + "](" + super.childsToString()
				+ ")";
	}

	// =========================================================================

	/**
	 * Retourne tout les noeuds enfants de query
	 */
	@Override
	public Node[] getDescendants()
	{
		List<Node> nodes = infos.nodes;
//		System.out.println(nodes.size() + " " + getNbOfDescendants());
//		System.out.println(this + "\n");

		assert nodes
				.size() == getNbOfDescendants() : "infos.nodes must have the same number of childs as Query ("
						+ nodes.size() + "," + getNbOfDescendants() + ")";

		return nodes.toArray(new Node[getNbOfDescendants()]);
	}

	@Override
	public Node[] getPaths()
	{
		if (infos.paths == null)
			infos.paths = super.getPaths();

		return infos.paths;
	}

	@Override
	public Node[] getTrees()
	{
		if (infos.trees == null)
			infos.trees = super.getTrees();

		return infos.trees;
	}
}
