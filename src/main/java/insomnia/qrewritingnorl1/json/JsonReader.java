package insomnia.qrewritingnorl1.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

import insomnia.qrewritingnorl1.json.special_element.ElementKey;
import insomnia.qrewritingnorl1.json.special_element.ElementRoot;
import insomnia.reader.Reader;
import insomnia.reader.ReaderException;

/**
 * Permet de lire un flux JSON
 * 
 * @author zuri
 * 
 */
public class JsonReader extends Reader
{
	/**
	 * Spécifie si le format est du JSON strict
	 * <ul>
	 * <li>true : format standard</li>
	 * <li>false : permet de spécifier des clés objet sans parenthèses</li>
	 * </ul>
	 */
	private JsonOptions	options	= new JsonOptions();

	private enum Token
	{
		OBJ_OPEN,
		OBJ_CLOS,
		ARR_OPEN,
		ARR_CLOS,
		COMA,
		COLON,
		LITERAL,
		STRING,
		LITERALORSTRING,
		NUMBER,
		START,
		END
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

	public JsonReader(File s) throws FileNotFoundException
	{
		super(s);
	}

	// =========================================================================

	public JsonOptions getOptions()
	{
		return options;
	}

	// =========================================================================

	public static boolean checkNumber(String s)
	{
		return s.matches("^-?(([1-9]\\d*)|0)(\\.\\d+)?([eE][+-]\\d+)?$");
	}

	public static boolean checkLiteral(String s)
	{
		return s.equals("null") || s.equals("true") || s.equals("false");
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

	private enum LexerState
	{
		START, STRING, LITERAL, LITERALNOSTRICT, NUMBER, CHAR_ESCAPE
	}

	// TODO: faire une classe Lexer
	// TODO: enregistrement ligne/colonne pour erreurs précises
	private LexerVal nextToken(InputStream stream) throws ReaderException,
			IOException
	{

		if (!stream.markSupported())
			throw new ReaderException("Le flux ne supporte pas le marquage");

		LexerState state = LexerState.START;
		String buffer = "";
		final boolean option_strict = options.getStrict();

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
			case START:

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
					state = LexerState.STRING;
				else if (!option_strict && JsonOptions.checkNonStrict(c))
				{
					stream.mark(1);
					buffer += c;
					state = LexerState.LITERALNOSTRICT;
				}
				else if (Character.isLetter(c))
				{
					stream.mark(1);
					buffer += c;
					state = LexerState.LITERAL;
				}
				else if (Character.isDigit(c) || c == '-')
				{
					stream.mark(1);
					buffer += c;
					state = LexerState.NUMBER;
				}
				else
					throw new ReaderException("Mauvais caractère lexer '" + c);
				break;

			case LITERALNOSTRICT:

				if (JsonOptions.checkNonStrict(c))
				{
					stream.mark(1);
					buffer += c;
				}
				else
				{
					if (d != -1)
						stream.reset();

					return new LexerVal(Token.LITERALORSTRING, buffer);
				}
				break;

			// Littéral
			case LITERAL:

				if (Character.isLetterOrDigit(c))
				{
					stream.mark(1);
					buffer += c;
				}
				else
				{
					if (d != -1)
						stream.reset();

					if (checkLiteral(buffer))
						return new LexerVal(Token.LITERAL, buffer);

					throw new ReaderException("Littéral " + buffer + " inconnu");
				}
				break;

			// number
			case NUMBER:

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
			case STRING:

				if (c == '"')
					return new LexerVal(Token.STRING, buffer);

				buffer += c;

				if (c == '\\')
					state = LexerState.CHAR_ESCAPE;

				break;

			// Echappement
			case CHAR_ESCAPE:
				buffer += c;
				state = LexerState.STRING;
				break;

			}

			// Fin du flux
			if (d == -1)
			{
				if (state == LexerState.START)
					break;
				else
					throw new ReaderException("Fin rencontrée dans le lexer");
			}
		}
		return new LexerVal(Token.END, null);
	}

	// =========================================================================

	@Override
	public Json read() throws ReaderException, IOException
	{
		Json doc = new Json();
		Element e = this._read().getRoot();
		doc.setDocument(e);
		close();
		return doc;
	}

	private enum ReadState
	{
		START,
		END,
		STORE_ELEMENT,
		COMMA_OR_ARRCLOS,
		COMMA_OR_OBJCLOS,
		COLON,
		ARRAY_ADD,
		OBJECT_ADD
	}

	private ElementRoot _read() throws ReaderException, IOException
	{
		// Lexer
		LexerVal lv = null; // Compilateur pas content si non initialisée
		Token token;
		String data;

		Stack<ReadState> states = new Stack<>();
		Stack<Element> elems = new Stack<>();

		ReadState state;

		Element current = null; // Nouvel élément
		boolean skipLexer = false;

		states.push(ReadState.END);
		states.push(ReadState.START);

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
				throw new ReaderException("Aucun état à affecter");

			// Récupération de l'état courrant
			state = states.pop();

			if (Config.DEBUG_READER)
				System.out.println(state + " " + token + " " + data);

			switch (state)
			{

			case START:

				if (token == Token.LITERAL)
				{
					current = new ElementLiteral(data);
					states.push(ReadState.STORE_ELEMENT);
					skipLexer = true;
				}
				else if (token == Token.STRING)
				{
					current = new ElementString(data);
					states.push(ReadState.STORE_ELEMENT);
					skipLexer = true;
				}
				else if (token == Token.LITERALORSTRING)
				{
					// states.push(ReadState.MAYBE_STRING_OR_LITERAL);
					Element p = elems.peek();
					states.push(ReadState.START);
					skipLexer = true;

					if (p instanceof ElementObject)
					{
						lv.token = Token.STRING;
					}
					else
					{
						if (checkLiteral(data))
							lv.token = Token.LITERAL;
						else if (checkNumber(data))
							lv.token = Token.NUMBER;
						else
							throw new ReaderException("Token " + data
									+ " non reconnu");
					}
				}
				else if (token == Token.NUMBER)
				{
					if (!checkNumber(data))
						throw new ReaderException("Nombre non reconnu : "
								+ data);

					current = new ElementNumber(data);
					states.push(ReadState.STORE_ELEMENT);
					skipLexer = true;
				}
				else if (token == Token.ARR_OPEN)
				{
					states.push(ReadState.STORE_ELEMENT);
					current = new ElementArray();
					skipLexer = true;
				}
				else if (token == Token.OBJ_OPEN)
				{
					states.push(ReadState.STORE_ELEMENT);
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
					throw new ReaderException("Token " + token + " non attendu");
				}
				else
				{
					skipLexer = true;
				}
				break;

			// Enregistrement élément pour ajout
			case STORE_ELEMENT:
			{
				Element p = elems.peek();
				elems.push(current);

				if (p instanceof ElementRoot)
				{
					if (current instanceof ElementArray
							|| current instanceof ElementObject)
						states.push(ReadState.START);
				}
				else if (p instanceof ElementArray)
				{
					if (current instanceof ElementArray
							|| current instanceof ElementObject)
					{
						states.push(ReadState.COMMA_OR_ARRCLOS);
						states.push(ReadState.START);
					}
					else
						states.push(ReadState.COMMA_OR_ARRCLOS);
				}
				else if (p instanceof ElementObject)
				{
					if (!(current instanceof ElementString))
						throw new ReaderException("Les clés doivent être de type string");

					states.push(ReadState.COLON);

					// Sentinelle
					elems.push(new ElementKey());
				}
				else if (p instanceof ElementKey)
				{
					if (current instanceof ElementArray
							|| current instanceof ElementObject)
					{
						states.push(ReadState.COMMA_OR_OBJCLOS);
						states.push(ReadState.START);
					}
					else
						states.push(ReadState.COMMA_OR_OBJCLOS);
				}
				else
					throw new ReaderException("Objet collection attendu dans la pile");
			}
				break;

			// Ajout dans Array
			case ARRAY_ADD:
			{
				current = elems.pop();
				Element p = elems.peek();

				if (!(p instanceof ElementArray))
					throw new ReaderException("Element non Array");

				((ElementArray) p).getArray().add(current);
			}
				break;

			// Attente , ou ]
			case COMMA_OR_ARRCLOS:

				if (token != Token.ARR_CLOS && token != Token.COMA)
					throw new ReaderException("Token ',' ou ']' attendue");
				{
					skipLexer = true;

					if (token == Token.COMA)
						states.push(ReadState.START);

					states.push(ReadState.ARRAY_ADD);
				}
				break;

			// Ajout dans Object
			case OBJECT_ADD:
			{
				current = elems.pop();

				if (!(elems.pop() instanceof ElementKey))
					throw new ReaderException("Erreur objet : clé attendue");

				Element key = elems.pop();
				Element p = elems.peek();

				if (!(p instanceof ElementObject))
					throw new ReaderException("Element non Object");

				((ElementObject) p).getObject().put(((ElementString) key).getString(), current);
			}
				break;

			// Attente ':'
			case COLON:

				if (token != Token.COLON)
					throw new ReaderException("Token ':' attendu");

				states.push(ReadState.START);
				break;

			// Attente , ou }
			case COMMA_OR_OBJCLOS:

				if (token != Token.OBJ_CLOS && token != Token.COMA)
					throw new ReaderException("Token ',' ou '}' attendus");
				{
					skipLexer = true;

					if (token == Token.COMA)
						states.push(ReadState.START);

					states.push(ReadState.OBJECT_ADD);
				}
				break;

			// Attente END
			case END:

				if (token != Token.END)
					throw new ReaderException("Token END attendue");
				{
					Element p = null;
					current = elems.pop();

					if (!elems.empty())
						p = elems.peek();

					if (!(p instanceof ElementRoot))
						throw new ReaderException("Element non Racine");

					((ElementRoot) p).e = current;
				}
				break;
			}

			if (token == Token.END)
			{
				if (skipLexer)
					continue;

				if (elems.empty())
					throw new ReaderException("Aucun élément Json à retourner");

				if (elems.size() > 1)
					throw new ReaderException("La pile contient "
							+ elems.size() + " éléments ; Retour impossible");

				return (ElementRoot) elems.pop();
			}
		}
	}
}
