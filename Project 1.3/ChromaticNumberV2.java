import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

/** This class contains all the methods needed to compute the chromatic number of a given graph
*/
public class ChromaticNumberV2 {
	protected static Map<Integer, int[]> connections;
	
	/*
		NOTE !!! We suppose, in isTree(), isCycle() and the chromatic number algorithms, that connections is already set to be TestGraph.connections;
	*/
	
	/** Given a lowerBound and an upperBound, it tries to reduce the difference between the two by using binary search
		Once a k is generated, we check using auxiliary method permute() if a k-colouring is possible.
			If it is, then we have a new upper bound, otherwise we have a new lower bound
	*/
	protected void binarySearch(Graph g, int lowerBound, int upperBound) {
		while (lowerBound != upperBound) {
			if permute(g.n, (int)(lowerBound + upperBound)/2)) {
				upperBound = (int)(lowerBound + upperBound)/2);
			}
			else {
				if (lowerBound != (int)(lowerBound + upperBound)/2)) {
					lowerBound = (int)(lowerBound + upperBound)/2);
				} else lowerBound = upperBound;
			}
		}
	}

	
	/** Checks if there is a possible colouring with k colors or less
	*/
	protected static boolean permute(int n, int k) {
		// Construct an array whereby indices represent vertices and values represent colours. Initialise with no colour
		int[] colouring = new int[n]; 
		for (int i = 0; i < n; i++) {
			colouring[i] = -1;
		}
		
		// Try all combinations and their permutations for vertices and colours
		boolean isFeasible = false; 
		int i = 0;
		while (!isFeasible && i < n) {
			// Loop through all possible colours for this vertex
			int j = colouring[i] + 1; 
			colouring[i] = -1;
			while (colouring[i] == -1 && j < k) {
				if (isProperColour(colouring, i, j)) {
					colouring[i] = j; 
				} else {
					j++;
				}
			}
			
			// If no colour was found for this vertex move to previous vertex, move to the next vertex
			if (colouring[i] == -1) {
				i--; 
			} else {
				i++;
			}
			
			// All vertices have been assigned a colour
			if (i == n) {
				isFeasible = true;
			}
		}
		
		return isFeasible;
	}
	
	/** Checks if the vertex at index vertexIndex in the coloring "colouring" can be colored in color "colour"
		
		@return a boolean value representing whether the vertex can or not be coloured in color "colour"
	*/
	public static boolean isProperColour (int[] colouring, int vertexIndex, int colour) {
		int[] adjacentVertices = connections.get(vertexIndex+1);
		boolean isPossible = true;
		int i = 0;
		while (i < adjacentVertices.length && isPossible) {
			if (colouring[adjacentVertices[i] - 1] == colour) {
				isPossible = false;
			} else {
				i ++;
			}
		}
		
		return isPossible;
	}
	
	/** Checks if the graph is a very special case (if there are 0 edges or the maximum number of edges)
			If this is not the case, then it calls the method computeChromaNumber, which actually computes the chromatic number.
		
		@param Graph g, the graph whose chromatic number we try to compute
	*/
	public static int chromaticNum(Graph g, int method) {
		connections = TestGraph.connections;
		
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
			if (method == 1) {
				//Start with the maximum size array
				int[][] result = new int[n-1][n];
				//!!!!! NOTE: could be optimized to use limited size, then enlarge if needed !!!!!

				//Initiate all the vertices on the same color (the first color)
				for (int i=1; i <= n; i ++) {
					result[0][i-1] = i;
				}
				
				Edge[] e = g.getEdges();

				chromaNum = bruteForceChromaNum(e, n, 0, result, n-1);
			}
			else {		//Method == 2
				chromaNum = concentricColoring(g, n);
			}
		}
		
		g.setChromaticNumber(chromaNum);
		return chromaNum;
	}
	
	/** This method checks whether a graph has a special "tree"-structure and returns an according boolean value
			IMPORTANT !!! It is assumed that the "connections" variable has been already initialized
		
		@param Graph g, the graph which we check for a "tree" structure
		
		@return a boolean value, true if it has a "tree"-structure, false if otherwise
	*/
	public static boolean isTree (Graph g) {
		boolean isTree = true;
		
		//Find the first vertex that has at least one connection
		int i = 1;
		while (i <= g.getN() && !g.seen[i]) {
			i ++;
		}
		
		//Build a new boolean array to record the vertices we saw in the tree
		boolean[] seenInTree = new boolean[g.getN() + 1];
		ArrayList<int[]> x = new ArrayList<int[]>();
		int[] toAdd = {0, i};
		x.add(toAdd);
		i = 0;
		while (i < x.size() && isTree) {
			seenInTree[x.get(i)[1]] = true;

			//Search for the vertexIndex that is not the same as the previous one (to avoid a loop)
			int[] connectionsOfVertex = connections.get(x.get(i)[1]);
			int j = 0;
			while (j < connectionsOfVertex.length && isTree) {
				if (connectionsOfVertex[j] != x.get(i)[0]) {
					//If this vertex was already seen, then exit the while loops
					if (seenInTree[connectionsOfVertex[j]]) {
						isTree = false;
					}
					else {
						//Add this connection to x
						int[] toAdd2 = {x.get(i)[1], connectionsOfVertex[j]};
						x.add(toAdd2);
					}
				}
				j ++;
			}
			
			//Remove the connection that was just used
			x.remove(i);
		}
		
		//Check if we have seen all the vertices in the graph
		if (isTree)
			isTree = Arrays.equals(g.seen, seenInTree);
		
		return isTree;
	}
	
	/** Checks if the Graph is a bipartite Graph (chromatic number = 2)
	*/
	public static boolean isBipartite(Graph g) {
		Vertex[] vertices = g.vertices;
		int blankColor = Vertex.DEFAULT_BLANK_COLOR;
		
		//Start by coloring the first vertex and adding all connected vertices to an arrayList
		g.setColor(vertices[1], 0);
		ArrayList<Vertex> verticesToColor = new ArrayList<Vertex>();
		for (int i = 0; i < vertices[1].adjacentVertices.length; i ++) {
			verticesToColor.add(vertices[1].adjacentVertices[i]);
		}
		
		//System.out.println("Blank Vertices: " + Arrays.deepToString(g.blankVertices));
		
		//Repeat until either we find out that the graph can not be colored in 2 colors or we finished coloring the graph with 2 colors
		boolean isBipartite = true;
		while (isBipartite && g.blankVertices.length != 0) {
			System.out.println("\n# Colored vertices: " + g.coloredVertices.length + "			  Size of verticesToColor: " + verticesToColor.size());
			//System.util.sleep(500);
			
			//If we have gone through all the verticesToColor, then the graph is composed of multiple unconnected subgraphs
			if (verticesToColor.size() == 0) {
				//And we need to add a new uncolored vertex to verticesToColor
				verticesToColor.add(g.blankVertices[0]);
			}
			
			if (verticesToColor.get(0).color == blankColor) {
				//Check if it is possible to color the first vertex in the arrayList in one of the two colors, and if yes, which one
				int[] availableColors = {0, 1};
				int j = 0;
				while (j < verticesToColor.get(0).adjacentVertices.length && (availableColors[0] != -1 || availableColors[1] != -1)) {
					if (verticesToColor.get(0).adjacentVertices[j].color != blankColor) {
						availableColors[verticesToColor.get(0).adjacentVertices[j].color] = blankColor;
					}
					
					j ++;
				}
				
				//Color the vertex in the first color of availableColors that is not Vertex.DEFAULT_BLANK_COLOR
				j = 0;
				while (j < availableColors.length) {
					if (availableColors[j] != blankColor) {
						g.setColor(verticesToColor.get(0), availableColors[j]);
					}
					j++;
				}
				
				//If it is not colored yet, then this vertex can not be colored in one of the two colors and the graph is not bipartite
				if (verticesToColor.get(0).color == blankColor)
					isBipartite = false;
				else {		
					//Otherwise, we add the vertices adjacent to the vertex just colored to the arrayList
					for (j = 0; j < verticesToColor.get(0).adjacentVertices.length; j ++) {
						if (verticesToColor.get(0).adjacentVertices[j].color == blankColor) {
							verticesToColor.add(verticesToColor.get(0).adjacentVertices[j]);
						}
					}
					
					//And remove the vertex itself
					verticesToColor.remove(0);
				}
			}
			else {
				verticesToColor.remove(0);
			}
		}
		
		return isBipartite;
	}
	
	/** This method checks whether a graph has a special "cycle"-structure and returns an according boolean value
			IMPORTANT !!! It is assumed that the "connections" variable has been already initialized
		
		@param Graph g, the graph of which we check for a "cycle" structure
		
		@return a boolean value, true if it has a "cycle"-structure, false if otherwise
	*/
	public static boolean isCycle (Graph g) {
		//First, we check if each vertex has only 2 connections
		boolean isCycle = true;
		int n = g.getN();
		int i = 1;
		while (i <= n && isCycle) {
			if (connections.get(i).length != 2 && g.seen[i]) {
				isCycle = false;
			}
			else {
				i ++;
			}
		}
		
		//Then, check if it really is a cycle, that is, that if we start at one vertex, we pass every vertex that has connections
		if (isCycle) {
			boolean[] seenInCycle = new boolean[n+1];
			
			//Search for the first vertex that has a connection
			i = 1;
			while (i <= n && !g.seen[i]) {
				i ++;
			}
			
			//Then, start following the connections until we are back at the vertexIndex where we started
			int oldX = 0;
			int newX = i;
			do {
				seenInCycle[newX] = true;
				
				//Search for the vertexIndex that is not the same as the previous one (to avoid a loop)
				int[] connectionsOfVertex = connections.get(newX);
				boolean foundNewX = false;
				i = 0;
				while (i < connectionsOfVertex.length && !foundNewX) {
					if (connectionsOfVertex[i] != oldX) {
						foundNewX = true;
						
						//Update all variables
						oldX = newX;
						newX = connectionsOfVertex[i];
					}
					else {
						i ++;
					}
				}
			} while (newX != i);
			
			isCycle = Arrays.equals(g.seen, seenInCycle);
		}
		
		return isCycle;
	}
	
	/** One of the chromatic number algorithms - NOT FINISHED
		It starts by coloring the vertex with the highest degree,
			then colors its adjacentVertices, then the adjacentVertices of them, and so on until the complete graph is colored
			
		@param g, the Graph which we want to find the chromatic number of
		@param n, the number of vertices
		
		@return an integer, the chromatic number ?
	*/
	public static int concentricColoring (Graph g, int n) {
		Vertex[] vertices = g.vertices;
		boolean[] vertexColored = new boolean[n+1];		//VertexColored has an entry at index vertexIndex equal to true if the vertex is colored, and false if not colored
		boolean[] vertexWillBeColored = new boolean[n+1];		//vertexWillBeColored has an entry true at a certain vertexIndex index if the vertex is colored or will be colored in the next iteration
		
		//Find the index of the vertex with the highest
		int max = 1;
		for (int i = 2; i <= n; i ++) {
			if (connections.get(i).length > connections.get(max).length) max = i;
		}
		
		//Color this vertex in the first color
		int[][] result = new int[n-1][n];
		result[0][0] = max;
		vertexColored[max] = true;
		vertexWillBeColored[max] = true;
		
		Vertex[] toColor = vertices[max].adjacentVertices;
		boolean completelyColored = false;
		while (completelyColored == false) {
			Vertex[] newToColor = new Vertex[g.getN()];
			int length = 0;
			
			for (int i = 0; i < toColor.length; i ++) {
				
			}
			
			//Re-evaluate completelyColored
			completelyColored = true;
			int i = 0;
			while (i < vertexColored.length && completelyColored) {
				//If the vertex is not yet colored, then completelyColored will become false and the loop will exit
				completelyColored = vertexColored[i++];
			}
		}
		
		return 0;
	}
	
	/** This is the recursive method that actually works to compute the chromatic number
		@param Edge[] e, the connections (or edges) of the graph
		@param n, the number of vertices of the graph
		@param edgeToCheck, the current edge that we need to check
		@param result, the color configuration
		@param min, the minimum color configuration found so far
	*/
	public static int bruteForceChromaNum (Edge[] e, int n, int edgeToCheck, int[][] result, int min) {		
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
						if (i < result.length) {
							if (MoveIsPossible(e, result, e[edgeToCheck].u, i)) {
								//If option1 here is possible, then try it out
								int[][] option1 = Move(result, 0, e[edgeToCheck].u, i);
								int numColors1 = bruteForceChromaNum(e, n, edgeToCheck+1, option1, min);
								option1 = Move(result, i, e[edgeToCheck].u, 0);

								if (numColors1 < min) {
									min = numColors1;
								}

								//oneOptionFound = true;
							}

							//If option2 is possible, try it out and 
							if (MoveIsPossible(e, result, e[edgeToCheck].v, i)) {
								int[][] option2 = Move(result, 0, e[edgeToCheck].v, i);
								int numColors2 = bruteForceChromaNum(e, n, edgeToCheck+1, option2, min);
								option2 = Move(result, i, e[edgeToCheck].v, 0);

								if (numColors2 < min) {
									min = numColors2;
								}

								//oneOptionFound = true;
							}
						}
					}
				}
				//If numCols >= min, then I can discard this branch of the search tree, and just return the minimum found so far (because the number of colors used can only grow, not shrink)
				return min;
			}
			//This gets only executed if the edge vertices are not both in the same color (the first, typically), 
			//therefore, nothing needs to be done to result, and we can just call the method to check for the next edge
			return bruteForceChromaNum(e, n, edgeToCheck+1, result, min);
		}
	}
	
	/** Checks if I can move item toMove from array[origin] to array[destination], that is, without having any element of array[destination] sharing an edge with toMove
		@param Edge[] e, the connections of the graph
		@param array, the color configuration
		@param toMove, the item (that represents a vertex) that we want to check if it can be moved
		@param destination, the color in the color configuration array which we want to move the vertex to
		
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
		@param array, the current color configuration
		@param origin, the starting color of the item
		@param toMove, the item (vertex) to be moved
		@param destination, the final color of the item
		
		@return a new color configuration, where the move has been operated
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
				array[destination][toMove-1] = toMove;
			}
			i ++;
		}
		
		//Return the modified array
		return array;
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