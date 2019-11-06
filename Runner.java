/* Copied Runner.java over from GA/PBIL project because I figured this
   would be a good starting point.
*/

public class Runner {
    
    public static String problem_file;
    public static Reader problem_reader;

    private static void print_help () {
        System.out.println();
        System.out.println("If running Genetic Algorithm:");
        System.out.println("java Runner file individuals selection crossover pC pM generations alg disp");
        System.out.println("    file            = name of the file containing the problem (string)");
	    System.out.println("    individuals     = number of individuals in population (int)");
	    System.out.println("    selection       = type of selection of breeding pool (string):");
	    System.out.println("                        ts   = tournament selection");
	    System.out.println("                        rs   = rank based selection");
	    System.out.println("                        gs   = group selection");
	    System.out.println("    crossover       = crossover method (string):");
	    System.out.println("                        1c   = 1-point crossover");
	    System.out.println("                        uc   = uniform crossover");
	    System.out.println("    pC              = crossover probability (double)");
	    System.out.println("    pM              = mutation probability (double)");
	    System.out.println("    generations     = max number of generations to run (int)");
        System.out.println("    alg             = which algorithm to run (string):");
        System.out.println("                        g    = genetic algorithm");
        System.out.println("                        p    = population based incremental learning");
        System.out.println("    disp (optional) = display interval (int)");
        System.out.println();

        System.out.println("If running Population Based Incremental Learning:");
        System.out.println("java Runner file individuals positive negative pM aM generations alg disp");
        System.out.println("    file            = name of the file containing the problem (string)");
        System.out.println("    individuals     = number of individuals in population (int)");
        System.out.println("    positive        = positive learning rate (double)");
        System.out.println("    negative        = negative learning rate (double)");
        System.out.println("    pM              = mutation probability (double)");
        System.out.println("    aM              = mutation amount (double)");
	    System.out.println("    generations     = max number of generations to run (int)");
        System.out.println("    alg             = which algorithm to run (string):");
        System.out.println("                        g    = genetic algorithm");
        System.out.println("                        p    = population based incremental learning");
        System.out.println("    disp (optional) = display interval (int)");
	    System.out.println();
	    System.exit(1); // prevent the program from continuing without the correct inputs
    }
    public static void main (String[] args) {

	    // process command line arguments
	    if (args.length != 9 && args.length != 8) {
            print_help();
	    }
		
	    else {
            if (args[7].equals("g")){ // convert arguments to values for GA
                problem_file = args[0];
                GA.POPULATION_SIZE = Integer.parseInt(args[1]);
	            if (args[2].equals("ts") || args[2].equals("rs") || args[2].equals("gs")) {
		            GA.SELECTION = args[2];
	            }
	            else {
		            System.out.println("Invalid third argument specifying selection type.  Please use:");
		            System.out.println("    ts   = tournament selection");
		            System.out.println("    rs   = rank based selection");
		            System.out.println("    gs   = group selection");
		            System.exit(1);
	            }
	            if (args[3].equals("1c") || args[3].equals("uc")) {
		            GA.CROSSOVER = args[3];
	            }
	            else {
		            System.out.println("Invalid fourth argument specifying crossover type.  Please use:");
		            System.out.println("    1c   = 1-point crossover");
		            System.out.println("    uc   = uniform crossover");
		            System.exit(1);
	            }
	            GA.PROB_CROSS = Double.parseDouble(args[4]);
	            GA.PROB_MUTE = Double.parseDouble(args[5]);
                GA.MAX_GEN = Integer.parseInt(args[6]);
                
                if (args.length == 9) {
                    GA.DISP_INTERVAL = Integer.parseInt(args[8]);
                    GA.SHOW_INTERVALS = true;
                }
                else {
                    GA.SHOW_INTERVALS = false;
                }

                problem_reader = new Reader(problem_file);
                GA.run_GA();
            }
            else if (args[7].equals("p")){ // convert arguments to values for PBIL
                problem_file = args[0];
                PBIL.POPULATION_SIZE = Integer.parseInt(args[1]);
                PBIL.POS_LEARN_RATE = Double.parseDouble(args[2]);
                PBIL.NEG_LEARN_RATE = Double.parseDouble(args[3]);
	            PBIL.PROB_MUTE = Double.parseDouble(args[4]);
	            PBIL.AMOUNT_MUTE = Double.parseDouble(args[5]);
                PBIL.MAX_GEN = Integer.parseInt(args[6]);
                PBIL.DISP_INTERVAL = Integer.parseInt(args[8]);

                if (args.length == 9) {
                    PBIL.DISP_INTERVAL = Integer.parseInt(args[8]);
                    PBIL.SHOW_INTERVALS = true;
                }
                else {
                    PBIL.SHOW_INTERVALS = false;
                }

                problem_reader = new Reader(problem_file);
                PBIL.run_PBIL();
            }
            else {
                System.out.println("Invalid eighth argument specifying algorithm.  Please use:");
		            System.out.println("    g    = genetic algorithm");
		            System.out.println("    p    = population based incremental learning");
		            System.exit(1);
            }

	        /* if (args[7].equals("g")) {
                System.out.println();
                System.out.println("The following are the values you entered:");
                System.out.println("    FILE NAME = " + args[0]);
                System.out.println("    POPULATION_SIZE = " + GA.POPULATION_SIZE);
                System.out.println("    SELECTION = " + GA.SELECTION);
                System.out.println("    CROSSOVER = " + GA.CROSSOVER);
                System.out.println("    PROB_CROSS = " + GA.PROB_CROSS);
                System.out.println("    PROB_MUTE = " + GA.PROB_MUTE);
                System.out.println("    MAX_GEN = " + GA.MAX_GEN);
                System.out.println("    DISP_INTERVAL = " + GA.DISP_INTERVAL);
            }
            else if (args[7].equals("p")) {
                System.out.println("The following are the values you entered:");
                System.out.println("    FILE NAME = " + args[0]);
                System.out.println("    POPULATION_SIZE = " + PBIL.POPULATION_SIZE);
                System.out.println("    POSITIVE LEARN RATE = " + PBIL.POS_LEARN_RATE);
                System.out.println("    NEGATIVE LEARN RATE = " + PBIL.NEG_LEARN_RATE);
                System.out.println("    PROB MUTE = " + PBIL.PROB_MUTE);
                System.out.println("    AMOUNT MUTE = " + PBIL.AMOUNT_MUTE);
                System.out.println("    MAX_GEN = " + PBIL.MAX_GEN);
                System.out.println("    DISP_INTERVAL = " + PBIL.DISP_INTERVAL);
            } */
	    }	
    }
	
}