package insomnia.qrewriting.query;

import java.util.Collection;

import insomnia.qrewriting.query.node.Node;

/**
 * Noeud représentant une requête.
 * Ce noeud à la particularité de définir un objet NodeInfos qui sera accessible par tous les noeuds de la requête.
 * 
 * @author zuri
 */
public class Query extends Node
{
	private void initInfos()
	{
		infos = new QueryInfos();
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
		infos = new QueryInfos(q.infos);
		infos.addQuery(this);
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

	@Override
	public String toString()
	{
		return "Query[" + getNbOfDescendants() + "](" + super.childsToString() + ")";
	}

	// =========================================================================

}
