package builder;

abstract public class Builder
{
	protected Object	builded;

	public Builder()
	{

	}

	public Builder(Object d)
	{
		setBuilded(d);
	}

	public void setBuilded(Object d)
	{
		builded = d;
	}

	public Object getBuilded()
	{
		return builded;
	}

	abstract public void build() throws BuilderException;
}