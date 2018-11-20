import java.util.Scanner;

class Edge {
	public int u;
	public int v;

	public Edge(int newU, int newV) {
		u = newU;
		v = newV;
	}
}

public class Graph {
	public static void main(String [] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the number of vertices");

		int n = in.nextInt();
		System.out.println("Enter the number of edges");
		int m = in.nextInt();

		Edge[] edges = generateRandomGraph(n, m);
		}
/**
The fist methos,called generateRandomGraph has two parameters:
- vertexCount which produces the number of vertices entered by the user
- edgeCount which produces the number of edges entered by the user
*/
	public static Edge[] generateRandomGraph(int vertexCount, int edgeCount) {
		Edge[] e = new Edge[edgeCount];

		// Generate random edges by using Math.random()
		int generatedEdges = 0;
		while (generatedEdges < edgeCount) {
			int randomU = (int)(vertexCount * Math.random()) +1;
			int randomV = (int)(vertexCount * Math.random()) +1;
			Edge currentEdge = new Edge(randomU, randomV);
			Edge currentEdgeReversed = new Edge(randomV, randomU);
/**
boolean isUniqueEdge checks if we do not have multiple edges with the same connections
*/
			boolean isUniqueEdge = true;
			for (int i = 0; i < generatedEdges; i++) {
				if (e[i].equals(currentEdge) || e[i].equals(currentEdgeReversed)) {
					isUniqueEdge = false;
				}

			}

			if (currentEdge.u != currentEdge.v && isUniqueEdge) {
				e[generatedEdges] = currentEdge;
				generatedEdges ++;
				System.out.println("Edge "+generatedEdges+": "+currentEdge.u + ","+currentEdge.v);

			}
		}

		return e;
	}
}
