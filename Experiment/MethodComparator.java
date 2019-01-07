import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

/**
 MethodComparator is a class whose main method is used to compare four methods of the graph class. Each of these provides hints for vertex and color.
 @author Tim Dick
 @version 1.0
 */

class MethodComparator {
    
    /**
        * This method should be called with the path for the output file as argument. It will save a file that contains the analysis of four different hint algorithms for 10 randomly generated graphs.
     */
    public static void main(String[] args) {
        // Compare the hints obtained through heuristic, the heuristic search and the exhaustive search
        try { compareHints(args[0]); } catch (IOException e) { System.out.println(e.getMessage()); }
    }
    
    private static void compareHints(String filePath) throws IOException {
        // Construct output file
        FileWriter fileWriter = new FileWriter(filePath + ".txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        
        int graphCount = 10;
        
        int[][] calls = new int[4][graphCount];
        int[][] vertexResets = new int[4][graphCount];
        int[][] colorResets = new int[4][graphCount];
        int[][] vertexHints = new int[4][graphCount];
        int[][] colorHints = new int[4][graphCount];
        int[][] chromaticNumbers = new int[4][graphCount];
        
        for (int i = 0; i < graphCount; i++) {
            // Construct random graph
            Graph graph = RandomGraphGenerator.generate(15,10); //GraphReader.readGraph(args);
            
            
            // Run the mere heuristic
            vertexHints[0][i] = graph.findVerticesWithHighestDegree(graph.blankVertices)[0].identifier;
            colorHints[0][i] = 1;
            
            // Run search with heuristic pruning
            graph.search("heuristic pruning",1, graph.blankVertices.length);
            calls[1][i] = graph.calls;
            vertexResets[1][i] = graph.vertexResets;
            colorResets[1][i] = graph.colorResets;
            vertexHints[1][i] = graph.hintVertex.identifier;
            colorHints[1][i] = graph.hintColor;
            chromaticNumbers[1][i] = graph.chromaticNumber;
            
            // Reset statistics
            graph.hintColor = 0;
            graph.hintVertex = null;
            graph.vertexResets = 0;
            graph.colorResets = 0;
            graph.calls = 0;
            graph.chromaticNumber = graph.vertices.length -1;
            
            // Run search with heuristic ordering
            graph.search("heuristic ordering",1, graph.blankVertices.length);
            calls[2][i] = graph.calls;
            vertexResets[2][i] = graph.vertexResets;
            colorResets[2][i] = graph.colorResets;
            vertexHints[2][i] = graph.hintVertex.identifier;
            colorHints[2][i] = graph.hintColor;
            chromaticNumbers[2][i] = graph.chromaticNumber;
            
            // Reset statistics
            graph.hintColor = 0;
            graph.hintVertex = null;
            graph.vertexResets = 0;
            graph.colorResets = 0;
            graph.calls = 0;
            graph.chromaticNumber = graph.vertices.length -1;
            
            // Run the exhaustive search
            graph.search("exhaustive", 1, graph.blankVertices.length);
            calls[3][i] = graph.calls;
            vertexResets[3][i] = graph.vertexResets;
            colorResets[3][i] = graph.colorResets;
            vertexHints[3][i] = graph.hintVertex.identifier;
            colorHints[3][i] = graph.hintColor;
            chromaticNumbers[3][i] = graph.chromaticNumber;
            
        }
        
        ////////////////////////////////////////// Print Graph numbers
        int count = 1;
        printWriter.print("Graph:\t\t\t");
        for (int i = 0; i < graphCount; i++) {
            printWriter.print(count); count++;
            printWriter.print("\t\t");
        }
        
        printWriter.println(); printWriter.println(); printWriter.println();
        
        ////////////////////////////////////////// Print mere heuristic
        printWriter.println("Mere Heuristic");
        printWriter.println();
        
        // Print Vertex Hints
        printWriter.print("Vertex Hints:\t\t");
        for (int i = 0; i < graphCount; i++) {
            printWriter.print(vertexHints[0][i]);
            printWriter.print("\t\t");
        }
        printWriter.println();
        
        // Print Color Hints
        printWriter.print("Color Hints:\t\t");
        for (int i = 0; i < graphCount; i++) {
            printWriter.print(colorHints[0][i]);
            printWriter.print("\t\t");
        }
        printWriter.println(); printWriter.println(); printWriter.println();
        
        ////////////////////////////////////////// Print sarch with heuristic pruning
        printWriter.println("Search with heuristic pruning");
        
        // Print calls
        printWriter.print("Calls:\t\t\t");
        for (int i = 0; i < graphCount; i++) {
            printWriter.print(calls[1][i]);
            printWriter.print("\t\t");
        }
        printWriter.println();
        
        // Print Vertex Resets
        printWriter.print("Vertex Resets:\t\t");
        for (int i = 0; i < graphCount; i++) {
            printWriter.print(vertexResets[1][i]);
            printWriter.print("\t\t");
        }
        printWriter.println();
        
        // Print Color Resets
        printWriter.print("Color Resets:\t\t");
        for (int i = 0; i < graphCount; i++) {
            printWriter.print(colorResets[1][i]);
            printWriter.print("\t\t");
        }
        printWriter.println();
        
        // Print Vertex Hints
        printWriter.print("Vertex Hints:\t\t");
        for (int i = 0; i < graphCount; i++) {
            printWriter.print(vertexHints[1][i]);
            printWriter.print("\t\t");
        }
        printWriter.println();
        
        // Print Color Hints
        printWriter.print("Color Hints:\t\t");
        for (int i = 0; i < graphCount; i++) {
            printWriter.print(colorHints[1][i]);
            printWriter.print("\t\t");
        }
        printWriter.println();
        
        // Print Color Hints
        printWriter.print("Chromatic Number:\t");
        for (int i = 0; i < graphCount; i++) {
            printWriter.print(chromaticNumbers[1][i]);
            printWriter.print("\t\t");
        }
        printWriter.println();
        
        ////////////////////////////////////////// Print sarch with heuristic ordering
        
        printWriter.println(); printWriter.println(); printWriter.println();
        printWriter.println("Search with heuristic ordering");
        
        // Print calls
        printWriter.print("Calls:\t\t\t");
        for (int i = 0; i < graphCount; i++) {
            printWriter.print(calls[2][i]);
            printWriter.print("\t\t");
        }
        printWriter.println();
        
        // Print Vertex Resets
        printWriter.print("Vertex Resets:\t\t");
        for (int i = 0; i < graphCount; i++) {
            printWriter.print(vertexResets[2][i]);
            printWriter.print("\t\t");
        }
        printWriter.println();
        
        // Print Color Resets
        printWriter.print("Color Resets:\t\t");
        for (int i = 0; i < graphCount; i++) {
            printWriter.print(colorResets[2][i]);
            printWriter.print("\t\t");
        }
        printWriter.println();
        
        // Print Vertex Hints
        printWriter.print("Vertex Hints:\t\t");
        for (int i = 0; i < graphCount; i++) {
            printWriter.print(vertexHints[2][i]);
            printWriter.print("\t\t");
        }
        printWriter.println();
        
        // Print Color Hints
        printWriter.print("Color Hints:\t\t");
        for (int i = 0; i < graphCount; i++) {
            printWriter.print(colorHints[2][i]);
            printWriter.print("\t\t");
        }
        printWriter.println();
        
        // Print Color Hints
        printWriter.print("Chromatic Number:\t");
        for (int i = 0; i < graphCount; i++) {
            printWriter.print(chromaticNumbers[2][i]);
            printWriter.print("\t\t");
        }
        printWriter.println();
        
        ////////////////////////////////////////// Print exhaustive search
        
        printWriter.println(); printWriter.println(); printWriter.println();
        printWriter.println("Exhaustive");
        
        // Print calls
        printWriter.print("Calls:\t\t\t");
        for (int i = 0; i < graphCount; i++) {
            printWriter.print(calls[3][i]);
            printWriter.print("\t\t");
        }
        printWriter.println();
        
        // Print Vertex Resets
        printWriter.print("Vertex Resets:\t\t");
        for (int i = 0; i < graphCount; i++) {
            printWriter.print(vertexResets[3][i]);
            printWriter.print("\t\t");
        }
        printWriter.println();
        
        // Print Color Resets
        printWriter.print("Color Resets:\t\t");
        for (int i = 0; i < graphCount; i++) {
            printWriter.print(colorResets[3][i]);
            printWriter.print("\t\t");
        }
        printWriter.println();
        
        // Print Vertex Hints
        printWriter.print("Vertex Hints:\t\t");
        for (int i = 0; i < graphCount; i++) {
            printWriter.print(vertexHints[3][i]);
            printWriter.print("\t\t");
        }
        printWriter.println();
        
        // Print Color Hints
        printWriter.print("Color Hints:\t\t");
        for (int i = 0; i < graphCount; i++) {
            printWriter.print(colorHints[3][i]);
            printWriter.print("\t\t");
        }
        printWriter.println();
        
        // Print Color Hints
        printWriter.print("Chromatic Number:\t");
        for (int i = 0; i < graphCount; i++) {
            printWriter.print(chromaticNumbers[3][i]);
            printWriter.print("\t\t");
        }
        printWriter.println();
        
        // Close the file
        fileWriter.close();
    }
    
}
