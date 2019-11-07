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
				distance = sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
			}
		}

	}

}