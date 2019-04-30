package insomnia.qrewriting.database.driver.internal.query;

import insomnia.qrewriting.context.Context;
import insomnia.qrewriting.query.node.Node;

public class MyNodeOperation extends Node
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
