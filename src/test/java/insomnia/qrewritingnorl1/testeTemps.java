package insomnia.qrewritingnorl1;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import insomnia.qrewritingnorl1.json.Element;
import insomnia.qrewritingnorl1.json.Json;
import insomnia.qrewritingnorl1.json.JsonReader;
import insomnia.qrewritingnorl1.query_building.RuleManagerBuilder_text;
import insomnia.qrewritingnorl1.query_building.mongo.FactoryOfJsonBuilder_query;
import insomnia.qrewritingnorl1.query_building.mongo.QueryBuilder_json;
import insomnia.qrewritingnorl1.query_rewriting.code.Encoding;
import insomnia.qrewritingnorl1.query_rewriting.generator.CodeGenerator;
import insomnia.qrewritingnorl1.query_rewriting.generator.CodeGeneratorException;
import insomnia.qrewritingnorl1.query_rewriting.generator.CodeGenerator_simple;
import insomnia.qrewritingnorl1.query_rewriting.query.Query;
import insomnia.qrewritingnorl1.query_rewriting.query.QueryBuilderException;
import insomnia.qrewritingnorl1.query_rewriting.rule.RuleManager;
import insomnia.qrewritingnorl1.query_rewriting.rule.RuleManagerBuilderException;
import insomnia.qrewritingnorl1.query_rewriting.thread.QThreadManager;
import insomnia.qrewritingnorl1.query_rewriting.thread.QThreadResult;
import insomnia.qrewritingnorl1.reader.TextReader;
import insomnia.reader.ReaderException;

/**
 * Created by ccolo on 16/05/2017.
 */
public class testeTemps
{

//	private static final int	NBMAXTHREAND	= 5;
	private static final int	NBQUERY			= 22;
	private static final String	RULE			= "requetec3/reglefinal.txt";

	public static void main(String[] args)
	{
		try
		{
			File ff = new File("temps_reecriture.txt");
			FileWriter fichier = new FileWriter(ff);
			// int i = 4;

			for (int i = 0; i < NBQUERY; i++)
			{
				System.out.println("i : " + i);
				String QUERY = "requetec3/insomnia_query" + i + ".json";
				Json js_query;

				JsonReader js_reader = new JsonReader(new File(QUERY));
				// JsonWriter js_writer = new JsonWriter();

				js_reader.getOptions().setStrict(false);

				js_query = js_reader.read();
				js_reader.close();

				Query query = new Query();
				QueryBuilder_json qb = new QueryBuilder_json(query, js_query);

				RuleManager rm = new RuleManager();
				RuleManagerBuilder_text rmbt = new RuleManagerBuilder_text(rm, new TextReader(new File(RULE)));

				qb.build();
				rmbt.build();

				CodeGenerator generator = new CodeGenerator_simple(query, rm);
				Encoding encoding = generator.getEncoding();
				System.out.println("Nombre d'états "
						+ encoding.getTotalNbStates());

				int j = 1;

				// for (int j = 30; j <= NBMAXTHREAND; j++)
				{
					// System.out.println("j : " + j);
					long startTime = System.currentTimeMillis();

					QThreadManager thm = new QThreadManager(query, encoding);
					// thm.setMode_nbThread(j);
					ArrayList<QThreadResult> res = thm.compute(new FactoryOfJsonBuilder_query());
					{
						ArrayList<Element> json_queries = new ArrayList<>(res.size());

						// for (QThreadResult r : res)
						// {
						// Json doc = (Json) r.builded;
						// json_queries.add(doc.getDocument());
						// }
						System.out.println("Nombre de réécritures "
								+ encoding.getTotalNbStates() + " = "
								+ json_queries.size());
					}
					long endTime = System.currentTimeMillis();

					if (j == 1)
					{
						fichier.write("Exécution du fichier " + QUERY + ", "
								+ encoding.getTotalNbStates()
								+ " réécritures \n");
					}
					fichier.write("avec " + j + " thread prend "
							+ (endTime - startTime) + " ms \n");
					fichier.flush();
				}
			}
			fichier.close();
		}
		catch (
			IOException
			| ReaderException
			| CodeGeneratorException
			| RuleManagerBuilderException
			| QueryBuilderException
			| InterruptedException
			| ExecutionException e1)
		{
			e1.printStackTrace();
			return;
		}
	}
}
