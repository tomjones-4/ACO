/* This class reads in information from the test problems and stores it in an arraylist of Double arrays.
   Arraylist is of length n, where n = num cities, and each list is length 2.
   city_coords.get(i)[0] is the x-coord of city i and city_coords.get(i)[1] is the y-coord of city i.
*/

import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.File;

public class Reader {

    public static int num_cities;
    public static ArrayList<Double[]> city_coords = new ArrayList<Double[]>();


	public Reader(String file_path){
		File file = new File(file_path);
		//read the file in
		try {	
			Scanner file_scan = new Scanner(file);
			while (file_scan.hasNextLine()){
                String line = file_scan.nextLine();
                //this line contains number of cities in problem
                if (line.substring(0, 3).equals("DIM")) {
                    int index_of_colon = line.indexOf(":");
                    num_cities = Integer.parseInt(line.substring(index_of_colon + 2)); //gets number of cities as int
                    //System.out.println("Num cities in Reader.java: " + num_cities); //debugging
                }
                else if (line.substring(0, 4).equals("NODE")) {
                    //rest of lines are city coords
                    String string_coords = file_scan.nextLine();
                    //while line is still coords and not end of file (EOF)
                    while (!string_coords.equals("EOF")) {
                        //each line has 3 numbers separated by spaces
                        String[] split_coords = string_coords.trim().split("\\s+");
                        //first number is city number, subtract 1 to properly index in city_coords array
                        int city_num = Integer.parseInt(split_coords[0]) - 1;
                        //second number is x_coord, parseDouble since sometimes scientific notation is used
                        Double x_coord = Double.parseDouble((split_coords[1]));
                        //third number is y_coord
                        Double y_coord = Double.parseDouble((split_coords[2]));
                        Double[] coords = {x_coord, y_coord};
                        city_coords.add(coords);
                        string_coords = file_scan.nextLine();
                    }
                }
            }
            // debugging
            
            // for (int i = 1; i <= num_cities; i++) { //this loop is for debugging
            //     System.out.println("City number " + i + ": " + city_coords.get(i-1)[0] + ", " + city_coords.get(i-1)[1]);
            // }
            
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
        }
    }
    
    //main method for testing Reader functionality and debugging
    
    public static void main(String[] args) {
        String file = args[0];
        Reader test = new Reader(file);
    }
    
    
    public static int get_num_cities() {
        return city_coords.size();
    }

    public static ArrayList<Double[]> get_city_coords() {
        return city_coords;
    }
}