import java.util.ArrayList;
import java.util.Random; //this import is used for nearest neighbor tour (can maybe delete if we move nn_tour elsewhere)

//import Ant.Tour;

public class ACS {

    public static Tour best_tour;

    public static ArrayList<Ant> ants = new ArrayList<Ant>();
    public static int NUM_ANTS = Runner.NUM_ANTS;
    public static int NUM_ITS = Runner.NUM_ITS;
    
    public static void run_ACS() {
        for (int i = 0; i < NUM_ANTS; i++) {
            Ant ant = new Ant();
            ants.add(ant);
        }
        Double best_tour_length = Double.MAX_VALUE; //max value?
        for (int i = 0; i < NUM_ITS; i++) {
            for (int j = 0; j < NUM_ANTS; j++) {

                Tour tour = ants.get(j).tour();
                
                if (tour.get_length() < best_tour_length) {
                    ArrayList<Double> old_pheremones_on_best_path = new ArrayList<Double>();
                    System.out.println("\nIteration: " + i + " Ant: " + j + " New best tour length: " + tour.get_length() + "\n");
                    //System.out.println("new best tour assignment: " + tour.get_cities_visited());                 
                    best_tour =  new Tour(tour.get_length(), tour.get_cities_visited());
                    best_tour_length = tour.get_length();
                }

                /* This local pheromone update happens AS the ant is doing its tour.
                   That is, after ant 1 completes its tour, the edges should be updated/reduced pheromone
                   before ant 2 goes on its your.
                   Note: Prof says we can assume ants run their tours one after the other,
                   rather than concurrently because it does not have significant effect
                   on results, and it's much easier to implement.
                */ 
                Paths.local_pheromone_update_ACS(tour);

            }
            
            /* offline pheromone update: every leg in tour of best ant so far gets updated
                   pher_ij = (1-rho)*(pher_ij) + rho*(1/length_best) if ij in best
            */
            Paths.offline_pheromone_update_ACS();
            //reset all ants' paths
            reset_ant_paths();
            System.out.println("Iteration " + i +  ": Best tour length so far: " + best_tour.get_length());
        }

    }

    public static void reset_ant_paths() {
        for (int i = 0; i < ants.size(); i++) {
            ants.get(i).reset_path();
        }
    }

    public static Tour run_NNTour() { //nearest neighbor tour
        Double nn_length = 0.0;
        ArrayList<Integer> nn_path = new ArrayList<Integer>();
        Random rand = new Random();
        int current_city = rand.nextInt(Reader.num_cities);
        nn_path.add(current_city);
        for (int i=0; i < Reader.num_cities - 1; i++) { //num cities to be added after start city
            Double min_dist = Double.MAX_VALUE;
            int min_city = -1;
            for (int j = 0; j < Reader.num_cities; j++) { //all cities to compare distance
                if (!nn_path.contains(j)) {
                    if (Paths.get_distance(current_city, j) < min_dist) {
                        min_city = j;
                        min_dist = Paths.get_distance(current_city, j);
                    }
                }
            }
            nn_path.add(min_city);
            nn_length += min_dist;
            current_city = min_city;
        }
        int start_city = nn_path.get(0);
        int last_city = nn_path.get(nn_path.size() - 1);
        nn_path.add(start_city);
        nn_length += Paths.get_distance(last_city, start_city);
        Tour nn_tour = new Tour(nn_length, nn_path);
        return nn_tour;
    }

}