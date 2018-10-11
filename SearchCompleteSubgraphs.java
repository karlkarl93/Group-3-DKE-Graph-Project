import java.io.*;
import java.util.*;

		class ColEdge
			{
			int u;
			int v;
			}
		
public class SearchCompleteSubgraphs
		{
		
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
			
			//n = amount of vertices
			//m = amount of edges
			//e[x].u and e[x].v are the two points connected by vertice #x
			
			Map<Integer, int[]> c = DictConnections(e, n);
			
			System.out.println("Connections: ");
			for (int i = 1; i <=n; i ++) {
				System.out.printf("%d: %s \n", i, Arrays.toString(c.get(i)));
			}
			
			int[][] subgraphs = findCompleteSubgraphs(c, n);
			int maxSize = subgraphs[0].length;
			for (int i = 1; i < subgraphs.length; i ++) {
				if (subgraphs[i].length > maxSize) {
					maxSize = subgraphs[i].length;
				}
			}
			
			System.out.println("The set of subgraphs is: " + Arrays.deepToString(subgraphs));
			System.out.println("\nThe lower bound is " + maxSize);
		}
	
	/** 
	Constructs a "dictionary" such that the keys are the vertices and their values are arrays containing all the points they are connected to
	*/
	public static Map<Integer, int[]> DictConnections (ColEdge[] e, int n) {
		Map<Integer, int[]> connections = new HashMap<Integer,int[]>();
		for(int i = 0; i < e.length; i ++) {
			if (connections.containsKey(e[i].u)) {
				connections.put(e[i].u, Add(connections.get(e[i].u), e[i].v));
			}
			else {
				connections.put(e[i].u, Add(new int[n-1], e[i].v));
			}
			
			if (connections.containsKey(e[i].v)) {
				connections.put(e[i].v, Add(connections.get(e[i].v), e[i].u));
			}
			else {
				connections.put(e[i].v, Add(new int[n-1], e[i].u));
			}
		}
		
		return connections;
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
	
	public static int[][] findCompleteSubgraphs (Map<Integer, int[]> connections, int n) {
		//Before everything else, initialize the output
		int[][] result = new int[n][n-1];
		
		//First, I create an array containing all the pairs of points I need to check
		int[][] toCheck = new int[n*(n-1)/2][2];
		int count = 0;
		for (int i = 1; i <= n; i ++) {
			for (int j = i+1; i <= n; i ++) {
				toCheck[count] = Add(Add(new int[2], i), j);
			}
		}
		
		for(int x = 0; x < toCheck.length; x ++) {
			if ((toCheck[x][0] != 0) && (toCheck[x][1] != 0)) {
				//Add the result of the search to the final result we will return
				AddArray(result, SearchIntersection(toCheck[x], toCheck, connections, n), n);
			}
		}
		
		return result;
	}
	
	public static int[] SearchIntersection (int[] pairsToCheck, int[][] toCheck, Map<Integer,int[]> connections, int n) {
		int[] completeSubGraph = new int[n-1];
		
		int x = pairsToCheck[0];
		int y = pairsToCheck[1];
		int[] inter = Intersect(connections.get(x), connections.get(y));
		
		for (int i = 2; i < toCheck.length; i ++) {
			inter = Intersect(inter, connections.get(toCheck[i]));
		}
		
		if (inter.length > 1) {	
			//Then, we may have a bigger complete subgraph, and we call SearchIntersection again with all
			int[] newToCheck = new int[pairsToCheck.length+1];
			for (int i = 0; i < pairsToCheck.length; i ++) {
				newToCheck[i] = pairsToCheck[i];
			}
			
			newToCheck[newToCheck.length-1] = inter[0];
			
			//We search an intersection for the previous points, plus a point of their intersection
			completeSubGraph = SearchIntersection(newToCheck, toCheck, connections, n);
		} 
		else if (inter.length == 1) {
			//If there is just 1 intersection to the existing points, 
			//Then we have a complete subgraph with the points of toCheck + the point of inter
			for (int i = 0; i < pairsToCheck.length; i ++) {
				completeSubGraph[i] = pairsToCheck[i];
			}
			completeSubGraph[pairsToCheck.length + 1] = inter[0];
			
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
		}
		
		return completeSubGraph;
	}
	
	/** 
	Searches if there is an empty array in big.
	If there is an empty, replace it with small.
	
	Otherwise, enlarge big (I double its size), 
		then add small in the first entry of the new enlarged part
	*/
	public static int[][] AddArray (int[][] big, int[] small, int n) {
		//Searches for an empty array, if it finds it, replace it with small
		int i = 0;
		boolean notFoundEmpty = true;
		while ((i < big.length) && notFoundEmpty) {
			if (arrayEmpty(big[i])) {
				big[i] = small;
				notFoundEmpty = false;
			}
			else {
				i ++;
			}
		}
		
		//Otherwise, if we did not find an empty array, we need to make big larger and add small to the gained size
		if (notFoundEmpty) {
			//Copy of big into newBig (newBig is twice as big as "big")
			int[][] newBig = new int[big.length][n-1];
			for (i = 0; i < big.length; i ++) {
				newBig[i] = big[i];
			}
			
			//Add the new entry,
			newBig[big.length] = small;
			//And then assign it back to big
			big = newBig;
		}
		
		return big;
	}
	
	
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
		
		return result;
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
}