/* The ant class handles the many functions and variables of an ant */
import java.util.*;
import java.io.*;

public class Ant {
	public ArrayList<Integer> path = new ArrayList<Integer>();
	private int current_city;
	private Random rand = new Random();

	public Ant() {
		current_city = rand.nextInt(Reader.num_cities);
		path.add(current_city);
    }
	
	//ant does tour, visiting each city once and then returns to start city
    public Tour tour() {
		for(int i=0; i < Reader.num_cities - 1; i++) { 
			this.choose();
		}
		this.return_to_start();

		double distance = 0;
		for(int i=0; i < path.size()-1; i++) {
			int current = path.get(i);
			int next = path.get(i + 1);
			distance += Paths.get_distance(current, next);
        }
        
        Tour tour = new Tour(distance, path);
        return tour;
	}

	//ant returns to start city after all cities have been visited
	private void return_to_start() {
		int start = path.get(0);
		path.add(start);
		current_city = start;
	}

	//resets ant's path to empty list, used to give ant fresh path going into new iteration in ACS
	public void reset_path() {
		path = new ArrayList<Integer>();
		current_city = rand.nextInt(Reader.num_cities);
		path.add(current_city);
	}

    private void choose() {
        double random = this.rand.nextDouble();
        if (random <= Runner.BEST_LEG) {
            greedy_choose();
        }
        else {
            prob_choose();
        }
    }

	//probabilistic choosing method that chooses based on pheromone levels and heuristic info
	private void prob_choose() {
		double [] p_vector = this.create_p_vector();
		double [] p_choice = new double[p_vector.length];
		double p_total = 0.0;
		
		/*sets indices in p_choice; the lowest index in p_choice with a value higher than
		  the randomly generated value will be the city index number that is added to path*/
		for (int i = 0; i < p_vector.length; i ++){
			p_total += p_vector[i];
			if (i == 0){
				p_choice[i] = p_vector[i];
			}
			else{
				p_choice[i] = p_vector[i] + p_choice[i-1];
			}
		}

		double randy = p_total * this.rand.nextDouble();
		
		for (int i = 0; i < p_choice.length; i++){
			if (p_choice[i] >= randy){
				current_city = i;
				path.add(current_city);
				break;
			}
		}
    }
	
	//greedy choosing algorithm which chooses city that chooses city with max τ * η^β
    private void greedy_choose() {
        int best_city = 0;
        double best_city_score = Double.MIN_VALUE;
        for (int i = 0; i < Reader.num_cities; i++) {
            if (!path.contains(i)) {
                double pheremone_level = Paths.get_pheremone(current_city, i);
                double distance = Paths.get_distance(current_city, i);
                double heuristic_info = 1/(distance);
                double raised_heur = Math.pow(heuristic_info, Runner.HEUR_POWER);
				double city_score = pheremone_level * raised_heur;
				
				//update best city if necessary
                if (city_score > best_city_score) {
                    best_city = i;
                    best_city_score = city_score;
                }
            }
        }
        current_city = best_city;
        path.add(current_city);
    }

	//creates probability vector, where p_vector[i] contains the probability that city i added to the path
	private double[] create_p_vector() {
		double [] numerators = new double[Reader.num_cities];
		double denominator = 0;
		for (int i=0; i < Reader.num_cities; i++) {

			//if city isn't in tour already, assign probability based on heuristic info and pheromones
			if (!this.path.contains(i)) { 
				double distance = Paths.get_distance(current_city, i);
				double heuristic_info = 1/(distance);
				double pheremone_level = Paths.get_pheremone(current_city, i);
				double raised_heur = Math.pow(heuristic_info, Runner.HEUR_POWER);
				double raised_pher = Math.pow(pheremone_level, Runner.PHER_POWER);
				numerators[i] = raised_heur * raised_pher;
                denominator += numerators[i];
			}

			//else, city is in tour and it's probability of being chosen is 0
			else {
				numerators[i] = 0.0;
			}
		}

		//assign probability values for each city into p_vector
		double [] p_vector = new double[Reader.num_cities];
		for (int i = 0; i < Reader.num_cities; i++) {
			p_vector[i] = numerators[i] / denominator;
		}
		return p_vector;
	}

}