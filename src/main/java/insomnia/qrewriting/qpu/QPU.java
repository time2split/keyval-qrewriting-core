package insomnia.qrewriting.qpu;

import java.util.ArrayList;

import insomnia.qrewriting.query.Query;

/**
 * Unité de calcul prenant un ensemble de codes et renvoyant un ensemble de
 * requêtes
 * 
 * @author zuri
 * 
 */
abstract public class QPU
{

	/**
	 * Réécriture
	 * 
	 * @return Les requêtes réécrites
	 */
	abstract public ArrayList<Query> process();
}
