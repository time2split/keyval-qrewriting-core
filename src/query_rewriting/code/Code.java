package query_rewriting.code;

/**
 * Code rerpésentant une requête. Attention pour les calculs, le premier élément
 * d'un code (affichage à gauche) est indexé à 0
 * 
 * @author zuri
 * 
 */
public class Code
{
	int	code[];

	public Code(int nbState)
	{
		code = new int[nbState];
	}

	public int getCode(int pos)
	{
		if (pos < 0 || pos >= code.length)
			return -1;

		return code[pos];
	}

	public int[] getArrayCodes()
	{
		return code;
	}

	public boolean setCode(int i, int state)
	{
		if (i < 0 || i >= code.length)
			return false;

		code[i] = state;
		return true;
	}

	@Override
	public String toString()
	{
		String ret = "";

		for (int state : code)
		{
			ret += Integer.toString(state);
		}
		return ret;
	}
}
