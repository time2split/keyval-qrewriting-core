import java.io.BufferedInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import json.Json;
import json.JsonReader;
import query_building.QueryBuilder_json;
import query_building.RuleManagerBuilder_text;
import query_rewriting.code.Code;
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
		Json js_rules;
		Json js_query;
		BufferedInputStream fs;

		js_reader = new JsonReader(new File("query.json"));
		js_query = js_reader.read();

		Query query = new Query();
		QueryBuilder_json qb = new QueryBuilder_json(query, js_query);

		RuleManager rm = new RuleManager();
		RuleManagerBuilder_text rmbt = new RuleManagerBuilder_text(rm, new File("rules.txt"));

		try
		{
			qb.build();
			rmbt.build();

			CodeGenerator generator = new CodeGenerator_simple(query, rm);
			ArrayList<Code> codes = generator.getEncoding().generateAllCodes();
			QPUSimple qpu = new QPUSimple(query, codes, generator.getContextManager(), generator.getEncoding());

			ArrayList<Query> rewrites = qpu.process();

			for (Query r : rewrites)
				System.out.println(r);

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
