package query_rewriting.qpu;

import java.util.ArrayList;
import java.util.Set;

import query_rewriting.code.Code;
import query_rewriting.code.ContextManager;
import query_rewriting.code.Encoding;
import query_rewriting.query.Query;

abstract public class QPU
{
	protected Query				query;
	protected ArrayList<Code>	codes;
	protected ContextManager	cm;
	protected Encoding			encoding;

	public QPU(Query q, ArrayList<Code> codesset, ContextManager cman,
			Encoding e)
	{
		codes = codesset;
		cm = cman;
		encoding = e;
		query = q;
	}

	abstract public ArrayList<Query> process();
}
