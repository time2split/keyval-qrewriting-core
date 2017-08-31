package insomnia.qrewritingnorl1.json;

public class Json implements Cloneable
{
	private Element	doc;

	public Json()
	{
	}

	public Json(Json document)
	{
		this.setDocument(document.getDocument().clone());
	}

	public Element getDocument()
	{
		return this.doc;
	}

	public void setDocument(Element e)
	{
		this.doc = e;
	}

	public String toString()
	{
		return "JSON : " + getDocument();
	}

	@Override
	public Json clone()
	{
		return new Json(this);
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof Json))
			return false;

		return getDocument().equals(((Json) o).getDocument());
	}

	@Override
	public int hashCode()
	{
		return getDocument().hashCode();
	}
}