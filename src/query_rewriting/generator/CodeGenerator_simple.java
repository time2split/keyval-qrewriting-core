package query_rewriting.generator;

import java.util.Collection;
import java.util.Set;

import query_rewriting.code.Context;
import query_rewriting.query.Query;
import query_rewriting.query.node.Node;
import query_rewriting.query.node.NodeValue;
import query_rewriting.query.node.NodeValueExists;
import query_rewriting.rule.RuleManager;

/**
 * Implémentation avec algo simple
 * 
 * @author zuri
 * 
 */
public class CodeGenerator_simple extends CodeGenerator
{

	public CodeGenerator_simple(Query q, RuleManager rm)
			throws CodeGeneratorException
	{
		super(q, rm);

		if ( !q.isUnfolded() )
			throw new CodeGeneratorException("La requête doit être dépliée");

		for ( Node n : q.getNodes() )
		{
			String k = n.getLabel().get();
			RuleManager rapplicables = rm.getApplicables(k);
			NodeValue val = n.getValue();

			// Je suis une feuille existentielle
			if ( n.isLeaf() && ( val instanceof NodeValueExists ) )
				rapplicables = rm.getApplicables(k);
			else
				rapplicables = rm.getApplicablesOnlyRAll(k);

			Set<String> applicables = rapplicables.getAllHypothesisWith(k);
			Context c = new Context(k, (Collection<String>) applicables);
			encoding.add(new NodeContext(n, c));
		}
	}
}
