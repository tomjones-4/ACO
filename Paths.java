/* This class has two 2d arrays, city_distances and pheremones. It also contains the
   methods for updating pheromones on the paths between cities for both ACS and EAS.*/
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;

public class Paths {

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

	public static void global_pheromone_update_EAS() {

		//create 2d array for updates to each path between cities. values are initialized to 0.0
		Double[][] pheromone_updates = new Double[Runner.NUM_CITIES][Runner.NUM_CITIES];
		for (int i = 0; i < Runner.NUM_CITIES; i++) {
			for (int j = 0; j < Runner.NUM_CITIES; j++) {
				pheromone_updates[i][j] = 0.0;
			}
		}
		for (int i = 0; i < Runner.NUM_ANTS; i++) {
			Tour tour = EAS.iteration_tours.get(i);

			//get all legs of tour and update value in pheromone_updates
			for (int j = 0; j < tour.get_size() - 1; j++) {
				int current = tour.get_cities_visited().get(i);
				int next = tour.get_cities_visited().get(i+1);
				pheromone_updates[current][next] += (1 / tour.get_length());
				pheromone_updates[next][current] += (1 / tour.get_length());
			}
		}
		for (int i = 0; i < Runner.NUM_CITIES; i++) {
			for (int j = 0; j < Runner.NUM_CITIES; j++) {
				pheremones[i][j] = (1 - Runner.EVAP_FACTOR) * pheremones[i][j] + pheromone_updates[i][j];
			}
		}
	}

	//adds pheromones to all legs in best tour
	public static void best_tour_pheremone_update_EAS(Tour best_tour) {
		for (int i = 0; i < best_tour.get_size() - 1; i++) {
			int current = best_tour.get_cities_visited().get(i);
			int next = best_tour.get_cities_visited().get(i+1);
			pheremones[current][next] += Runner.ELITISM * (1 / best_tour.get_length());
			pheremones[next][current] += Runner.ELITISM * (1 / best_tour.get_length());
		}
	}

	//offline pheromone update: legs in tour of best ant so far get pheromone levels updated
	public static void offline_pheromone_update_ACS(Tour best_tour) {

		for (int i = 0; i < best_tour.get_size() - 1; i++) {
			int current = best_tour.get_cities_visited().get(i);
			int next = best_tour.get_cities_visited().get(i+1);
			pheremones[current][next] = (1-Runner.EVAP_FACTOR) * pheremones[current][next] + Runner.EVAP_FACTOR * (1/best_tour.get_length());
			pheremones[next][current] = (1-Runner.EVAP_FACTOR) * pheremones[next][current] + Runner.EVAP_FACTOR * (1/best_tour.get_length());
		}
	}

	//local pheromone update after ant completes its tour: pheromone wears away on used legs
	public static void local_pheromone_update_ACS(Tour tour) {
		for (int i = 0; i < tour.get_size() - 1; i++) {
			int current = tour.get_cities_visited().get(i);
			int next = tour.get_cities_visited().get(i+1);
			pheremones[current][next] = (1-Runner.WEARING_AWAY) * pheremones[current][next] + Runner.WEARING_AWAY * Runner.INITIAL_PHER;
			pheremones[next][current] = (1-Runner.WEARING_AWAY) * pheremones[next][current] + Runner.WEARING_AWAY * Runner.INITIAL_PHER;
		}
	}

	public static Double get_distance(int city1, int city2){
		return city_distances[city1][city2];
	}

	public static Double get_pheremone(int city1, int city2){
		return pheremones[city1][city2];
	}
}