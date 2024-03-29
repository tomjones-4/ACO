import java.util.*;
import java.io.*;

public class EAS {
    public static Tour best_tour;
	public static ArrayList<Ant> ants = new ArrayList<Ant>();
    public static int NUM_ANTS = Runner.NUM_ANTS;
    public static int NUM_ITS = Runner.NUM_ITS;
    public static Double best_tour_length = Double.MAX_VALUE;
    public static ArrayList<Tour> iteration_tours = new ArrayList<Tour>();

    public static void run_EAS() {

    	for (int i = 0; i < NUM_ANTS; i++) {
            Ant ant = new Ant();
            ants.add(ant);
        }

        for (int i = 0; i < Runner.NUM_ITS; i++) {

            //reset the previous iteration's ant tours
            iteration_tours.clear();

            for (int j = 0; j < Runner.NUM_ANTS; j++) {
                Tour current_tour = ants.get(j).tour();

                //add tour to iteration_tours to allow for updating of pheromones after each iteration
                iteration_tours.add(current_tour);

                //set new best_tour if necessary
                if (current_tour.get_length() < best_tour_length) {
                    System.out.println("Iteration: " + i + " Ant: " + j + " New best tour length: " + current_tour.get_length());                
                    best_tour = new Tour(current_tour.get_length(), current_tour.get_cities_visited());
                    best_tour_length = current_tour.get_length();
                }
            }

            //print update for end of iteration
            System.out.println("Iteration " + i +  ": Best tour length so far: " + best_tour.get_length());

            //update pheromone levels on all paths
            Paths.global_pheromone_update_EAS();

            //give extra pheromone boost to legs used in best tour of all time
            Paths.best_tour_pheremone_update_EAS(best_tour);

            reset_ant_paths();

            //check 10 minute time limit after each iteration
            long end = System.currentTimeMillis();
            if ((end-Runner.START) / 100 > 6000) {
                System.out.println("10 minute time limit reached");
                break;
            }
        }
    }

    public static void reset_ant_paths() {
        for (int i = 0; i < ants.size(); i++) {
            ants.get(i).reset_path();
        }
    }
}