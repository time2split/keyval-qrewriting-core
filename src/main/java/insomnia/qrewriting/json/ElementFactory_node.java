package insomnia.qrewriting.json;

import insomnia.factory.Factory;
import insomnia.json.Element;
import insomnia.json.ElementLiteral;
import insomnia.json.ElementNumber;
import insomnia.json.ElementString;
import insomnia.qrewriting.query.node.Node;
import insomnia.qrewriting.query.node.NodeValue;
import insomnia.qrewriting.query.node.NodeValueNumber;

public class ElementFactory_node implements Factory<Element>
{
	/**
	 * Query node
	 */
	Node node;

	public ElementFactory_node setNode(Node n)
	{
		node = n;
		return this;
	}

	@Override
	public Element create()
	{
		if (node == null)
			return null;

		Element ret;
		NodeValue nval = node.getValue();

		if (nval.isLiteral())
			ret = new ElementLiteral(nval.toString());
		else if (nval.isString())
			ret = new ElementString(nval.toString());
		else if (nval.isNumber())
			ret = new ElementNumber(((NodeValueNumber) nval).getNumber());
		else
			ret = null;
		
		return ret;
	}

}
