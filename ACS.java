import java.util.ArrayList;

//import Ant.Tour;

public class ACS {

    public static Tour best_tour;
    //public static Tour best_tour = new Tour();
    //public static Ant test_ant = new Ant();
    //public static Tour best_tour = new Ant.Tour();
    public static ArrayList<Ant> ants = new ArrayList<Ant>();
    public static int NUM_ANTS = Runner.NUM_ANTS;
    public static int NUM_ITS = Runner.NUM_ITS;
    
    public static void run_ACS() {
        for (int i = 0; i < NUM_ANTS; i++) {
            Ant ant = new Ant();
            ants.add(ant);
            if (i == NUM_ANTS - 1) { //debugging
                System.out.println(ants.get(i).path); //I think at this point the path is only of length one
            } //debugging                             //because only one city has been added
        }
        //best_tour = ants.get(0).tour();
        Double best_tour_length = Double.MAX_VALUE;
        for (int i = 0; i < NUM_ITS; i++) {
            for (int j = 0; j < NUM_ANTS; j++) {
                System.out.println("Ant number: " + j); //debugging
                Tour tour = ants.get(j).tour();
                /* local pheromone update: each ant reduces amount of pheromone on each leg of its respective tour
                   pher_ij = (1-epsilon)*(pher_ij) + epsilon*pher_0
                */
                Paths.local_pheromone_update_ACS(tour);
                /* This local pheromone update happens AS the ant is doing its tour.
                   That is, after ant 1 completes its tour, the edges should be updated/reduced pheromone
                   before ant 2 goes on its your.
                   Note: Prof says we can assume ants run their tours one after the other,
                   rather than concurrently because it does not have significant effect
                   on results, and it's much easier to implement.
                */ 
                if (tour.get_length() < best_tour_length) {
                    System.out.println("\n\n\n\nIteration: " + i + " Ant: " + j + " New best tour length: " + tour.get_length());
                    best_tour = tour;
                    best_tour_length = tour.get_length();
                }
                
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

}