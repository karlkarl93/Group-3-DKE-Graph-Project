import java.io.*;
import java.util.*;

		class ColEdge
			{
			int u;
			int v;
			}

public class BottomUp {	
	public static boolean DEBUG;
		
		public final static String COMMENT = "//";
		
		public static void main( String args[] )
			{
			if( args.length < 1 )
				{
				System.out.println("Error! No filename specified.");
				System.exit(0);
				}

			Scanner in = new Scanner(System.in);
			
			System.out.println("Do you want to enter Debug mode or not ? (enter 'y' or 'n')");
			String c = in.next().toLowerCase();
			
			while ((! c.equals("y")) && (! c.equals("n")) && (! c.equals("yes")) && (! c.equals("no"))) {
				System.out.println("Please enter 'y' or 'n'! Do you want to enter DEBUG mode or not ?");
				c = in.next().toLowerCase();
			}
			
			if (c.equals("y") | c.equals("yes")) {
				DEBUG = true;
			}
			else {
				DEBUG = false;
			}
			
			String inputfile = args[0];
			
			boolean seen[] = null;
			
			//! n is the number of vertices in the graph
			int n = -1;
			
			//! m is the number of edges in the graph
			int m = -1;
			
			//! e will contain the edges of the graph
			ColEdge e[] = null;
			
			try 	{ 
			    	FileReader fr = new FileReader(inputfile);
			        BufferedReader br = new BufferedReader(fr);

			        String record = new String();
					
					//! The first few lines of the file are allowed to be comments, staring with a // symbol.
					//! These comments are only allowed at the top of the file.
					
					//! -----------------------------------------
			        while ((record = br.readLine()) != null)
						{
						if( record.startsWith("//") ) continue;
						break; // Saw a line that did not start with a comment -- time to start reading the data in!
						}
	
					if( record.startsWith("VERTICES = ") )
						{
						n = Integer.parseInt( record.substring(11) );					
						if(DEBUG) System.out.println(COMMENT + " Number of vertices = "+n);
						}

					seen = new boolean[n+1];
						
					record = br.readLine();
					
					if( record.startsWith("EDGES = ") )
						{
						m = Integer.parseInt( record.substring(8) );				
						if(DEBUG) System.out.println(COMMENT + " Expected number of edges = "+m);
						}

					e = new ColEdge[m];	
												
					for( int d=0; d<m; d++)
						{
						if(DEBUG) System.out.println(COMMENT + " Reading edge "+(d+1));
						record = br.readLine();
						String data[] = record.split(" ");
						if( data.length != 2 )
								{
								System.out.println("Error! Malformed edge line: "+record);
								System.exit(0);
								}
						e[d] = new ColEdge();
						
						e[d].u = Integer.parseInt(data[0]);
						e[d].v = Integer.parseInt(data[1]);

						seen[ e[d].u ] = true;
						seen[ e[d].v ] = true;
						
						if(DEBUG) System.out.println(COMMENT + " Edge: "+ e[d].u +" "+e[d].v);
				
						}
									
					String surplus = br.readLine();
					if( surplus != null )
						{
						if( surplus.length() >= 2 ) if(DEBUG) System.out.println(COMMENT + " Warning: there appeared to be data in your file after the last edge: '"+surplus+"'");						
						}
					
					}
			catch (IOException ex)
				{ 
		        // catch possible io errors from readLine()
			    System.out.println("Error! Problem reading file "+inputfile);
				System.exit(0);
				}

			int notUsed = 0;
			for( int x=1; x<=n; x++ )
				{
				if( seen[x] == false )
					{
					notUsed ++;
					if(DEBUG) System.out.println(COMMENT + " Warning: vertex "+x+" didn't appear in any edge : it will be considered a disconnected vertex on its own.");
					}
				}

			//! At this point e[0] will be the first edge, with e[0].u referring to one endpoint and e[0].v to the other
			//! e[1] will be the second edge...
			//! (and so on)
			//! e[m-1] will be the last edge
			//! 
			//! there will be n vertices in the graph, numbered 1 to n

			//! INSERT YOUR CODE HERE!
			
			if (DEBUG && (notUsed > 0)) System.out.println("\nThere are " + (n-notUsed) + " actually used points.");
			
			System.out.println("\nDo you want to test a specific method or launch the default chromatic Number search ? (enter 1 or 2)");
			String option = in.next();
			
			do {
				if (option.equals("1")) {
					System.out.println("\nWhich method do you want to check ? (enter 'Karloid' to test Karloid or anything else to test BottomUpWorkingMethod)");
					String methodToUse = in.next().toLowerCase();
					
					if (methodToUse.equals("karloid")) {
						System.out.println("\nUsing method Karloid! \n");
					}
					else {
						System.out.println("\nUsing method BottomUpWorkingMethod \n");
					}
					long start = System.nanoTime();
					int chromaNumber = testChromaNumberMethod(e, n, m, notUsed, methodToUse, start, 15);
					long end = System.nanoTime();
					
					System.out.printf("\nThe chromatic number is %d \nExecution Time: %f\n", chromaNumber, (double)((end-start)/Math.pow(10, 9)));
				}
				else if (option.equals("2")) {
					int secsTime = 10;
				
					//Try computing the chromatic number within a given time
					int chromaNumber = chromaticNumber(e, n, m, notUsed, secsTime);
			
					if (chromaNumber != -1) {
						System.out.println("The chromatic number is " + chromaNumber);
					}
					else {
						int upBound = upperBound(e, n);
					
						System.out.println("The upper bound is " + upBound);
					}
				}
				else {
					System.out.println("Please enter a valid answer (1 for testing a specific method or 2 for running default algorithm): ");
					option = in.next();
				}
			} while ((! option.equals("1")) && (! option.equals("2")));
		}
	
	/** Same as chromaticNumber, but instead of giving a limited time, the User specifies which method to use
	*/
	public static int testChromaNumberMethod(ColEdge[] e, int n, int m, int notUsed, String methodToUse, long start, int timeInterval) {
		//If empty graph, then we need only 1 color
		if (m == 0) {
			if (DEBUG) {
				System.out.println("There are no edges!");
			}
			return 1;
		}
		//Else if we have a complete graph (maximum number of connections), then we need n colors
		else if (m == ((n-notUsed)*(n-notUsed-1)/2)) {
			if (DEBUG) {
				if (notUsed == 0) {
					System.out.println("Maximum edges, everything inter-connected!");
				}
				else {
					System.out.println("Excluding the vertices not connected at all, everything is inter-connected!");
				}
			}
			return (n-notUsed);
		}
		//Otherwise, it's complicated
		else {
			return testBottomUp(e, n, methodToUse, start, timeInterval);
		}
	}
	
	/** Same as BottomUp, but instead of a given limited time, the User specifies which method to use
	*/
	public static int testBottomUp(ColEdge[] e, int n, String methodToUse, long start, int timeInterval) {
		//Start with the maximum size array,
		int[][] result = new int[n-1][n];
		// Note: could be optimized to use limited size, then enlarge if needed
		
		//Initiate all the vertices on the same colour
		for (int i=1; i <= n; i ++) {
			result[0][i-1] = i;
		}
		
		if (methodToUse.equals("karloid")) {
			return testKarloid(e, n, result, start, timeInterval);
		}
		else {
			return testBottomUpWorkingMethod(e, n, 0, result, n-1, start, timeInterval);
		}
	}
	
	/** Same as Karloid, but without limited time
	*/
	public static int testKarloid (ColEdge[] e, int n, int[][] result, long start, int timeInterval) {
		int[] moved = new int[n];
		for (int i = 0; i < result.length; i ++) {
			for (int j = 0; j < e.length; j ++) {
				if (DEBUG) {
					//Every 15 seconds, print the time passed on the algorithm
					if ((int)((System.nanoTime()-start)/Math.pow(10, 9)) > timeInterval) {
						System.out.printf("Have been working for %d secs now \n", (timeInterval));
						timeInterval += 15;
					}
				}
				
				if ((ArrayContains(result[i], e[j].u)) && (ArrayContains(result[i], e[j].v))) {
					if (i == 0) {
						Add(result[i+1], e[j].v);
						Erase(result[i], e[j].v);
					}
					else {
						if (ArrayContains(moved, e[j].v)) {
							Add(result[i-2], e[j].v);
							Erase(result[i], e[j].v);
							Erase(moved, e[j].v);
							i = 0;
						}
						else {
							Add(result[i+1], e[j].v);
							Erase(result[i], e[j].v);
							Add(moved, e[j].v);
						}
					}
				}
			}
		}
		
		if (DEBUG) {
			System.out.println("The result is " + Arrays.deepToString(result));
		}
		
		return usedColors(result);
	}
	
	public static int testBottomUpWorkingMethod (ColEdge[] e, int n, int edgeToCheck, int[][] result, int min, long start, int timeInterval) {		
		if (DEBUG) {
			//Every 15 seconds, tell the User the time elapsed
			if ((int)((System.nanoTime()-start)/Math.pow(10, 9)) > timeInterval) {
				System.out.printf("Have been working for %d secs now \n", timeInterval);
				timeInterval += 15;
			}
		}
		
		//Normally, this if will only trigger when edgeToCheck == e.length, but for security, I say if edgeToCheck >= e.length
		if (edgeToCheck >= e.length) {
			//Returns the amount of colors of the color assignment (result)
			return usedColors(result);
		}
		else {
			int j = 0;
			//If result[j = 0] contains both values of the edge, (because it is not possible to have two edge points in another color)
			if (ArrayContains(result[j], e[edgeToCheck].u) && ArrayContains(result[j], e[edgeToCheck].v)) {
				//Separate the numbers

				//n = number of colors used until now
				int numCols = usedColors(result);

				//Only try out possible options to continue if you currently have less colors than the current minimum
				if (numCols < min) {
					for (int i = 1; i <= numCols; i ++) {
						boolean oneOptionFound = false;

						//We only try the creation of a new color if there is no possible option to add the item toMove to an existing color
						if ((i < numCols) | (! oneOptionFound)) {
							if (i < result.length) {
								if (MoveIsPossible(e, result, e[edgeToCheck].u, i)) {
									//If option1 here is possible, then try it out
									int[][] option1 = Move(result, j, e[edgeToCheck].u, i);
									int numColors1 = testBottomUpWorkingMethod(e, n, edgeToCheck+1, option1, min, start, timeInterval);
									option1 = Move(result, i, e[edgeToCheck].u, j);

									if (numColors1 < min) {
										min = numColors1;
									}

									oneOptionFound = true;
								}

								//If option2 is possible, try it out and 
								if (MoveIsPossible(e, result, e[edgeToCheck].v, i)) {
									int[][] option2 = Move(result, j, e[edgeToCheck].v, i);
									int numColors2 = testBottomUpWorkingMethod(e, n, edgeToCheck+1, option2, min, start, timeInterval);
									option2 = Move(result, i, e[edgeToCheck].v, j);

									if (numColors2 < min) {
										min = numColors2;
									}

									oneOptionFound = true;
								}
							}
						}
					}
				}
				return min;
			}
			//This gets only executed if the edge vertices are not in the same color, 
			//therefore, nothing needs to be done to result
			return testBottomUpWorkingMethod(e, n, edgeToCheck+1, result, min, start, timeInterval);
		}
	}
	
	public static int chromaticNumber(ColEdge[] e, int n, int m, int notUsed, int time) {
		//If empty graph, then we need only 1 color
		if (m == 0) {
			if (DEBUG) {
				System.out.println("There are no edges!");
			}
			return 1;
		}
		//Else if we have a complete graph (maximum number of connections), then we need n colors
		else if (m == ((n-notUsed)*(n-notUsed-1)/2)) {
			if (DEBUG) {
				if (notUsed == 0) {
					System.out.println("Maximum edges, everything inter-connected!");
				}
				else {
					System.out.println("Excluding the vertices not connected at all, everything is inter-connected!");
				}
			}
			return (n-notUsed);
		}
		//Otherwise, it's complicated
		else {
			return BottomUp(e, n, time);
		}
	}
	
	public static int BottomUp (ColEdge[] e, int n, int time) {
		//Start with the maximum size array,
		int[][] result = new int[n-1][n];
		// Note: could be optimized to use limited size, then enlarge if needed
		
		//Initiate all the vertices on the same colour
		for (int i=1; i <= n; i ++) {
			result[0][i-1] = i;
		}
		
		if (DEBUG) System.out.println("Trying to find the chromatic number using Karloid");
		int res = Karloid(e, n, result, time/2);
		
		if (res == -1) {
			if (DEBUG) System.out.println("Interrupted Karloid\n");
			long start = System.currentTimeMillis();			
			try {
				if (DEBUG) System.out.println("Trying to find the chromatic number using BottomUpWorkingMethod");
				res = BottomUpWorkingMethod(e, n, 0, result, n-1, start, time/2);
			}
			catch (StackOverflowError StackOverflow) {
				res = -1;
				if (DEBUG) System.out.println("BottomUpWorkingMethod failed because of a Stackoverflow Error!");
			}
				
			if (res == -1) {
				if (DEBUG) System.out.println("Interrupted BottomUpWorkingMethod too");
				return -1;
			}
			else {
				if (DEBUG) System.out.println("Successfully found the chromatic number using BottomUpWorkingMethod\n");
				return res;
			}
		}
		else {
			if (DEBUG) System.out.println("Successfully found the chromatic number using Karloid\n");
			return res;
		}
	}
	
	/** Chromatic number method
	*/
	public static int Karloid (ColEdge[] e, int n, int[][] result, int time) {
		long start = System.currentTimeMillis();
		int[] moved = new int[n];
		for (int i = 0; i < result.length; i ++) {
			for (int j = 0; j < e.length; j ++) {
				if (((double)((System.currentTimeMillis()-start)/Math.pow(10, 3))) > time) {
					return -1;
				}
				
				if ((ArrayContains(result[i], e[j].u)) && (ArrayContains(result[i], e[j].v))) {
					if (i == 0) {
						Add(result[i+1], e[j].v);
						Erase(result[i], e[j].v);
					}
					else {
						if (ArrayContains(moved, e[j].v)) {
							Add(result[i-2], e[j].v);
							Erase(result[i], e[j].v);
							Erase(moved, e[j].v);
							i = 0;
						}
						else {
							Add(result[i+1], e[j].v);
							Erase(result[i], e[j].v);
							Add(moved, e[j].v);
						}
					}
				}
			}
		}
		
		if (DEBUG) {
			System.out.println("The result is " + Arrays.deepToString(result));
		}
		
		return usedColors(result);
	}
	
	/** 
	This method should separate the two points connected by e[edgeToCheck], if they are in the same result[x] array (that is, they share the same color).
	It should then try each possible separation (moving e[edgeToCheck].u to any other color and moving e[edgeToCheck].v to any other color).
	In case we can't move a point to a given color, we just skip that possibility.
	
	If the two points connected by e[edgeToCheck] are in different colors, it should just call itself to check the next edge, without changing result[][]
	
	Finally, if edgeToCheck = e.length, it should just return the number of colors the result has
	*/
	public static int BottomUpWorkingMethod (ColEdge[] e, int n, int edgeToCheck, int[][] result, int min, long start, int time) {		
		//First, check if the time is up
		if (((double)((System.currentTimeMillis()-start)/Math.pow(10,3))) > time) {
			return -1;
		}
		
		//Normally, this if will only trigger when edgeToCheck == e.length, but for security, I say if edgeToCheck >= e.length
		if (edgeToCheck >= e.length) {
			//Returns the amount of colors of the color assignment (result)
			return usedColors(result);
		}
		else {
			int j = 0;
			//If result[j = 0] contains both values of the edge, (because it is not possible to have two edge points in another color)
			if (ArrayContains(result[j], e[edgeToCheck].u) && ArrayContains(result[j], e[edgeToCheck].v)) {
				//Separate the numbers

				//n = number of colors used until now
				int numCols = usedColors(result);

				//Only try out possible options to continue if you currently have less colors than the current minimum
				if (numCols < min) {
					for (int i = 1; i <= numCols; i ++) {
						boolean oneOptionFound = false;

						//We only try the creation of a new color if there is no possible option to add the item toMove to an existing color
						if ((i < numCols) | (! oneOptionFound)) {
							if (i < result.length) {
								if (MoveIsPossible(e, result, e[edgeToCheck].u, i)) {
									//If option1 here is possible, then try it out
									int[][] option1 = Move(result, j, e[edgeToCheck].u, i);
									int numColors1 = BottomUpWorkingMethod(e, n, edgeToCheck+1, option1, min, start, time);
									option1 = Move(result, i, e[edgeToCheck].u, j);

									if (numColors1 < min) {
										min = numColors1;
									}
									
									oneOptionFound = true;
								}

								//If option2 is possible, try it out and 
								if (MoveIsPossible(e, result, e[edgeToCheck].v, i)) {
									int[][] option2 = Move(result, j, e[edgeToCheck].v, i);
									int numColors2 = BottomUpWorkingMethod(e, n, edgeToCheck+1, option2, min, start, time);
									option2 = Move(result, i, e[edgeToCheck].v, j);

									if (numColors2 < min) {
										min = numColors2;
									}
									
									oneOptionFound = true;
								}
							}
						}
					}
				}
				return min;
			}
			//This gets only executed if the edge vertices are not in the same color, 
			//therefore, nothing needs to be done to result
			return BottomUpWorkingMethod(e, n, edgeToCheck+1, result, min, start, time);
		}
	}
	
	public static int upperBound(ColEdge[] e, int n) {
		//Start with the maximum size array,
		int[][] result = new int[n-1][n];
		// Note: could be optimized to use limited size, then enlarge if needed
		
		//Initiate all the vertices on the same colour
		for (int i=1; i <= n; i ++) {
			result[0][i-1] = i;
		}
		
		return KarloidUpperBound(e, n, result);
	}
	
	/** Upper bound method for the chromatic number
	*/
	public static int KarloidUpperBound (ColEdge[] e, int n, int[][] result) {
		for (int i = 0; i < result.length; i ++) {
			for (int j = 0; j < e.length; j ++) {
				if ((ArrayContains(result[i], e[j].u)) && (ArrayContains(result[i], e[j].v))) {
					Add(result[i+1], e[j].v);
					Erase(result[i], e[j].v);
				}
			}
		}
		return usedColors(result);
	}
	
	/**
	Checks if I can move item toMove from array[origin] to array[destination], that is, without having any element of array[destination] sharing an edge with toMove
	*/
	public static boolean MoveIsPossible (ColEdge[] e, int[][] array, int toMove, int destination) {
		//As I should only need to move points from the 1st color to another one, I can assume that the position
		//of the point is array[0][toMove-1]
		boolean movePossible = true;
		int j = 0;
		
		//If the destination array is not empty, we need to check if we can add toMove in it, otherwise, it is a new color we create
		if (! arrayEmpty(array[destination])) {
			while ((j < e.length) && (movePossible)) {
				//For each edge, if the item to move is one of the two points, check if the other item is in the array I try to move the item to
				if ((e[j].u == toMove) | (e[j].v == toMove)) {
					if ((ArrayContains(array[destination], e[j].u)) | (ArrayContains(array[destination], e[j].v))) {
						movePossible = false;
					}
				}
				j ++;
			}
		}
		
		return movePossible;
	}
	
	/** 
	Should move item toMove from array[origin] to array[destination].
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
	
	public static void Add (int[] array, int element) {
		array[element-1] = element;
	}
	
	public static void Erase (int[] array, int element) {
		array[element-1] = 0;
	}
	
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