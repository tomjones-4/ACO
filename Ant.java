/* The ant class handles the many functions and variables of an ant */
import java.util.BitSet;
import java.util.*;
import java.io.*;

public class Ant {

	//represent unvisited cities as a bitset. if a city is unvisited, it is a zero (all cities start as zeros)
	//then once it is visited it becomes a one.
	private BitSet univisited_cities;
	private int starting_city;

	public Ant() {
		univisited_cities = new BitSet(Reader.num_cities - 1);
	}

	public static decide() {

	}
}