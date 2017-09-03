package insomnia.qrewritingnorl1.query_rewriting.qpu;

import java.util.ArrayList;
import java.util.Collection;

import insomnia.qrewritingnorl1.query_rewriting.code.Code;
import insomnia.qrewritingnorl1.query_rewriting.code.Encoding;
import insomnia.qrewritingnorl1.query_rewriting.query.Query;

/**
 * Unité de calcul prenant un ensemble de codes et renvoyant un ensemble de
 * requêtes
 * 
 * @author zuri
 * 
 */
abstract public class QPU
{
	protected Query		query;
	protected Code[]	codes;
	protected Encoding	encoding;

	public QPU(Query q, Collection<Code> codesset, Encoding e)
	{
		this(q, codesset.toArray(new Code[0]), e);
	}

	public QPU(Query q, Code[] codesset, Encoding e)
	{
		codes = codesset;
		encoding = e;
		query = q;
	}

	/**
	 * Réécriture
	 * 
	 * @return Les requêtes réécrites
	 */
	abstract public ArrayList<Query> process();
}
