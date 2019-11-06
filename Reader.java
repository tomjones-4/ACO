/* Copied Runner.java over from GA/PBIL project because I figured this
   would be a good starting point.
*/

//import java.util.Scanner;
import java.util.*;
import java.io.*;

public class Reader {

	private StringBuilder file_string = new StringBuilder();
	private String output_string = new String();
	private int number_of_variables;
	private int number_of_clauses;
	private List<Integer[]> clause_list = new ArrayList<Integer[]>();

	//Get information from the problem file like number of variables and number of clauses
	public Reader(String file_path){
		File file = new File(file_path);
		//Read the file in as a string
		try {	
			Scanner file_scan = new Scanner(file);
			while (file_scan.hasNextLine()){
				String line = file_scan.nextLine();
				if (line.charAt(0) == ('c') || line.charAt(0) == ('C'))  {
					//do nothing so comments in cnf files are not included
				} 
				else {
					this.file_string.append(line + "\n");
				}
			}
			this.output_string = this.file_string.toString();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

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