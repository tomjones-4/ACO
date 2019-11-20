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


	/*The following three methods are pheremone update methods for EAS*/
	//Apply this after each ant completes a tour.
	public static void local_pheremone_update_EAS(Tour tour) {
		for (int i = 0; i < tour.get_cities_visited().size() - 1; i++) {
			int current = tour.get_cities_visited().get(i);
			int next = tour.get_cities_visited().get(i+1);
			pheremones[current][next] += (1/tour.get_length());
		}
	}
	//Apply this once (feeding in the best tour) after each iteration.
	public static void best_tour_pheremone_update_EAS(Tour tour) {
		for (int i = 0; i < tour.get_cities_visited().size() - 1; i++) {
			int current = tour.get_cities_visited().get(i);
			int next = tour.get_cities_visited().get(i+1);
			pheremones[current][next] += Runner.ELITISM * (1/tour.get_length());
		}
	}
	//Apply this at the end of each iteration
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
					//double second = 0;

					//if path (i, j) is in best tour, increase its pheromone level
					if (ACS.best_tour.get_cities_visited().get(i_index+1) == j) {
						second += Runner.EVAP_FACTOR * (1/ACS.best_tour.get_length());
					}

					//else, pheromone levels decrease for path
					else {
						second = 0;
					}
					pheremones[i][j] = first + second;
				}
			}
		}
	}

	////local pheromone update after ant completes its tour: pheromone wears away on used legs
	public static void local_pheromone_update_ACS(Tour tour) {
		for (int i = 0; i < tour.get_cities_visited().size() - 1; i++) {
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