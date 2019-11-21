import java.util.ArrayList;
import java.util.Random;

public class ACS {

    public static Tour best_tour;
    public static ArrayList<Ant> ants = new ArrayList<Ant>();
    public static int NUM_ANTS = Runner.NUM_ANTS;
    public static int NUM_ITS = Runner.NUM_ITS;
    public static Double best_tour_length = Double.MAX_VALUE;
    
    public static void run_ACS() {

        //initialize ants
        for (int i = 0; i < NUM_ANTS; i++) {
            Ant ant = new Ant();
            ants.add(ant);
        }

        for (int i = 0; i < NUM_ITS; i++) {
            for (int j = 0; j < NUM_ANTS; j++) {

                //ant does tour
                Tour tour = ants.get(j).tour();

                //assign new best tour if necessary, print out update
                if (tour.get_length() < best_tour_length) {
                    System.out.println("\nIteration: " + i + " Ant: " + j + " New best tour length: " + tour.get_length() + "\n");                
                    best_tour = new Tour(tour.get_length(), tour.get_cities_visited());
                    best_tour_length = tour.get_length();
                }
                
                //local pheromone update after ant completes its tour
                Paths.local_pheromone_update_ACS(tour);

            }

            /*offline pheromone update: every leg gets updated,
            legs in tour of best ant so far getpheromone levels increased*/
            Paths.offline_pheromone_update_ACS();

            //reset all ants' paths
            reset_ant_paths();

            //print update for end of iteration
            System.out.println("Iteration " + i +  ": Best tour length so far: " + best_tour.get_length());
        }
    }

    public static void reset_ant_paths() {
        for (int i = 0; i < ants.size(); i++) {
            ants.get(i).reset_path();
        }
    }
}