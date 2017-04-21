package json;

import java.io.File;
import java.io.InputStream;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import json.special_element.ElementKey;
import json.special_element.ElementRoot;
import reader.Reader;

public class JsonReader extends Reader
{
	private enum Token
	{
		OBJ_OPEN, OBJ_CLOS, ARR_OPEN, ARR_CLOS, COMA, COLON, LITERAL, STRING, NUMBER, START, END
	}

	// =========================================================================

	public JsonReader(InputStream s)
	{
		super(s);
	}

	public JsonReader(String s)
	{
		super(s);
	}

	public JsonReader(File s)
	{
		super(s);
	}

	// =========================================================================

	// Retour de nextToken()
	private static class LexerVal
	{

		Token	token;
		String	data;

		LexerVal(Token t, String d)
		{
			this.token = t;
			this.data = d;
		}
	}

	// TODO: faire une classe Lexer
	// TODO: enregistrement ligne/colonne pour erreurs précises
	private static LexerVal nextToken(InputStream stream) throws Exception
	{

		if (!stream.markSupported())
			throw new Exception("Le flux ne supporte pas le marquage !");

		/*
		 * 0 start 20 littéral 30 number 50 chaine
		 */
		int state = 0;
		String buffer = "";

		while (true)
		{
			int d = stream.read();
			char c = (char) d;

			if (Config.DEBUG_LEXER)
				System.out.println("l " + state + " c:" + c + " buff: "
						+ buffer);

			switch (state)
			{

			// Start
			case 0:

				if (Character.isWhitespace(c))
					continue;

				if (d == -1)
					break;
				if (c == '{')
					return new LexerVal(Token.OBJ_OPEN, "{");
				if (c == '}')
					return new LexerVal(Token.OBJ_CLOS, "}");
				if (c == '[')
					return new LexerVal(Token.ARR_OPEN, "[");
				if (c == ']')
					return new LexerVal(Token.ARR_CLOS, "]");
				if (c == ':')
					return new LexerVal(Token.COLON, ":");
				if (c == ',')
					return new LexerVal(Token.COMA, ",");

				if (c == '"')
					state = 50;
				else if (Character.isLetter(c))
				{
					stream.mark(1);
					buffer += c;
					state = 20;
				}
				else if (Character.isDigit(c) || c == '-')
				{
					stream.mark(1);
					buffer += c;
					state = 30;
				}
				else
					throw new Exception("Mauvais caractère lexer '" + c + "'!");
				break;

			// Littéral
			case 20:

				if (Character.isLetterOrDigit(c))
				{
					stream.mark(1);
					buffer += c;
				}
				else
				{
					if (d != -1)
						stream.reset();

					if (buffer.equals("true") || buffer.equals("false")
							|| buffer.equals("null"))
						return new LexerVal(Token.LITERAL, buffer);

					throw new Exception("Littéral " + buffer + " inconnu !");
				}
				break;

			// number
			case 30:

				if (Character.isDigit(c) || c == 'e' || c == 'E' || c == '.'
						|| c == '+' || c == '-')
				{
					stream.mark(1);
					buffer += c;
				}
				else
				{
					stream.reset();
					return new LexerVal(Token.NUMBER, buffer);
				}
				break;

			// Chaine
			case 50:

				if (c == '"')
					return new LexerVal(Token.STRING, buffer);

				buffer += c;

				if (c == '\\')
					state = 55;

				break;

			// Echappement
			case 55:
				buffer += c;
				state = 50;
				break;

			}

			// Fin du flux
			if (d == -1)
			{
				if (state == 0)
					break;
				else
					throw new Exception("Fin rencontrée dans lexer !");
			}
		}
		return new LexerVal(Token.END, null);
	}

	// =========================================================================

	public Json read()
	{
		Json doc = new Json();

		try
		{
			Element e = this._read().getRoot();
			doc.setDocument(e);
			closeNoe();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return doc;
	}

	private ElementRoot _read() throws Exception
	{
		// Lexer
		LexerVal lv = null; // Compilateur pas content si non initialisée
		Token token;
		String data;

		Stack<Integer> states = new Stack<Integer>();
		Stack<Element> elems = new Stack<Element>();

		int state;

		// TODO: mettre la vérification dans le lexer
		Pattern pnum = Pattern.compile("^-?(([1-9]\\d*)|0)(\\.\\d+)?([eE][+-]\\d+)?$");

		Element current = null; // Nouvel élément
		boolean skipLexer = false;

		states.push(1000);
		states.push(0);

		elems.push(new ElementRoot());

		while (true)
		{
			// On saute le lexer
			if (!skipLexer)
				lv = nextToken(this.getSource());

			// Le non saut est rétabli
			if (skipLexer)
				skipLexer = false;

			token = lv.token;
			data = lv.data;

			// Debug
			if (states.empty())
				throw new Exception("Aucun état à affecter !");

			// Récupération de l'état courrant
			state = states.pop().intValue();

			if (Config.DEBUG_READER)
				System.out.println(state + " " + token + " " + data);

			switch (state)
			{

			case 0:

				if (token == Token.LITERAL)
				{
					current = new ElementLiteral(data);
					states.push(900);
					skipLexer = true;
				}
				else if (token == Token.STRING)
				{
					current = new ElementString(data);
					states.push(900);
					skipLexer = true;
				}
				else if (token == Token.NUMBER)
				{
					Matcher m = pnum.matcher(data);

					if (!m.find())
						throw new Exception("Nombre non reconnu : " + data);

					current = new ElementNumber(data);
					states.push(900);
					skipLexer = true;
				}
				else if (token == Token.ARR_OPEN)
				{
					states.push(900);
					current = new ElementArray();
					skipLexer = true;
				}
				else if (token == Token.OBJ_OPEN)
				{
					states.push(900);
					current = new ElementObject();
					skipLexer = true;
				}
				else if (token == Token.ARR_CLOS)
				{
				}
				else if (token == Token.OBJ_CLOS)
				{
				}
				else if (token == Token.COMA || token == Token.COLON
						|| token == Token.ARR_CLOS)
				{
					throw new Exception("Token " + token + " non attendu");
				}
				else
				{
					skipLexer = true;
				}
				break;

			// Enregistrement élément pour ajout
			case 900:
			{
				Element p = elems.peek();
				elems.push(current);

				if (p instanceof ElementRoot)
				{

					if (current instanceof ElementArray
							|| current instanceof ElementObject)
						states.push(0);
				}
				else if (p instanceof ElementArray)
				{
					if (current instanceof ElementArray
							|| current instanceof ElementObject)
					{
						states.push(920);
						states.push(0);
					}
					else
						states.push(920);
				}
				else if (p instanceof ElementObject)
				{
					if (!(current instanceof ElementString))
						throw new Exception("Les clés doivent être de type string !");

					states.push(960);

					// Sentinelle
					elems.push(new ElementKey());
				}
				else if (p instanceof ElementKey)
				{

					if (current instanceof ElementArray
							|| current instanceof ElementObject)
					{
						states.push(970);
						states.push(0);
					}
					else
						states.push(970);
				}
				else
					throw new Exception("Objet collection attendu dans la pile !");
			}
				break;

			// Ajout dans Array
			case 910:
			{
				current = elems.pop();
				Element p = elems.peek();

				if (!(p instanceof ElementArray))
					throw new Exception("Element non Array !");

				((ElementArray) p).getArray().add(current);
			}
				break;

			// Attente , ou ]
			case 920:

				if (token != Token.ARR_CLOS && token != Token.COMA)
					throw new Exception("Token ',' ou ']' attendue !");
				{
					skipLexer = true;

					if (token == Token.COMA)
						states.push(0);

					states.push(910);
				}
				break;

			// Ajout dans Object
			case 950:
			{
				current = elems.pop();

				if (!(elems.pop() instanceof ElementKey))
					throw new Exception("Erreur objet : clé attendue !");

				Element key = elems.pop();
				Element p = elems.peek();

				if (!(p instanceof ElementObject))
					throw new Exception("Element non Object !");

				((ElementObject) p).getObject().put(((ElementString) key).getString(), current);
			}
				break;

			// Attente ':'
			case 960:

				if (token != Token.COLON)
					throw new Exception("Token ':' attendu!");

				states.push(0);
				break;

			// Attente , ou }
			case 970:

				if (token != Token.OBJ_CLOS && token != Token.COMA)
					throw new Exception("Token ',' ou '}' attendus !");
				{
					skipLexer = true;

					if (token == Token.COMA)
						states.push(0);

					states.push(950);
				}
				break;

			// Attente END
			case 1000:

				if (token != Token.END)
					throw new Exception("Token END attendue !");
				{
					Element p = null;
					current = elems.pop();

					if (!elems.empty())
						p = elems.peek();

					if (!(p instanceof ElementRoot))
						throw new Exception("Element non Racine !");

					((ElementRoot) p).e = current;
				}
				break;
			}

			if (token == Token.END)
			{
				if (skipLexer)
					continue;

				if (elems.empty())
					throw new Exception("Aucun élément Json à retourner !");

				if (elems.size() > 1)
					throw new Exception("La pile contient " + elems.size()
							+ " éléments ! Retour impossible !");

				return (ElementRoot) elems.pop();
			}
		}
	}
}
