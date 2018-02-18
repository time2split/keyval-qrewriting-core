package insomnia.qrewriting.qpu;

import java.util.ArrayList;
import java.util.Collection;

import insomnia.numeric.Interval;
import insomnia.qrewriting.code.Code;
import insomnia.qrewriting.code.Context;
import insomnia.qrewriting.code.Encoding;
import insomnia.qrewriting.generator.NodeContext;
import insomnia.qrewriting.query.Label;
import insomnia.qrewriting.query.Query;
import insomnia.qrewriting.query.node.Node;

/**
 * Implémentation d'un qpu avec algo simple
 * 
 * @author zuri
 * 
 */
public class QPUSimple extends QPU
{

	Query		query;
	Code[]		codes;
	Encoding	encoding;

	public QPUSimple(Query q, Encoding e)
	{
		this(q, e.generateAllCodes(), e);
	}

	public QPUSimple(Query q, Interval i, Encoding e)
	{
		query = q;
		encoding = e;
		codes = encoding.generateAllCodes(i);
	}

	public QPUSimple(Query q, Collection<Code> codesset, Encoding e)
	{
		this(q, codesset.toArray(new Code[0]), e);
	}

	public QPUSimple(Query q, Code[] c, Encoding e)
	{
		query = q;
		codes = c;
		encoding = e;
	}

	@Override
	public ArrayList<Query> process()
	{
		ArrayList<Query> ret = new ArrayList<>(codes.length);
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
