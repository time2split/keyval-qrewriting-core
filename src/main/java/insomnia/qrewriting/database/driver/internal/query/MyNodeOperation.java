package insomnia.qrewriting.database.driver.internal.query;

import insomnia.qrewriting.query.Label;
import insomnia.qrewriting.query.node.Node;

public class MyNodeOperation extends Node
{
	public enum Operation
	{
		Or
	};

	Operation operation;

	public MyNodeOperation(Operation op)
	{
		super();
		setLabel(new Label(op.toString()));
		setOperation(op);
	}

	public void setOperation(Operation op)
	{
		operation = op;
	}
}
