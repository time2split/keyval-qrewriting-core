package tests;

import database.mongo.MongoConnection;

public class mongoConnection
{

	public static void main(String[] args)
	{
		// MongoClient client = new MongoClient();
		MongoConnection mongo = new MongoConnection("mongodb://localhost");
	}
}
