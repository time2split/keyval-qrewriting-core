package json;

import java.util.Collection;

import printer.Printer;

public abstract class Element implements Cloneable
{
	public boolean isArray()
	{
		return false;
	}

	public boolean isObject()
	{
		return false;
	}

	public boolean isNumber()
	{
		return false;
	}

	public boolean isString()
	{
		return false;
	}

	public boolean isLiteral()
	{
		return false;
	}

	@Override
	public abstract Element clone();

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof Element))
			return false;

		return ((Element) o).getValue().equals(getValue());
	}

	@Override
	public int hashCode()
	{
		return this.getValue().hashCode();
	}

	public abstract Object getValue();

	public Element followPath(String[] keys, int offset)
	{
		// Comportement par défaut des objets non traversables
		if (offset == keys.length)
			return this;

		if (keys[0].equals(""))
			return this.followPath(keys, offset++);

		return null;
	}

	final public Element followPath(String[] keys)
	{
		return followPath(keys, 0);
	}

	final public Element followPath(Collection<String> keys)
	{
		return followPath((String[]) keys.toArray(new String[0]));
	}

	final public Element followPath(String path)
	{
		return followPath(path.split("\\."));
	}

	public String toString()
	{
		return "element(" + getValue().toString() + ")";
	}

	public void printns(Printer p)
	{
		p.printPrefix();
		this.prints(p);
	}

	public void printnp(Printer p)
	{
		this.prints(p);
		p.printSuffix();
	}

	public void print(Printer p)
	{
		p.printPrefix();
		this.prints(p);
		p.printSuffix();
	}

	public void prints(Printer p)
	{
		p.prints(getValue().toString());
	}
}
