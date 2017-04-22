package query_rewriting.rule;

import reader.Reader;
import builder.Builder;

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

	@Override
	abstract public void build() throws RuleManagerBuilderException;
}
