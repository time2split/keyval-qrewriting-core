package insomnia.qrewriting.database.driver.internal.query;

import insomnia.qrewriting.context.Context;
import insomnia.qrewriting.query.node.DefaultNode;

public class MyNodeOperation extends DefaultNode
{
	public enum Operation
	{
		Or
	};

	Operation operation;

	public MyNodeOperation(Context context, Operation op)
	{
		super();
		setLabel(context.getLabelFactory().from(op.toString()));
		setOperation(op);
	}

	public void setOperation(Operation op)
	{
		operation = op;
	}
}
