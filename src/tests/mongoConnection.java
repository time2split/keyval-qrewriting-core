package tests;

import database.mongo.MongoConnection;

public class mongoConnection
{

	public static void main(String[] args)
	{
		MongoConnection mongo = new MongoConnection("mongodb://localhost");
	}
}
