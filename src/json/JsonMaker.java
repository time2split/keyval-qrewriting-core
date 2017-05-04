package json;

import java.util.Collection;

/**
 * Permet de faciliter la construction de certains documents
 * 
 * @author zuri
 * 
 */
public final class JsonMaker
{

	public static Json makeArray(Collection<Element> ce, String label)
	{
		Json ret = new Json();
		ElementObject root = new ElementObject();
		ElementArray or = new ElementArray();

		ret.setDocument(root);
		root.getObject().put(label, or);

		for (Element e : ce)
		{
			or.getArray().add(e);
		}
		return ret;
	}
}
