/*This class has two 2d arays, city_distances and pheremones. */
import java.lang.Math;
import java.util.ArrayList;

public class Paths {

	public static Double[][] city_distances;
	public Double[][] pheremones;

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

	}

	public Double get_distance(int city1, int city2){
		return city_distances[city1][city2];
	}

	public void update_pheremone(int city1, int city2, Double new_value){
		pheremones[city1][city2] = new_value;
	}

	public Double get_pheremone(int city1, int city2){
		return pheremones[city1][city2];
	}

	public static void main(String[] args){
		String file = args[0];
        Reader test = new Reader(file);
        Paths path_test = new Paths(test.get_city_coords());
	}

}