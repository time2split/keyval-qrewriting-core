package database.mongo;

import java.io.IOException;
import java.util.ArrayList;

import json.Json;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import database.Connection;

public class MongoConnection extends Connection
{
	MongoClient	client;
	String		databaseName;

	public MongoConnection(String uri)
	{
		client = new MongoClient(new MongoClientURI(uri));

		for (String a : client.listDatabaseNames())
		{
			System.out.println(a);
		}
	}

	public MongoClient getClient()
	{
		return client;
	}

	@Override
	public void close() throws IOException
	{
		client.close();
	}

	@Override
	public ArrayList<Json> find(Json query)
	{
		return null;
	}

	@Override
	public void useDatabase(String d)
	{
		databaseName = d;
	}
}
