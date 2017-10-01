package insomnia.qrewritingnorl1.query_rewriting.generator;

import java.util.Collection;
import java.util.Set;

import insomnia.qrewritingnorl1.node.Node;
import insomnia.qrewritingnorl1.node.NodeValue;
import insomnia.qrewritingnorl1.node.NodeValueExists;
import insomnia.qrewritingnorl1.query_rewriting.code.Context;
import insomnia.qrewritingnorl1.query_rewriting.query.Query;
import insomnia.qrewritingnorl1.query_rewriting.rule.RuleManager;

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

		if (!q.isUnfolded())
			throw new CodeGeneratorException("La requête doit être dépliée");

		for (Node n : q.getNodes())
		{
			String k = n.getLabel().get();
			RuleManager rapplicables = rm.getApplicables(k);
			NodeValue val = n.getValue();
			boolean existRule;

			// Je suis une feuille existentielle
			if (n.isLeaf() && (val instanceof NodeValueExists))
			{
				rapplicables = rm.getApplicables(k);
				existRule = true;
			}
			else
			{
				rapplicables = rm.getApplicablesOnlyRAll(k);
				existRule = false;
			}
			Set<String> applicables = rapplicables.getAllHypothesisWith(k);
			Context c = new Context(k, (Collection<String>) applicables);
			encoding.add(new NodeContext(n, c, existRule));
		}
	}
}
