public class TestColorProminence {
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

        // Color some vertices
        graph.setColor(graph.vertices[1], 2);
        graph.setColor(graph.vertices[2], 2);
        graph.setColor(graph.vertices[3], 1);
        
        // Request the most prominent colors
        int[] sortedColors = graph.sortColorsByProminence(graph.vertices);
        
        // Print the result
        printArray(sortedColors);
    }
    
    public static void printArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
        System.out.println();
    }
}
