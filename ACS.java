import java.util.ArrayList;

import Ant.Tour;

public class ACS {

    public static Tour best_tour;
    public static ArrayList<Ant> ants = new ArrayList<Ant>();
    public static int NUM_ANTS = Runner.NUM_ANTS;
    public static int NUM_ITS = Runner.NUM_ITS;
    
    public static void main(String[] args) {
        for (int i = 0; i < NUM_ANTS; i++) {
            Ant ant = new Ant();
            ants.add(ant);
        }
        Tour best_tour = ants.get(0).tour();
        for (int i = 0; i < NUM_ITS; i++) {
            Tour best_iter_tour = ants.get(0).tour();
            for (int j = 1; j < NUM_ANTS; j++) {
                Tour tour = ants.get(j).tour();
                /* local pheromone update: each ant reduces amount of pheromone on each leg of its respective tour
                   pher_ij = (1-epsilon)*(pher_ij) + epsilon*pher_0
                */
                if (tour.get_length() < best_iter_tour.get_length()) {
                    best_iter_tour = tour;
                    if (best_iter_tour.get_length() < best_tour.get_length()) {
                        best_tour = best_iter_tour;
                    }
                }
                /* offline pheromone update: every leg in tour of best ant of iteration gets updated
                   pher_ij = (1-rho)*(pher_ij) + rho*(1/length_best) if ij in best
                */
            }
        }
    }




}