package factory;

public abstract class Factory
{
	private Object	data;

	public Factory()
	{

	}

	public Factory(Object data)
	{
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

	abstract public Object create();
}
