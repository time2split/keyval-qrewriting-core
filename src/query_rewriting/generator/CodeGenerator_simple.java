package query_rewriting.generator;

import java.util.Collection;
import java.util.Set;

import query_rewriting.code.Context;
import query_rewriting.query.Query;
import query_rewriting.query.node.Node;
import query_rewriting.rule.RuleManager;

public class CodeGenerator_simple extends CodeGenerator
{

	public CodeGenerator_simple(Query q, RuleManager rm)
			throws CodeGeneratorException
	{
		super(q, rm);

		if (!q.isUnfolded())
			throw new CodeGeneratorException("La requête doit être dépliée");

		for (Node n : q.getNodes())
		{
			String k = n.getLabel().get();

			Set<String> applicables = rm.getApplicables(k).getAllHypothesisWith(k);
			cm.put(k, new Context(k, (Collection<String>) applicables));

			Context c = cm.get(k);
			encoding.add(new NodeContext(n, c));
		}
	}
}
