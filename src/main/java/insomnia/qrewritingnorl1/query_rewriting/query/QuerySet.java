package insomnia.qrewritingnorl1.query_rewriting.query;


//import java.util.ArrayList;


///
//public class QuerySet extends ArrayList<Query>
//{
//	private static final long serialVersionUID = -205497054344842242L;
//
//	
//	public Query toOrRequest()
//	{
//		Query r = new Query();
//		ElementObject root = new ElementObject();
//		ElementArray array = new ElementArray();
//		root.getObject().put("$or", array);
//		r.setDocument(root);
//		
//		for(Query r_i : this)
//		{
//			array.getArray().add(r_i.getDocument());
//		}
//		return r;
//	}
//}
