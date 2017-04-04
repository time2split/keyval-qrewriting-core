package query_rewriting.query;

import java.util.ArrayList;

public class Label extends ArrayList<String>
{
	private static final long	serialVersionUID	= 1L;

	public Label(String l)
	{
		add(l);
	}

	public ArrayList<String> getLabels()
	{
		return this;
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
}
