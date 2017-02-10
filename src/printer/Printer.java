package printer;


import java.io.IOException;
import java.io.OutputStream;

/**
 * Impression dans un flux
 * TODO: Supprimer les accès public : faire des accesseurs
 * @author zuri
 *
 */
public class Printer {
	public String prefix    = "  ";
	public String suffix    = "\n";

	private int depth = 0;
	public int offset = 0;
	
	public OutputStream out = System.out;
	

	public final void printSuffix(){
		this.write(this.suffix);
	}
	
	
	public final void printPrefix()
	{
		int c = this.depth - offset;
		
		for( ; c > 0 ; c--)
		{
			this.write(this.prefix);
		}
	}
	
	
	public void write(String s){
		
		try
		{
			this.out.write(s.getBytes());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void incDepth()
	{
		this.depth++;
	}
	
	
	public void decDepth()
	{
		this.depth--;
	}
	

	public void print(String[] s)
	{
		printPrefix();
		
		for(String a : s)
			prints(a);
		
		printSuffix();
	}
	

	public void print(String s)
	{
		printPrefix();
		prints(s);
		printSuffix();
	}
	
	
	public void printnp(String s)
	{
		prints(s);
		printSuffix();
	}
	
	
	public void printns(String s)
	{
		printPrefix();
		prints(s);
	}

	
	public void prints(String s)
	{
		this.write(s);
	}
	
	
	public void flush()
	{
		try
		{
			this.out.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}