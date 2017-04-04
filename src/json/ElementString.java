package json;

import printer.Printer;


public class ElementString extends Element{
	private String val;
	
	
	public ElementString(String val)
	{
		this.val = val;
	}
	
	
	public ElementString(ElementString e)
	{
		this.val = new String(e.val);
	}


	public String getString()
	{
		return this.val;
	}

	
	@Override
	public Object getValue()
	{
		return getString();
	}
	
	
	@Override
	public Object clone()
	{
		return new ElementString(this);
	}
	
	
	@Override
	public boolean isString(){ return true; }
	
	
	public String toString()
	{
		return "\"" + getString() + "\"";
	}
	
	
	@Override
	public void prints(Printer p)
	{
		p.prints("\"" + getString() + "\"");
	}
	
}
