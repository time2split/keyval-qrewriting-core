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
	Query                 query;
	boolean               opt_generateNodeId = true;

	public NodeBuilder(Class<? extends Node> nodeClass)
	{
		super();
		setNodeClass(nodeClass);
	}

	public NodeBuilder(Node builded)
	{
		super(builded);
		setTheQuery(builded.getQuery());
		setNodeClass(builded.getClass());
	}

	private void setNodeClass(Class<? extends Node> nodeClass)
	{
		this.nodeClass = nodeClass;
	}

	@Override
	protected void setBuilded(Node node)
	{
		super.setBuilded(node);
		currentNode = node;
		stack_node  = new Stack<>();
		stack_node.add(currentNode);
	}

	public void setNode(Node node)
	{
		setBuilded(node);
	}

	public Node getNode()
	{
		return (Node) getBuilded();
	}

	public void setTheQuery(Query q)
	{
		query = q;
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

	private Node newCopy(Node toCopy) throws BuilderException
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

	private Node newNode() throws BuilderException
	{
		try
		{
			return nodeClass.getDeclaredConstructor().newInstance();
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			throw new BuilderException(e);
		}
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

	// ========================================================================

	public NodeBuilder root() throws BuilderException
	{
		__root(newNode());
		return this;
	}

	public NodeBuilder root(Node toCopy) throws BuilderException
	{
		__root(newCopy(toCopy));
		return this;
	}

	public NodeBuilder __root(Node root) throws BuilderException
	{
		setBuilded(root);

		if (query == null)
			query = root.getQuery();
		else if (root.getQuery() == null)
			root.setQuery(query);

		return this;
	}

	public NodeBuilder child() throws BuilderException
	{
		if (getNode() == null)
		{
			root();
			return this;
		}
		__child(newNode());
		return this;
	}

	public NodeBuilder child(Node toCopy) throws BuilderException
	{
		if (getNode() == null)
		{
			root(toCopy);
			return this;
		}
		__child(newCopy(toCopy));
		return this;
	}

	private NodeBuilder __child(Node child) throws BuilderException
	{
		stack_node.add(currentNode);

		currentNode.addChild(opt_generateNodeId, child);
		child.setParent(currentNode);

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
}
