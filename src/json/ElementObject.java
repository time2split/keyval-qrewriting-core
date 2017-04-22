package json;

import java.util.HashMap;

public class ElementObject extends Element
{
	private HashMap<String, Element>	object;

	public ElementObject()
	{
		this.object = new HashMap<String, Element>();
	}

	public ElementObject(HashMap<String, Element> object)
	{
		this.object = object;
	}

	public ElementObject(ElementObject e)
	{
		this.object = new HashMap<String, Element>(e.object.size());

		for (String key : e.object.keySet())
			this.object.put(new String(key), e.object.get(key).clone());
	}

	@Override
	public Element followPath(String[] keys, int offset)
	{

		if (offset < 0 || offset > keys.length)
			return null;

		if (offset == keys.length)
			return this;

		String key = keys[offset];

		if (key.equals(""))
			return this.followPath(keys, offset++);

		Element e = object.get(key);

		if (e == null)
			return null;

		return e.followPath(keys, offset + 1);
	}

	@Override
	public Object getValue()
	{
		return getObject();
	}

	public HashMap<String, Element> getObject()
	{
		return this.object;
	}

	@Override
	public boolean isObject()
	{
		return true;
	}

	public String toString()
	{
		return getObject().toString();
	}

	@Override
	public ElementObject clone()
	{
		return new ElementObject(this);
	}

	@Override
	public boolean equals(Object o)
	{

		if (!(o instanceof ElementObject))
			return false;
		/*
		 * if(object.equals(((ElementObject)o).object))
		 * System.out.println(object + "!!=====>>>>>" +
		 * ((ElementObject)o).object);
		 * System.out.println(object.entrySet().equals(((ElementObject)o).object.
		 * entrySet()));
		 * //
		 */
		return object.equals(((ElementObject) o).object);
	}

	@Override
	public int hashCode()
	{
		int i = 0;

		for (String key : object.keySet())
			i += key.hashCode() + object.get(key).hashCode();

		return i;
	}
}