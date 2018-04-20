package interfaces;

import java.util.ArrayList;

import sfg.DirectedGraph;
import sfg.Node;
import sfg.PathsUtility;
import sfg.SFGI;

public class SFG implements SFGI {
	private DirectedGraph sfg;
	private final int SOURCE_NOOD;
	private final int SINK_NOOD;
	private PathsUtility pathUtility;

	public SFG(int numOfNodes) {
		sfg = new DirectedGraph(numOfNodes);
		pathUtility = new PathsUtility(this);
		this.SOURCE_NOOD = 0;
		this.SINK_NOOD = numOfNodes - 1;
	}

	public int getSOURCE_NOOD() {
		return SOURCE_NOOD;
	}

	public int getSINK_NOOD() {
		return SINK_NOOD;
	}

	@Override
	public void connectXToY(int x, int y, float gain) {
		sfg.connectXToY(x, y, gain);
	}

	@Override
	public int size() {
		return sfg.size();
	}

	@Override
	public void testConnectivity() {
		System.out.println(this.sfg.toString());
	}

	public ArrayList<Node> getAdjacencyListOf(int nodeId) {
		return sfg.getAdjacencyListOf(nodeId);
	}

	@Override
	public ArrayList<ArrayList<Integer>> getAllForwardPaths() {
		return pathUtility.getAllForwardPaths();
	}

	@Override
	public ArrayList<ArrayList<Integer>> getAllLoops() {
		return removeDuplicatedLoops(pathUtility.getAllLoops());
	}

	private ArrayList<ArrayList<Integer>> removeDuplicatedLoops(ArrayList<ArrayList<Integer>>[] allLoops) {
		return pathUtility.removeDuplicatedLoops(allLoops);
	}

	@Override
	public ArrayList<ArrayList<Integer>>[] getAllNoneTouchingLoops(ArrayList<ArrayList<Integer>> allNoneRepeatedLoops) {
		return pathUtility.getAllNoneTouchingLoops(allNoneRepeatedLoops);
	}

	@Override
	public float getForwardPathGain(ArrayList<Integer> forwardPath) {
		float pathGain = 1;
		for (int i = 0; i < forwardPath.size() - 1; i++) {
			pathGain *= getGainOfNextNodeById(forwardPath.get(i), forwardPath.get(i + 1));
		}
		return pathGain;
	}

	@Override
	public float getLoopGain(ArrayList<Integer> loopPath) {
		float loopGain = 1;
		for (int i = 0; i < loopPath.size(); i++) {
			loopGain *= getGainOfNextNodeById(loopPath.get(i), loopPath.get((i + 1) % loopPath.size()));
		}
		return loopGain;
	}

	@Override
	public float getDeltaGain(ArrayList<ArrayList<Integer>> nonRepeatedLoops,
			ArrayList<ArrayList<Integer>>[] nonTouchingLoops) {
		float deltaGain = 1;
		float sign = -1;
		float loopGainSum = 0;
		for (int i = 0; i < nonRepeatedLoops.size(); i++) {
			loopGainSum += this.getLoopGain(nonRepeatedLoops.get(i));
		}
		deltaGain += loopGainSum * sign;

		for (int i = 0; i < nonTouchingLoops.length; i++) {
			ArrayList<ArrayList<Integer>> multipleLoopsCombination = nonTouchingLoops[i];
			float multipleLoopsCombinationGain = 0;
			sign = sign * -1;
			for (int j = 0; j < multipleLoopsCombination.size(); j++) {
				ArrayList<Integer> multileLoops = multipleLoopsCombination.get(j);
				float multipleLoopsGain = 1;
				for (Integer loopId : multileLoops) {
					multipleLoopsGain *= this.getLoopGain(nonRepeatedLoops.get(loopId));
				}
				multipleLoopsCombinationGain += multipleLoopsGain;
			}
			deltaGain += multipleLoopsCombinationGain * sign;
		}
		return deltaGain;
	}

	@Override
	public float getDeltaForGivenForwardPath(ArrayList<Integer> forwardPath,
			ArrayList<ArrayList<Integer>> nonRepeatedLoops, ArrayList<ArrayList<Integer>>[] nonTouchingLoops) {
		ArrayList<ArrayList<Integer>> nonRepeatedLoopsNonTouchingForwardPath = pathUtility
				.removeLoopsTouchingTheForwardPath(forwardPath, nonRepeatedLoops);
		ArrayList<ArrayList<Integer>>[] nonTouchingLoopsNonTouchingForwardPath = pathUtility
				.removeMultipleLoopsTouchingTheForwardPath(forwardPath, nonRepeatedLoops, nonTouchingLoops);
		return this.getDeltaGain(nonRepeatedLoopsNonTouchingForwardPath, nonTouchingLoopsNonTouchingForwardPath);
	}

	private float getGainOfNextNodeById(int nodeId, int nextNodeId) {
		float gain = 0;
		ArrayList<Node> adjacencyListOfGivenNood = sfg.getAdjacencyListOf(nodeId);
		for (Node node : adjacencyListOfGivenNood) {
			if (node.getNextNodeId() == nextNodeId) {
				gain = node.getNextNodeGain();
				break;
			}
		}
		return gain;
	}

	@Override
	public float getOverAllGain(ArrayList<ArrayList<Integer>> forwardPaths,
			ArrayList<ArrayList<Integer>> nonRepeatedLoops, ArrayList<ArrayList<Integer>>[] nonTouchingLoops) {
		float numeratorOfMansonFormula = 0;
		for (ArrayList<Integer> forwardPath : forwardPaths) {
			numeratorOfMansonFormula += this.getForwardPathGain(forwardPath)
					* this.getDeltaForGivenForwardPath(forwardPath, nonRepeatedLoops, nonTouchingLoops);
		}
		return numeratorOfMansonFormula / this.getDeltaGain(nonRepeatedLoops, nonTouchingLoops);
	}
}
