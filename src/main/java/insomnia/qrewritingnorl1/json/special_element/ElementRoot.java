package insomnia.qrewritingnorl1.json.special_element;

import insomnia.qrewritingnorl1.json.Element;

public class ElementRoot extends Element
{
	public Element	e;

	public ElementRoot()
	{
	}

	ElementRoot(ElementRoot e)
	{
		this.e = e.clone();
	}

	@Override
	public Object getValue()
	{
		return getRoot();
	}

	public Element getRoot()
	{
		return e;
	}

	public String toString()
	{
		return "JSON(" + this.e + ")";
	}

	@Override
	public ElementRoot clone()
	{
		return new ElementRoot(this);
	}
}
