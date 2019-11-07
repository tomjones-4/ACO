/* Copied Runner.java over from GA/PBIL project because I figured this
   would be a good starting point.
*/

public class Runner {
    
    public static String problem_file;
    public static Reader problem_reader;

    public static string COLONY_TYPE;
    public static int NUM_ANTS;
    public static int NUM_ITS;
    public static double PHER_POWER;
    public static double HEUR_POWER;
    public static double EVAP_FACTOR;

    public static double WEARING_AWAY;
    public static double BEST_LEG;
    public static double DISP_INTERVAL;

    public static double ELITISM;

    private static void print_help () {
        System.out.println();
        System.out.println("If running Ant Colony System:");
        System.out.println("java Runner file type num_ants num_its pher_power heur_power evap_factor wearing_away best_leg");
        System.out.println("    file            = name of the file containing the problem (string)");
        System.out.println("    colony_type            = Ant Colony System (ACS) or Elitist Ant System (EAS) (String)");
	    System.out.println("    num_ants        = number of ants (int)");
	    System.out.println("    num_its         = number of iterations (int)");
	    System.out.println("    pher_power      = weight placed on pheremones (double)");
	    System.out.println("    heur_power      = weight placed on heuristic information (double)");
        System.out.println("    evap_factor     = amount of evaporation between iterations (double)");
	    System.out.println("    wearing_away    = amount ants wear away legs they travel on (double)");
	    System.out.println("    best_leg        = probability that an ant will choose next leg from best tour (double)");
        System.out.println("    disp (optional) = display interval (int)");
        System.out.println();

        System.out.println("If running Elitist Ant System:");
        System.out.println("java Runner file type num_ants num_its pher_power heur_power evap_factor elitism");
        System.out.println("    file            = name of the file containing the problem (string)");
        System.out.println("    colony_type     = Ant Colony System (ACS) or Elitist Ant System (EAS) (String)");
        System.out.println("    num_ants        = number of ants (int)");
        System.out.println("    num_its         = number of iterations (int)");
        System.out.println("    pher_power      = weight placed on pheremones (double)");
        System.out.println("    heur_power      = weight placed on heuristic information (double)");
        System.out.println("    evap_factor     = amount of evaporation between iterations (double)");
        System.out.println("    elitism         = elitism factor");
        System.out.println("    disp (optional) = display interval (int)");
	    System.out.println();
	    System.exit(1); // prevent the program from continuing without the correct inputs
    }
    public static void main (String[] args) {

	    // process command line arguments
	    if (args.length != 9 && args.length != 8 && args.length != 10) {
            print_help();
	    }
		
	    else {
            COLONY_TYPE = args[1];
            NUM_ANTS = Integer.parseInt(args[2]);
            NUM_ITS = Integer.parseInt(args[3]);
            PHER_POWER = Double.parseDouble(args[4]);
            HEUR_POWER = Double.parseDouble(args[5]);
            EVAP_FACTOR = Double.parseDouble(args[6]);

            if (COLONY_TYPE.equals("ACS")){
                WEARING_AWAY = Double.parseDouble(args[7]);
                BEST_LEG = Double.parseDouble(args[8]);
                if (args.length > 9) {
                    DISP_INTERVAL = Integer.parseInt(args[9]);
                }
            }
            else {
                ELITISM = Double.parseDouble(args[7]);
                if (args.length > 8) {
                    DISP_INTERVAL = Integer.parseInt(args[8]);
                }
            }
        }
            
    }
	
}