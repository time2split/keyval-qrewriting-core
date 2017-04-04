package json;


import java.util.ArrayList;
import java.util.regex.Pattern;

import printer.Printer;


public class ElementArray extends Element
{
	private ArrayList<Element> array;
	
	
	public ElementArray()
	{
		this.array = new ArrayList<Element>();
	}

	
	public ElementArray(ElementArray e)
	{
		this.array = new ArrayList<Element>(e.array.size());
		
		for(Element el : e.array)
			this.array.add((Element)el.clone());
	}
	
	
	public ElementArray(ArrayList<Element> array)
	{
		this.array = array;
	}
	
	
	@Override
	public Element followPath(String[] keys, int offset)
	{
		if(offset < 0 || offset > keys.length)
			return null;

		//System.out.println("ARR " + this.array + " " + offset);
		
		if(offset == keys.length)
			return this;
		
		String key = keys[offset];
		
		if(key.equals(""))
			return this.followPath(keys,offset++);
		
		if(key.charAt(0) != '$')
			return null;
		
		String val = key.substring(1);
		
		if(!Pattern.compile("\\d+").matcher(val).matches())
			return null;
		
		int i = Integer.valueOf(val);
		
		try{
			return array.get(i).followPath(keys, offset + 1);
		}
		catch(IndexOutOfBoundsException e){
			return null;
		}
	}
	
	
	public ArrayList<Element> getArray()
	{
		return this.array;
	}
	
	
	@Override
	public Object getValue()
	{
		return getArray();
	}

	
	@Override
	public boolean isArray(){ return true; }
	
	
	public String toString()
	{
		return getArray().toString();
	}
	

	@Override
	public void prints(Printer p)
	{
		p.printnp("[");
		p.incDepth();
		ArrayList<Element> a = getArray();
		int ac = a.size()-1;
		int c = 0;
		
		for(Element e : a)
		{
			e.printns(p);

			if(ac == c)
				p.printnp("");
			else
				p.printnp(",");

			c++;
		}
		p.decDepth();
		p.printns("]");
	}


	@Override
	public Object clone() {
		return new ElementArray(this);
	}


	@Override
	public boolean equals(Object o) {

		if(!(o instanceof ElementArray))
			return false;
		
		return array.equals(((ElementArray)o).array);
	}


	@Override
	public int hashCode() {
		int i = 0;
		
		for(Element e : array)
			i += e.hashCode();
		
		return i;
	}
}
