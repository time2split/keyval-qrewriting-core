package insomnia.qrewriting.qpu;

import java.util.ArrayList;
import java.util.Collection;

import insomnia.numeric.Interval;
import insomnia.qrewriting.code.Code;
import insomnia.qrewriting.code.CodeContext;
import insomnia.qrewriting.code.Encoding;
import insomnia.qrewriting.context.Context;
import insomnia.qrewriting.context.HasContext;
import insomnia.qrewriting.generator.NodeContext;
import insomnia.qrewriting.query.Query;
import insomnia.qrewriting.query.node.Node;

/**
 * Implémentation d'un qpu avec algo simple
 * 
 * @author zuri
 */
public class QPUSimple extends QPU implements HasContext
{
	Context  context;
	Query    query;
	Code[]   codes;
	Encoding encoding;

	public QPUSimple(Context context, Query q, Encoding e)
	{
		this(context, q, e.generateAllCodes(), e);
	}

	public QPUSimple(Context context, Query q, Interval i, Encoding e)
	{
		query    = q;
		encoding = e;
		codes    = encoding.generateAllCodes(i);
		setContext(context);
	}

	public QPUSimple(Context context, Query q, Code[] c, Encoding e)
	{
		query    = q;
		codes    = c;
		encoding = e;
		setContext(context);
	}

	public QPUSimple(Context context, Query q, Collection<Code> codesset, Encoding e)
	{
		this(context, q, codesset.toArray(new Code[0]), e);
	}

	@Override
	public void setContext(Context context)
	{
		this.context = context;
	}

	@Override
	public Context getContext()
	{
		return context;
	}

	@Override
	public ArrayList<Query> process()
	{
		ArrayList<Query> ret       = new ArrayList<>(codes.length);
		int              nbNodes   = query.getNbOfDescendants() + 1;
		int              nbCodePos = nbNodes - 1;

		for (Code code : codes)
		{
			Query q = new Query(query);
			ret.add(q);

			for (int pos = 0; pos < nbCodePos; pos++)
			{
				NodeContext nc       = encoding.get(pos);
				Node        qRefNode = nc.getNode();
				int         id       = qRefNode.getId();

				CodeContext ctx = nc.getContext();

				Node myNode = q.getNode(id);

				myNode.setLabel(context.getLabelFactory().from(ctx.getK(code.getCode(pos))));
			}
		}
		return ret;
	}
}
