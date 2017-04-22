package query_building;

import java.io.IOException;
import java.util.ArrayList;

import query_rewriting.rule.Rule;
import query_rewriting.rule.RuleAll;
import query_rewriting.rule.RuleExists;
import query_rewriting.rule.RuleManager;
import query_rewriting.rule.RuleManagerBuilder;
import query_rewriting.rule.RuleManagerBuilderException;
import reader.ReaderException;
import reader.TextReader;
import builder.BuilderException;

/**
 * Construit l'ensemble de règles à partir d'un fichier texte. Format : Une
 * règle par ligne #hypo #concl, les exists rules commencent par un '?'en début
 * de règle. Les commentaires sont possible avec un '#' en début de ligne. Les
 * espaces en début et fin de lignes sont ignorés.
 * 
 * @author zuri
 * 
 */
public class RuleManagerBuilder_text extends RuleManagerBuilder
{
	private TextReader	reader;

	public RuleManagerBuilder_text(RuleManager rman)
	{
		super(rman);
		setReader(new TextReader());
	}

	public RuleManagerBuilder_text(RuleManager rman, TextReader r)
	{
		super(rman);
		setReader(r);
	}

	public TextReader getReader()
	{
		return reader;
	}

	public void setReader(TextReader r)
	{
		reader = r;
	}

	@Override
	public void build() throws RuleManagerBuilderException
	{
		RuleManager rm = getRuleManager();
		TextReader reader = getReader();
		reader.setModeLine();
		int li = 0;

		try
		{
			for (String line : (ArrayList<String>) reader.read())
			{
				li++;
				line = line.trim();

				if (line.isEmpty() || line.charAt(0) == '#')
					continue;

				boolean rexists = line.charAt(0) == '?';

				if (rexists)
					line = line.substring(1).trim();

				String[] words = line.split("[\\s]+");

				if (words.length != 2)
					throw new RuleManagerBuilderException("'"
							+ reader.getSource().toString() + "' ligne " + li
							+ " la règle doit contenir 2 éléments : "
							+ words.length + " trouvés");

				Rule r = rexists ? new RuleExists(words[0], words[1])
						: new RuleAll(words[0], words[1]);
				rm.add(r);
			}
		}
		catch (ReaderException | IOException e)
		{
			throw new RuleManagerBuilderException(e.getMessage());
		}
	}

	@Override
	public RuleManager newBuild() throws BuilderException
	{
		RuleManager ret = new RuleManager();
		setRuleManager(ret);
		build();
		return ret;
	}
}
