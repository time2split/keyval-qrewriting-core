package insomnia.qrewriting.qpu;

import java.util.ArrayList;

import insomnia.numeric.Interval;
import insomnia.qrewriting.code.Code;
import insomnia.qrewriting.code.CodeContext;
import insomnia.qrewriting.code.Encoding;
import insomnia.qrewriting.context.Context;
import insomnia.qrewriting.context.HasContext;
import insomnia.qrewriting.generator.NodeContext;
import insomnia.qrewriting.query.DefaultQuery;
import insomnia.qrewriting.query.Query;
import insomnia.qrewriting.query.node.Node;

/**
 * Implémentation d'un qpu avec algo simple
 * 
 * @author zuri
 */
public class QPUSimple implements QPU, HasContext
{
	Context  context;
	Query    query;
	Encoding encoding;
	Interval interval;

	public QPUSimple(Context context, Query q, Encoding e)
	{
		this(context, q, e.generateCodeInterval(), e);
	}

	public QPUSimple(Context context, Query q, Interval i, Encoding e)
	{
		query    = q;
		encoding = e;
		interval = i;
		setContext(context);
	}

	protected void setContext(Context context)
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
		int              nbCodes   = (int) interval.size();
		ArrayList<Query> ret       = new ArrayList<>(nbCodes);
		int              nbNodes   = query.getRoot().getNbOfDescendants() + 1;
		int              nbCodePos = nbNodes - 1;
		Code             code      = encoding.getCodeFrom((int) interval.geta());

		while (nbCodes-- > 0)
		{
			Query q = new DefaultQuery(query);
			ret.add(q);

			for (int pos = 0; pos < nbCodePos; pos++)
			{
				NodeContext nc = encoding.get(pos);
				int         id = nc.getNodeId();

				CodeContext ctx = nc.getContext();

				Node myNode = q.getRoot().getNode(id);

				myNode.setLabel(context.getLabelFactory().from(ctx.getK(code.getCode(pos))));
			}
			code.add(1);
		}
		return ret;
	}
}
