package insomnia.qrewritingnorl1.json;

public class ElementLiteral extends Element
{
	public enum Literal
	{
		TRUE, FALSE, NULL
	}

	private Literal	val;

	public ElementLiteral(String val)
	{
		if (val.equals("true"))
			setLiteral(Literal.TRUE);
		else if (val.equals("false"))
			setLiteral(Literal.FALSE);
		else
			setLiteral(Literal.NULL);
	}

	public ElementLiteral(Literal val)
	{
		setLiteral(val);
	}

	public ElementLiteral(ElementLiteral e)
	{
		this.setLiteral(e.val);
	}

	boolean setLiteral(Literal val)
	{
		this.val = val;
		return true;
	}

	public Literal getLiteral()
	{
		return this.val;
	}

	@Override
	public Object getValue()
	{
		return getLiteral();
	}

	@Override
	public boolean isLiteral()
	{
		return true;
	}

	public String toString()
	{
		switch (getLiteral())
		{
		case TRUE:
			return "true";
		case FALSE:
			return "false";
		case NULL:
			return "null";
		default:
			return "UNKNOW_LITERAL";
		}
	}

	@Override
	public ElementLiteral clone()
	{
		return new ElementLiteral(this);
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof ElementLiteral))
			return false;

		return ((ElementLiteral) o).getLiteral() == this.getLiteral();
	}

	@Override
	public int hashCode()
	{
		return getLiteral().ordinal();
	}
}