import java.util.Arrays;

public class ChromaticNumberV2 {
	public static void main (String[] args) {
		Graph graph1 = ReadGraphV2.readGraph(args);
		
		int chromaNum = chromaticNum(graph1);
		
		System.out.println("Found chromatic number ! \nIt is " + chromaNum);
	}
	
	/** Checks if the graph is a very special case (if there are 0 edges or the maximum number of edges)
			If this is no the case, then it calls the method computeChromaNumber, which actually computes the chromatic number.
		@param Edge[] e, an array containing all the edges of the current graph
		@param n, the number of vertices of the current graph
		@param notUsed, the number of vertices that have no edge connected to them at all
	*/
	public static int chromaticNum(Graph g) {
		Edge[] e = g.getEdges();
		int n = g.getN();
		int notUsed = g.getNotUsed();
		int m = g.getM();
		
		int chromaNum = n;
		
		//If empty graph, then we need only 1 color
		if (m == 0) {
			chromaNum = 1;
		}
		//Else if we have a complete graph (maximum number of connections), then we need n colors (or, if notUsed > 0, we need (n-notUsed) colors)
		else if (m == ((n-notUsed)*(n-notUsed-1)/2)) {
			chromaNum = (n-notUsed);
		}
		//Otherwise, it's something in between, and it's more complicated
		else {
			//Start with the maximum size array
			int[][] result = new int[n-1][n];
			//!!!!! NOTE: could be optimized to use limited size, then enlarge if needed !!!!!
			
			//Initiate all the vertices on the same color (the first color)
			for (int i=1; i <= n; i ++) {
				result[0][i-1] = i;
			}
			
			chromaNum = computeChromaNumber(e, n, 0, result, n-1);
		}
		
		g.setChromaticNumber(chromaNum);
		return chromaNum;
	}
	
	public static int computeChromaNumber (Edge[] e, int n, int edgeToCheck, int[][] result, int min) {		
		//Normally, this if will only trigger when edgeToCheck == e.length, but for security, I say if edgeToCheck >= e.length
		if (edgeToCheck >= e.length) {
			//Returns the amount of colors of the color assignment (result)
			return usedColors(result);
		}
		else {
			//If result[0] contains both values of the edge, (because it is not possible to have two connected points in another color)
			if (ArrayContains(result[0], e[edgeToCheck].u) && ArrayContains(result[0], e[edgeToCheck].v)) {
				//Separate the numbers. This can be done by two ways: Either move e[edgeToCheck].u to any other color, or move e[edgeToCheck].v to another color.
				//Here, I try moving one of them at a time, to any other color (if it does not bring up any new conflict with another edge)

				//numCols = number of colors used until now
				int numCols = usedColors(result);

				//Only try out possible options to continue if you currently have less colors than the current minimum
				if (numCols < min) {
					for (int i = 1; i <= numCols; i ++) {
						/*
						boolean oneOptionFound = false;

						//We only try the creation of a new color if there is no possible option to add the item toMove to an existing color
						if ((i < numCols) | (! oneOptionFound)) {
							*/
							if (i < result.length) {
								if (MoveIsPossible(e, result, e[edgeToCheck].u, i)) {
									//If option1 here is possible, then try it out
									int[][] option1 = Move(result, 0, e[edgeToCheck].u, i);
									int numColors1 = computeChromaNumber(e, n, edgeToCheck+1, option1, min);
									option1 = Move(result, i, e[edgeToCheck].u, 0);

									if (numColors1 < min) {
										min = numColors1;
									}

									//oneOptionFound = true;
								}

								//If option2 is possible, try it out and 
								if (MoveIsPossible(e, result, e[edgeToCheck].v, i)) {
									int[][] option2 = Move(result, 0, e[edgeToCheck].v, i);
									int numColors2 = computeChromaNumber(e, n, edgeToCheck+1, option2, min);
									option2 = Move(result, i, e[edgeToCheck].v, 0);

									if (numColors2 < min) {
										min = numColors2;
									}

									//oneOptionFound = true;
								}
							}
						//}
					}
				}
				//If numCols >= min, then I can discard this branch of the search tree, and just return the minimum found so far (because the number of colors used can only grow, not shrink)
				return min;
			}
			//This gets only executed if the edge vertices are not both in the same color (the first, typically), 
			//therefore, nothing needs to be done to result, and we can just call the method to check for the next edge
			return computeChromaNumber(e, n, edgeToCheck+1, result, min);
		}
	}
	
	/** Checks if I can move item toMove from array[origin] to array[destination], that is, without having any element of array[destination] sharing an edge with toMove
		@return a boolean value, true if the move is valid, otherwise false
	*/
	public static boolean MoveIsPossible (Edge[] e, int[][] array, int toMove, int destination) {
		//As I should only need to move points from the 1st color to another one, I can assume that the position
		//of the point is array[0][toMove-1]
		boolean movePossible = true;
		int j = 0;
		
		//If the destination array is not empty, we need to check if we can add toMove in it, otherwise, it is a new color we create
		if (! arrayEmpty(array[destination])) {
			while ((j < e.length) && (movePossible)) {
				//For each edge, if the item to move is one of the two points, check if the other item is in the array I try to move the item to
				if ((e[j].u == toMove) || (e[j].v == toMove)) {
					if ((ArrayContains(array[destination], e[j].u)) || (ArrayContains(array[destination], e[j].v))) {
						movePossible = false;
					}
				}
				j ++;
			}
		}
		
		return movePossible;
	}
	
	/** Should move item toMove from array[origin] to array[destination].
		The User should have already checked if the move is possible (that is, doesn't bring together any edge-connected-points) using movePossible()
	*/
	public static int[][] Move(int[][] array, int origin, int toMove, int destination) {
		int i = 0;
		boolean notFoundToMove = true;
		//For each item in the origin array, while we have not found the item we need to move
		while ((i < array[origin].length) && (notFoundToMove)) {
			if (array[origin][i] == toMove) {
				//Once we find the item we need to move,
				notFoundToMove = false;
				
				//Deletes the original toMove
				array[origin][i] = 0;
				//And adds it in array[destination]
				Add(array[destination], toMove);
			}
			i ++;
		}
		
		//Return the modified array
		return array;
	}
	
	/** Adds the integer "element" to array, at the position element-1
	*/
	public static void Add (int[] array, int element) {
		array[element-1] = element;
	}
	
	/** Returns the number of color the array color composition uses (that is, the number of non-empty 1D-arrays inside "array"), where an empty array is filled with 0's, the default int value
		@return an integer, number of colors used by this color assignment
	*/
	public static int usedColors (int[][] array) {
		int i = 0;
		boolean empty = false;
		int numColors = 0;
		while ((i < array.length) && (! empty)) {
			if (! arrayEmpty(array[i])) {
				numColors ++;
				i ++;
			}
			else {
				empty = true;
			}
		}
		
		return numColors;
	}
	
	/** Returns a boolean indicating whether a given array of integers is empty (= filled with 0s) or not
		@return true if it is filled with only 0s, otherwise, false
	*/
	public static boolean arrayEmpty (int[] array) {
		boolean isEmpty = true;
		int i = 0;
		while ((i < array.length) && (isEmpty)) {
			if (array[i] != 0) {
				isEmpty = false;
			}
			else {
				i ++;
			}
		}
		
		return isEmpty;
	}
	
	/** Returns a boolean indicating whether an array of integers contains a given integer.
		@return true if the array contains the int, otherwise false
	*/
	public static boolean ArrayContains(int[] array, int x) {
		boolean found = false;
		int i = 0;
		
		while ((! found) && (i < array.length)) {
			if (array[i] == x) {
				found = true;
			}
			else {
				i ++;
			}
		}
		
		return found;
	}
}