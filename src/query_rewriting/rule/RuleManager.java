package query_rewriting.rule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RuleManager extends ArrayList<Rule>
{
	private static final long	serialVersionUID	= 1L;

	public ArrayList<Rule> getRules()
	{
		return this;
	}

	@Override
	public boolean add(Rule r)
	{

		if (contains(r))
			return false;

		return super.add(r);
	}

	/**
	 * A_Rls(k)
	 * 
	 * @return
	 */
	public RuleManager getApplicables(String k1)
	{
		RuleManager ret = new RuleManager();
		ArrayList<String> conclusions = new ArrayList<String>();
		conclusions.add(k1);

		while (!conclusions.isEmpty())
		{
			ArrayList<String> nc = new ArrayList<String>();

			for (String k : conclusions)
			{
				for (Rule r : this)
				{
					if (r.getConclusion().equals(k))
					{
						nc.add(r.getHypothesis());
						ret.add(r);
					}
				}
			}
			conclusions = nc;
		}
		return ret;
	}

	public Set<String> getAllKeys()
	{
		HashSet<String> ret = new HashSet<String>(size());

		for (Rule r : this)
		{
			ret.add(r.getHypothesis());
			ret.add(r.getConclusion());
		}

		return ret;
	}

	public Set<String> getAllHypothesis()
	{
		HashSet<String> ret = new HashSet<String>(size());

		for (Rule r : this)
			ret.add(r.getHypothesis());

		return ret;
	}

	public Set<String> getAllHypothesisWithout(String k)
	{
		Set<String> ret = getAllHypothesis();

		if (ret.contains(k))
			ret.remove(k);

		return ret;
	}

	public Set<String> getAllHypothesisWith(String k)
	{
		Set<String> ret = getAllHypothesisWithout(k);
		ret.add(k);
		return ret;

	}
}
