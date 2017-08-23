package query_rewriting.rule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Ensemble de règles
 * 
 * @author zuri
 * 
 * 
 */
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
		if ( contains(r) )
			return false;

		return super.add(r);
	}

	public RuleManager getApplicables(String k1)
	{
		RuleManager ret = new RuleManager();
		ArrayList<String> conclusions = new ArrayList<>();
		conclusions.add(k1);

		while ( !conclusions.isEmpty() )
		{
			ArrayList<String> nc = new ArrayList<>();

			for ( String k : conclusions )
			{
				for ( Rule r : this )
				{
					if ( r.getConclusion().equals(k) && !ret.contains(r) )
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

	public RuleManager getApplicablesOnlyRAll(String k1)
	{
		RuleManager ret = new RuleManager();
		ArrayList<String> conclusions = new ArrayList<>();
		conclusions.add(k1);

		while ( !conclusions.isEmpty() )
		{
			ArrayList<String> nc = new ArrayList<>();

			for ( String k : conclusions )
			{
				for ( Rule r : this )
				{
					if ( r.getConclusion().equals(k) && !ret.contains(r)
							&& r instanceof RuleAll )
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

	public boolean hasExistsRule()
	{
		for ( Rule r : this )
		{
			if ( r instanceof RuleExists )
				return true;
		}
		return false;
	}

	public Set<String> getAllKeys()
	{
		HashSet<String> ret = new HashSet<>(size());

		for ( Rule r : this )
		{
			ret.add(r.getHypothesis());
			ret.add(r.getConclusion());
		}
		return ret;
	}

	public Set<String> getAllHypothesis()
	{
		HashSet<String> ret = new HashSet<>(size());

		for ( Rule r : this )
			ret.add(r.getHypothesis());

		return ret;
	}

	public Set<String> getAllHypothesisWithout(String k)
	{
		Set<String> ret = getAllHypothesis();

		if ( ret.contains(k) )
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
