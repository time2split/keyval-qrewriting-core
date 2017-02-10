package json;


import printer.Printer;


public class Json
implements Cloneable
{
	private Element doc;
	private Printer printer;
	
	
	public Json()
	{
		printer = new Printer();
		//printer.out = new ByteArrayOutputStream();
	}
	
	
	public Json(Json document)
	{
		this.setDocument((Element) document.getDocument().clone());
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
	
	
	public void print()
	{
		getDocument().prints(this.printer);
		//System.out.println(this.printer.out);
	}
	
	
	public Printer getPrinter()
	{
		return this.printer;
	}
	
	
	@Override
	public Object clone()
	{
		return new Json(this);
	}
	
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Json))
			return false;
		
		return getDocument().equals(((Json)o).getDocument());
	}
	
	
	@Override
	public int hashCode()
	{
		return getDocument().hashCode();
	}
}
