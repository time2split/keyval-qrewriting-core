package query_building;

import java.io.File;

import query_rewriting.rule.Rule;
import query_rewriting.rule.RuleManager;
import query_rewriting.rule.RuleManagerBuilder;
import query_rewriting.rule.RuleManagerBuilderException;

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

			if (line.isEmpty())
				continue;

			String[] words = line.split("[ \t\n\f\r]");

			if (words.length != 2)
				throw new RuleManagerBuilderException("'" + source.toString()
						+ "' ligne " + li
						+ " la règle doit contenir 2 éléments : "
						+ words.length + " trouvés");

			rm.add(new Rule(words[0], words[1]));
		}
	}
}
