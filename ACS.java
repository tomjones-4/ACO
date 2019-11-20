import java.util.ArrayList;
import java.util.Random; //this import is used for nearest neighbor tour (can maybe delete if we move nn_tour elsewhere)

public class ACS {

    public static Tour best_tour;
    public static ArrayList<Ant> ants = new ArrayList<Ant>();
    public static int NUM_ANTS = Runner.NUM_ANTS;
    public static int NUM_ITS = Runner.NUM_ITS;
    
    public static void run_ACS() {

        //initialize ants
        for (int i = 0; i < NUM_ANTS; i++) {
            Ant ant = new Ant();
            ants.add(ant);
        }

        //set equal to max vax value, and first tour will become best tour
        Double best_tour_length = Double.MAX_VALUE;

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

    //nearest neighbor tour method: ant chooses first city randomly, then closest new city until all cities chosen
    public static Tour run_NNTour() {
        Double nn_length = 0.0;
        ArrayList<Integer> nn_path = new ArrayList<Integer>();
        Random rand = new Random();
        int current_city = rand.nextInt(Reader.num_cities);
        nn_path.add(current_city);
        for (int i=0; i < Reader.num_cities - 1; i++) {
            Double min_dist = Double.MAX_VALUE;
            int min_city = -1;
            for (int j = 0; j < Reader.num_cities; j++) {

                //look at cities not in path so far
                if (!nn_path.contains(j)) {

                    //update nearest new city if necessary
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

        //after all cities have been visited, return to start city and add that distance
        nn_path.add(start_city);
        nn_length += Paths.get_distance(last_city, start_city);
        Tour nn_tour = new Tour(nn_length, nn_path);
        return nn_tour;
    }
}