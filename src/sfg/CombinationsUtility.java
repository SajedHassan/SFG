package sfg;

import java.util.ArrayList;

public class CombinationsUtility {
	//returns all combinations bigger than 1 and smaller than n
	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Integer>>[] getAllCombinationsOfN(int n) {
		ArrayList<ArrayList<Integer>>[] allCombinations = new ArrayList[n - 1];
    	for (int i = 0; i < allCombinations.length; i++) {
			allCombinations[i] = new ArrayList<ArrayList<Integer>>();
		}
		for (Integer i = 3; i < Math.pow(2, n); i++) {
			int num = i;
			int pos = 0;
			int numOfSetBits = 0;
			ArrayList<Integer> combination = new ArrayList<Integer>();
	        while (num > 0)
	        {
	            int check = num & 1;
	            numOfSetBits += check;
	            num >>= 1;
	            if (check == 1) {
	            	combination.add(pos);
	            }
	            pos++;
	        }
	        if (numOfSetBits > 1) {
	        	allCombinations[numOfSetBits - 2].add(combination);
	        }    
	    }
		return allCombinations;
	}
}
