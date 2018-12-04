public class TestVertexDegreeHint {
    /**
     This is a tester class. It is designed to show how a graph object containing can be represented in memory. Additionally this tester class shows how an instance of a graph can analyse its current state to provide two types of hints for the user. One of these two hints tells the user which vertex has the higehst number of edges and the other tells the user which vertex has the fewest number of color options.
     @author Tim Dick
     @version %I%, %G%
     */
    
    public static void main(String[] args) {
        
        // Create an array of 3 edges connecting 4 vertices in total
        final Edge[] EDGES = {new Edge(1, 2), new Edge(2, 3), new Edge(3, 4)};
        final int VERTEXCOUNT = 4;
        
        // Create a graph based on edges and vertices
        Graph graph = new Graph(EDGES, VERTEXCOUNT);

        // Request to find the vertex with highest degree
        Vertex[] topVertices = graph.findVerticesWithHighestDegree(graph.vertices);
        
        // Print the result
        printArray(topVertices);
    }
    
    public static void printArray(Vertex[] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].adjacentVertices.length; j++) {
                System.out.println(array[i].adjacentVertices[j].toString());
            }
            System.out.println();
        }
        System.out.println();
    }
}
