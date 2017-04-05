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
	// Todo : utiliser enumération JAVA
	static private final int	TOKEN_OBJ_OPEN	= 1;
	static private final int	TOKEN_OBJ_CLOS	= 2;
	static private final int	TOKEN_ARR_OPEN	= 3;
	static private final int	TOKEN_ARR_CLOS	= 4;
	static private final int	TOKEN_COMA		= 5;
	static private final int	TOKEN_COLON		= 6;

	static private final int	TOKEN_LITERAL	= 10;
	static private final int	TOKEN_STRING	= 11;
	static private final int	TOKEN_NUMBER	= 12;

	static private final int	TOKEN_START		= 0;
	static private final int	TOKEN_END		= 100;

	private String tokenName(int t)
	{
		if (t == TOKEN_OBJ_OPEN)
			return "OBJ_OPEN";
		if (t == TOKEN_OBJ_CLOS)
			return "OBJ_CLOS";
		if (t == TOKEN_ARR_OPEN)
			return "ARR_OPEN";
		if (t == TOKEN_ARR_CLOS)
			return "ARR_CLOS";
		if (t == TOKEN_COMA)
			return "COMA";
		if (t == TOKEN_COLON)
			return "COLON";
		if (t == TOKEN_LITERAL)
			return "LITERAL";
		if (t == TOKEN_STRING)
			return "STRING";
		if (t == TOKEN_START)
			return "START";
		if (t == TOKEN_END)
			return "END";
		if (t == TOKEN_NUMBER)
			return "NUMBER";
		return "???";
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

		int		token;
		String	data;

		LexerVal(int t, String d)
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
					return new LexerVal(TOKEN_OBJ_OPEN, "{");
				if (c == '}')
					return new LexerVal(TOKEN_OBJ_CLOS, "}");
				if (c == '[')
					return new LexerVal(TOKEN_ARR_OPEN, "[");
				if (c == ']')
					return new LexerVal(TOKEN_ARR_CLOS, "]");
				if (c == ':')
					return new LexerVal(TOKEN_COLON, ":");
				if (c == ',')
					return new LexerVal(TOKEN_COMA, ",");

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
						return new LexerVal(TOKEN_LITERAL, buffer);

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
					return new LexerVal(TOKEN_NUMBER, buffer);
				}
				break;

			// Chaine
			case 50:

				if (c == '"')
					return new LexerVal(TOKEN_STRING, buffer);

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
		return new LexerVal(TOKEN_END, null);
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
		int token;
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
				System.out.println(state + " " + tokenName(token) + " " + data);

			switch (state)
			{

			case 0:

				if (token == TOKEN_LITERAL)
				{
					current = new ElementLiteral(data);
					states.push(900);
					skipLexer = true;
				}
				else if (token == TOKEN_STRING)
				{
					current = new ElementString(data);
					states.push(900);
					skipLexer = true;
				}
				else if (token == TOKEN_NUMBER)
				{
					Matcher m = pnum.matcher(data);

					if (!m.find())
						throw new Exception("Nombre non reconnu : " + data);

					current = new ElementNumber(data);
					states.push(900);
					skipLexer = true;
				}
				else if (token == TOKEN_ARR_OPEN)
				{
					states.push(900);
					current = new ElementArray();
					skipLexer = true;
				}
				else if (token == TOKEN_OBJ_OPEN)
				{
					states.push(900);
					current = new ElementObject();
					skipLexer = true;
				}
				else if (token == TOKEN_ARR_CLOS)
				{
				}
				else if (token == TOKEN_OBJ_CLOS)
				{
				}
				else if (token == TOKEN_COMA || token == TOKEN_COLON
						|| token == TOKEN_ARR_CLOS)
				{
					throw new Exception("Token " + tokenName(token)
							+ " non attendu");
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

				if (token != TOKEN_ARR_CLOS && token != TOKEN_COMA)
					throw new Exception("Token ',' ou ']' attendue !");
				{
					skipLexer = true;

					if (token == TOKEN_COMA)
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

				if (token != TOKEN_COLON)
					throw new Exception("Token ':' attendu!");

				states.push(0);
				break;

			// Attente , ou }
			case 970:

				if (token != TOKEN_OBJ_CLOS && token != TOKEN_COMA)
					throw new Exception("Token ',' ou '}' attendus !");
				{
					skipLexer = true;

					if (token == TOKEN_COMA)
						states.push(0);

					states.push(950);
				}
				break;

			// Attente END
			case 1000:

				if (token != TOKEN_END)
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

			if (token == TOKEN_END)
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
