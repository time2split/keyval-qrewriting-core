package query;


import java.util.ArrayList;

import json.ElementArray;
import json.ElementObject;


public class QuerySet extends ArrayList<Query>
{
	private static final long serialVersionUID = -205497054344842242L;

	
	public Query toOrRequest()
	{
		Query r = new Query();
		ElementObject root = new ElementObject();
		ElementArray array = new ElementArray();
		root.getObject().put("$or", array);
		r.setDocument(root);
		
		for(Query r_i : this)
		{
			array.getArray().add(r_i.getDocument());
		}
		return r;
	}
}
