package insomnia.qrewriting.query;

import java.util.Collection;

import insomnia.qrewriting.query.node.Node;

/**
 * Noeud représentant une requête Ce noeud à la particularité de définir un
 * nouvel objet NodeInfos et de se baser sur celui-ci pour ces calculs
 * 
 * @author zuri
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

	@Override
	public String toString()
	{
		return "Query[" + getNbOfDescendants() + "](" + super.childsToString() + ")";
	}

	// =========================================================================

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
