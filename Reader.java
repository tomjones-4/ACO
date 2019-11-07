/* Copied Reader.java over from GA/PBIL project because I figured this
   would be a good starting point.
*/

/* Important information from test file are the coordinates of the cities.
   I'm thinking the best way to store these is to have an array of lists callied cities.
   Array is of length n, where n = num cities, and each list is length 2.
   cities[i][0] is the x-coord of city i and cities[i][1] is the y-coord of city i.
   The coordinates start after a line that says NODE_COORD_SECTION
   The information stored in each line is the number of the city, then x, then y.
*/

//import java.util.Scanner;
import java.util.*;
import java.io.*;

public class Reader {

	private StringBuilder file_string = new StringBuilder();
	private String output_string = new String();
	private int number_of_variables;
    private int number_of_clauses;
    private int num_cities;
    private int[][] city_coords;
	private List<Integer[]> clause_list = new ArrayList<Integer[]>();

	//Get information from the problem file like number of variables and number of clauses
	public Reader(String file_path){
		File file = new File(file_path);
		//Read the file in as a string
		try {	
			Scanner file_scan = new Scanner(file);
			while (file_scan.hasNextLine()){
                String line = file_scan.nextLine();
                if (line.substring(0, 3).equals("DIM")) { //this line contains number of cities in problem
                    //int len_line = line.length(); //debugging
                    //System.out.println(len_line); //debugging
                    num_cities = Integer.parseInt(line.substring(12)); //gets number of cities as int
                    System.out.println(num_cities); //debugging
                    city_coords = new int[num_cities][2];
                }
                else if (line.substring(0, 4).equals("NODE")) { //rest of lines are city coords
                    String string_coords = file_scan.nextLine();
                    while (!string_coords.equals("EOF")) {
                        String[] split_coords = string_coords.split(" ");
                        int city_num = Integer.parseInt(split_coords[0]) - 1;
                        //int x_coord = new BigDecimal(split_coords[1]).intValue();
                        //int y_coord = new BigDecimal(split_coords[2]).intValue();
                        int x_coord = (int) Double.parseDouble((split_coords[1]));
                        int y_coord = (int) Double.parseDouble((split_coords[2]));
                        city_coords[city_num][0] = x_coord;
                        city_coords[city_num][1] = y_coord;
                        string_coords = file_scan.nextLine();
                    }
                }
                //System.out.println(line);
				//else {
				//	this.file_string.append(line + "\n");
				//}
            }
            for (int i = 1; i <= num_cities; i++) {
                System.out.println("City number: " + i + ": " + city_coords[i-1][0] + ", " + city_coords[i-1][1]);
            }
			//this.output_string = this.file_string.toString();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
        }
        /*
		//Extract data from the file
		String[] source_ary = this.output_string.split("\n");
		String first_line = source_ary[0];

		//Splits based on white spaces, even if there are multiple white spaces
		String[] first_line_ary = first_line.split("\\s+");

		this.number_of_variables = Integer.valueOf(first_line_ary[2]);
		this.number_of_clauses = Integer.valueOf(first_line_ary[3]);
	
		//Represent the clauses as a list of integer arrays.
		for(int i = 1; i <= this.number_of_clauses; i++) {
			String clause_string = source_ary[i];
			String [] clause_string_ary = clause_string.split(" ");
			Integer[] single_clause_ary = new Integer[clause_string_ary.length - 1];
			for(int j = 0; j<=clause_string_ary.length-2; j++) {
				String clause_element = clause_string_ary[j];
				int numerical_clause_element = Integer.valueOf(clause_element);
				single_clause_ary[j] = numerical_clause_element;
			}
			this.clause_list.add(single_clause_ary);
        } 
        */
    }
    
    //main method for testing Reader functionality
    public static void main(String[] args) {
        String file = args[0];
        Reader test = new Reader(file);
    }
	
	public String getFileString() {
		return this.output_string;
	}

	public int getNumberOfVariables() {
		return this.number_of_variables;
	}

	public int getNumberOfClauses() {
		return this.number_of_clauses;
	}

	public List getClauseList() {
		return this.clause_list;
	}
}