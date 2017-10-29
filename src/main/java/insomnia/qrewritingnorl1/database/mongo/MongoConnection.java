//package insomnia.qrewritingnorl1.database.mongo;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientURI;
//
//import insomnia.json.Json;
//import insomnia.qrewritingnorl1.database.Connection;
//
//public class MongoConnection extends Connection
//{
//	MongoClient	client;
//	String		databaseName;
//
//	public MongoConnection(String uri)
//	{
//		client = new MongoClient(new MongoClientURI(uri));
//
//		for ( String a : client.listDatabaseNames() )
//		{
//			System.out.println(a);
//		}
//	}
//
//	public MongoClient getClient()
//	{
//		return client;
//	}
//
//	@Override
//	public void close() throws IOException
//	{
//		client.close();
//	}
//
//	// TODO
//	@Override
//	public ArrayList<Json> find(Json query)
//	{
//		return null;
//	}
//
//	@Override
//	public void useDatabase(String d)
//	{
//		databaseName = d;
//	}
//}
