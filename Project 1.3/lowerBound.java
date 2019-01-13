import java.util.*;
import java.io.*;

/*
	NOTE !!!!! If we are to run multiple graphs in one run, then the system of using global "eigenvalues" variables needs to be modified ...
				And we'll probably need to recompute the eigenvalues each time
*/

public class lowerBound {
	protected static int[] eigenvalues = new int[0];
	protected static Map<Integer, int[]> connections;
	
	/** The general method that calls the other hidden algorithms to compute lower bound(s) ...
	
		@param g, the graph of which we search lower bound(s)
		
	Methods available:
		- searchBiggestCompleteSubGraph
		- eigenvaluesLowerBound --- Not working ---
	*/
	public static int lowerBound (Graph g) {
		connections = TestGraph.connections;
		
		return searchBiggestCompleteSubGraph(g);
	}
	
	/** One of the lowerBound methods --- Not working ---
		Computes the lower bound by constructing the adjacency matrix of the graph, then computes its eigenvalues
		and then uses the formula of Hoffman to compute a lower bound
		
		@param g, the Graph of which we search a lower bound
		
		@return int, the found lower bound
	*/
	public static int eigenvaluesLowerBound(Graph g) {
		if (eigenvalues.length == 0) {
			int[][] adj = new int[g.getN()][g.getN()];
			Edge[] edges = g.getEdges();

			//Construct the adjacency matrix
			for (int i = 0; i < edges.length; i ++) {
				adj[edges[i].u][edges[i].v] ++;
				adj[edges[i].v][edges[i].u] ++;
			}

			//Compute its eigenvalues
			int[] eigenvalues = new int[g.getN()];

			//
			// Problemo ... -------------------------------------------------------------------------------------
			// Would need to use a library to calculate the eigenvalues ...
			// e.g. JAMA (http://math.nist.gov/javanumerics/jama/)
			//
			
			UpperBound.eigenvalues = eigenvalues;
		}
		
		//Search out the smallest and the biggest
		int min = eigenvalues[0];
		int max = eigenvalues[0];
		for (int i = 1; i < eigenvalues.length; i ++) {
			if (eigenvalues[i] < min) min = eigenvalues[i];
			else if (eigenvalues[i] > max) max = eigenvalues[i];
		}
		
		return (1 - (min/max));
	}
	
	/** One of the lowerBound methods
	NOTE: If the biggest complete subgraph is size 2 (if that makes sense and is possible as return value)
		-	Doesn't work currently
	
	Computes the lower bound by searching all the complete subgraphs of the Graph g,
	and then searches the size of the biggest complete subgraph to return it as a lower bound.
		
		@param g, the Graph of which we search a lower bound
		
		@return int, the found lower bound
	*/
	public static int searchBiggestCompleteSubGraph (Graph g) {
		//Find all subgraphs
		int[][] subgraphs = findCompleteSubgraphs(g.getN(), g.getEdges());
		int maxSize;
		if (subgraphs.length > 0) {
			maxSize = subgraphs[0].length;
			for (int i = 1; i < subgraphs.length; i ++) {
				if (subgraphs[i].length > maxSize) {
					maxSize = subgraphs[i].length;
				}
			}

			/*
			//Print out the results
			System.out.println("The set of subgraphs is: " + Arrays.deepToString(subgraphs));
			System.out.println("\nThe lower bound is " + maxSize);
			*/
		}
		else {
			maxSize = 2;
		}
		
		return maxSize;
	}
	
	/** Calls SearchIntersection with all the vertice pairs of the Graph
		
		@param connections, the map containing all the connections between vertices
		@param n, the number of vertices
		
		@return int[][] a 2-Dimensional array containing all the found subgraphs
	*/
	public static int[][] findCompleteSubgraphs (int n, Edge[] edges) {
		//Before everything else, initialize the output
		int[][] result = new int[n][n-1];
		int length = 0;
		
		//First, I create an array containing all the pairs of points I need to check
		int[][] toCheck = new int[edges.length][2];
		for (int i = 0; i < edges.length; i ++) {
			int a = edges[i].u;
			int b = edges[i].v;
			
			if (b < a) {
				//Switch them
				int tmp = a;
				a = b;
				b = tmp;
			}
			
			toCheck[i][0] = a;
			toCheck[i][1] = b;
		}
		
		for(int x = 0; x < toCheck.length; x ++) {
			if ((toCheck[x][0] != 0) && (toCheck[x][1] != 0)) {
				int[] toAdd = SearchIntersection(toCheck[x], toCheck, n);
				if (toAdd.length > 0) {
					//Enlarge result if needed
					if (length >= result.length) {
						int[][] newResult = new int[2*result.length][n-1];
						for (int i = 0; i < result.length; i ++) {
							newResult[i] = result[i];
						}

						result = newResult;
					}

					//And add the result of the search to the final result we will return
					result[length++] = toAdd;
				}
			}
		}
		
		//Reduce result to its actual size
		int[][] newResult = new int[length][n-1];
		for (int i = 0; i < length; i ++) {
			newResult[i] = result[i];
		}
		
		//Return the result
		return newResult;
	}
	
	/** Auxiliary method for findCompleteSubgraphs method
		Adds the given integer to an empty spot in the int[] array (empty spots contain 0's)
		@param int[] array, the integer array
		@param int element, the integer to be added to the array
		
		@return the array reference
	*/
	public static int[] Add (int[] array, int element) {
 		int i = 0;
 		boolean notAdded = true;
 		while ((i < array.length) && (notAdded)) {
 			if (array[i] == 0) {
 				array[i] = element;
 				notAdded = false;
 			}
 			else {
 				i ++;
 			}
 		}
 		
 		return array;
 	}
	
	/** Searches for complete subgraphs by checking if there are intersections of all the points constituting the complete subgraph with other vertices
		
		@param pairsToCheck, the indexes of the vertices which we check for further intersection
		@param toCheck, the 2-Dimensional array containing all the pairs that are to be checked for further intersection
		@param connections, the map containing all the connections between vertices
		@param n, the number of vertices
		
		@returns the maximal clique formed with the starting points (this method is called recursively)
	*/
	public static int[] SearchIntersection (int[] pairsToCheck, int[][] toCheck, int n) {
		int[] completeSubGraph = new int[n-1];
		
		int x = pairsToCheck[0];
		int y = pairsToCheck[1];
		int[] inter = Intersect(connections.get(x), connections.get(y));
		
		for (int i = 2; i < pairsToCheck.length; i ++) {
			inter = Intersect(inter, connections.get(pairsToCheck[i]));
		}
		
		if (inter.length > 1) {	
			//Then, we may have a bigger complete subgraph, and we call SearchIntersection again with all
			int[] newToCheck = new int[pairsToCheck.length+1];
			for (int i = 0; i < pairsToCheck.length; i ++) {
				newToCheck[i] = pairsToCheck[i];
			}
			
			newToCheck[newToCheck.length-1] = inter[0];
			
			//We search an intersection for the previous points, plus a point of their intersection
			completeSubGraph = SearchIntersection(newToCheck, toCheck, n);
		} 
		else if (inter.length == 1) {
			//If there is just 1 intersection to the existing points, 
			//Then we have a complete subgraph with the points of toCheck + the point of inter
			completeSubGraph = new int[pairsToCheck.length + 1];
			for (int i = 0; i < pairsToCheck.length; i ++) {
				completeSubGraph[i] = pairsToCheck[i];
			}
			completeSubGraph[pairsToCheck.length] = inter[0];
			
			//Delete all pairs that are composed of points from the complete subgraph just found
			for (int i = 0; i < completeSubGraph.length-1; i ++) {
				for (int j = i+1; j < completeSubGraph.length; j ++) {
					//For each pair of elements index i and j in completeSubGraph, I select the big and small one,
					int big;
					int small;
					if (completeSubGraph[i] > completeSubGraph[j]) {
						big = completeSubGraph[i];
						small = completeSubGraph[j];
					}
					else {
						big = completeSubGraph[j];
						small = completeSubGraph[i];
					}
					
					//Then, I search for them in toCheck and "delete" them once I find them
					for (int k = 0; k < toCheck.length; k ++) {
						if ((toCheck[k][0] == small) && (toCheck[k][1] == big)) {
							toCheck[k][0] = 0;
							toCheck[k][1] = 0;
						}
					}
				}
			}
		}
		else if (inter.length == 0) {
			//Then, we have our complete subgraph (except if we only have 2 points, then we only have a single connection between the two)
			if (pairsToCheck.length > 2) {
				//Set the subgraph to return to the array of points we analyzed
				completeSubGraph = pairsToCheck;
				
				//Delete all pairs that are composed of points from the complete subgraph just found
				for (int i = 0; i < completeSubGraph.length-1; i ++) {
					for (int j = i+1; j < completeSubGraph.length; j ++) {
						//For each pair of elements index i and j in completeSubGraph, I select the big and small one,
						int big;
						int small;
						if (completeSubGraph[i] > completeSubGraph[j]) {
							big = completeSubGraph[i];
							small = completeSubGraph[j];
						}
						else {
							big = completeSubGraph[j];
							small = completeSubGraph[i];
						}

						//Then, I search for them in toCheck and "delete" them once I find them
						for (int k = 0; k < toCheck.length; k ++) {
							if ((toCheck[k][0] == small) && (toCheck[k][1] == big)) {
								toCheck[k][0] = 0;
								toCheck[k][1] = 0;
							}
						}
					}
				}
			}
			else {
				completeSubGraph = new int[0];
			}
		}
		
		return completeSubGraph;
	}
	
	/** Auxiliary method for SearchIntersection
		Searches for ints that are in the two given int[] arrays
		
		@param int[] array1, the first array
		@param int[] array2, the second array
		
		@return an int[] array containing all the elements that are in both arrays given as parameters
	*/
	public static int[] Intersect(int[] array1, int[] array2) {
		int min = array1.length;
		if (array2.length < min) {
			min = array2.length;
		}
		
		int[] result = new int[min];
		int count = 0;
		
		int i = 0;
		while (i < array1.length) {
			int j = 0;
			boolean found = false;
			while ((j < array2.length) && (! found)) {
				if (array1[i] == array2[j]) {
					result[count] = array1[i];
					count ++;
					found = true;
				}
				else {
					j ++;
				}
			}
			i ++;
		}
		
		int[] newResult = new int[count];
		for (i = 0; i < newResult.length; i ++) {
			newResult[i] = result[i];
		}
		
		return newResult;
	}
}