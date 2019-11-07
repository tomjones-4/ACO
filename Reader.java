/* This class reads in information from the test problems and stores it in a 2D in array.
   Array is of length n, where n = num cities, and each list is length 2.
   city_coords[i][0] is the x-coord of city i and city_coords[i][1] is the y-coord of city i.
*/

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;

public class Reader {

    public int num_cities;
    public Double[][] city_coords;

	public Reader(String file_path){
		File file = new File(file_path);
		//Read the file in as a string
		try {	
			Scanner file_scan = new Scanner(file);
			while (file_scan.hasNextLine()){
                String line = file_scan.nextLine();
                if (line.substring(0, 3).equals("DIM")) { //this line contains number of cities in problem
                    num_cities = Integer.parseInt(line.substring(12)); //gets number of cities as int
                    city_coords = new int[num_cities][2];
                }
                else if (line.substring(0, 4).equals("NODE")) {
                    //rest of lines are city coords
                    String string_coords = file_scan.nextLine();
                    //while line is still coords and not end of file (EOF)
                    while (!string_coords.equals("EOF")) {
                        //each line has 3 numbers separated by spaces
                        String[] split_coords = string_coords.split(" ");
                        //first number is city number, subtract 1 to properly index in city_coords array
                        int city_num = Integer.parseInt(split_coords[0]) - 1;
                        //second number is x_coord, parseDouble since sometimes scientific notation is used
                        Double x_coord = Double.parseDouble((split_coords[1]));
                        //third number is y_coord
                        Double y_coord = Double.parseDouble((split_coords[2]));
                        city_coords[city_num][0] = x_coord;
                        city_coords[city_num][1] = y_coord;
                        string_coords = file_scan.nextLine();
                    }
                }
            }
            // debugging
            /*
            for (int i = 1; i <= num_cities; i++) { //this loop is for debugging
                System.out.println("City number: " + i + ": " + city_coords[i-1][0] + ", " + city_coords[i-1][1]);
            }
            */
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
        }
    }
    
    //main method for testing Reader functionality
    /*
    public static void main(String[] args) {
        String file = args[0];
        Reader test = new Reader(file);
    }
    */
    
    public int get_num_cities() {
        return num_cities;
    }

    public int[][] get_city_coords() {
        return city_coords;
    }
}