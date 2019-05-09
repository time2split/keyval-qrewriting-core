package insomnia.qrewriting.code;

import java.util.Arrays;

/**
 * Code représentant une requête.
 * Attention pour les calculs, le premier élément d'un code (affichage à gauche) est indexé à 0
 * 
 * @author zuri
 */
public class Code
{
	private int      code[];
	private Encoding encoding;

	public Code(int nbState, Encoding encoding)
	{
		code          = new int[nbState];
		this.encoding = encoding;
	}

	public Code(Code toCpy)
	{
		code     = toCpy.code.clone();
		encoding = toCpy.encoding;
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

	public String format(int... nbDigits)
	{
		StringBuilder buffer = new StringBuilder(nbDigits.length * 3);
		final int     c      = code.length;

		if (nbDigits.length < c)
			return null;

		for (int i = 0; i < c; i++)
		{
			String format = "%0" + nbDigits[i] + "d";
			buffer.append(String.format(format, code[i]));
		}
		return buffer.toString();
	}

	@Override
	public int hashCode()
	{
		return Arrays.hashCode(code);
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Code))
			return false;

		return Arrays.equals(code, ((Code) o).code);
	}

	@Override
	public String toString()
	{
		return format(encoding.getCodeFormat());
	}
}
