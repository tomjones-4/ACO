# Ant Colony Optimization (ACO)

The Java code in this project is used for ant colony optimization (ACO) to find near-optimal solutions for the Traveling Salesman Problem (TSP). TSP is explained in more depth in the report on our findings, but the gist of the problem is to find the shortest path that visits all cities exactly once and returns to the starting city, i.e., to find to shortest tour. ACO is one of the more popular nature inspired algorithms, and as a result, there are several variations. The two we implemented are called Ant Colony System (ACS) and Elitist Ant System (EAS). Both these variations are explained in detail in the report, so this README will explain how to interact with the code, rather than the theory behind the algorithms.

For ACS, the user specifies 10 arguments via the command line. The user chooses (1) the problem to run the algorithm on; (2) the algorithm; (3) the number of ants; (4) the number of iterations; (5) the pheromone power; (6) the heuristic power; (7) the evaporation factor; (8) the wearing away rate; (9) the initial pheromone level (the user can specify a number or type "def" which will use the recommended default value); and (10) the probability of choosing each city greedily.

A sample command line for running ACS could look like: java Runner ALL_tsp/d2103.tsp ACS 20 100 1 5 0.1 0.1 def 0.9

For EAS, the user specifies 8 arguments via the command line. The user chooses (1) the problem to run the algorithm on; (2) the algorithm; (3) the number of ants; (4) the number of iterations; (5) the pheromone power; (6) the heuristic power; (7) the evaporation factor; and (8) the elitism factor.

A sample command line for running EAS could look like: java Runner ALL_tsp/d2103.tsp EAS 20 100 1 5 0.1 20

As the algorithm runs, the program outputs updates whenever there is a new best tour found, and at the end of every iteration. After the algorithm finishes running, either because it has completed all the iterations or it exceeded its 10 minute time limit, the program outputs the length of the best tour, the path of the best tour, and how long it took.
