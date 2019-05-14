package insomnia.qrewriting.database.driver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import insomnia.qrewriting.database.driver.DriverQueryEvaluator.Cursor;
import insomnia.qrewriting.query.Query;

public class CursorAggregation implements Cursor
{
	List<Cursor> cursors;
	int          size = -1;
	int          i    = 0;

	public CursorAggregation(Collection<Cursor> cursors)
	{
		this.cursors = Collections.synchronizedList(new ArrayList<>(cursors));
		computeSize();
	}

	public CursorAggregation()
	{
		cursors = Collections.synchronizedList(new ArrayList<>());
	}

	public void add(Cursor cursor)
	{
		cursors.add(cursor);
		size = -1;
	}

	private void computeSize()
	{
		size = 0;

		for (Cursor cursor : cursors)
			size += cursor.size();
	}

	@Override
	public long size()
	{
		if (size == -1)
			computeSize();

		return size;
	}

	// =====================================================================

	@Override
	public Iterator<Query> iterator()
	{
		return new Iterator<Query>()
		{
			Iterator<Query> current;

			{
				if (cursors.size() == 0)
					current = Collections.emptyIterator();
				else
					current = cursors.get(i).iterator();
			}

			@Override
			public boolean hasNext()
			{
				if (!current.hasNext())
				{
					if (cursors.size() == i)
						return false;

					current = cursors.get(++i).iterator();
				}
				return true;
			}

			@Override
			public Query next()
			{
				return current.next();
			}
		};
	}
}