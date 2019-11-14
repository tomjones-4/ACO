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
        //maybe should be Reader.num_cities-1. When ant is constructed, random city is added to path.
        //that means there are n-1 other cities to choose.
		for(int i=0; i < Reader.num_cities - 1; i++) { 
			this.choose();
		}
		System.out.println("Ant done choosing cities, about to return to start. Cities missing:"); //debugging
		for (int i = 0; i < Reader.num_cities; i++) { //debugging
			if (!path.contains(i)) {
				System.out.println(i);
			}
		}  //debugging
		this.return_to_start();
		
		//for debugguing
		System.out.println("Path after tour has been created: " + path); //debugging

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
		
		/* I think this is where cities are getting left off of tours. In greedy_choose a city
		   is always added to tour. In this, a city is only added if p_choice[i] >= rand, so
		   it's possible that this isn't always happening, in which case a city just isn't added.
		*/

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
			}
		}
        double [] p_vector = new double[Reader.num_cities];
        //This is the second time we do a for loop and check to see if each city i is in the path already
        //I think it will be faster to check whether numerators[i] == 0. If it does, set p_vector[i] = 0.
        //Otherwise, p_vector[i] = numerators[i] / denominator
		for (int i=0; i < Reader.num_cities; i++) {
			if (!this.path.contains(i)) {
				p_vector[i] = numerators[i] / denominator;
			}
			//adding this statement to see if it fixes the missing cities problem
			else {
				p_vector[i] = 0;
			}
			//statement done^
		}
		return p_vector;
	}

}