package json;

public class JsonOptions implements Cloneable
{
	private boolean	option_strict	= true;
	private boolean	option_compact	= false;

	public JsonOptions()
	{

	}

	public JsonOptions(JsonOptions copy)
	{
		option_strict = copy.option_strict;
		option_compact = copy.option_compact;
	}

	public void setStrict(boolean s)
	{
		option_strict = s;
	}

	public void setCompact(boolean s)
	{
		option_compact = s;
	}

	public boolean getStrict()
	{
		return option_strict;
	}

	public boolean getCompact()
	{
		return option_compact;
	}

	public static boolean checkNonStrict(String s)
	{
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);

			if (!checkNonStrict(c))
				return false;
		}
		return true;
	}

	public static boolean checkNonStrict(char c)
	{
		return Character.isAlphabetic(c) || Character.isDigit(c)
				|| ("" + c).matches("[~^/\\$#@&_.+*?-]");
	}

	@Override
	public JsonOptions clone()
	{
		return new JsonOptions(this);
	}
}
