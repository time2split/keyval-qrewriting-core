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

	/**
	 * Créer un document comportant un tableau de label $label dont les éléments
	 * proviennent de $ce
	 * 
	 * @param ce
	 * @param label
	 * @return
	 */
	public static Json makeArray(Collection<Element> ce, String label)
	{
		Json ret = new Json();
		ElementObject root = new ElementObject();
		ElementArray or = new ElementArray();

		ret.setDocument(root);
		root.getObject().put(label, or);

		for ( Element e : ce )
		{
			or.getArray().add(e);
		}
		return ret;
	}
}
