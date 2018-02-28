package insomnia.qrewriting.query.node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import insomnia.qrewriting.query.Label;

/**
 * Noeud d'une requête
 * 
 * @author zuri
 * 
 */
public class Node implements Cloneable
{
	private Node		parent;
	private Label		label;
	private NodeChilds	childs;
	private NodeValue	value;
	private int			id				= 0;
	private boolean		isPath			= true;

	private int			nbOfDescendant	= 0;

	public Node()
	{
		childs = new NodeChilds();
		setLabel(new Label());
	}

	/**
	 * Constructeur par copie
	 * 
	 * @param n
	 */
	public Node(Node n)
	{
		setLabel(new Label(n.label));
		setId(n.id);
		childs = new NodeChilds(n.childs, this);
		isPath = n.isPath;
		nbOfDescendant = n.nbOfDescendant;

		if (n.value != null)
			setValue(n.value.clone());
	}

	public Node(Label l)
	{
		childs = new NodeChilds();
		setLabel(l);
	}

	public Node(NodeValue v)
	{
		childs = new NodeChilds();
		setLabel(new Label());
		setValue(v);
	}

	public Node(Label l, NodeValue v)
	{
		childs = new NodeChilds();
		setLabel(l);
		setValue(v);
	}

	// =======================================================

	public void setParent(Node parent)
	{
		this.parent = parent;
	}

	public Node setParentMe(Node parent)
	{
		setParent(parent);
		return this;
	}

	public Node getParent()
	{
		return parent;
	}

	public void setLabel(Label l)
	{
		label = l;
	}

	public Node setLabelMe(Label l)
	{
		setLabel(l);
		return this;
	}

	public Label getLabel()
	{
		return label;
	}

	public void setValue(NodeValue v)
	{
		value = v;
	}

	public Node setValueMe(NodeValue v)
	{
		setValue(v);
		return this;
	}

	public NodeValue getValue()
	{
		return value;
	}

	public void setId(int i)
	{
		id = i;
	}

	public Node setIdMe(int i)
	{
		setId(i);
		return this;
	}

	public int getId()
	{
		return id;
	}

	// =======================================================

	public NodeChilds getChilds()
	{
		return childs;
	}

	public boolean isPath()
	{
		return isPath;
	}

	public int getNbOfDescendant()
	{
		return nbOfDescendant;
	}

	public int getNbOfChilds()
	{
		return childs.size();
	}

	/**
	 * Est une feuille
	 * 
	 * @return true|false
	 */
	public boolean isLeaf()
	{
		return nbOfDescendant == 0;
	}

	public boolean isUnfolded()
	{
		for (Node n : getNodes())
		{
			if (n.getLabel().size() > 1)
				return false;
		}
		return true;
	}

	// =======================================================

	public Node[] getLeafs()
	{
		ArrayList<Node> ret = new ArrayList<>();

		for (Node n : getNodes())
		{
			if (n.isLeaf())
				ret.add(n);
		}
		return ret.toArray(new Node[0]);
	}

	/**
	 * @return Toutes les clés à partir du noeud
	 */
	public Set<String> getAllKeys()
	{
		Set<String> ret = new HashSet<>();

		for (Node n : getNodes())
		{
			Label l = n.getLabel();

			if (l != null)
			{
				for (String k : l)
					ret.add(k);
			}
		}
		return ret;
	}

	/**
	 * @return Tous les noeuds sauf lui même
	 */
	public Node[] getNodes()
	{
		ArrayList<Node> ret = new ArrayList<>(256);
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
	public Node getNode(int id)
	{
		for (Node n : getNodes())
		{
			if (n.getId() == id)
				return n;
		}
		return null;
	}

	// =======================================================

	public void addChild(Node... childs)
	{
		final int moreNbOfDesc;
		final boolean nisPath = isPath;

		// Calcul du nombre de descendants à ajouter
		{
			int tmpNbOfDesc = 0;

			for (Node child : childs)
			{
				this.childs.add(child);
				child.setParent(this);
				tmpNbOfDesc += 1 + child.nbOfDescendant;
			}
			moreNbOfDesc = tmpNbOfDesc;
		}

		if (isPath)
		{
			final int nbChilds = getNbOfChilds();
			isPath = nbChilds <= 1;
		}
		// Mise à jour des noeuds parents
		backPropagation(n ->
		{
			n.nbOfDescendant += moreNbOfDesc;

			if (nisPath && !isPath)
				n.isPath = false;
		});
	}

	public Node addChildMe(Node... childs)
	{
		addChild(childs);
		return this;
	}

	/**
	 * Applique une méthode depuis le noeud courant exclus jusqu'à la racine
	 */
	private void backPropagation(Consumer<Node> method)
	{
		Node n = this;

		do
		{
			method.accept(n);
			n = n.getParent();
		}
		while (n != null);
	}

	// =======================================================

	@Override
	public String toString()
	{
		boolean doonce = false;
		String ret = "" + label + "(" + id + ":" + nbOfDescendant + ":" + isPath
				+ ")";

		if (value != null)
			ret += "(=" + value + ")";

		ret += " => {";

		for (Node c : childs.getChilds())
		{
			if (doonce)
				ret += ", ";

			ret += c;
			doonce = true;
		}
		return ret + "}";
	}

	@Override
	public Node clone()
	{
		return new Node(this);
	}
}
