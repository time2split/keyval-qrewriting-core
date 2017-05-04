package tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import json.Json;
import json.JsonBuilderException;
import json.JsonReader;
import json.JsonWriter;
import numeric.Interval;
import query_building.RuleManagerBuilder_text;
import query_building.mongo.JsonBuilder_query;
import query_building.mongo.QueryBuilder_json;
import query_rewriting.code.Code;
import query_rewriting.code.ContextManager;
import query_rewriting.code.Encoding;
import query_rewriting.generator.CodeGenerator;
import query_rewriting.generator.CodeGeneratorException;
import query_rewriting.generator.CodeGenerator_simple;
import query_rewriting.qpu.QPUSimple;
import query_rewriting.query.Query;
import query_rewriting.query.QueryBuilderException;
import query_rewriting.rule.RuleManager;
import query_rewriting.rule.RuleManagerBuilderException;
import reader.ReaderException;
import reader.TextReader;
import writer.WriterException;

/**
 * Permet de tester/comprendre l'utilisation des objets pour effectuer les
 * réécritures
 * 
 * @author zuri
 * 
 */
public class Rewriting
{
	/**
	 * Nombre de Threads (simulation car pas de vrais threads utilisés)
	 */
	static final int	nbThread	= 1;
	static final String	rulesPath	= "test_rewriting/simple_rules.txt";
	static final String	queryPath	= "test_rewriting/simple_query.json";

	public static void main(String[] args)
	{
		Json js_query;

		try (
			JsonReader js_reader = new JsonReader(new File(queryPath)) ;
			JsonWriter js_writer = new JsonWriter())
		{
			js_reader.getOptions().setStrict(false);

			// Lecture
			js_query = js_reader.read();

			// Requête et builder
			Query query = new Query();
			QueryBuilder_json qb = new QueryBuilder_json(query, js_query);

			// Règles et builder
			RuleManager rm = new RuleManager();
			RuleManagerBuilder_text rmbt = new RuleManagerBuilder_text(rm, new TextReader(new File(rulesPath)));

			// Construction requête et règles
			qb.build();
			rmbt.build();
			CodeGenerator generator = new CodeGenerator_simple(query, rm);

			// Encodage (sémantique des codes)
			Encoding encoding = generator.getEncoding();

			// Contextes de codes
			ContextManager contexts = generator.getContextManager();

			// Exemples d'utilisations
			{
				/*
				 * Récupère l'ensemble de tous les codes
				 */
				ArrayList<Code> codes;
				{
					System.out.println("\n\n===== TOUS LES CODES =====");
					codes = encoding.generateAllCodes();
					System.out.println("nombre de codes : " + codes.size());
					System.out.println(codes);
				}

				/*
				 * Récupère les codes à partir de leur représentation numérique
				 */
				{
					System.out.println("\n\n===== CODES DEPUIS ENTIER =====");

					for (int i = 0; i < 100; i++)
						System.out.println(i + " = "
								+ generator.getEncoding().getCodeFrom(i));
				}

				/*
				 * Récupération d'intervalles de codes
				 */
				ArrayList<Interval> cutting;
				{
					System.out.println("\n\n===== RECUPERATION D'INTERVALLES =====");
					cutting = encoding.generateCodeInterval().cutByNumberOfIntervals(nbThread, Interval.OPTION_HOMOGENEOUS);

					for (Interval in : cutting)
					{
						System.out.println(in);
					}
				}

				/*
				 * Réécriture d'un intervalle
				 */
				{
					System.out.println("\n\n===== REECRITURE D'INTERVALLES =====");
					for (Interval in : cutting)
					{
						System.out.println(in);
						System.out.println(encoding.generateAllCodes(in));
					}
				}

				/*
				 * Réécritures des codes du premier intervalle
				 */
				ArrayList<Query> rewrites;
				{
					// Unité de réécriture
					QPUSimple qpu = new QPUSimple(query, encoding.generateAllCodes(cutting.get(0)), encoding, contexts);
					rewrites = qpu.process();
				}

				/*
				 * Conversion requête vers JSON
				 */
				{
					System.out.println("\n\n===== CONVERSION REQUÊTE VERS JSON =====");
					JsonBuilder_query jsonBuilder = new JsonBuilder_query();
					js_writer.getOptions().setCompact(true);

					for (Query r : rewrites)
					{
						jsonBuilder.setQuery(r);
						System.out.println("\n\n" + r);
						js_writer.write(jsonBuilder.newBuild());
					}
				}
			}
		}
		catch (
			ReaderException
			| IOException
			| JsonBuilderException
			| WriterException
			| CodeGeneratorException
			| QueryBuilderException
			| RuleManagerBuilderException e1)
		{
			e1.printStackTrace();
			return;
		}
	}
}