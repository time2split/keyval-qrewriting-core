package insomnia.qrewriting.query.node;

import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

import insomnia.builder.Builder;
import insomnia.builder.BuilderException;
import insomnia.qrewriting.query.Label;
import insomnia.qrewriting.query.Query;

public final class NodeBuilder extends Builder<Node>
{
	Class<? extends Node> nodeClass;
	Stack<Node>           stack_node;
	Node                  currentNode;
	boolean               opt_generateNodeId = true;

	public NodeBuilder(Class<? extends Node> nodeClass)
	{
		super();
		setNodeClass(nodeClass);
	}

	public NodeBuilder(Node builded)
	{
		super(builded);
		setNodeClass(builded.getClass());
	}

	private void setNodeClass(Class<? extends Node> nodeClass)
	{
		this.nodeClass = nodeClass;
	}

	@Override
	public void setBuilded(Node node)
	{
		super.setBuilded(node);
		currentNode = node;
		stack_node  = new Stack<>();
		stack_node.add(currentNode);
	}

	public void generateNodeId(boolean v)
	{
		opt_generateNodeId = v;
	}

	// ========================================================================

	@Override
	public void build() throws BuilderException
	{
		stack_node.clear();
	}

	@Override
	public Node newBuild() throws BuilderException
	{
		Node build = newNode();
		setBuilded(build);
		build();
		return build;
	}

	public static Node newNode(Class<? extends Node> nodeClass) throws BuilderException
	{
		return newNode(null, nodeClass);
	}

	public static Node newNode(Query query, Class<? extends Node> nodeClass) throws BuilderException
	{
		try
		{
			Node ret = nodeClass.getDeclaredConstructor().newInstance();
			ret.setQuery(query);
			return ret;
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			throw new BuilderException(e);
		}
	}

	private static Node newCopy(Query query, Node toCopy, Class<? extends Node> nodeClass) throws BuilderException
	{
		try
		{
			return nodeClass.getDeclaredConstructor(Query.class, Node.class).newInstance(query, toCopy);
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			throw new BuilderException(e);
		}
	}

	private Node newCopy(Query query, Node toCopy) throws BuilderException
	{
		return newCopy(query, toCopy, nodeClass);
	}

	private Node newNode() throws BuilderException
	{
		return newNode((Query) null);
	}

	private Node newNode(Query query) throws BuilderException
	{
		return newNode(query, nodeClass);
	}

	// ========================================================================

	public NodeBuilder setLabel(Label l)
	{
		currentNode.setLabel(l);
		return this;
	}

	public NodeBuilder setValue(NodeValue v)
	{
		currentNode.setValue(v);
		return this;
	}

	public NodeBuilder setId(int i)
	{
		currentNode.setId(i);
		return this;
	}

	public NodeBuilder setParent(Node parent)
	{
		currentNode.setParent(parent);
		return this;
	}

	public NodeBuilder addChild(boolean generateNodeId, Node... child)
	{
		currentNode.addChild(generateNodeId, child);
		return this;
	}

	// ========================================================================

	public NodeBuilder root(Query query) throws BuilderException
	{
		__root(newNode(query));
		return this;
	}

	public NodeBuilder root(Query query, Node toCopy) throws BuilderException
	{
		__root(newCopy(query, toCopy));
		return this;
	}

	private NodeBuilder __root(Node root) throws BuilderException
	{
		setBuilded(root);

//		if (query == null)
//			query = root.getQuery();
//		else if (root.getQuery() == null)
//			root.setQuery(query);

		return this;
	}

	public NodeBuilder child() throws BuilderException
	{
		if (getBuilded() == null)
		{
			root(null);
			return this;
		}
		__child(newNode(currentNode.getQuery()));
		return this;
	}

	public NodeBuilder child(Node toCopy) throws BuilderException
	{
		if (getBuilded() == null)
		{
			root(null, toCopy);
			return this;
		}
		__child(newCopy(currentNode.getQuery(), toCopy));
		return this;
	}

	private NodeBuilder __child(Node child) throws BuilderException
	{
		stack_node.add(currentNode);
		currentNode.addChild(opt_generateNodeId, child);
		currentNode = child;
		return this;
	}

	public NodeBuilder end() throws BuilderException
	{
		if (stack_node.empty())
			throw new BuilderException("No more parent to reach");

		currentNode = stack_node.pop();
		return this;
	}

	public NodeBuilder remove() throws BuilderException
	{
		if (stack_node.empty())
			throw new BuilderException("No more parent to reach");

		Node parent = stack_node.pop();
		parent.getChilds().removeChild(currentNode);
		currentNode = parent;
		return this;
	}
}
