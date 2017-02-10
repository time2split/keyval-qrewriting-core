package rule;


import query.Query;
import query.QuerySet;


/**
 * Reécriture de requêtes
 * @author zuri
 *
 */
abstract public class QRewriter
{
	public abstract QuerySet rewrite(Query q, RuleSet rules) throws Exception;
	
	
	public QRewriter setOption(int optKey, Object val)
	{
		return this;
	}
}
