package query_rewriting.code;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Contexte de codage
 * 
 * @author zuri
 * 
 */
public class Context
{
	private String		key;
	private Set<String>	replacements	= new HashSet<String>();

	public Context(String k)
	{
		key = k;
		replacements.add(k);
	}

	public Context(String k, Collection<String> e)
	{
		this(k);
		replacements.addAll(e);
	}

	public Set<String> getReplacements()
	{
		return replacements;
	}

	public String getK(int codeState)
	{
		return (new ArrayList<String>(replacements)).get(codeState);
	}

	public int getCodeState(String k)
	{
		if (!replacements.contains(k))
			return -1;

		// TODO: optimisation
		return (new ArrayList<String>(replacements)).indexOf(k);
	}

	@Override
	public String toString()
	{
		return "" + replacements;
	}

	public int size()
	{
		return replacements.size();
	}
}
