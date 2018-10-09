import java.io.*;
import java.util.*;

		class ColEdge
			{
			int u;
			int v;
			}

public class BottomUp {
	public final static boolean DEBUG = true;
		
		public final static String COMMENT = "//";
		
		public static void main( String args[] )
			{
			if( args.length < 1 )
				{
				System.out.println("Error! No filename specified.");
				System.exit(0);
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

			for( int x=1; x<=n; x++ )
				{
				if( seen[x] == false )
					{
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
			
			int chromaNumber = chromaticNumber(e, n, m);
			
			System.out.println("The chromatic number of Karloid is " + chromaNumber);
			
		}
	
	public static int chromaticNumber(ColEdge[] e, int n, int m) {
		//If empty graph, then we need only 1 color
		if (m == 0) {
			if (DEBUG) {
				System.out.println("There are no edges!");
			}
			return 1;
		}
		//Else if complete graph (maximum number of connections), then we need n colors
		else if (m == (n*(n-1)/2)) {
			if (DEBUG) {
				System.out.println("Maximum edges, everything inter-connected!");
			}
			return n;
		}
		//Otherwise, it's complicated
		else {
			return BottomUp(e, n, m);
		}
	}
	
	public static int BottomUp (ColEdge[] e, int n, int m) {
		//Start with the maximum size array,
		int[][] result = new int[n-1][n];
		// Note: could be optimized to use limited size, then enlarge if needed
		
		//Initiate all the vertices on the same colour
		for (int i=1; i <= n; i ++) {
			result[0][i-1] = i;
		}
		
		return Karloid(e, n, m, result);
	}
	
	/** Chromatic number method
	*/
	public static int Karloid (ColEdge[] e, int n, int m, int[][] result) {
		int[] moved = new int[n];
		for (int i = 0; i < result.length; i ++) {
			for (int j = 0; j < e.length; j ++) {
				if ((ArrayContains(result[i], e[j].u)) && (ArrayContains(result[i], e[j].v))) {
					if (i == 0) {
						result[i+1] = Add(result[i+1], e[j].v);
						result[i] = Erase(result[i], e[j].v);
					}
					else {
						if (ArrayContains(moved, e[j].v)) {
							result[0] = Add(result[0], e[j].v);
							result[i] = Erase(result[i], e[j].v);
							moved = Erase(moved, e[j].v);
							i = 0;
						}
						else {
							result[i+1] = Add(result[i+1], e[j].v);
							result[i] = Erase(result[i], e[j].v);
							moved = Add(moved, e[j].v);
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
	
	/*
	n = number of points (vertices)
	m = number of edges (connections)
	e[0].u = 1
	e[0].v = 2
	
	public static int BottomUpWorkingMethod (ColEdge[] e, int n, int m, int edgeToCheck, int[][] result) {
		//Need to separate the numbers that are connected by e[edgeToCheck]
		
		//Loop through the colors, if they are not filled with 0's (meaning they have no vertices)
		int j = 0;
		//The boolean represents the fact if we have found a case where we need to do something because of the current edge
		boolean noEdgeCase = true
		while ((j < result.length) && (! arrayEmpty(result[j])) && (noEdgeCase)) {
			//If they contain both values of the edge,
			if (ArrayContains(result[j], e[edgeToCheck].u) && ArrayContains(result[j], e[edgeToCheck].v)) {
				//Separate the numbers
				
				//n = number of colors used until now
				int n = usedColors(result);
				int min = -1;
				for (int i = 0; i < n; i ++) {
					int[][] option1 = Move(result[][], j, e[edgeToCheck].u, j+i+1);
					int[][] option2 = Move(result[][], j, e[edgeToCheck].v, j+i+1);
					
					int numColors1 = BottomUpWorkingMethod(e, n, m, edgeToCheck+1, option1);
					int numColors2 = BottomUpWorkingMethod(e, n, m, edgeToCheck+1, option2);
					
					if (numColors1 < min) {
						min = numColors1;
					}
					if (numColors2 < min) {
						min = numColors2;
					}
				}
				
				//Then, end the while-loop because we found the case where we need to take an action because of the edge
				noEdgeCase = false
			}
			else {
				j ++;
			}
		}
		
		
		
		return result;
	}
	*/
	
	/** Upper bound method for the chromatic number
	*/
	public static int KarloidUpperBound (ColEdge[] e, int n, int m, int[][] result) {
		for (int i = 0; i < result.length; i ++) {
			for (int j = 0; j < e.length; j ++) {
				if ((ArrayContains(result[i], e[j].u)) && (ArrayContains(result[i], e[j].v))) {
					result[i+1] = Add(result[i+1], e[j].v);
					result[i] = Erase(result[i], e[j].v);
				}
			}
		}
		
		System.out.println("The result is " + Arrays.deepToString(result));
		
		return usedColors(result);
	}
	
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
	
	public static int[] Erase (int[] array, int element) {
		int i = 0;
		boolean notErased = true;
		while ((i < array.length) && (notErased)) {
			if (array[i] == element) {
				array[i] = 0;
				notErased = false;
			}
			else {
				i ++;
			}
		}
		
		return array;
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