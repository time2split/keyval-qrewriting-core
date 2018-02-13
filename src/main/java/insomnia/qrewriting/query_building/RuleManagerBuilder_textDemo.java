package insomnia.qrewriting.query_building;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import insomnia.builder.BuilderException;
import insomnia.qrewriting.query_rewriting.rule.Rule;
import insomnia.qrewriting.query_rewriting.rule.RuleAll;
import insomnia.qrewriting.query_rewriting.rule.RuleExists;
import insomnia.qrewriting.query_rewriting.rule.RuleManager;
import insomnia.qrewriting.query_rewriting.rule.RuleManagerBuilder;
import insomnia.qrewriting.query_rewriting.rule.RuleManagerBuilderException;

/**
 * Construit l'ensemble de règles à partir d'un fichier texte en ligne.
 * <dl>
 * <dt>Forall</dt>
 * <dd>hypo -> concl</dd>
 * <dt>Exists</dt>
 * <dd>hypo -> [E] concl</dd>
 * </dl>
 * 
 * @author zuri
 * 
 */
public class RuleManagerBuilder_textDemo extends RuleManagerBuilder
{

	public RuleManagerBuilder_textDemo()
	{
		super(new RuleManager());
	}

	public RuleManagerBuilder_textDemo(RuleManager rman)
	{
		super(rman);
	}

	public RuleManagerBuilder_textDemo addLines(BufferedReader reader)
			throws RuleManagerBuilderException
	{
		try
		{
			return addLines(
				IOUtils.readLines(IOUtils.toBufferedReader(reader)));
		}
		catch (IOException e)
		{
			throw new RuleManagerBuilderException(e.getMessage());
		}
	}

	public RuleManagerBuilder_textDemo addLines(List<String> lines)
			throws RuleManagerBuilderException
	{
		for (String line : lines)
			addLine(line);
		return this;
	}

	public RuleManagerBuilder_textDemo addLines(String[] lines)
			throws RuleManagerBuilderException
	{
		for (String line : lines)
			addLine(line);
		return this;
	}

	RuleManagerBuilder_textDemo addLine(String line)
			throws RuleManagerBuilderException
	{
		if (line.isEmpty() || line.charAt(0) == '#')
			return this;

		final RuleManager rm = getRuleManager();
		Pattern p = Pattern.compile("([^\\s]+) *-> (\\[E\\])? *([^\\s]+)");
		Matcher m = p.matcher(line);

		if (m.matches() == false)
			throw new RuleManagerBuilderException(
				"Format de la ligne incorrect '" + line + "'");
		
		boolean rexists = m.group(2) != null;
		Rule r = rexists ? new RuleExists(m.group(1), m.group(3))
				: new RuleAll(m.group(1), m.group(3));
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
