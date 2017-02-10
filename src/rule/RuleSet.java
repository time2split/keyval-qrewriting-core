package rule;


import java.util.ArrayList;

import json.Element;
import json.ElementArray;
import json.ElementObject;
import json.ElementString;
import json.Json;


public class RuleSet
implements Cloneable
{
	ArrayList<Rule> rules = new ArrayList<Rule>();

	
	public RuleSet()
	{
	}

	
	public RuleSet(Json document) throws Exception
	{
		add(document);
	}
	
	
	public RuleSet(RuleSet rules)
	{
		for(Rule r : rules.rules)
			add((Rule) r.clone());
	}
	
	
	public void add(Json document) throws Exception
	{
		Element e = document.getDocument();
		
		if(!e.isObject())
			throw new Exception("Mauvais format de règle !");
		
		ElementObject  eo = (ElementObject) e;
		
		for(String key : eo.getObject().keySet())
		{
			Element val = eo.getObject().get(key);
			
			if(key.charAt(0) == '?')
			{
				String from = key.substring(1);

				if(val.isString())
				{
					String to     = (String)val.getVal();
					add(new RuleExists(from,to));
				}
				else if(val.isArray())
				{
					for(Element e_i : ((ElementArray)val).getArray())
					{
						if(!e_i.isString())
							throw new Exception("Mauvais type de requête tab !");

						String to = (String)e_i.getVal();
						add(new RuleExists(from,to));
					}
				}
				else throw new Exception("Mauvais type de requête !");
			}
			else if(key.equals("$k<-"))
			{
				if(!val.isArray())
					throw new Exception("Mauvais type de requête !");
				
				String to = null;
						
				for(Element e_p : ((ElementArray)val).getArray())
				{
					if(!e_p.isString())
						throw new Exception();
					
					if(to == null)
					{
						to = ((ElementString)e_p).getString();
						continue;
					}
					String from = ((ElementString)e_p).getString();
					add(new RuleEquals(from,to));
				}
			}
			else if(key.equals("$="))
			{
				if(!val.isArray())
					throw new Exception("Mauvais type de requête !");

				for(Element e_a : ((ElementArray)val).getArray())
				{
					if(!e_a.isString())
						throw new Exception("Mauvais type de requête tab !");
					
					String from  = (String)e_a.getVal();
					
					for(Element e_b : ((ElementArray)val).getArray())
					{
						if(!e_a.isString())
							throw new Exception("Mauvais type de requête tab !");
						
						String to  = (String)e_b.getVal();
						
						if(from.equals(to))
							continue;
						
						add(new RuleEquals(from,to));
					}
				}
			}
			else
			{
				String from = key;
				
				if(val.isString())
				{
					String to     = (String)val.getVal();
					add(new RuleEquals(from,to));
				}
				else if(val.isArray())
				{
					
					for(Element e_i : ((ElementArray)val).getArray())
					{
						
						if(!e_i.isString())
							throw new Exception("Mauvais type de requête tab !");

						String to = (String)e_i.getVal();
						add(new RuleEquals(from,to));
					}
				}
				else throw new Exception("Mauvais type de requête !");
			}
		}
	}
	
	
	public void add(Rule r)
	{
		rules.add(r);
	}
	
	
	public void del(Rule r)
	{
		int i = 0;
		int c = rules.size();

		for( ; i < c ; i++)
		{
			Rule r_i = rules.get(i);
			
			if(r_i.cmp(r) == 0)
			{
				rules.remove(i);
				return;
			}
		}
	}
	
	
	public boolean contains(Rule r)
	{
		return rules.contains(r);
	}

	
	public ArrayList<Rule> searchRulesByFrom(String from)
	{
		ArrayList<Rule> ret = new ArrayList<Rule>();
		
		for(Rule r : rules)
		{	
			
			if(r.getFrom().equals(from))
				ret.add(r);
		}
		return ret;
	}
	
	
	@Override
	public String toString()
	{
		String s = "";
		
		for(Rule r : rules)
		{
			s += r.toString() + "\n";
		}
		return s;
	}
	
	
	@Override
	public Object clone()
	{
		return new RuleSet(this);
	}
}
