package rule;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import json.Element;
import json.ElementArray;
import json.ElementLiteral;
import json.ElementObject;
import query.Query;
import query.QuerySet;

/**
 * Forward checking
 * @author zuri
 *
 */
public class StupidRewriter extends QRewriter
{
	
	
	@Override
	public QuerySet rewrite(Query q, RuleSet rules) throws Exception
	{
		QuerySet result        = new QuerySet();
		ArrayList<String> path = new ArrayList<String>();
		
		result.add((Query) q.clone());
		rewrite(path,rules,result);
		
		return result;
	}
	
	
	private void rewrite(ArrayList<String> path, RuleSet rules, QuerySet result) throws Exception
	{
		QuerySet generatedQ = new QuerySet();

		//System.out.println("REWRITE " + result + path);
		
		while(true)
		{
			for(Query q : result)
			{
				Element e = q.getDocument().followPath(path);
				//System.out.println("Follow " + path + "\n" + e + "\n");
				
				if(e == null || !e.isObject())
					continue;
				
				ElementObject eo                = (ElementObject) e;
				HashMap<String, Element> object = eo.getObject();
				{
					
					//On parcour les clés de l'objet courant
					Set<String> a = object.keySet();
					String[] keys = object.keySet().toArray(new String[0]);
					
					for(int i = 0 ; i < keys.length ; i++)
					{
						String from = keys[i];
						RuleSet currentRuleSet   = new RuleSet(rules);
						ArrayList<Rule> to_apply = currentRuleSet.searchRulesByFrom(from);
						
						//Il y a des règles à appliquer
						for(Rule r : to_apply)
						{
							//On supprime la règle courante
							currentRuleSet.del(r);
							
							if(r instanceof RuleEquals)
							{
								RuleEquals re = (RuleEquals) r;
								Query   qnew  = (Query)q.clone();
								ElementObject qnew_e = (ElementObject) qnew.getDocument().followPath(path);
								
								//Remplacement de la clé
								{
									qnew_e.getObject().put(re.getTo(), qnew_e.getObject().get(from));
									qnew_e.getObject().remove(from);
								}
								
								if(!result.contains(qnew))
									generatedQ.add(qnew);
							}
							else if(r instanceof RuleExists)
							{
								String to = r.getTo();
								HashMap<String, Element> obj;
								ElementObject e_tmp = null;
								
								if(object.containsKey(to))
								{
									Element tmp = object.get(to);
									
									if(tmp.isObject())
									{
										e_tmp = (ElementObject)tmp;
										
										if(e_tmp.getObject().containsKey("$exists"))
										{
											Element exists_e = e_tmp.getObject().get("$exists");
											
											if(!(exists_e instanceof ElementLiteral))
												throw new Exception("Mauvais format pour $exists");
											
											ElementLiteral exists_el = (ElementLiteral) exists_e;
											
											if(exists_el.getLiteral() != ElementLiteral.TRUE)
												throw new Exception("Les règles doivent inférer $exists = vrai or il est définie faux !");
										}
									}
									else
									{
										e_tmp = new ElementObject();
										e_tmp.getObject().put("$eq", tmp);
										object.put(to, e_tmp);
									}
								}
								else
								{
									e_tmp = new ElementObject();
									object.put(to,e_tmp);
								}
								
								if(e_tmp != null)
									e_tmp.getObject().put("$exists", new ElementLiteral(ElementLiteral.TRUE));
							}
						}
					}
				}
			}

			//Sortie : plus rien à générer
			if(generatedQ.isEmpty())
				break;
			
			result.addAll(generatedQ);
			generatedQ.clear();
		}
		
		//Récursion dans les noeuds enfants
		for(Query q_i : ((QuerySet)result.clone()))
		{
			Element e = q_i.getDocument().followPath(path);
			
			if(e == null)
				continue;
			
			if(e.isObject())
			{
				rewrite_in((ElementObject)e,path,rules,result);
			}
			else if(e.isArray())
			{
				rewrite_in((ElementArray)e,path,rules,result);
			}
		}
	}
	
	
	private void rewrite_in(ElementObject eo, ArrayList<String>path,RuleSet rules,QuerySet result) throws Exception
	{
		for(String key : eo.getObject().keySet())
		{
			Element e = eo.getObject().get(key);
			//System.err.println(eo + " " + path);
			
			if(e.isObject())
			{
				path.add(key);
				rewrite(path,rules,result);
				path.remove(path.size() - 1);
			}
			else if(e.isArray())
			{
				path.add(key);
				rewrite_in((ElementArray)e,path,rules,result);
				path.remove(path.size() - 1);
			}
		}
	}
	
	
	private void rewrite_in(ElementArray ea, ArrayList<String>path,RuleSet rules,QuerySet result) throws Exception
	{
		int i = 0;
		int c = ea.getArray().size();
		//ea = (ElementArray) ea.clone();
		
		for( ; i < c ; i++ )
		{
			Element e = ea.getArray().get(i);
			path.add("$" + i);
			
			if(e.isObject())
				rewrite(path,rules,result);
			else if(e.isArray())
				rewrite_in((ElementArray) e,path,rules,result);
				
			path.remove(path.size() - 1);
		}
	}
}
