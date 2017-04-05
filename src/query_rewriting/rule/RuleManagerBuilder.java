package query_rewriting.rule;

import java.io.File;

import reader.Reader;

/**
 * Constructeur de RuleManager
 * 
 * @author zuri
 * 
 */
abstract public class RuleManagerBuilder extends Reader
{
	protected RuleManager	rm;

	public RuleManagerBuilder(RuleManager rman)
	{
		setRuleManager(rman);
	}

	public RuleManagerBuilder(RuleManager rman, File f)
	{
		super(f);
		setRuleManager(rman);
	}

	public void setRuleManager(RuleManager rman)
	{
		rm = rman;
	}

	abstract public void build() throws RuleManagerBuilderException;
}
