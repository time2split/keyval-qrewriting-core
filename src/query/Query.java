package query;


import json.*;


public class Query extends Json{


	public Query()
	{
		
	}
	
	
	public Query(Json document)
	{
		Element e = document.getDocument();
		
		if(!e.isObject())
			throw new Error("Query error !");
		
		//TODO: préciser les vérifications aux clés opérations connues ?
		this.setDocument((Element) e.clone());
	}
	
	
	@Override
	public Object clone()
	{
		return new Query(this);
	}
}
