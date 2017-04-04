package query_building;

import java.util.HashMap;

import json.Element;
import json.ElementLiteral;
import json.ElementObject;
import json.ElementString;
import json.Json;
import query_rewriting.query.Label;
import query_rewriting.query.Query;
import query_rewriting.query.QueryBuilder;
import query_rewriting.query.QueryBuilderException;
import query_rewriting.query.node.Node;
import query_rewriting.query.node.NodeChilds;
import query_rewriting.query.node.NodeValueExists;
import query_rewriting.query.node.NodeValueString;

public class QueryBuilder_json extends QueryBuilder
{
	private Json	doc;
	private int		lastNodeId	= 0;

	public QueryBuilder_json(Json d)
	{
		setJson(d);
	}

	public QueryBuilder_json(Query q, Json d)
	{
		super(q);
		setJson(d);
	}

	public void setJson(Json d)
	{
		doc = d;
	}

	/**
	 * Validation de l'arbre json
	 * 
	 * @param d
	 * @throws QueryBuilderException
	 */
	static private void s_validate(Json d) throws QueryBuilderException
	{
		Element e = d.getDocument();

		if (!e.isObject())
			throw new QueryBuilderException("Le document json doit commencer par un objet");

		s_validate_obj((ElementObject) e);
	}

	final static int	VAL_STATE_EMATCH	= 1;

	static private void s_validate_obj(ElementObject e)
			throws QueryBuilderException
	{
		s_validate_obj(e, 0);
	}

	/**
	 * 
	 * @param e
	 * @param state
	 *            1 : elemMatch
	 * @throws QueryBuilderException
	 */
	static private void s_validate_obj(ElementObject e, int state)
			throws QueryBuilderException
	{
		HashMap<String, Element> childs = e.getObject();
		int cnt = childs.size();

		for (String k : childs.keySet())
		{
			Element c = childs.get(k);

			switch (k)
			{
			case "$elemMatch":

				if (state == VAL_STATE_EMATCH)
					throw new QueryBuilderException("$elemMatch imbriquées impossible");

				if (!c.isObject())
					throw new QueryBuilderException("$elemMatch doit être un object");

				s_validate_obj((ElementObject) c, VAL_STATE_EMATCH);
				break;

			case "$exists":

				if (state == VAL_STATE_EMATCH)
					throw new QueryBuilderException("$exists dans $elemMatch impossible");

				if (cnt > 1)
					throw new QueryBuilderException("$exists doit être seul (feuille)");

				if (!c.isLiteral()
						|| ((ElementLiteral) c).getLiteral() != ElementLiteral.TRUE)
					throw new QueryBuilderException("$exists doit être un literal 'true'");

				break;

			default:

				if (c.isObject())
					s_validate_obj((ElementObject) c);
				else if (c.isString())
					;
				else
					throw new QueryBuilderException("Seul les éléments objets ou string sont pris en compte");

			}
		}
	}

	@Override
	public void build() throws QueryBuilderException
	{
		s_validate(doc);
		Node n = query.getRoot();

		if (n == null)
		{
			n = new Node();
			n.setId(lastNodeId++);
			query.setRoot(n);
		}
		s_build_obj((ElementObject) doc.getDocument(), n);
	}

	private void s_build_obj(ElementObject e, Node n)
			throws QueryBuilderException
	{
		HashMap<String, Element> obj = e.getObject();
		NodeChilds childs = n.getChilds();

		for (String k : obj.keySet())
		{
			Element c = obj.get(k);

			switch (k)
			{
			case "$elemMatch":
				s_build_obj((ElementObject) c, n);
				break;

			case "$exists":
				n.setValue(new NodeValueExists());
				break;

			default:
				Node cn = new Node(new Label(k));
				cn.setId(lastNodeId++);
				childs.add(cn);

				if (c.isObject())
					s_build_obj((ElementObject) c, cn);
				else if (c.isString())
					cn.setValue(new NodeValueString(((ElementString) c).getString()));
				else
					throw new QueryBuilderException("??? revoir la validation");
			}
		}
	}
}
