package insomnia.qrewriting.query;

import insomnia.builder.BuilderException;

public class QueryBuilderException extends BuilderException
{
	private static final long serialVersionUID = 1L;

	public QueryBuilderException(Exception e)
	{
		super(e);
	}

	public QueryBuilderException(String m)
	{
		super(m);
	}
}
