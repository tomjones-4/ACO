/*This class has two 2d arays, city_distances and pheremones. */
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;

//import javafx.util.Pair;

public class Paths {

	public static Double[][] city_distances;
	public static Double[][] pheremones;

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
		//System.out.println(Arrays.deepToString(city_distances).replace("], ", "]\n"));
		//System.exit(0);



	}

	public void generate_init_pheremones(){
		for (int i=0; i < city_distances.length; i++){
			for (int j=0; j < city_distances.length; j ++){
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
			}
		}
	}

	public static void offline_pheromone_update_ACS() {
		for (int i = 0; i < Runner.problem_reader.city_coords.size(); i++) {
			int i_index = ACS.best_tour.get_cities_visited().indexOf(i); //where is city i on the best tour
			for (int j = 0; j < Runner.problem_reader.city_coords.size(); j++) {
				if (i != j) { //can't have pheremones leading from a city to itself
					double first = (1-Runner.EVAP_FACTOR) * pheremones[i][j];
					double second = 0;
					if (ACS.best_tour.get_cities_visited().get(i_index+1) == j) { 	/*Does city j come after city i on the best tour?
						Note: there is no risk of an out of bounds exception because the last city is also the first city,
						method indexOf(Object o) returns the first occurence.*/
						second += Runner.EVAP_FACTOR * (1/ACS.best_tour.get_length()); //wearing.away * Δτ(r,s)
					}
					else {
						second = 0;
					}
					pheremones[i][j] = first + second;
				}
			}
		}
	}

	public static void local_pheromone_update_ACS(Tour tour) {
		//System.out.println("Number of cities in tour: " + tour.get_cities_visited().size()); //debugging
		if (tour.get_cities_visited().size() != city_distances.length + 1) {
			System.out.println("Num cities in tour: " + tour.get_cities_visited().size() + ". Number expected: " + (city_distances.length + 1));
			System.exit(0);
		}
		//debugging
		for (int i = 0; i < tour.get_cities_visited().size() - 1; i++) {
			int current = tour.get_cities_visited().get(i);
			int next = tour.get_cities_visited().get(i+1);

			pheremones[current][next] = (1-Runner.WEARING_AWAY) * pheremones[current][next] + Runner.WEARING_AWAY * Runner.INITIAL_PHER;

		}
	}

	public static Double get_distance(int city1, int city2){
		return city_distances[city1][city2];
	}

	public static void adjust_pheremone(int city1, int city2, Double new_value){
		pheremones[city1][city2] = new_value;
	}

	public static Double get_pheremone(int city1, int city2){
		return pheremones[city1][city2];
	}

	public Double calculate_distance_of_path(ArrayList<Integer> path) {
		Double total_distance = 0.0;
		for (int i = 0; i<path.size()-1;i++) {
			int current_city = path.get(i);
			int next_city = path.get(i+1);
			total_distance+=this.get_distance(current_city, next_city);
		}
		return total_distance;
	}

	//debugging
	/*public static void main(String[] args){
		String file = args[0];
        Reader test = new Reader(file);
        Paths path_test = new Paths(test.get_city_coords());
	}*/

}