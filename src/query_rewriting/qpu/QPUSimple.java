package query_rewriting.qpu;

import java.util.ArrayList;
import java.util.Collection;

import query_rewriting.code.Code;
import query_rewriting.code.Context;
import query_rewriting.code.ContextManager;
import query_rewriting.code.Encoding;
import query_rewriting.generator.NodeContext;
import query_rewriting.query.Label;
import query_rewriting.query.Query;
import query_rewriting.query.node.Node;

/**
 * Implémentation d'un qpu avec algo simple
 * 
 * @author zuri
 * 
 */
public class QPUSimple extends QPU
{

	public QPUSimple(Query query, Collection<Code> codesset, Encoding e,
			ContextManager cman)
	{
		super(query, codesset, e, cman);
	}

	@Override
	public ArrayList<Query> process()
	{
		ArrayList<Query> ret = new ArrayList<>(codes.size());
		int nbNodes = query.getNodes().size() + 1;
		int nbCodePos = nbNodes - 1;

		for (Code code : codes)
		{
			Query q = query.clone();
			ret.add(q);

			for (int pos = 0; pos < nbCodePos; pos++)
			{
				NodeContext nc = encoding.get(pos);
				Node qRefNode = nc.getNode();
				int id = qRefNode.getId();

				Context ctx = nc.getContext();

				Node myNode = q.getNode(id);

				myNode.setLabel(new Label(ctx.getK(code.getCode(pos))));
			}
		}
		return ret;
	}
}
