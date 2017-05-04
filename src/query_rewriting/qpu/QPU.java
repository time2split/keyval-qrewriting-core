package query_rewriting.qpu;

import java.util.ArrayList;
import java.util.Collection;

import query_rewriting.code.Code;
import query_rewriting.code.ContextManager;
import query_rewriting.code.Encoding;
import query_rewriting.query.Query;

/**
 * Unité de calcul prenant un ensemble de codes et renvoyant un ensemble de
 * requêtes
 * 
 * @author zuri
 * 
 */
abstract public class QPU
{
	protected Query				query;
	protected Collection<Code>	codes;
	protected ContextManager	cm;
	protected Encoding			encoding;

	public QPU(Query q, Collection<Code> codesset, Encoding e,
			ContextManager cman

	)
	{
		codes = codesset;
		cm = cman;
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
