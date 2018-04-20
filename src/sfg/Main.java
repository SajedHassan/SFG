package sfg;

import java.util.ArrayList;

import interfaces.SFG;

public class Main {

	public static void main(String[] args) {
        SFGI g = new SFG(6);
        g.connectXToY(0,1, 1);
        g.connectXToY(1,2, 2);
        g.connectXToY(2,3 ,3);
        g.connectXToY(3,4 ,4);
        g.connectXToY(4,5 ,5);
        g.connectXToY(3,3 ,6);
        g.connectXToY(4,1 ,7);
        g.connectXToY(2,1 ,8);
        g.connectXToY(2,4 ,9);
        g.connectXToY(3,1 ,10);

        g.testConnectivity();

        System.out.println("Forward Paths");
        ArrayList<ArrayList<Integer>> fp = g.getAllForwardPaths();
		for (int i = 0; i < fp.size(); i++) {
			System.out.println(fp.get(i));
			System.out.println(g.getForwardPathGain(fp.get(i)));
		}


		System.out.println("\nNon repeated loops");
		ArrayList<ArrayList<Integer>> nonRL = g.getAllLoops();
		for (int i = 0; i < nonRL.size(); i++) {
			System.out.println(nonRL.get(i));
			System.out.println(g.getLoopGain(nonRL.get(i)));
		}

		ArrayList<ArrayList<Integer>>[] com = g.getAllNoneTouchingLoops(nonRL);
    	System.out.println(com[0].size() + " <==");

		for (int i = 0; i < com.length; i++) {
			ArrayList<ArrayList<Integer>> c = com[i];
			System.out.println("\n" + (i + 2) + " :");
			for (int j = 0; j < c.size(); j++) {
				System.out.println(c.get(j));
			}
		}
		
		System.out.println(g.getOverAllGain(fp, nonRL, com));
	}

}
