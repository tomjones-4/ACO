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
    //lines in between these comments are needed if Tom's version of pheromone updates is correct
    public static ArrayList<Tour> iteration_tours = new ArrayList<Tour>();
    //end

    public static void run_EAS() {

    	for (int i = 0; i < NUM_ANTS; i++) {
            Ant ant = new Ant();
            ants.add(ant);
        }

        for (int i = 0; i < Runner.NUM_ITS; i++) {
            //lines in between these comments are needed if Tom's version of pheromone updates is correct
            iteration_tours.clear();
            //end
            for (int j = 0; j < Runner.NUM_ANTS; j++) {
                Tour current_tour = ants.get(j).tour();
                //lines in between these comments are needed if Tom's version of pheromone updates is correct
                iteration_tours.add(current_tour);
                //end
                if (current_tour.get_length() < best_tour_length) {
                    System.out.println("Iteration: " + i + " Ant: " + j + " New best tour length: " + current_tour.get_length());                
                    best_tour = new Tour(current_tour.get_length(), current_tour.get_cities_visited());
                    best_tour_length = current_tour.get_length();
                }
                Paths.local_pheremone_update_EAS(current_tour); //Will's line
            }
            System.out.println("Iteration " + i +  ": Best tour length so far: " + best_tour.get_length());
            //lines in between these comments are needed if Tom's version of pheromone updates is correct
            //Paths.global_pheromone_update_EAS_Tom();
            //Paths.best_tour_pheremone_update_EAS_Tom(best_tour);
            //end
            Paths.best_tour_pheremone_update_EAS(best_tour); //Will's line
            Paths.global_pheremone_update_EAS(); //Will's line

            reset_ant_paths();
        }
    }

    public static void reset_ant_paths() {
        for (int i = 0; i < ants.size(); i++) {
            ants.get(i).reset_path();
        }
    }
}