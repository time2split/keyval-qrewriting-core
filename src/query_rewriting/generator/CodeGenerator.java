package query_rewriting.generator;

import query_rewriting.code.ContextManager;
import query_rewriting.code.Encoding;
import query_rewriting.query.Query;
import query_rewriting.rule.RuleManager;

abstract public class CodeGenerator
{
	private Query				query;
	private RuleManager			rm;
	protected ContextManager	cm			= new ContextManager();
	protected Encoding			encoding	= new Encoding();

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

	public ContextManager getContextManager()
	{
		return cm;
	}

	public Encoding getEncoding()
	{
		return encoding;
	}
}
