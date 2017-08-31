package insomnia.qrewritingnorl1;


import java.io.IOException;

import insomnia.qrewritingnorl1.database.mongo.MongoConnection;

public class mongoConnection
{

	public static void main(String[] args)
	{
		// MongoClient client = new MongoClient();
		MongoConnection mongo = new MongoConnection("mongodb://localhost");
		try {
			mongo.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
