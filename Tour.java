import java.util.ArrayList;

public  class Tour{
        public Double length;
        public ArrayList<Integer> cities_visited = new ArrayList<Integer>();

        public Tour(Double length, ArrayList<Integer> cities_visited) {
            this.length = length;
            this.cities_visited = cities_visited;
        }

        public Tour() {
            length = 0.0;
            ArrayList<Integer> cities = new ArrayList<Integer>();
            cities_visited = cities;
        }

        public Double get_length() {
            return length;
        }

        public ArrayList<Integer> get_cities_visited() {
            return cities_visited;
        }

        public int get_size() {
            return cities_visited.size();
        }
    }
