package insomnia.qrewritingnorl1.query_rewriting.thread;

import insomnia.qrewritingnorl1.query_rewriting.query.Query;

public class QThreadResult
{
	public Query	query;
	public Object	builded;

	public QThreadResult(Query q, Object b)
	{
		query = q;
		builded = b;
	}

	@Override
	public String toString()
	{
		return "" + query + " ; " + builded + "\n";
	}
}
