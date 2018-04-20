package sfg;

public class Node {
	private int currentNodeId;
	private float currentNodeGain;

	public Node(int nextNodeId, float nextNodeGain) {
		this.currentNodeId = nextNodeId;
		this.currentNodeGain = nextNodeGain;
	}

	public int getNextNodeId() {
		return currentNodeId;
	}

	public void setNextNodeId(int nextNodeId) {
		this.currentNodeId = nextNodeId;
	}

	public float getNextNodeGain() {
		return currentNodeGain;
	}

	public void setNextNodeGain(float nextNodeGain) {
		this.currentNodeGain = nextNodeGain;
	}
}
