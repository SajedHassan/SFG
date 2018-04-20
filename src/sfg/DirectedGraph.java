package sfg;

import java.util.ArrayList;
import java.util.List;

public class DirectedGraph {
	private List<ArrayList<Node>> adjacencyList;

	public DirectedGraph(int numOfNodes) {
		adjacencyList = new ArrayList<ArrayList<Node>>(numOfNodes);
		// Initialize the ArrayList
		for (int i = 0; i < numOfNodes; i++) {
			adjacencyList.add(i, new ArrayList<Node>());
		}
	}

	public void connectXToY(int x, int y, float gain) {
		adjacencyList.get(x).add(new Node(y, gain));
	}

	public int size() {
		return adjacencyList.size();
	}

	public ArrayList<Node> getAdjacencyListOf(int nodeId) {
		return adjacencyList.get(nodeId);
	}

	@Override
	public String toString() {
		StringBuilder graphConnections = new StringBuilder();
		for (int i = 0; i < adjacencyList.size(); i++) {
			ArrayList<Node> sourceNoodSuccessors = adjacencyList.get(i);
			graphConnections.append(i + " ==> ");
			for (Node nextNode : sourceNoodSuccessors) {
				graphConnections.append(nextNode.getNextNodeId() + " ");
			}
			graphConnections.append("\n");
		}
		return graphConnections.toString();
	}
}
