package json;

import printer.Printer;


public class ElementLiteral extends Element{
	static final int MIN = 1;
	static final int MAX = 3;

	static final int UNKNOW  = 0;
	static public final int TRUE  = 1;
	static public final int FALSE = 2;
	static public final int NULL  = 3;
	
	
	private int val;
	
	
	public ElementLiteral(String val)
	{
		if(val.equals("true"))
			setLiteral(TRUE);
		else if(val.equals("false"))
			setLiteral(FALSE);
		else if(val.equals("null"))
			setLiteral(NULL);
		else
			setLiteral(UNKNOW);
	}
	
	
	public ElementLiteral(int val)
	{
		setLiteral(val);
	}
	
	
	public ElementLiteral(ElementLiteral e) {
		this.setLiteral(e.val);
	}


	boolean setLiteral(int val)
	{
		if(val < MIN || val > MAX)
		{
			System.err.println("Attention, la valeur de ElementLiteral n'est pas correcte ; affectation à TRUE.");
			this.val = TRUE;
			return false;
		}
		this.val = val;
		return true;
	}
	
	
	public int getLiteral()
	{
		return this.val;
	}
	
	
	@Override
	public Object getValue()
	{
		return getLiteral();
	}
	
	
	@Override
	public boolean isLiteral(){ return true; }
	
	
	public String toString()
	{
		switch(getLiteral()){
			case TRUE:return "true";
			case FALSE:return "false";
			case NULL: return "null";
			default:return "UNKNOW_LITERAL";
		}
	}
	
	
	@Override
	public void prints(Printer p)
	{
		p.prints(this.toString());
	}


	@Override
	public ElementLiteral clone() {
		return new ElementLiteral(this);
	}


	@Override
	public boolean equals(Object o) {
		
		if(!(o instanceof ElementLiteral))
			return false;
		
		return ((ElementLiteral)o).getLiteral() == this.getLiteral();
	}


	@Override
	public int hashCode() {
		return getLiteral();
	}
}
