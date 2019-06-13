package insomnia.qrewriting.qpu;

import java.util.Collection;

import insomnia.qrewriting.query.Query;

/**
 * Unité de calcul prenant un ensemble de codes et renvoyant un ensemble de
 * requêtes
 * 
 * @author zuri
 * 
 */
public interface QPU
{

	/**
	 * Réécriture
	 * 
	 * @return Les requêtes réécrites
	 */
	Collection<Query> process();
}
