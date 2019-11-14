/*This class has two 2d arays, city_distances and pheremones. */
import java.lang.Math;
import java.util.ArrayList;

//import javafx.util.Pair;

public class Paths {

	public static Double[][] city_distances;
	public static Double[][] pheremones;

	public Paths(ArrayList<Double[]> city_coords){
		city_distances = new Double[city_coords.size()][city_coords.size()];
		pheremones = new Double[city_coords.size()][city_coords.size()];

		//populate the city_distances with eucleadian distances between city_coords
		for (int i=0; i < city_coords.size(); i++){
			for (int j=0; j < city_coords.size(); j ++){
				Double y2 = city_coords.get(i)[1];
				Double y1 = city_coords.get(j)[1];
				Double x2 = city_coords.get(i)[0];
				Double x1 = city_coords.get(j)[0];
				Double distance = Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
				city_distances[i][j] = distance; 
				//For debugging
				//System.out.println(distance);
			}
		}
		//populate pheremones with all ones initially
		for (int i=0; i < city_coords.size(); i++){
			for (int j=0; j < city_coords.size(); j ++){
				pheremones[i][j] = 1.0;
			}
		}


	}

	/*public void update_pheremones(ArrayList<Pair<Double, ArrayList<Integer>>> tour_list) {
		
		/* In ACS, three seperate mechanisms update the pheremones between iterations.
		1)The greatest ant of all times adds pheremones to its paths.
		2)There is a GLOBAL wearing away of pheremones on ALL paths.
		3)There is a LOCAL wearing away. Ants which do not make the best tour decrease pheremone on their paths.
		These three mechanisms are captured in TWO algorithms.

		Global updating (1 and 2):
			*(r,s) is every edge in the whole map
			τ(r,s) <-- (1-wearing.away) * τ(r,s) + wearing.away * Δτ(r,s)
		Local updating (1 and 3): 
			*this time (r,s) is just any path an ant actually goes over in the iteration
			τ(r,s) <-- (1-evap.factor) * τ(r,s) + evap.factor * Δτ(r,s)

		Δτ(r,s) = { (1/length of global best tour), 	if (r,s) is on global best tour
				  {  0, 								otherwise
		*/

		/* Tom's comments
		   I'm fairly certain there are only two instances to update the pheromones.
		   There is a local pheromone update and an "offline" pheromone update.
		   The local update is each ant reduces the amount of pheromone on the paths in
		   its tour. The "offline" update is the best so far ant adds pheromone to all the
		   legs in its best so far tour, while pheromone diminishes from paths not in best tour.
		   The local update occurs after each ant's tour.
		   The "offline" update occurs at the end of each iteration.
		   For reference: http://www.scholarpedia.org/article/Ant_colony_optimization#Ant_colony_system
		*/
		


		/*}

	}*/

	public static void offline_pheromone_update_ACS() {
		for (int i = 0; i < Runner.problem_reader.city_coords.size(); i++) {
			for (int j = 0; j < Runner.problem_reader.city_coords.size(); j++) {
				if (i != j) { //can't have pheremones leading from a city to itself
					//double first = (1-Runner.WEARING_AWAY) * this.pheremones[i][j]; //(1-wearing.away) * τ(r,s)
					double first = (1-Runner.EVAP_FACTOR) * pheremones[i][j];
					double second = 0;
					int i_index = ACS.best_tour.get_cities_visited().indexOf(i); //where is city i on the best tour
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
		System.out.println("Number of cities in tour: " + tour.get_cities_visited().size());
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

	//debugging
	/*public static void main(String[] args){
		String file = args[0];
        Reader test = new Reader(file);
        Paths path_test = new Paths(test.get_city_coords());
	}*/

}