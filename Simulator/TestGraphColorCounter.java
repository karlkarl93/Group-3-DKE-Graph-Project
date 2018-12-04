public class TestGraphColorCounter {
    /**
     This is a tester class. It is designed to show how a graph object containing can be represented in memory. Additionally this tester class shows how an instance of a graph can analyse its current state to provide two types of hints for the user. One of these two hints tells the user which vertex has the higehst number of edges and the other tells the user which vertex has the fewest number of color options.
     @author Tim Dick
     @version %I%, %G%
     */
    
    public static void main(String[] args) {
        
        // Create an array of 3 edges connecting 4 vertices in total
        final Edge[] EDGES = {new Edge(1, 2), new Edge(2, 3), new Edge(2, 4)};
        final int VERTEXCOUNT = 4;
        
        // Create a graph based on edges and vertices
        Graph graph = new Graph(EDGES, VERTEXCOUNT);
        
        // Set the color of a vertex
        graph.setColor(graph.vertices[1], 1);
        // Print the colors in current trial
        System.out.print("Colors in current trial:");
        printArray(graph.colors);
        
        
        // Set another vertex's color and observe colors in current trial
        graph.setColor(graph.vertices[2], 1);
        // Print the colors in current trial
        System.out.print("Colors in current trial:");
        printArray(graph.colors);
        
        // Recolor the second vertex
        graph.setColor(graph.vertices[2], 2);
        // Print the colors in current trial
        System.out.print("Colors in current trial:");
        printArray(graph.colors);
        
        // Recolor the second vertex
        graph.setColor(graph.vertices[2], 1);
        // Print the colors in current trial
        System.out.print("Colors in current trial:");
        printArray(graph.colors);
        
    }
    
    public static void printArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.println(" " + array[i]);
        }
        System.out.println();
    }
}
