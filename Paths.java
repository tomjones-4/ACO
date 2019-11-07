/*This class has two 2d arays, city_distances and pheremones. */

public class Paths {

	public Double[][] city_distances;
	public Double[][] pheremones;

	public Paths(Double[][] city_cords){
		city_distances = new Double[city_cords.length][city_cords.length];
		pheremones = new Double[city_cords.length][city_cords.length];

		//Populate the city_distances with eucleadian distances between city_cords
		for (int i=0; i < city_cords.length; i++){
			for (int j=0; j < city_cords.length; j ++){
				Double y2 = city_cords.get(i).get(1);
				Double y1 = city_cords.get(j).get(1);
				Double x2 = city_cords.get(i).get(0);
				Double x1 = city_cords.get(j).get(0);
				Double distance = sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
				city_distances.get(i).set(j, distance); 
			}
		}

	}

	public Double get_distance(int city1, int city2){
		return this.city_distances.get(city1).get(city2);
	}

	public void update_pheremone(int city1, int city2, Double new_value){
		this.pheremones.get(city1).set(city2, new_value);
	}

	public Double get_pheremone(int city1, int city2){
		return this.pheremones.get(city1).get(city2);
	}

}