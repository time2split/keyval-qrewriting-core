package insomnia.qrewriting.query;

import insomnia.qrewriting.query.node.Node;

public interface Query
{
	public Node getRoot();

	public QueryInfos getInfos();

	// ========================================================================

	public void setRoot(Node node);

	public void addNode(boolean generateNodeId, Node... child);
}
