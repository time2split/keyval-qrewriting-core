package insomnia.qrewriting.query.node;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import insomnia.qrewriting.query.Label;
import insomnia.qrewriting.query.Query;

/**
 * Node of a query.
 * Every implemented Node class MUST have a null constructor and a copy constructor.
 * 
 * @author zuri
 */
public interface Node extends Iterable<Node>
{
	int getId();

	Label getLabel();

	NodeValue getValue();

	/**
	 * @return La requête à laquelle appartient le noeud
	 */
	Query getQuery();

	int getNbOfDescendants();

	int getNbOfParents();

	int getNbOfChilds();

	// ========================================================================

	boolean isPath();

	/**
	 * Est une feuille
	 * 
	 * @return true|false
	 */
	boolean isLeaf();

	boolean isUnfolded();

	boolean childsArePaths();

	// ========================================================================

	/**
	 * @return Le noeud dont l'identifiant est $id
	 */
	Node getNode(int id);

	Node getParent();

	Node[] getParents();

	NodeChilds getChilds();

	Node[] getDescendants();

	Node[] getLeaves();

	/**
	 * Récupère toutes les racines d'arbres
	 * 
	 * @param ret
	 * @param n
	 */
	Node[] getTrees();

	/**
	 * Récupère toutes les racines de chemins
	 * 
	 * @param ret
	 * @param n
	 */
	Node[] getPaths();

	/**
	 * Retourne les données de la fonction collect jusqu'à la racine
	 * 
	 * @param collect
	 */
	<T> List<T> backCollect(Function<Node, T> collect);

	// ========================================================================

	default String childsToString()
	{
		StringBuilder ret    = new StringBuilder();
		boolean       doonce = false;

		ret.append("{" + getId() + " ");

		for (Node c : getChilds())
		{
			if (doonce)
				ret.append(", ");

			ret.append(c);
			doonce = true;
		}
		return ret + " " + getId() + "}";
	}

	// ========================================================================

	void addChild(boolean generateNodeId, Node... node);

	void addChilds(boolean generateNodeId, Collection<Node> childs);

	void setQuery(Query q);

	void setLabel(Label l);

	void setValue(NodeValue v);

	void setId(int i);

	void setParent(Node parent);

	/**
	 * Applique une méthode depuis le noeud courant exclu jusqu'aux feuilles
	 */
	void deepPropagation(Consumer<Node> method);

	/**
	 * Applique une méthode depuis le noeud courant exclu jusqu'à la racine
	 */
	void backPropagation(Consumer<Node> method);
}
