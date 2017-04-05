package query_building;

import java.io.File;

import query_rewriting.rule.Rule;
import query_rewriting.rule.RuleAll;
import query_rewriting.rule.RuleExists;
import query_rewriting.rule.RuleManager;
import query_rewriting.rule.RuleManagerBuilder;
import query_rewriting.rule.RuleManagerBuilderException;

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

	public RuleManagerBuilder_text(RuleManager rman)
	{
		super(rman);
	}

	public RuleManagerBuilder_text(RuleManager rman, File f)
	{
		super(rman, f);
	}

	@Override
	public void build() throws RuleManagerBuilderException
	{
		String contents = getContents();
		int li = 0;

		for (String line : contents.split("\n"))
		{
			li++;
			line = line.trim();

			if (line.isEmpty() || line.charAt(0) == '#')
				continue;

			boolean rexists = line.charAt(0) == '?';

			if (rexists)
				line = line.substring(1).trim();

			String[] words = line.split("[ \t\n\f\r]+");

			if (words.length != 2)
				throw new RuleManagerBuilderException("'" + source.toString()
						+ "' ligne " + li
						+ " la règle doit contenir 2 éléments : "
						+ words.length + " trouvés");

			Rule r = rexists ? new RuleExists(words[0], words[1])
					: new RuleAll(words[0], words[1]);
			rm.add(r);
		}
	}
}
