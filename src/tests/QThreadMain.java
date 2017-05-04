package tests;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import json.Element;
import json.Json;
import json.JsonMaker;
import json.JsonReader;
import json.JsonWriter;
import numeric.Interval;

import org.bson.Document;

import query_building.RuleManagerBuilder_text;
import query_building.mongo.FactoryOfJsonBuilder_query;
import query_building.mongo.QueryBuilder_json;
import query_rewriting.code.ContextManager;
import query_rewriting.code.Encoding;
import query_rewriting.generator.CodeGenerator;
import query_rewriting.generator.CodeGeneratorException;
import query_rewriting.generator.CodeGenerator_simple;
import query_rewriting.query.Query;
import query_rewriting.query.QueryBuilderException;
import query_rewriting.rule.RuleManager;
import query_rewriting.rule.RuleManagerBuilderException;
import query_rewriting.thread.QThreadManager;
import query_rewriting.thread.QThreadResult;
import reader.ReaderException;
import reader.TextReader;
import writer.WriterException;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import database.mongo.MongoConnection;

public class QThreadMain
{

	public QThreadMain(Query query, Interval generateCodeInterval,
			Encoding encoding, ContextManager contexts)
	{
		// TODO Auto-generated constructor stub
	}

	static final String	rulesPath	= "test_rewriting/animaux_rules.txt";
	static final String	queryPath	= "test_rewriting/angular_query.json";

	public static void main(String[] args)
	{
		Json js_query;

		try (
			JsonReader js_reader = new JsonReader(new File(queryPath)) ;
			JsonWriter js_writer = new JsonWriter())
		{
			js_reader.getOptions().setStrict(false);

			js_query = js_reader.read();

			Query query = new Query();
			QueryBuilder_json qb = new QueryBuilder_json(query, js_query);

			RuleManager rm = new RuleManager();
			RuleManagerBuilder_text rmbt = new RuleManagerBuilder_text(rm, new TextReader(new File(rulesPath)));

			qb.build();
			rmbt.build();

			CodeGenerator generator = new CodeGenerator_simple(query, rm);
			Encoding encoding = generator.getEncoding();
			ContextManager contexts = generator.getContextManager();

			// Utilisation des threads
			QThreadManager thm = new QThreadManager(query, encoding, contexts);
			thm.setMode_nbThread(1);
			ArrayList<QThreadResult> res = thm.compute(new FactoryOfJsonBuilder_query());

			{
				ArrayList<Element> json_queries = new ArrayList<>(res.size());

				for (QThreadResult r : res)
				{
					Json doc = (Json) r.builded;
					json_queries.add(doc.getDocument());
				}
				Json or = JsonMaker.makeArray(json_queries, "$or");
				{
					MongoConnection mongo = new MongoConnection("mongodb://localhost");
					MongoClient client = mongo.getClient();
					MongoDatabase db = client.getDatabase("angular");
					MongoCollection<Document> coll = db.getCollection("produits");

					JsonWriter writer = new JsonWriter();
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					writer.setDestination(out);
					writer.setEol("");
					writer.setTab("");
					writer.write(or);
					writer.close();
					System.out.println(out);

					FindIterable<Document> ret = coll.find(Document.parse(out.toString()));

					for (Document r : ret)
					{
						System.out.println(r);
					}
				}
			}
		}
		catch (
			ReaderException
			| IOException
			| CodeGeneratorException
			| QueryBuilderException
			| RuleManagerBuilderException
			| InterruptedException
			| ExecutionException
			| WriterException e1)
		{
			e1.printStackTrace();
			return;
		}
	}
}
