package sfg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import interfaces.SFG;

public class PathsUtility {
	private SFG sfg;
	private ArrayList<ArrayList<Integer>> forwardPaths;
	private ArrayList<ArrayList<Integer>> loopsOfCertainNode;

	public PathsUtility(SFG sfg) {
		this.sfg = sfg;
	}

	public ArrayList<ArrayList<Integer>> getAllForwardPaths() {
		forwardPaths = new ArrayList<ArrayList<Integer>>();
		boolean[] isVisited = new boolean[this.sfg.size()];
		ArrayList<Integer> pathList = new ArrayList<Integer>();
		pathList.add(sfg.getSOURCE_NOOD());
		getAllPaths(sfg.getSOURCE_NOOD(), sfg.getSINK_NOOD(), isVisited, pathList);
		return forwardPaths;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Integer>>[] getAllLoops() {
		ArrayList<ArrayList<Integer>>[] loops;
		loops = new ArrayList[sfg.size()];
		for (int i = 0; i < sfg.size(); i++) {
			loopsOfCertainNode = new ArrayList<ArrayList<Integer>>();
			loops[i] = getAllLoopsForCertainNode(i);
		}
		return loops;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Integer>> removeDuplicatedLoops(ArrayList<ArrayList<Integer>>[] allLoops) {
		ArrayList<ArrayList<Integer>> nonRepeatedLoops = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> loopsOfCertainNode = allLoops[0];
		HashMap<String, String> hashMap = new HashMap<String, String>();
		for (int i = 0; i < loopsOfCertainNode.size(); i++) {
			ArrayList<Integer> loopToBeSorted = (ArrayList<Integer>) loopsOfCertainNode.get(i).clone();
			Collections.sort(loopToBeSorted);
			hashMap.put(loopToBeSorted.toString(), null);
			nonRepeatedLoops.add(loopsOfCertainNode.get(i));
		}
		for (int i = 1; i < allLoops.length; i++) {
			loopsOfCertainNode = allLoops[i];
			for (int j = 0; j < loopsOfCertainNode.size(); j++) {
				ArrayList<Integer> loopToBeSorted = (ArrayList<Integer>) loopsOfCertainNode.get(j).clone();
				Collections.sort(loopToBeSorted);
				if (!hashMap.containsKey(loopToBeSorted.toString())) {
					hashMap.put(loopToBeSorted.toString(), null);
					nonRepeatedLoops.add(loopsOfCertainNode.get(j));
				}
			}
		}
		return nonRepeatedLoops;
	}

	public ArrayList<ArrayList<Integer>>[] getAllNoneTouchingLoops(ArrayList<ArrayList<Integer>> allNoneRepeatedLoops) {
		CombinationsUtility combinationUtility = new CombinationsUtility();
		ArrayList<ArrayList<Integer>>[] allCombinations;
		allCombinations = combinationUtility.getAllCombinationsOfN(allNoneRepeatedLoops.size());
		for (int i = 0; i < allCombinations.length; i++) {
			ArrayList<ArrayList<Integer>> groupOfCombinations = allCombinations[i];
			boolean allCurrentCombinationsAreTouching = true;
			for (int j = 0; j < groupOfCombinations.size(); j++) {
				ArrayList<Integer> combination = groupOfCombinations.get(j);
				boolean touching = isTouching(combination, allNoneRepeatedLoops);
				allCurrentCombinationsAreTouching = allCurrentCombinationsAreTouching && touching;
				if (touching) {
					groupOfCombinations.remove(j);
					j--;
					// System.out.println("in");
				}
			}
			if (allCurrentCombinationsAreTouching) {
				for (int j = i + 1; j < allCombinations.length; j++) {
					allCombinations[j].clear();
				}
				break;
			}
		}
		// After removing touching combinations.
		return allCombinations;
	}

	@SuppressWarnings("unchecked")
	private boolean isTouching(ArrayList<Integer> combination, ArrayList<ArrayList<Integer>> allNoneRepeatedLoops) {
		boolean touching = false;
		HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
		// System.out.println(combination + " <=============");
		for (int k = 0; k < combination.size() && !touching; k++) {
			ArrayList<Integer> oneLoopOfTheCombinationToBeChecked = (ArrayList<Integer>) allNoneRepeatedLoops
					.get(combination.get(k)).clone();
			// System.out.println(oneLoopOfTheCombinationToBeChecked + " <==");

			for (int i = 0; i < oneLoopOfTheCombinationToBeChecked.size(); i++) {
				if (!hashMap.containsKey(oneLoopOfTheCombinationToBeChecked.get(i))) {
					hashMap.put(oneLoopOfTheCombinationToBeChecked.get(i), null);
				} else {
					touching = true;
				}
			}
		}
		// System.out.println(touching);
		return touching;
	}

	private ArrayList<ArrayList<Integer>> getAllLoopsForCertainNode(int nodeId) {
		boolean[] isVisited = new boolean[this.sfg.size()];
		ArrayList<Integer> pathList = new ArrayList<Integer>();
		pathList.add(nodeId);
		getLoopsOfNode(nodeId, nodeId, isVisited, pathList);
		return loopsOfCertainNode;
	}

	@SuppressWarnings("unchecked")
	private void getAllPaths(Integer source, Integer sink, boolean[] isVisited, ArrayList<Integer> localPathList) {
		isVisited[source] = true;
		if (source.equals(sink)) {
			this.forwardPaths.add((ArrayList<Integer>) localPathList.clone());
		}
		ArrayList<Node> currentNoodSuccessors = sfg.getAdjacencyListOf(source);
		for (Node nextNode : currentNoodSuccessors) {
			if (!isVisited[nextNode.getNextNodeId()]) {
				localPathList.add(nextNode.getNextNodeId());
				getAllPaths(nextNode.getNextNodeId(), sink, isVisited, localPathList);
				localPathList.remove(localPathList.size() - 1);
			}
		}
		isVisited[source] = false;
	}

	@SuppressWarnings("unchecked")
	private void getLoopsOfNode(Integer source, Integer sink, boolean[] isVisited, ArrayList<Integer> localPathList) {
		isVisited[source] = true;
		ArrayList<Node> currentNoodSuccessors = sfg.getAdjacencyListOf(source);
		for (Node nextNode : currentNoodSuccessors) {
			if (!isVisited[nextNode.getNextNodeId()]) {

				localPathList.add(nextNode.getNextNodeId());
				getLoopsOfNode(nextNode.getNextNodeId(), sink, isVisited, localPathList);
				localPathList.remove(localPathList.size() - 1);

			} else if (sink.equals(nextNode.getNextNodeId())) {
				this.loopsOfCertainNode.add((ArrayList<Integer>) localPathList.clone());
			}
		}
		isVisited[source] = false;
	}

	public boolean isTouchingTheForwardPath(ArrayList<Integer> forwardPath, ArrayList<Integer> pathToCompareWith) {
		boolean touching = false;
		HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
		for (Integer nodeId : forwardPath) {
			hashMap.put(nodeId, null);
		}
		for (Integer nodeId : pathToCompareWith) {
			if (hashMap.containsKey(nodeId)) {
				touching = true;
				break;
			}
		}
		return touching;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Integer>> removeLoopsTouchingTheForwardPath(ArrayList<Integer> forwardPath,
			ArrayList<ArrayList<Integer>> nonRepeatedLoops) {
		ArrayList<ArrayList<Integer>> nonRepeatedLoopsWithoutTouchingForwardPath = (ArrayList<ArrayList<Integer>>) nonRepeatedLoops
				.clone();

		for (int i = 0; i < nonRepeatedLoopsWithoutTouchingForwardPath.size(); i++) {
			if (isTouchingTheForwardPath(forwardPath, nonRepeatedLoopsWithoutTouchingForwardPath.get(i))) {
				nonRepeatedLoopsWithoutTouchingForwardPath.remove(i);
				i--;
			}
		}

		return nonRepeatedLoopsWithoutTouchingForwardPath;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Integer>>[] removeMultipleLoopsTouchingTheForwardPath(ArrayList<Integer> forwardPath,
			ArrayList<ArrayList<Integer>> nonRepeatedLoops, ArrayList<ArrayList<Integer>>[] nonTouchingLoops) {
		ArrayList<ArrayList<Integer>>[] nonTouchingLoopsWithoutTouchingForwardPath = new ArrayList[nonTouchingLoops.length];

		for (int i = 0; i < nonTouchingLoopsWithoutTouchingForwardPath.length; i++) {
			nonTouchingLoopsWithoutTouchingForwardPath[i] = (ArrayList<ArrayList<Integer>>) nonTouchingLoops[i].clone();
			for (int j = 0; j < nonTouchingLoopsWithoutTouchingForwardPath[i].size(); j++) {
				nonTouchingLoopsWithoutTouchingForwardPath[i].set(j, (ArrayList<Integer>) nonTouchingLoops[i].get(j).clone());
			}
		}

		for (int i = 0; i < nonTouchingLoopsWithoutTouchingForwardPath.length; i++) {
			for (int j = 0; j < nonTouchingLoopsWithoutTouchingForwardPath[i].size(); j++) {
				ArrayList<Integer> combination = nonTouchingLoopsWithoutTouchingForwardPath[i].get(j);
				for (int k = 0; k < combination.size(); k++) {
					if (isTouchingTheForwardPath(forwardPath, nonRepeatedLoops.get(combination.get(k)))) {
						nonTouchingLoopsWithoutTouchingForwardPath[i].get(j).remove(k);
						k--;
					}
				}
				if (combination.size() < i + 2) {
					nonTouchingLoopsWithoutTouchingForwardPath[i].remove(j);
					j--;
					if (combination.size() > 1) {
						nonTouchingLoopsWithoutTouchingForwardPath[combination.size() - 2].add(combination);
					}
				}
			}
		}
		return nonTouchingLoopsWithoutTouchingForwardPath;
	}

}
