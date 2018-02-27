package insomnia.qrewriting.query;

import java.util.ArrayList;

/**
 * Clé d'un arc d'une requête
 * 
 * @author zuri
 * 
 */
public class Label extends ArrayList<String>
{
	private static final long serialVersionUID = 1L;

	public Label(String l)
	{
		add(l);
	}

	public String[] getLabels()
	{
		return this.toArray(new String[0]);
	}

	@Override
	public String toString()
	{
		return super.toString();
	}

	public String get()
	{
		return get(0);
	}

	@Override
	public int hashCode()
	{
		int ret = 0;
		for (String tmp : this)
		{
			ret += tmp.hashCode();
		}
		return ret;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Label)
		{
			return equals2(this, (Label) o);
		}
		return false;
	}

	private boolean equals2(Label a, Label b)
	{
		if (a.size() != b.size())
			return false;

		final int c = a.size();

		for (int i = 0; i < c; i++)
		{
			if (!a.get(i).equals(b.get(i)))
				return false;
		}
		return true;
	}
}
