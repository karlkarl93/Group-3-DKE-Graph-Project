import java.io.*;
import java.util.*;

public class MapConnections {
	protected static int n;							//the number of vertices
	protected static int[] numAdjacentVertices;		//the number of adjacent vertices of each vertex
	
	/** Constructs a "dictionary" such that 
			- the keys are the vertices and 
			- the values are arrays containing the indexes of the vertices they are connected to
		
		@param g, the Graph of which to compute the connections
	*/
	public static Map<Integer, int[]> connections (Graph g) {
		Edge[] e = g.getEdges();
		n = g.getN();
		
		Map<Integer, int[]> connections = new HashMap<Integer,int[]>();
		numAdjacentVertices = new int[n+1];
		for (int i = 1; i <= n; i ++) {
			connections.put(i, new int[n-1]);
		}
		
		for(int i = 0; i < e.length; i ++) {
			connections.put(e[i].u, Add(e[i].u, connections.get(e[i].u), e[i].v));
			numAdjacentVertices[e[i].u] ++;
			
			connections.put(e[i].v, Add(e[i].v, connections.get(e[i].v), e[i].u));
			numAdjacentVertices[e[i].v] ++;
		}
		
		for (int i = 1; i <= n; i ++) {
			int[] c = connections.get(i);
			
			//Create a new array that has the real length of the adjacentVertices of this vertex (when disregarding the 0's)
			int[] newC = new int[numAdjacentVertices[i]];
			for (int j = 0; j < newC.length; j ++) {
				//And add it the real vertice-indexes (of this vertex's adjacentVertices)
				newC[j] = c[j];
			}
			
			//Finally, set the connections of this vertex to its correct size
			connections.put(i, newC);
		}
		
		return connections;
	}
	
	/** Shows the connections of a certain Graph
		
		@param c, the map containing the connections of the different vertices of the Graph
	*/
	public static void showConnections (Map<Integer, int[]> c) {
		for (int i = 1; i <= n; i ++) {
			System.out.printf("%d: %s \n", i, Arrays.toString(c.get(i)));
		}
	}
	
	/** Auxiliary method for connections() method
		
		@param vertexIndex, the index of the vertex we want to modify the adjacentVertices array of
		@param array, the current adjacentVertices array of the vertex
		@param element, the new vertex-index to be added to the adjacentVertices array
		
		@return the new array with element added to it
	*/
	public static int[] Add (int vertexIndex, int[] array, int element) {
		array[numAdjacentVertices[vertexIndex]] = element;
 		
		return array;
 	}
}