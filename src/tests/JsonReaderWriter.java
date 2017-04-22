package tests;

import java.io.File;
import java.io.IOException;

import json.Json;
import json.JsonReader;
import json.JsonWriter;
import reader.ReaderException;
import writer.WriterException;

/**
 * Permet de tester/comprendre comment fonctionne les JsonReader/Writer
 * 
 * @author zuri
 * 
 */
public class JsonReaderWriter
{
	public static void main(String[] args)
	{
		/**
		 * $input peut être un String|File|InputStream
		 */
		// String input =
		// "{a : 1, b : 13.5, c : true, d : {A : 45}, e : [1,false,{A : 1},{A : 2}]}";
		File input = new File("test_rewriting/animaux_query.json");

		try (
			JsonReader read = new JsonReader(input) ;
			JsonWriter write = new JsonWriter())
		{
			// Changement du mode strict
			read.getOptions().setStrict(false);

			// Lecture du flux
			Json json = read.read();

			// Ecriture
			write.getOptions().setCompact(!true);
			write.write(json);
		}
		catch (ReaderException | IOException | WriterException e)
		{
			e.printStackTrace();
		}
	}
}
