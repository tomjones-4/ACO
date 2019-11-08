/* The ant class handles the many functions and variables of an ant */
import java.util.BitSet;
import java.util.*;
import java.io.*;

public class Ant {

	//represent unvisited cities as a bitset. if a city is unvisited, it is a zero (all cities start as zeros)
	//then once it is visited it becomes a one.
	public ArrayList<Integer> path = new ArrayList<Integer>();
	private int current_city;
	private Random rand = new Random();


	public Ant() {
		current_city = rand.nextInt(Reader.num_cities);
		path.add(current_city);
	}

	private void choose(double[] p_vector) {
		p_vector = this.create_p_vector();
		double chance = this.rand.nextDouble();
		double summer = p_vector[0];
		int index = 0;
		while (chance > summer) {
			index++;
			summer += p_vector[index];
		}
		current_city = index;
		path.add(current_city);

	}

	private double[] create_p_vector() {
		double [] numerators = new double[Reader.num_cities - 1];
		double denominator = 0;
		for (int i=0; i < Reader.num_cities; i++) {
			if (!this.path.contains(i)) {
				double distance = Paths.city_distances[this.current_city][i];
				double heuristic_info = 1/(distance);
				double pheremone_level = Runner.PATHS.get_pheremone(this.current_city, i);
				double unraised_numerator = heuristic_info * pheremone_level;
				denominator+=unraised_numerator;
				double raised_heur = Math.pow(heuristic_info, Runner.HEUR_POWER);
				double raised_pher = Math.pow(pheremone_level, Runner.PHER_POWER);
				numerators[i] = raised_heur * raised_pher;
			}
		}
		double [] p_vector = new double[Reader.num_cities-1];
		for (int i=0; i < Reader.num_cities; i++) {
			if (!this.path.contains(i)) {
				p_vector[i] = numerators[i] / denominator;
			}
		}
		return p_vector;
	}
}