import java.util.ArrayList;

//import Ant.Tour;

public class ACS {

    //public static Tour best_tour;
    public static Tour best_tour = new Tour();
    public static ArrayList<Ant> ants = new ArrayList<Ant>();
    public static int NUM_ANTS = Runner.NUM_ANTS;
    public static int NUM_ITS = Runner.NUM_ITS;
    
    public static void main(String[] args) {
        for (int i = 0; i < NUM_ANTS; i++) {
            Ant ant = new Ant();
            ants.add(ant);
        }
        best_tour = ants.get(0).tour();
        for (int i = 0; i < NUM_ITS; i++) {
            for (int j = 0; j < NUM_ANTS; j++) {
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
                if (tour.get_length() < best_tour.get_length()) {
                    best_tour = tour;
                }
                
            }
            /* offline pheromone update: every leg in tour of best ant so far gets updated
                   pher_ij = (1-rho)*(pher_ij) + rho*(1/length_best) if ij in best
            */
            Paths.offline_pheremone_update_ACS();
            System.out.println("Iteration " + i +  ": Best tour length so far: " + best_tour.get_length());
        }

    }

}