/*This class has two 2d arays, city_distances and pheremones. */
import java.lang.Math;
import java.util.ArrayList;

import Ant.Tour;
import javafx.util.Pair;

public class Paths {

	/* Should we store best_tour in Paths or in ACS or both? If we store it here, we need
	   to update it here every time we find a new best_tour in ACS.
	*/

	public static Double[][] city_distances;
	public static Double[][] pheremones;
	public static Pair<Double, ArrayList<Integer>> best_tour;

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

	public void update_pheremones(ArrayList<Pair<Double, ArrayList<Integer>>> tour_list) {
		
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
		if (Runner.COLONY_TYPE.equals("ACS")) {
			//Global/Offline
			for (int i = 0; i < Runner.problem_reader.city_coords.size(); i++) {
				for (int j = 0; j < Runner.problem_reader.city_coords.size(); j++) {
					if (i != j) { //can't have pheremones leading from a city to itself
						//double first = (1-Runner.WEARING_AWAY) * this.pheremones[i][j]; //(1-wearing.away) * τ(r,s)
						double first = (1-Runner.EVAP_FACTOR) * pheremones[i][j];
						double second = 0;
						int i_index = best_tour.getValue().indexOf(i); //where is city i on the best tour
						if (best_tour.getValue().get(i_index+1) == j) { 	/*Does city j come after city i on the best tour?
							Note: there is no risk of an out of bounds exception because the last city is also the first city,
							method indexOf(Object o) returns the first occurence.*/
							second += Runner.EVAP_FACTOR * (1/best_tour.getKey()); //wearing.away * Δτ(r,s)
						} else {
							second = 0;
						}
						pheremones[i][j] = first + second;
					}
				}
			}

			//Local MAYBE THIS ISSS Supposed to happen after each ant completes its tour? Not sure TBH.
			for (int i = 0; i < tour_list.get(0).getValue().size(); i++) {
				ArrayList<Integer> current_path = tour_list.get(i).getValue();
				for (int j = 0; j < current_path.size()-1; j++) {
					Integer current_city = current_path.get(j);
					double first = (1-Runner.EVAP_FACTOR) * pheremones[j][j+1];
					double second = 0;
					int j_index = best_tour.getValue().indexOf(current_city); //index of the current city in the best path.
					if (best_tour.getValue().get(j_index + 1) == current_path.get(j+1)) {
						second += Runner.EVAP_FACTOR * (1/best_tour.getKey());
					}
					else {
						second = 0;
					}
					pheremones[j][j+1] = first+second;

				}

			}
		} 
		else { //ie Colony type is EACY

		}

	}

	public static void offline_pheromone_update_ACS() {
		for (int i = 0; i < Runner.problem_reader.city_coords.size(); i++) {
			for (int j = 0; j < Runner.problem_reader.city_coords.size(); j++) {
				if (i != j) { //can't have pheremones leading from a city to itself
					//double first = (1-Runner.WEARING_AWAY) * this.pheremones[i][j]; //(1-wearing.away) * τ(r,s)
					double first = (1-Runner.EVAP_FACTOR) * pheremones[i][j];
					double second = 0;
					int i_index = best_tour.getValue().indexOf(i); //where is city i on the best tour
					if (best_tour.getValue().get(i_index+1) == j) { 	/*Does city j come after city i on the best tour?
						Note: there is no risk of an out of bounds exception because the last city is also the first city,
						method indexOf(Object o) returns the first occurence.*/
						second += Runner.EVAP_FACTOR * (1/best_tour.getKey()); //wearing.away * Δτ(r,s)
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
		int start_city = tour.get_cities_visited().get(0);
		for (int i = 0; i < tour.get_length() - 1; i++) {
			pheremones[i][i+1] = (1-Runner.WEARING_AWAY) * pheremones[i][i+1] + Runner.WEARING_AWAY * Runner.INITIAL_PHER;
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

	public static void main(String[] args){
		String file = args[0];
        Reader test = new Reader(file);
        Paths path_test = new Paths(test.get_city_coords());
	}

}