package insomnia.qrewritingnorl1.query_building.mongo;

import java.util.HashMap;

import insomnia.json.Element;
import insomnia.json.ElementLiteral;
import insomnia.json.ElementNumber;
import insomnia.json.ElementObject;
import insomnia.json.ElementString;
import insomnia.json.Json;
import insomnia.qrewritingnorl1.node.Node;
import insomnia.qrewritingnorl1.node.NodeChilds;
import insomnia.qrewritingnorl1.node.NodeValueExists;
import insomnia.qrewritingnorl1.node.NodeValueLiteral;
import insomnia.qrewritingnorl1.node.NodeValueNumber;
import insomnia.qrewritingnorl1.node.NodeValueString;
import insomnia.qrewritingnorl1.query_rewriting.query.Label;
import insomnia.qrewritingnorl1.query_rewriting.query.Query;
import insomnia.qrewritingnorl1.query_rewriting.query.QueryBuilder;
import insomnia.qrewritingnorl1.query_rewriting.query.QueryBuilderException;

/**
 * Construit une requête à partir d'un document Json
 * 
 * @author zuri
 * 
 */
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

	public void setQuery(Query q)
	{
		setBuilded(q);
	}

	@Override
	public Query newBuild() throws QueryBuilderException
	{
		Query ret = new Query();
		setQuery(ret);
		build();
		return ret;
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

		s_validate_obj((ElementObject) e, State.ROOT);
	}

	static private void s_validate_obj(ElementObject e)
			throws QueryBuilderException
	{
		s_validate_obj(e, State.ELEM);
	}

	enum State
	{
		ROOT, ELEM_MATCH, ELEM
	}

	/**
	 * 
	 * @param e
	 * @param state
	 *            1 : elemMatch
	 * @throws QueryBuilderException
	 */
	static private void s_validate_obj(ElementObject e, State state)
			throws QueryBuilderException
	{
		HashMap<String, Element> childs = e.getObject();
		final int cnt = childs.size();
		boolean noElem = true;

		if (state == State.ELEM && cnt > 1)
		{
			throw new QueryBuilderException("Un objet ne peut être utilisé que avec $elemMatch ou $exists (représentation de valeur objet impossible)");
		}

		for (String k : childs.keySet())
		{
			Element c = childs.get(k);

			switch (k)
			{
			case "$elemMatch":

				if (state == State.ELEM_MATCH)
					throw new QueryBuilderException("$elemMatch imbriquées impossible");

				if (!c.isObject())
					throw new QueryBuilderException("$elemMatch doit être un object");

				noElem = false;
				s_validate_obj((ElementObject) c, State.ELEM_MATCH);
				break;

			case "$exists":

				if (state == State.ELEM_MATCH)
					throw new QueryBuilderException("$exists dans $elemMatch impossible");

				if (cnt > 1)
					throw new QueryBuilderException("$exists doit être seul (feuille)");

				if (!c.isLiteral()
						|| ((ElementLiteral) c).getLiteral() != ElementLiteral.Literal.TRUE)
					throw new QueryBuilderException("$exists doit être un literal 'true'");

				noElem = false;
				break;

			default:

				if (c.isObject())
					s_validate_obj((ElementObject) c);
				else if (c.isString())
					;
				else if (c.isLiteral())
					;
				else if (c.isNumber())
					;
				else
					throw new QueryBuilderException("Seul les éléments objets, string, literal et number sont pris en compte");

			}
		}

		if (noElem && state == State.ELEM)
		{
			throw new QueryBuilderException("Un objet ne peut être utilisé que avec $elemMatch ou $exists (représentation de valeur objet impossible)");
		}
	}

	@Override
	public void build() throws QueryBuilderException
	{
		if (doc == null)
			throw new QueryBuilderException("Builder Json : pas de document Json en paramètre");

		s_validate(doc);
		Query query = getQuery();
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
				Node cn = null;
				String[] tmp = k.split("\\.");

				if (tmp.length > 1)
				{
					NodeChilds tmpch = childs;

					for (int i = 0; i < tmp.length; i++)
					{
						cn = new Node(new Label(tmp[i]));
						cn.setId(lastNodeId++);
						tmpch.add(cn);
						tmpch = cn.getChilds();
					}
				}
				else
				{
					cn = new Node(new Label(k));
					cn.setId(lastNodeId++);
					childs.add(cn);
				}

				if (c.isObject())
					s_build_obj((ElementObject) c, cn);
				else if (c.isString())
					cn.setValue(new NodeValueString(((ElementString) c).getString()));
				else if (c.isLiteral())
					cn.setValue(new NodeValueLiteral(((ElementLiteral) c).toString()));
				else if (c.isNumber())
					cn.setValue(new NodeValueNumber(((ElementNumber) c).getNumber()));
				else
					throw new QueryBuilderException("??? revoir la validation");
			}
		}
	}
}
