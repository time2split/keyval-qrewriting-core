package json;


public class ElementNumber extends Element {
	double number;
	

	public ElementNumber(String n)
	{
		setNumber(Double.valueOf(n));
	}
	
	
	public ElementNumber(double n)
	{
		setNumber(n);
	}
	
	
	public ElementNumber(ElementNumber e)
	{
		this.number = e.number;
	}
	
	
	public double getNumber()
	{
		return this.number;
	}
	
	
	public void setNumber(double n)
	{
		this.number = n;
	}
	
	
	@Override
	public Object getValue() {
		return getNumber();
	}
	
	
	public String toString()
	{
		return String.valueOf(getNumber());
	}


	@Override
	public ElementNumber clone() {
		return new ElementNumber(this);
	}


	@Override
	public boolean equals(Object o) {
		
		if(!(o instanceof ElementNumber))
			return false;
		
		return ((ElementNumber)o).getNumber() == this.getNumber();
	}


	@Override
	public int hashCode() {
		return Double.hashCode(getNumber());
	}
}
