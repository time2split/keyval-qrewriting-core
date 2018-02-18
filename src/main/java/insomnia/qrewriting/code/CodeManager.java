package insomnia.qrewriting.code;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Ensemble de codes
 * 
 * @author zuri
 * 
 */
public class CodeManager extends HashSet<Code>
{
	private static final long	serialVersionUID	= 1L;
	private Encoding			encoding;

	public CodeManager(Encoding e)
	{
		encoding = e;
	}

	public CodeManager(Encoding e, Collection<Code> c)
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
