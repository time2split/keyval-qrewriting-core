package query_rewriting.generator;

import query_rewriting.code.Encoding;
import query_rewriting.query.Query;
import query_rewriting.rule.RuleManager;

/**
 * Construit le ContextManager et l'Encoding à partir d'une requête et de ses
 * règles de réécriture
 * 
 * @author zuri
 * 
 */
abstract public class CodeGenerator
{
	private Query		query;
	private RuleManager	rm;
	protected Encoding	encoding	= new Encoding();

	public CodeGenerator(Query q, RuleManager rman)
			throws CodeGeneratorException
	{
		query = q;
		rm = rman;
	}

	public Query getQuery()
	{
		return query;
	}

	public RuleManager getRuleManager()
	{
		return rm;
	}

	public Encoding getEncoding()
	{
		return encoding;
	}
}
