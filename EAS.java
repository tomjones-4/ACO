/* Everything for EAS is drawn from here: 
https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=14&cad=rja&uact=8&ved=2ahUKEwigu86pkPXlAhWmct8KHbECCLcQFjANegQIBxAC&url=http%3A%2F%2Fwww.mayfeb.com%2FOJS%2Findex.php%2FCON%2Farticle%2Fdownload%2F543%2F221&usg=AOvVaw2pjLvb8-JMgOZdT1ALhCkF
*/


import java.util.*;
import java.io.*;


public class EAS {
    public static Tour best_tour;
	public static ArrayList<Ant> ants = new ArrayList<Ant>();
    public static int NUM_ANTS = Runner.NUM_ANTS;
    public static int NUM_ITS = Runner.NUM_ITS;
    public static Double best_tour_length = Double.MAX_VALUE;

    public static void run_EAS() {

    	for (int i = 0; i < NUM_ANTS; i++) {
            Ant ant = new Ant();
            ants.add(ant);
        }

        for (int i = 0; i < Runner.NUM_ITS; i++) {
            for (int j = 0; j < Runner.NUM_ANTS; j++) {
                Tour current_tour = ants.get(j).tour();
                if (current_tour.get_length() < best_tour_length) {
                    best_tour = current_tour;
                }
            }
        }
    }
}