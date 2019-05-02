package insomnia.qrewriting.query.node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import insomnia.qrewriting.query.Label;
import insomnia.qrewriting.query.Query;

/**
 * Noeud d'une requête
 * 
 * @author zuri
 */
public class DefaultNode implements Node
{
	private Node       parent;
	private Label      label;
	private NodeChilds childs;
	private NodeValue  value;
	private int        id     = -1;
	private boolean    isPath = true;

	private int nbOfDescendants = 0;
	private int nbOfParents     = 0;

	private Query query;

	public DefaultNode()
	{
		childs = new NodeChilds();
	}

	public DefaultNode(Node n)
	{
		this(n.getQuery(), n);
	}

	/**
	 * Copy $n with $query as associated query.
	 * 
	 * @param n
	 */
	public DefaultNode(Query query, Node n)
	{
		setQuery(query);
		setLabel(n.getLabel());

		id              = n.getId();
		childs          = n.getChilds().copy(query, this);
		isPath          = n.isPath();
		nbOfDescendants = n.getNbOfDescendants();
		nbOfParents     = n.getNbOfParents();

		if (n.getValue() != null)
			setValue(n.getValue().clone());
	}

	// =======================================================

	@Override
	public void setQuery(Query q)
	{
		query = q;
	}

	@Override
	public Query getQuery()
	{
		return query;
	}

	@Override
	public void setParent(Node parent)
	{
		this.parent = parent;
	}

	@Override
	public Node getParent()
	{
		return parent;
	}

	@Override
	public void setLabel(Label l)
	{
		label = l;
	}

	@Override
	public Label getLabel()
	{
		return label;
	}

	@Override
	public void setValue(NodeValue v)
	{
		value = v;
	}

	@Override
	public NodeValue getValue()
	{
		return value;
	}

	@Override
	public void setId(int i)
	{
		id = i;
	}

	@Override
	public int getId()
	{
		return id;
	}

	// =======================================================

	@Override
	public NodeChilds getChilds()
	{
		return childs;
	}

	@Override
	public boolean isPath()
	{
		return isPath;
	}

	@Override

	public int getNbOfDescendants()
	{
		return nbOfDescendants;
	}

	@Override
	public int getNbOfParents()
	{
		return nbOfParents;
	}

	@Override
	public int getNbOfChilds()
	{
		return childs.size();
	}

	/**
	 * Est une feuille
	 * 
	 * @return true|false
	 */
	@Override
	public boolean isLeaf()
	{
		return nbOfDescendants == 0;
	}

	@Override
	public boolean isUnfolded()
	{
		for (Node n : getDescendants())
		{
			if (n.getLabel().size() > 1)
				return false;
		}
		return true;
	}

	@Override
	public boolean childsArePaths()
	{
		for (Node child : childs)
		{
			if (!child.isPath())
				return false;
		}
		return true;
	}

	// =======================================================

	@Override
	public Node[] getLeaves()
	{
		ArrayList<Node> ret = new ArrayList<>();

		for (Node n : getDescendants())
		{
			if (n.isLeaf())
				ret.add(n);
		}
		return ret.toArray(new Node[0]);
	}

	/**
	 * @return Toutes les clés à partir du noeud
	 */
	// public Set<String> getAllKeys()
	// {
	// Set<String> ret = new HashSet<>();
	//
	// for (Node n : getDescendants())
	// {
	// Label l = n.getLabel();
	//
	// if (l != null)
	// {
	// for (String k : l)
	// ret.add(k);
	// }
	// }
	// return ret;
	// }

	/**
	 * Récupère tous les parents du noeud
	 * 
	 * @param ret
	 * @param n
	 */
	@Override
	public Node[] getParents()
	{
		ArrayList<Node> ret = new ArrayList<>();
		Node            n   = this.getParent();

		while (n != null)
		{
			ret.add(n);
			n = n.getParent();
		}
		Collections.reverse(ret);
		return ret.toArray(new Node[0]);
	}

	/**
	 * Récupère toutes les racines de chemins
	 * 
	 * @param ret
	 * @param n
	 */
	private Node[] getPaths(boolean ignoreRoot)
	{
		ArrayList<Node> ret = new ArrayList<>();
		s_getPaths(this, ret, ignoreRoot);
		return ret.toArray(new Node[0]);
	}

	/**
	 * Récupère toutes les racines d'arbres
	 * 
	 * @param ret
	 * @param n
	 */
	private Node[] getTrees(boolean ignoreRoot)
	{
		ArrayList<Node> ret = new ArrayList<>();
		s_getTrees(this, ret, ignoreRoot);
		return ret.toArray(new Node[0]);
	}

	@Override
	public Node[] getTrees()
	{
		return getTrees(true);
	}

	@Override
	public Node[] getPaths()
	{
		return getPaths(true);
	}

	private void s_getPaths(Node node, ArrayList<Node> ret, boolean ignoreRoot)
	{
		if (node.isPath() && !(ignoreRoot && node.getParent() == null))
		{
			ret.add(node);
			return;
		}

		for (Node child : node)
			s_getPaths(child, ret, ignoreRoot);
	}

	private void s_getTrees(Node node, ArrayList<Node> ret, boolean ignoreRoot)
	{
		if (!node.isPath() && !(ignoreRoot && node.getParent() == null))
			ret.add(node);

		for (Node child : node)
			s_getTrees(child, ret, ignoreRoot);
	}

	/**
	 * @return Tous les descendants, noeud this inclus
	 */
	@Override
	public Node[] getDescendants()
	{
		ArrayList<Node> ret = new ArrayList<>();
		s_getNodes(ret, this);
		ret.remove(this);
		return ret.toArray(new Node[0]);
	}

	private void s_getNodes(ArrayList<Node> ret, Node n)
	{
		ret.add(n);

		for (Node c : n.getChilds())
			s_getNodes(ret, c);
	}

	/**
	 * @return Le noeud dont l'identifiant est $id
	 */
	@Override
	public Node getNode(int id)
	{
		for (Node n : getDescendants())
		{
			if (n.getId() == id)
				return n;
		}
		return null;
	}

	// =======================================================

	@Override
	public void addChild(boolean generateNodeId, Node... childs)
	{
		assert (query != null);
		final int     moreNbOfDesc;
		final boolean nisPath = isPath;

		// Calcul du nombre de descendants à ajouter
		{
			int tmpNbOfDesc = 0;

			for (Node childd : childs)
			{
				DefaultNode child = (DefaultNode) childd;
				this.childs.add(child);
				child.setQuery(query);
				child.setParent(this);
				tmpNbOfDesc += 1 + child.nbOfDescendants;

				// Calcul des nbParents
				child.nbOfParents = this.nbOfParents + 1;

				if (!child.isLeaf())
					child.deepPropagation(n -> ((DefaultNode) n).nbOfParents++);
			}
			moreNbOfDesc = tmpNbOfDesc;
		}
		query.addNode(generateNodeId, childs);

		if (isPath)
		{
			final int nbChilds = getNbOfChilds();
			isPath = nbChilds <= 1;

			// 2e verif, le fils est un path ?
			for (Node child : this)
			{
				if (!child.isPath())
				{
					isPath = false;
					break;
				}
			}
		}
		// Mise à jour des noeuds parents
		backPropagation(nn -> {
			DefaultNode n = (DefaultNode) nn;
			n.nbOfDescendants += moreNbOfDesc;

			if (nisPath && !isPath)
				n.isPath = false;
		});
	}

	@Override
	public void addChilds(boolean generateNodeId, Collection<Node> childs)
	{
		addChild(generateNodeId, childs.toArray(new Node[0]));
	}

	// =======================================================

	@Override
	public void deepPropagation(Consumer<Node> method)
	{
		for (Node n : this.getDescendants())
		{
			method.accept(n);
			n = n.getParent();
		}
	}

	/**
	 * Applique une méthode depuis le noeud courant exclu jusqu'à la racine
	 */
	@Override
	public void backPropagation(Consumer<Node> method)
	{
		Node n = this;

		do
		{
			method.accept(n);
			n = n.getParent();
		} while (n != null);
	}

	/**
	 * Retourne les données de la fonction collect jusqu'à la racine
	 * 
	 * @param collect
	 */
	private <T> List<T> backCollect(Function<Node, T> collect, boolean reverse, boolean jumpRoot)
	{
		ArrayList<T> ret = new ArrayList<>();
		Node         n   = this;

		do
		{
			if (jumpRoot && n.getParent() == null)
				break;

			ret.add(collect.apply(n));
			n = n.getParent();
		} while (n != null);

		if (reverse && ret.size() > 1)
			Collections.reverse(ret);

		ret.trimToSize();
		return ret;
	}

	@Override
	public <T> List<T> backCollect(Function<Node, T> collect)
	{
		return backCollect(collect, true, true);
	}

	// =======================================================

	@Override
	public String toString()
	{
		String ret = "<" + id + ":" + label + "[p" + nbOfParents + ":d" + nbOfDescendants + ":c" + getNbOfChilds() + ":P" + isPath + "]";

		if (value != null)
			ret += "=" + value;

		ret += id + ">";

		if (!isLeaf())
			ret += " => " + childsToString();

		return ret;
	}

	public String childsToString()
	{
		StringBuilder ret    = new StringBuilder();
		boolean       doonce = false;

		ret.append("{" + id + " ");

		for (Node c : childs.getChilds())
		{
			if (doonce)
				ret.append(", ");

			ret.append(c);
			doonce = true;
		}
		return ret + " " + id + "}";
	}

	@Override
	public Iterator<Node> iterator()
	{
		return getChilds().iterator();
	}

	/**
	 * La comparaison se fait sur le hashcode
	 * 
	 * @see #hashCode()
	 */
	@Override
	public boolean equals(Object e)
	{
		if (e instanceof Node)
		{
			return ((Node) e).hashCode() == hashCode();
		}
		return false;
	}

	/**
	 * Le code est fonction de l'id du noeud et de sa requête d'appartenance Il
	 * faut faire attention à bien comparer des noeuds déjà présents dans une
	 * requête
	 */
	@Override
	public int hashCode()
	{
		return id;
	}
}
