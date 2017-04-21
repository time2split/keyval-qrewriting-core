package json;

import builder.Builder;

/**
 * Permet de construire un document Json
 * 
 * @author zuri
 * 
 */
abstract public class JsonBuilder extends Builder
{

	public JsonBuilder()
	{

	}

	public JsonBuilder(Json d)
	{
		setJson(d);
	}

	public void setJson(Json d)
	{
		setBuilded(d);
	}

	protected Json getJson()
	{
		return (Json) getBuilded();
	}

	@Override
	abstract public void build() throws JsonBuilderException;

	@Override
	abstract public Object newBuild() throws JsonBuilderException;
}
