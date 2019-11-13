/* The ant class handles the many functions and variables of an ant */
import java.util.*;
import java.io.*;
import javafx.util.Pair;

public class Ant {
	public ArrayList<Integer> path = new ArrayList<Integer>();
	private int current_city;
	private Random rand = new Random();

	public Ant() {
		current_city = rand.nextInt(Reader.num_cities);
		path.add(current_city);
    }
    
    public static class Tour {
        public Double length;
        public ArrayList<Integer> cities_visited = new ArrayList<Integer>();

        public Tour(Double length, ArrayList<Integer> cities_visited) {
            this.length = length;
            this.cities_visited = cities_visited;
        }

        public Tour() {
            length = 0;
            ArrayList<Integer> cities = new ArrayList<Integer>();
            cities_visited = cities;
        }

        public int get_length() {
            return length;
        }

        public ArrayList<Integer> get_cities_visited() {
            return cities_visited;
        }
    }

	/* Returns a pair with two values; a double with the total distance of the path and an array list of integers
	which represents the taken path so far. */
	public Pair <Double, ArrayList<Integer>> tour() {
		for(int i=0; i < Reader.num_cities-1; i++) {
			this.choose();
		}
		this.return_to_start();
		
		//for debugguing
		System.out.println(path);

		double distance = 0;
		for(int i=0; i < path.size()-2; i++) {
			int current = path.get(i);
			int next = path.get(i + 1);
			distance += Runner.PATHS.city_distances[current][next];		
		}
		//one possible issue - Runner.PATHS.best_tour will start at zero unless we tell it otherwise in runner (i guess).
		Pair<Double, ArrayList<Integer>> ans = new Pair<Double, ArrayList<Integer>>(distance, this.path);
		if(distance < Runner.PATHS.best_tour.getKey()) {
			Runner.PATHS.best_tour = ans;
		}
		return ans;
    }
    
    //same method as above commented out one, just uses a class called Tour within Ant class to help clarify
    /*public Tour tour() {
        //maybe should be Reader.num_cities-1. When ant is constructed, random city is added to path.
        //that means there are n-1 other cities to choose.
		for(int i=0; i < Reader.num_cities - 1; i++) { 
			this.choose();
		}
		this.return_to_start();
		
		//for debugguing
		System.out.println(path);

		double distance = 0;
		for(int i=0; i < path.size()-2; i++) {
			int current = path.get(i);
			int next = path.get(i + 1);
			distance += Runner.PATHS.city_distances[current][next];
        }
        
        Tour tour = new Tour(distance, path);
        return tour;
	}*/

	private void return_to_start() {
		int start = path.get(0);
		path.add(start);
		current_city = start;
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

    /* I think we need to change choose() method to account for Action Selection Rule in ACS
       Alternative solution: create a method called greedy_choose() which chooses best city, and leave
       choose() as is, maybe change its name to random_choose() to clarify.
    */
	private void prob_choose() {
		double [] p_vector = this.create_p_vector();
		double [] p_choice = new double[p_vector.length];
		double p_total = 0.0;
		
		for (int i = 0; i < p_vector.length; i ++){
			p_total += p_vector[i];
			if (i == 0){
				p_choice[i] = p_vector[i];
			}
			else{
				p_choice[i] = p_vector[i] + p_choice[i-1];
			}
		}
		
		double rand = p_total * this.rand.nextDouble();
		
		for (int i = 0; i < p_choice.length; i++){
			if (p_choice[i] >= rand){
				current_city = i;
				path.add(current_city);
				break;
			}
		}


    }
    
    private void greedy_choose() {
        int best_city = 0;
        double best_city_score = Double.MAX_VALUE;
        for (int i = 0; i < Reader.num_cities; i++) {
            if (!path.contains(i)) {
                double pheremone_level = Paths.get_pheremone(current_city, i);
                double distance = Paths.get_distance(current_city, i);
                double heuristic_info = 1/(distance);
                double raised_heur = Math.pow(heuristic_info, Runner.HEUR_POWER);
                double city_score = pheremone_level * raised_heur;
                if (city_score < best_city_score) {
                    best_city = i;
                    best_city_score = city_score;
                }
            }
        }
        current_city = best_city;
        path.add(current_city);
    }


	private double[] create_p_vector() {
		double [] numerators = new double[Reader.num_cities];
        double denominator = 0;
        //I think this should be i < Reader.num_cities because we need to check last city, too
		for (int i=0; i < Reader.num_cities; i++) { 
			if (!this.path.contains(i)) {
				double distance = Paths.city_distances[this.current_city][i];
				
				double heuristic_info = 1/(distance);
			
				double pheremone_level = Runner.PATHS.get_pheremone(this.current_city, i);
				
				double unraised_numerator = heuristic_info * pheremone_level;
				
				double raised_heur = Math.pow(heuristic_info, Runner.HEUR_POWER);
				
				double raised_pher = Math.pow(pheremone_level, Runner.PHER_POWER);
				
                numerators[i] = raised_heur * raised_pher;
                
                denominator += numerators[i];
				// System.out.println(distance);
				// System.out.println(heuristic_info);
				// System.out.println(pheremone_level);
				// System.out.println(unraised_numerator);
				// System.out.println("denominator");
				// System.out.println(denominator);
				// System.out.println(raised_heur);
				// System.out.println(raised_pher);
				// System.out.println(numerators[i]);
			}
		}
        double [] p_vector = new double[Reader.num_cities-1];
        //This is the second time we do a for loop and check to see if each city i is in the path already
        //I think it will be faster to check whether numerators[i] == 0. If it does, set p_vector[i] = 0.
        //Otherwise, p_vector[i] = numerators[i] / denominator
		for (int i=0; i < Reader.num_cities; i++) {
			if (!this.path.contains(i)) {
				p_vector[i] = numerators[i] / denominator;
			}
		}
		// for (int i = 0; i < p_vector.length; i++){
		// 	System.out.println(p_vector[i]);
		// }
		return p_vector;
	}

}