package insomnia.qrewriting.rule;

import insomnia.builder.AbstractBuilder;
import insomnia.builder.BuilderException;
import insomnia.qrewriting.context.Context;
import insomnia.qrewriting.context.HasContext;

/**
 * Constructeur de RuleManager
 * 
 * @author zuri
 */
abstract public class RuleManagerBuilder extends AbstractBuilder<RuleManager> implements HasContext
{
	private Context context;

	public RuleManagerBuilder(Context context)
	{
		setContext(context);
	}

	public RuleManagerBuilder(Context context, RuleManager rman)
	{
		setContext(context);
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

	protected void setContext(Context context)
	{
		this.context = context;
	}

	@Override
	public Context getContext()
	{
		return context;
	}

	// @Override
	// abstract public void build() throws RuleManagerBuilderException;

	@Override
	abstract public RuleManager newBuild() throws BuilderException;
}
