package insomnia.qrewriting.query_building;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;

import insomnia.builder.BuilderException;
import insomnia.qrewriting.query_rewriting.rule.Rule;
import insomnia.qrewriting.query_rewriting.rule.RuleAll;
import insomnia.qrewriting.query_rewriting.rule.RuleExists;
import insomnia.qrewriting.query_rewriting.rule.RuleManager;
import insomnia.qrewriting.query_rewriting.rule.RuleManagerBuilder;
import insomnia.qrewriting.query_rewriting.rule.RuleManagerBuilderException;
import insomnia.reader.TextReader;

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
	private int			iline	= 0;

	public RuleManagerBuilder_text()
	{
		super(new RuleManager());
	}

	public RuleManagerBuilder_text(RuleManager rman)
	{
		super(rman);
	}

	public RuleManagerBuilder_text addLines(BufferedReader reader)
			throws RuleManagerBuilderException
	{
		try
		{
			return addLines(IOUtils.readLines(IOUtils.toBufferedReader(reader)));
		}
		catch (IOException e)
		{
			throw new RuleManagerBuilderException(e.getMessage());
		}
	}

	public RuleManagerBuilder_text addLines(List<String> lines)
			throws RuleManagerBuilderException
	{
		for (String line : lines)
			addLine(line);
		return this;
	}

	public RuleManagerBuilder_text addLines(String[] lines)
			throws RuleManagerBuilderException
	{
		for (String line : lines)
			addLine(line);
		return this;
	}

	RuleManagerBuilder_text addLine(String line)
			throws RuleManagerBuilderException
	{
		if (line.isEmpty() || line.charAt(0) == '#')
			return this;

		boolean rexists = line.charAt(0) == '?';
		final RuleManager rm = getRuleManager();

		if (rexists)
			line = line.substring(1).trim();

		iline += 1;
		String[] words = line.split("[\\s]+");

		if (words.length != 2)
			throw new RuleManagerBuilderException(
				"'" + reader.getSource().toString() + "' ligne " + iline
						+ " la règle doit contenir 2 éléments : " + words.length
						+ " trouvés");

		Rule r = rexists ? new RuleExists(words[0], words[1])
				: new RuleAll(words[0], words[1]);
		rm.add(r);
		return this;
	}

	@Override
	public void build() throws RuleManagerBuilderException
	{
	}

	@Override
	public RuleManager newBuild() throws BuilderException
	{
		build();
		return getRuleManager();
	}
}
