package insomnia.qrewritingnorl1.reader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import insomnia.reader.Reader;
import insomnia.reader.ReaderException;

/**
 * Permet de lire des fichiers texte selon divers modes
 * 
 * <ul>
 * <li>LINE : lecture par ligne avec l'option $option_skipEmpty</li>
 * <li>WORD : lecture par mot suivant le pattern $wordPattern</li>
 * <li>BLOCK : lecture par block suivant la taille $blockSize</li>
 * </ul>
 * 
 * @author zuri
 * 
 */
public class TextReader extends Reader
{
	public enum Mode
	{
		LINE, WORD, BLOCK
	}

	Mode	mode				= Mode.LINE;
	Pattern	wordPattern			= null;
	int		blockSize			= 4096;
	boolean	option_skipEmpty	= false;

	public TextReader()
	{
		super();
	}

	public TextReader(InputStream s)
	{
		super(s);
	}

	public TextReader(String s)
	{
		super(s);
	}

	public TextReader(File f) throws FileNotFoundException
	{
		super(new BufferedInputStream(new FileInputStream(f)));
	}

	/**
	 * Lit la prochaine entité
	 */
	@Override
	public String nextRead() throws ReaderException, IOException
	{
		InputStream source = getSource();
		String buff = "";
		int d;

		switch (mode)
		{
		case LINE:

			while (-1 != (d = source.read()))
			{
				if (d == '\n')
				{
					if (buff.isEmpty() && option_skipEmpty)
						continue;

					return buff;
				}
				else
					buff += (char) d;
			}
			break;

		case WORD:

			while (-1 != (d = source.read()))
			{
				Matcher m = wordPattern.matcher(Character.toString((char) d));

				if (m.matches())
					buff += (char) d;
				else
				{
					if (buff.isEmpty())
						continue;

					return buff;
				}
			}
			break;

		case BLOCK:
			byte tmp[] = new byte[blockSize];
			d = source.read(tmp);

			if (d == -1)
				return null;

			return new String(tmp);
		}

		if (!buff.isEmpty())
			return buff;

		return null;
	}

	/**
	 * Lit toutes les entités
	 */
	@Override
	public ArrayList<String> read() throws ReaderException, IOException
	{
		ArrayList<String> ret = new ArrayList<>(256);
		String next;

		while (true)
		{
			next = nextRead();

			if (next == null)
				return ret;

			ret.add(next);
		}
	}

	public void setSkipEmpty(boolean v)
	{
		option_skipEmpty = v;
	}

	public void setBlockSize(int bs)
	{
		blockSize = bs;
	}

	public void setModeWord()
	{
		setModeWord("[\\S]");
	}

	public void setModeWord(String p)
	{
		mode = Mode.WORD;
		wordPattern = Pattern.compile(p);
	}

	public void setModeBlock()
	{
		mode = Mode.BLOCK;
	}

	public void setModeLine()
	{
		mode = Mode.LINE;
	}
}