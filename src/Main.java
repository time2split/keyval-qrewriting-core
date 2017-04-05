import java.io.File;
import java.util.ArrayList;

import json.Json;
import json.JsonReader;
import numeric.Interval;
import query_building.QueryBuilder_json;
import query_building.RuleManagerBuilder_text;
import query_rewriting.code.Code;
import query_rewriting.code.Encoding;
import query_rewriting.generator.CodeGenerator;
import query_rewriting.generator.CodeGeneratorException;
import query_rewriting.generator.CodeGenerator_simple;
import query_rewriting.qpu.QPUSimple;
import query_rewriting.query.Query;
import query_rewriting.query.QueryBuilderException;
import query_rewriting.rule.RuleManager;
import query_rewriting.rule.RuleManagerBuilderException;

public class Main
{
	static final String	dir	= "/home/zuri/works/TER/java/src/";

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args)
	{
		JsonReader js_reader;
		Json js_query;

		js_reader = new JsonReader(new File("test_rewriting/animaux_query.json"));
		js_query = js_reader.read();

		Query query = new Query();
		QueryBuilder_json qb = new QueryBuilder_json(query, js_query);

		RuleManager rm = new RuleManager();
		RuleManagerBuilder_text rmbt = new RuleManagerBuilder_text(rm, new File("test_rewriting/animaux_rules.txt"));

		int nbThread = 8;

		try
		{
			qb.build();
			rmbt.build();

			CodeGenerator generator = new CodeGenerator_simple(query, rm);
			// Tous les codes
			Encoding encoding = generator.getEncoding();
			ArrayList<Code> codes = encoding.generateAllCodes();
			QPUSimple qpu = new QPUSimple(query, codes, generator.getContextManager(), generator.getEncoding());

			ArrayList<Interval> cutting = (new Interval(0, encoding.getTotalNbStates() - 1)).cutByNumberOfIntervals(nbThread);
			// Reécriture avec tous les codes
			// ArrayList<Query> rewrites = qpu.process();

			// Display
			{
				// System.out.println(codes);

				// for (int i = 0; i < 64; i++)
				// System.out.println(generator.getEncoding().getCodeFrom(i));

				// for (Query r : rewrites)
				// System.out.println(r);

				for (Interval in : cutting)
				{
					System.out.println(encoding.generateAllCodes(in));
				}
			}

		}
		catch (QueryBuilderException e)
		{
			System.err.println("Erreur de construction de la requête : "
					+ e.getMessage());
		}
		catch (RuleManagerBuilderException e)
		{
			System.err.println("Erreur de construction des règles : "
					+ e.getMessage());
		}
		catch (CodeGeneratorException e)
		{
			e.printStackTrace();
		}
	}
}
