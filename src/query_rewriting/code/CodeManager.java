package query_rewriting.code;

import java.util.HashSet;
import java.util.Set;

public class CodeManager extends HashSet<Code>
{
	private Encoding	encoding;

	public CodeManager(Encoding e)
	{
		encoding = e;
	}

	public CodeManager(Encoding e, Set<Code> c)
	{
		super(c);
		encoding = e;
	}

	public Encoding getEncoding()
	{
		return encoding;
	}

	public Set<Code> getCodes()
	{
		return this;
	}
}
