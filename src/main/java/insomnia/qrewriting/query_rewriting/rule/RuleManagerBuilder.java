package insomnia.qrewriting.query_rewriting.rule;

import insomnia.builder.Builder;
import insomnia.builder.BuilderException;

/**
 * Constructeur de RuleManager
 * 
 * @author zuri
 * 
 */
abstract public class RuleManagerBuilder extends Builder
{
	public RuleManagerBuilder()
	{
	}

	public RuleManagerBuilder(RuleManager rman)
	{
		setRuleManager(rman);
	}

	public RuleManager getRuleManager()
	{
		return (RuleManager) getBuilded();
	}

	public void setRuleManager(RuleManager rman)
	{
		setBuilded(rman);
	}

	// @Override
	// abstract public void build() throws RuleManagerBuilderException;

	@Override
	abstract public RuleManager newBuild() throws BuilderException;
}
