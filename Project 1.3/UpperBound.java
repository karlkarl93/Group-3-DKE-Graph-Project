import java.io.*;
import java.util.*;
import Jama.Matrix;
import Jama.EigenvalueDecomposition;

/*
	NOTE !!!!! If we are to run multiple graphs in one run, then the system of using global "eigenvalues" variables needs to be modified ...
				And we'll probably need to recompute the eigenvalues each time
*/

public class UpperBound {
	protected static EigenvalueDecomposition eigenvalues;
	protected static double maxEigenvalue;
	protected static double minEigenvalue;
	protected static Map<Integer, int[]> connections;
	
	/** The general method that calls the other hidden methods that compute the upper bounds
	
		@param g, the Graph of which we try to compute the upper bound(s)
		
	Methods available:
		- upperBoundMaxDegree
		- upperBoundTopDown
		- upperBoundEigenvalues --- Not working ---
	*/
	public static int upperBound(Graph g, int method) {
		connections = TestGraph.connections;
		if (method == 1) {
			return upperBoundMaxDegree(g);
		} else if (method == 2) {
			return upperBoundTopDown(g);
		} else {
			return upperBoundEigenvalues(g);
		}
	}
	
	/** One of the UpperBound methods --- Not working ---
		Computes the eigenvalues of the adjacency matrix of the Graph (if it has not already been computed, and then)
	*/
	public static int upperBoundEigenvalues (Graph g) {
		if (eigenvalues == null) {
			double[][] adj = new double[g.getN()][g.getN()];
			Edge[] edges = g.getEdges();

			//Construct the adjacency matrix
			for (int i = 0; i < edges.length; i ++) {
				adj[edges[i].u][edges[i].v] ++;
				adj[edges[i].v][edges[i].u] ++;
			}

			Matrix x = new Matrix(adj);

			//Compute its eigenvalues
			EigenvalueDecomposition eigenvalues = x.eig();

			/*
			double maxEigenvalue = eigenvalues[0][0];
			double minEigenvalue = maxEigenvalue;
			for (int i = 1; i < eigenvalues.length; i ++) {
				if (maxEigenvalue < eigenvalues[i]) 
					maxEigenvalue = eigenvalues[i];
				else if (minEigenvalue > eigenvalues[i]) 
					minEigenvalue = eigenvalues[i];
			}
			*/

			lowerBound.eigenvalues = eigenvalues;
			lowerBound.maxEigenvalue = maxEigenvalue;
			lowerBound.minEigenvalue = minEigenvalue;
		}
		
		return (int)(maxEigenvalue + 1);
	}
	
	/** One of the UpperBound methods
		First, constructs a map of connections of the vertices using MapConnections.connections(), 
		and then goes through the "adjacentVertices" of each vertex and keeps track of the maximum number of adjacentVertices encountered
		Finally, returns the final maximum+1 as upperBound
	*/
	public static int upperBoundMaxDegree (Graph g) {
		Vertex[] vertices = g.vertices;
		int max = vertices[1].adjacentVertices.length;
		for (int i = 2; i < vertices.length; i ++) {
			if (max < vertices[i].adjacentVertices.length) {
				max = vertices[i].adjacentVertices.length;
			}
		}
		
		return max + 1;
	}
		
	/** One of the upperBound methods
		It starts by having all vertex take a different color, then tries to "reunite" those vertices who are not connected by making a certain vertex give its colour to others
	*/
	public static int upperBoundTopDown (Graph g) {
		// Compiles an array of vertex objects that store index, edges and colour information
		Vertex[] vertices = g.vertices;
		
		for (int i = 1; i < vertices.length; i ++) {
			vertices[i].color = i;
		}
		
		int provisionIndex = 1; // referring to the current vertex that tries to provide its colour to the other vertices
		int numberOfVariableVertices = vertices.length; // referring to the vertices that have not yet provided or inherited their colour
		int upperBound = vertices.length;
		
		// Descend the upper bound
		while (numberOfVariableVertices > 1) { // trying to descend as long as there are vertices that have a variable colour
			
			if (!vertices[provisionIndex].isVariable) { provisionIndex ++; } // jumping to the next vertex if the current vertex is frozen
			else {
				
				boolean didProvideColour = false;
				
				// Try to disseminate the colour of the current providing vertex
				for (int candidateIndex = 1; candidateIndex < vertices.length; candidateIndex++) {
					
					// Select the vertices that are variable and different from the current providing vertex. They are identified as candidates to inherit the colour of the current providing vertex
					if (vertices[candidateIndex].isVariable && candidateIndex != provisionIndex) {
						
						// Check whether the candidate vertex can be recoloured
						boolean vertexCanBeRecoloured = true;
						int edgeIndex = 0;

						// Disqualify candidate if it edges with the providing vertex
						while (vertexCanBeRecoloured && edgeIndex < vertices[provisionIndex].adjacentVertices.length) {
							if (candidateIndex == vertices[provisionIndex].adjacentVertices[edgeIndex].vertexIndex) { vertexCanBeRecoloured = false; }
							edgeIndex ++;
						}
						
						// Disqualify candidate if it edges with a third vertex that has the same colour as the providing vertex
						edgeIndex = 0;
						while (vertexCanBeRecoloured && edgeIndex < vertices[candidateIndex].adjacentVertices.length) {
							int thirdVertexIndex = vertices[candidateIndex].adjacentVertices[edgeIndex].vertexIndex;
							if (vertices[thirdVertexIndex].color == vertices[provisionIndex].color) { vertexCanBeRecoloured = false; }
							edgeIndex ++;
						}
						
						// Recolour the candidate if possible
						if (vertexCanBeRecoloured) {
							vertices[candidateIndex].color = vertices[provisionIndex].color;
							vertices[candidateIndex].isVariable = false;
							upperBound --;
							numberOfVariableVertices --;
							didProvideColour = true;
						}
					}
				}
				
				// Freeze the variable that tried to provide its colour
				vertices[provisionIndex].isVariable = false;
				numberOfVariableVertices --;
			}
		}
		
		//System.out.println("Upper bound: " + upperBound);
		return upperBound;
	}
	
	private static int[] append(int newElement, int[] oldArray) {
		if (oldArray.length == 0) {
			int[] tmp = {newElement};
			return tmp;
		}
		int[] newArray = new int[oldArray.length+1];
		for (int i = 0; i < oldArray.length; i++) {
			newArray[i] = oldArray[i];
		}
		newArray[newArray.length-1] = newElement;
		return newArray;
	}
}