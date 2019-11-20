/* This class has two 2d arrays, city_distances and pheremones. */
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;

public class Paths {

	private static Double MIN_PHER = 0.001;
	public static Double[][] city_distances;
	public static Double[][] pheremones;

	//constructor which initializes all distances between cities in city_distnaces
	public Paths(ArrayList<Double[]> city_coords){
		city_distances = new Double[city_coords.size()][city_coords.size()];
		pheremones = new Double[city_coords.size()][city_coords.size()];

		//populate the city_distances with Euclidean distances between city_coords
		for (int i=0; i < city_coords.size(); i++){
			for (int j=0; j < city_coords.size(); j ++){
				Double y2 = city_coords.get(i)[1];
				Double y1 = city_coords.get(j)[1];
				Double x2 = city_coords.get(i)[0];
				Double x1 = city_coords.get(j)[0];
				Double distance = Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
				city_distances[i][j] = distance; 

			}
		}
	}

	//initialize pheromone levels on all paths
	public void generate_init_pheremones(){
		for (int i=0; i < city_distances.length; i++) {
			for (int j=0; j < city_distances.length; j ++) {
				pheremones[i][j] = Runner.INITIAL_PHER;
			}
		}
	}


	/* Tom's comments
	   I'm not sure if there are any "local" pheromone updates in EAS the same way there are in ACS.
	   From what I can tell from Wikipedia, there are the global updates where each leg is updated
	   with pher = (1 - evap) * pher + sum(delta_pher), where delta_pher = 1/L_k if leg is in ant k's
	   tour, where L_k is length of ant k's tour, and delta_pher = 0 if leg is not in ant k's tour. 
	   This update takes place after the iteration, so it can be thought of as a global update,
	   although it depends on each ant's tour. 
	   
	   Then, the best_tour pheromone update is, after every iteration, update each leg in the best
	   tour as follows: pher = pher + elitism * 1/L_best, where L_best is length of best tour.
	   
	   Reference: https://en.wikipedia.org/wiki/Ant_colony_optimization_algorithms#Ant_Colony_System_(ACS) 
	*/

	//Tom's methods start

	public static void global_pheromone_update_EAS_Tom() {

		//create 2d array for updates to each path between cities. values are initialized to 0.0
		pheromone_updates = new Double[Runner.NUM_CITIES][Runner.NUM_CITIES];
		for (int i = 0; i < Runner.NUM_ANTS; i++) {
			Tour tour = EAS.iteration_tours.get(i);

			//get all legs of tour and update value in pheromone_updates
			for (int j = 0; j < tour.get_size() - 1; j++) {
				int current = tour.get_cities_visited().get(i);
				int next = tour.get_cities_visited().get(i+1);
				pheromone_updates[current][next] += 1 / (tour.get_length());
			}
		}
		for (int i = 0; i < Runner.NUM_CITIES; i++) {
			for (int j = 0; j < Runner.NUM_CITIES; j++) {
				pheremones[i][j] = (1 - Runner.EVAP_FACTOR) * pheremones[i][j] + pheromone_updates[i][j];
			}
		}
	}

	//literally just copied Will's method because I think it's correct
	//adds pheromones to all legs in best tour
	public static void best_tour_pheremone_update_EAS_Tom(Tour best_tour) {
		for (int i = 0; i < best_tour.get_size() - 1; i++) {
			int current = best_tour.get_cities_visited().get(i);
			int next = best_tour.get_cities_visited().get(i+1);
			pheremones[current][next] += Runner.ELITISM * (1 / best_tour.get_length());
		}
	}

	//Tom's methods end

	/*The following three methods are pheremone update methods for EAS*/
	//Apply this after each ant completes a tour.
	public static void local_pheremone_update_EAS(Tour tour) {
		for (int i = 0; i < tour.get_size() - 1; i++) {
			int current = tour.get_cities_visited().get(i);
			int next = tour.get_cities_visited().get(i+1);
			pheremones[current][next] += (1/tour.get_length());
		}
	}
	//Apply this once (feeding in the best tour) after each iteration.
	public static void best_tour_pheremone_update_EAS(Tour tour) {
		for (int i = 0; i < tour.get_size() - 1; i++) {
			int current = tour.get_cities_visited().get(i);
			int next = tour.get_cities_visited().get(i+1);
			pheremones[current][next] += Runner.ELITISM * (1/tour.get_length());
		}
	}
	//Apply this at the end of each iteration
	//WHAT does this method do?
	public static void global_pheremone_update_EAS() {
		for (int i = 0; i < Runner.problem_reader.city_coords.size(); i++) {
			for (int j = 0; j < Runner.problem_reader.city_coords.size(); j++) {
				if (i!=j) {
					double first = (1-Runner.EVAP_FACTOR) * pheremones[i][j];
				}
				if (pheremones[i][j] < MIN_PHER) {
					pheremones[i][j] = MIN_PHER;
				}
			}
		}
	}

	/*offline pheromone update: every leg gets updated,
      legs in tour of best ant so far getpheromone levels increased*/
	public static void offline_pheromone_update_ACS() {
		for (int i = 0; i < Runner.problem_reader.city_coords.size(); i++) {

			//locate city i's index in best_tour
			int i_index = ACS.best_tour.get_cities_visited().indexOf(i);
			for (int j = 0; j < Runner.problem_reader.city_coords.size(); j++) {

				//can't have pheremones leading from a city to itself
				if (i != j) {
					double first = (1-Runner.EVAP_FACTOR) * pheremones[i][j];
					double second = 0;

					//if path (i, j) is in best tour, increase its pheromone level
					if (ACS.best_tour.get_cities_visited().get(i_index+1) == j) {
						second += Runner.EVAP_FACTOR * (1/ACS.best_tour.get_length());
					}

					pheremones[i][j] = first + second;
				}
			}
		}
	}

	//local pheromone update after ant completes its tour: pheromone wears away on used legs
	public static void local_pheromone_update_ACS(Tour tour) {
		for (int i = 0; i < tour.get_size() - 1; i++) {
			int current = tour.get_cities_visited().get(i);
			int next = tour.get_cities_visited().get(i+1);
			pheremones[current][next] = (1-Runner.WEARING_AWAY) * pheremones[current][next] + Runner.WEARING_AWAY * Runner.INITIAL_PHER;
		}
	}

	public static Double get_distance(int city1, int city2){
		return city_distances[city1][city2];
	}

	//CLEANUP: do we ever use this method?
	public static void adjust_pheremone(int city1, int city2, Double new_value){
		pheremones[city1][city2] = new_value;
	}

	public static Double get_pheremone(int city1, int city2){
		return pheremones[city1][city2];
	}

	//CLEANUP: do we ever use this method?
	public Double calculate_distance_of_path(ArrayList<Integer> path) {
		Double total_distance = 0.0;
		for (int i = 0; i<path.size()-1;i++) {
			int current_city = path.get(i);
			int next_city = path.get(i+1);
			total_distance += get_distance(current_city, next_city);
		}
		return total_distance;
	}
}