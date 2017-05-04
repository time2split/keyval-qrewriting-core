package builder;

public abstract class BuilderData extends Builder
{
	private Object	data;

	public BuilderData()
	{
		super();
	}

	protected BuilderData(Object what)
	{
		super(what);
	}

	protected BuilderData(Object what, Object data)
	{
		super(what);
		setData(data);
	}

	public void setData(Object d)
	{
		data = d;
	}

	public Object getData()
	{
		return data;
	}
}
