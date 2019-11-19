/* The ant class handles the many functions and variables of an ant */
import java.util.*;
import java.io.*;
//import javafx.util.Pair;

public class Ant {
	public ArrayList<Integer> path = new ArrayList<Integer>();
	private int current_city;
	private Random rand = new Random();

	public Ant() {
		//System.out.println("Number of cities in Ant.java: " + Reader.num_cities); //debugging
		current_city = rand.nextInt(Reader.num_cities);
		path.add(current_city);
    }
    
    //same method as above commented out one, just uses a class called Tour within Ant class to help clarify
    public Tour tour() {
		for(int i=0; i < Reader.num_cities - 1; i++) { 
			this.choose();
		}
		/*System.out.println("Ant done choosing cities, about to return to start. Cities missing:"); //debugging
		for (int i = 0; i < Reader.num_cities; i++) { //debugging
			if (!path.contains(i)) {
				System.out.println(i);
			}
		}*/  //debugging
		this.return_to_start();
		
		//for debugguing
		//System.out.println("Path after tour has been created: " + path); //debugging

		double distance = 0;
		for(int i=0; i < path.size()-2; i++) {
			int current = path.get(i);
			int next = path.get(i + 1);
			distance += Runner.PATHS.city_distances[current][next];
        }
        
        Tour tour = new Tour(distance, path);
        return tour;
	}

	private void return_to_start() {
		int start = path.get(0);
		path.add(start);
		current_city = start;
	}

	public void reset_path() {
		path = new ArrayList<Integer>();
		current_city = rand.nextInt(Reader.num_cities);
		path.add(current_city);
	}

    private void choose() {
        double random = this.rand.nextDouble();
        if (random <= Runner.BEST_LEG) {
        	//System.out.println("greedy_choose");
            greedy_choose();
        }
        else {
        	//System.out.println("prob_choose");
            prob_choose();
        }
    }

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
		
		/* I think this is where cities are getting left off of tours. In greedy_choose a city
		   is always added to tour. In this, a city is only added if p_choice[i] >= rand, so
		   it's possible that this isn't always happening, in which case a city just isn't added.
		*/
		//System.out.println("P_total: " + p_total);
		double randy = p_total * this.rand.nextDouble();

		//debugging
		//System.out.println("p_total: " + p_total);

		//debugging statements below
		//System.out.println("p_total: " + p_total);
		//System.out.println("p_choice[num_cities - 1]: " + p_choice[p_vector.length - 1]);
		/*These statements indicate the last entries in p_choice are equal to p_total, as desired,
		  so I'm not sure where the cities are missing.
		  Some cities are missing with really high frequency: 45, 11, 31, 10, 50 are some of them
		*/
		
		for (int i = 0; i < p_choice.length; i++){
			if (p_choice[i] >= randy){
				current_city = i;
				path.add(current_city);
				break;
			}
			//debugging statement below
			if (i == p_choice.length - 1) {
				System.out.println("\n\n\nFailed to choose city in prob_choose().");
				System.out.println("p_choice[p_choice.length - 1]: " + p_choice[p_choice.length - 1]);
				System.out.println("p_choice[p_choice.length - 2]: " + p_choice[p_choice.length - 2]);
				System.out.println("p_total: " + p_total);
				System.out.println("Length of path at this point: " + path.size() + "\n\n\n");
			}
		}

		//Is it possible pheromone levels get negative sometimes and that's what is causing error?


    }
	
	//Greedy choosing algorithm which chooses city that chooses city with max τ * η^β
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
		Boolean no_cities_left = true; //debugging
        //I think this should be i < Reader.num_cities because we need to check last city, too
		for (int i=0; i < Reader.num_cities; i++) { 
			numerators[i] = 0.0; //debugging
			//if city isn't in tour already, assign probability based on heuristic info and pheromones
			if (!this.path.contains(i)) { 
				no_cities_left = false; //debugging
				double distance = Paths.city_distances[this.current_city][i];
				double heuristic_info = 1/(distance);
				double pheremone_level = Runner.PATHS.get_pheremone(this.current_city, i);
				double unraised_numerator = heuristic_info * pheremone_level;
				double raised_heur = Math.pow(heuristic_info, Runner.HEUR_POWER);
				double raised_pher = Math.pow(pheremone_level, Runner.PHER_POWER);
                numerators[i] = raised_heur * raised_pher;
                denominator += numerators[i];
			} else {
				numerators[i] = 0;
			}
		}
		if (no_cities_left) { //debugging
			System.out.println("\n\n\nNo cities left\n\n\n");
			
		}

		ArrayList<Double> print_vector = new ArrayList<Double>();
        double [] p_vector = new double[Reader.num_cities];
        //This is the second time we do a for loop and check to see if each city i is in the path already
        //I think it will be faster to check whether numerators[i] == 0. If it does, set p_vector[i] = 0.
        //Otherwise, p_vector[i] = numerators[i] / denominator
		/*for (int i=0; i < Reader.num_cities; i++) {
			if (!this.path.contains(i)) {
				p_vector[i] = numerators[i] / denominator;
			}
			//adding this statement to see if it fixes the missing cities problem
			else {
				p_vector[i] = 0;
			}
			//statement done^
		}*/

		//We already set numerators[i] = 0 if city i is in path already, so we can just do the following:
		for (int i = 0; i < Reader.num_cities; i++) {
			p_vector[i] = numerators[i] / denominator;
		}
		
		for (int i=0; i<Reader.num_cities; i++) {
			print_vector.add(p_vector[i]);
		}
		//System.out.println("Print vector: " + print_vector);
		return p_vector;
	}

}