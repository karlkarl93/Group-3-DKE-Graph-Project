
/**
 * This class can be used to test the heuristic search that entails pruning.
 @author Tim Dick
 @version 1.0
 */

class HeuristicSearch {
    /**
     * This method should be called with the path to the graph that should be tested as argument. It prints the chromatic number for this graph.
     */
    public static void main(String[] args) {
        Graph graph = GraphReader.readGraph(args);
        graph.search("heuristic pruning", 1, graph.blankVertices.length);
        System.out.println("Chromatic Number: " + graph.chromaticNumber);
    }
}
