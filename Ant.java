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

	/* Returns a pair with two values; a double with the total distance of the path and an array list of integers
	which represents the taken path so far. */
	public Pair <Double, ArrayList<Integer>> tour() {
		for(int i=0; i < Reader.num_cities -1; i++) {
			this.choose();
		}
		this.return_to_start();
		
		//for debugguing
		System.out.println(this.path);

		double distance = 0;
		// for(int i=0; i < path.size()-1; i++) {
		// 	distance += Runner.PATHS.city_distances[i][i+1];
		// }
		Pair<Double, ArrayList<Integer>> ans = new Pair<Double, ArrayList<Integer>>(distance, this.path);
		return ans;
	}

	private void return_to_start() {
		int start = path.get(0);
		path.add(start);
		current_city = start;
	}

	private void choose() {
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
			//System.out.println(p_choice[i]);
		}


	}

	private double[] create_p_vector() {
		double [] numerators = new double[Reader.num_cities - 1];
		double denominator = 0;
		for (int i=0; i < Reader.num_cities - 1; i++) {
			if (!this.path.contains(i)) {
				double distance = Paths.city_distances[this.current_city][i];
				
				double heuristic_info = 1/(distance);
			
				double pheremone_level = Runner.PATHS.get_pheremone(this.current_city, i);
				
				double unraised_numerator = heuristic_info * pheremone_level;
				
				denominator += unraised_numerator;
				
				double raised_heur = Math.pow(heuristic_info, Runner.HEUR_POWER);
				
				double raised_pher = Math.pow(pheremone_level, Runner.PHER_POWER);
				
				numerators[i] = raised_heur * raised_pher;
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
		for (int i=0; i < Reader.num_cities - 1; i++) {
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