/**
 * Graph is a class that structures all information about the graph, including the vertices, the edges and their color status. This class also includes methods to find the chromatic number of the graph through heuristical processing.
 * @author Tim Dick
 * @version 1.0
 */

class Graph {
    
    // Fields
    boolean debug = false;
    protected Vertex[] vertices;
    protected Edge[] edges;
    protected Vertex[] coloredVertices = new Vertex[0];
    protected Vertex[] blankVertices;
    protected int[] colors = {0}; // always includes 0, even when all vertices are colored
    protected int hintColor;
    protected Vertex hintVertex;
    int vertexResets = 0;
    int colorResets = 0;
    int calls = 0;
    int chromaticNumber;
    
    /**
     * This method is used to construct a graph object from an array of edges and the number of vertices
     * @param edges The edges used to construct the graph
     * @param vertexCount the number of vertices used to construct the graph
     */
    protected Graph(Edge[] edges, int vertexCount) {
        this.edges = edges;
        // 1. Initialise the arrays with references to vertices
        // 1.1 Initialise the vertices arrays such that one null object will remain at index 0
        blankVertices = new Vertex[vertexCount];
        vertices = new Vertex[vertexCount + 1];
        chromaticNumber = vertexCount;
        
        // 1.2 Create default vertices
        vertices = new Vertex[vertexCount+1];
        for (int i = 1; i < vertices.length; i ++) {
            vertices[i] = new Vertex(i);
        }
        
        for (int i = 0; i < blankVertices.length; i ++) {
            blankVertices[i] = vertices[i+1];
        }
        
        
        // 1.3 Loop through the edges and add the corresponding vertices the arrays of adjacent vertices for each vertex.
        for (int i = 0; i < edges.length ; i++) {
            vertices[edges[i].u].appendAdjacentVertex(vertices[edges[i].v]);
            vertices[edges[i].v].appendAdjacentVertex(vertices[edges[i].u]);
        }
    }
    
    
    /**
     * This method is used to search through the graph to find a color configuration that uses the least number of colors possible. When being called on a blank graph it computes the chromatic number. When being called on a partially colored graph it computes the best possible option, given the current state of the graph. The graph uses backtracking to prune unneccesary branches of the search tree.
     * @param type The type of search that should be performed. <p>When this string is set to heuristic ordering the algorithm orders the branches of the search tree heuristically. For each level of the tree it will then process the remaining blank vertices in order of descending degree (number of edges a vertex has). This makes sense because the more edges a vertex has the more constrains can be put on it when its edging vertices are colored. Coloring it first counteracts this process. Vertices are furthermore ordered according to the number of color options that are available. The vertices with fewest color options are tried out first. This makes sense because it might not be possible to color these vertices a few steps down the saerch tree. If the algorithm would wait longer the adjacent vertices of the curretn vertex might be colored in a way that does forces the current vertex to be colored with a novel color. Similarly, at each level of the search tree the colors that are tried out for a given vertex are processed in descending order of prominence. This heuristic might lead to greedy colouring from time to time.
         <p> When the type parameter is set to heuristic pruning the search space will be heavily pruned. For each level of the search tree only the blank vertices are processed that have the highest degree. Similarly, only the colors are used that are the most prominent.
         <p> When the type is set to exhaustive the search will go through all vertices and all color options in a random order.
     * @param stride This parameter keeps track of the level of the search tree. It should always be set to 1 by the calling method.
     * @param limit This parameter keeps that of the limit of colors that the current branch may not exceed. If it would exceed this limit it will prune the current branch.
     * @return returns the smallest number of colors used during the search.
    */
    protected int search(String type, int stride, int limit) {
        calls++;
        if (blankVertices.length == 0) {
            if (colors.length < chromaticNumber) chromaticNumber = colors.length;
            return colors.length;
        }
        else {
            Vertex[] candidateVertices = {};
            if (type.equals("heuristic ordering"))  candidateVertices = sortVerticesByDegree(blankVertices);
            else if (type.equals("heuristic pruning")) candidateVertices = findVerticesWithHighestDegree(blankVertices);
            else if (type.equals("exhaustive")) candidateVertices = blankVertices;
          
            int i = 0;
            
            while (colors.length -1 < limit && i < candidateVertices.length) {
                findColorOptionsForSingleVertex(candidateVertices[i]);
                int[] candidateColors = {0};
                if (type.equals("heuristic ordering")) candidateColors = sortColorsByProminence(vertices, candidateVertices[i].colorOptions);
                else if (type.equals("heuristic pruning")) {
                        int[] tmp = sortColorsByProminence(vertices, candidateVertices[i].colorOptions);
                        if (tmp.length > 1) {
                            int[] tmp2 = {tmp[0]};
                            candidateColors = tmp2;
                        } else candidateColors = tmp;
                    }
                else if (type.equals("exhaustive")) candidateColors = candidateVertices[i].colorOptions;
                
                candidateColors = Auxilaries.appendInt(candidateVertices[i].colorOptions, colors.length);
                
                int j = 0;
                while (colors.length -1 < limit && j < candidateColors.length) {
                    setColor(candidateVertices[i], candidateColors[j]);
                    int colorsUsed = search(type, stride + 1, limit);
                    if (stride == 1 && colorsUsed < limit) {
                        hintVertex = candidateVertices[i]; hintColor = candidateColors[j]; vertexResets++; colorResets++;}
                    if (colorsUsed < limit) {
                        limit = colorsUsed;
                    }
                    setColor(candidateVertices[i], Vertex.DEFAULT_BLANK_COLOR);
                    
                    j++;
                }
                
                i++;
            }
            return limit;
        }
    }

    /**
     * This method sorts vertices by their degree, i.e. the number of edges they have
     * @param vertices The vertices that should be sorted
     * @return The vertices in sorted order
     */
    protected Vertex[] sortVerticesByDegree(Vertex[] vertices) {
        Vertex[] sortedVertices = new Vertex[vertices.length];
        int sortitionIndex = 0;
        while (sortitionIndex < sortedVertices.length) {
            Vertex[] verticesWithHighestDegree = findVerticesWithHighestDegree(vertices);
            for (int i = 0; i < verticesWithHighestDegree.length; i++) {
                sortedVertices[sortitionIndex] = verticesWithHighestDegree[i];
                vertices = removeVertex(vertices, verticesWithHighestDegree[i]);
                sortitionIndex++;
            }
        }
        return sortedVertices;
    }
    
    /**
     * This method sorts the colors by their prominence
     * @param vertices The vertices for which the colors whould be sorted
     * @param the colors that should be sorted
     * @return the colors in sorted order
     */
    protected int[] sortColorsByProminence(Vertex[] vertices, int[] colors) {
        // Loop through vertices and increment the rank of the vertex's color when it is in the colors array
        int[] colorRanks = new int[vertices.length];
        
        for (int i = 1; i < vertices.length; i++) {
            int currentColor = vertices[i].color;
            // Loop through the colors array and increment the rank of the color when the current vertex has one such color
            if (vertices[i].color != Vertex.DEFAULT_BLANK_COLOR) {
                for (int j = 0; j < colors.length; j++) {
                    if (currentColor == colors[j]) colorRanks[vertices[i].color]++;
                }
            }
        }
        // Loop through the color rank array and sort the indices by value in descending order
        colorRanks = Auxilaries.sortIndicesByValueInDescendingOrder(colorRanks);
        
        // Remove trailing zeros that occur when their are less colors than vertices
        colorRanks = Auxilaries.removeZeros(colorRanks);
        return colorRanks;
    }
    
    /**
     * This method should be used to color vertices. It ensures that the arrays of blank vertices and colored vertices (field variables of this class) are kept uptodate.
     * @param vertex The vertex to be colored
     * @param color The color to be assigned
     */
    protected void setColor(Vertex vertex, int color) {
        if (debug) System.out.println("SetColor()");
        
        // 0. Check validity of input
        if (vertex.color == color) return;
        
        // 1. Assign the color to the vertex and update the coloredVertices and blankVertices arrays accordingly
        
        // 1.1 Handle the case in which the vertex was blank and is now being assigned a real color.
        if (vertex.color == Vertex.DEFAULT_BLANK_COLOR && color != Vertex.DEFAULT_BLANK_COLOR) {
            colorBlankVertex(vertex, color);
        }
        
        // 1.2 Handle the case in which the vertex was colored before and is now being assigned the default blank color
        else if (vertex.color != Vertex.DEFAULT_BLANK_COLOR && color == Vertex.DEFAULT_BLANK_COLOR) {
            resetToDefaultColor(vertex);
        }
        
        // 1.3 Handle the case in which the vertex switches from one color to another
        else if (vertex.color != Vertex.DEFAULT_BLANK_COLOR && color != Vertex.DEFAULT_BLANK_COLOR) {
            recolor(vertex, color);
        }
        
        // 1.4 Else if the vertex was blank before and is being assigned the default blank color just leave it as it is
        
    }
    
    private void recolor(Vertex vertex, int color) {
        if (debug) System.out.println("recolor()");
        
        // 1. If no other vertex has this color remove it from colors
        int count = 0; int i = 0;
        while (count < 2 && i < coloredVertices.length) {
            
            if (coloredVertices[i].color == vertex.color) count ++;
            i++;
        }
        if (count == 1) colors = Auxilaries.removeInt(colors, vertex.color);
        
        // 2. Color the vertex
        vertex.color = color;
        
        
        // 3. Add the new color to the array if it does not already exist
        if (!Auxilaries.containsInt(colors, vertex.color)) colors =  Auxilaries.appendInt(colors, color);
    }
    
    private void resetToDefaultColor(Vertex vertex) {
        if (debug) System.out.println("resetToDefaultColor()");
        
        // 1. If no other vertex has this color remove it from the colors array
        int count = 0; int i = 0;
        while (count < 2 && i < coloredVertices.length) {
            
            if (coloredVertices[i].color == vertex.color) count ++;
            i++;
        }
        if (count == 1) colors = Auxilaries.removeInt(colors, vertex.color);
        
        // 2. Reset the vertex's color to the default color
        vertex.color = Vertex.DEFAULT_BLANK_COLOR;
        
        // 3. Remove the vertex's reference from coloredVertices
        coloredVertices = removeVertex(coloredVertices, vertex);
        
        // 4. Insert the color 0 at index 0 in the colors array when this vertex was the first to be reset to default blank color
        if (blankVertices.length == 0) colors = Auxilaries.insertIntAtFirstIndex(colors, 0);
        
        // 5. Append its reference to blankVertices
        blankVertices = appendVertex(blankVertices, vertex);
        
        
    }
    
    private void colorBlankVertex(Vertex vertex, int color) {
        if (debug) System.out.println("colorBlankVertex()");
        // 1. Color the vertex
        vertex.color = color;
        
        // 2. Remove the vertex's reference from blankVertices
        blankVertices = removeVertex(blankVertices, vertex);
        
        // 3. Append its reference to coloredVertices
        coloredVertices = appendVertex(coloredVertices, vertex);
        
        // 4. If the color has not been used before append it to the colors array
        if (!Auxilaries.containsInt(colors, vertex.color)) colors =  Auxilaries.appendInt(colors, color);
        
        // 5. If this vertex was the last blank vertex, remove the color 0 from the colors array
        if (blankVertices.length == 0) colors = Auxilaries.removeInt(colors, 0);
    }
    
    /**
        * This method searches for the vertices with highest degree.
        * @param vertices The array of vertices that should be searched through.
        * @return The vertices that have the highest degree. Will contain only one vertex when it is the only one with this many edges.
     
     */
    protected Vertex[] findVerticesWithHighestDegree(Vertex[] vertices) {
        if (debug) System.out.println("findVerticesWithHighestDegree()");
        // 1.1 Loop through the vertices once to find the highest degree
        int highestDegree = 0;
        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i] != null && vertices[i].adjacentVertices.length > highestDegree) highestDegree = vertices[i].adjacentVertices.length;
        }
        // 1.2 Loop through vertices again and retrieve the ones with a degree as high as the highest degree found in the array
        Vertex[] mostConnectedVertices = new Vertex[0];
        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i] != null && vertices[i].adjacentVertices.length == highestDegree) mostConnectedVertices = appendVertex(mostConnectedVertices, vertices[i]);
        }
        
        return mostConnectedVertices;
    }
    
    /** This method finds the vertices that have the fewest color options among the vertices that have been passed as a parameter. As a side effect it mutates the color options of these vertices. The method should be used as part of a heuristical search that tries to identify the best vertices to color next. The calling method can decrease processing time by passing a subset of vertices rather than the entire array of vertices of the graph. For example, it can pass the vertices with the highest degree.
     @param vertices the vertices that should be checked for their color options
     @return the indices of the vertices that have the fewest color options
     */
    protected Vertex[] findVerticesWithFewestColorOptions(Vertex[] vertices) {
        if (debug) System.out.println("findVerticesWithFewestColorOptions()");

        // 1. Loop through the array of vertices passed as argument and save the smallest number of color options found
        int fewestColorOptions = colors.length; //
        for (int i = 1; i < vertices.length; i++) {
            int numberOfColorOptions = findColorOptionsForSingleVertex(vertices[i]);
            if (numberOfColorOptions < fewestColorOptions) fewestColorOptions = numberOfColorOptions;
        }
        
        // 2. Loop through the array of vertices again. This time record the indices of vertices that have as few color options as specified by the 'fewestColorOptions' variable
        Vertex[] verticesWithFewestColorOptions = new Vertex[0];
        for (int i = 1; i < vertices.length; i++) {
            if (vertices[i].colorOptions.length == fewestColorOptions) {
                verticesWithFewestColorOptions = appendVertex(verticesWithFewestColorOptions, vertices[i]);
            }
        }
        
        return verticesWithFewestColorOptions;
    }
    
    /**
     This method computes the color options for a specified vertex. The method mutates the 'colorOptions' parameter of the specified vertex which allows the calling method to access the color options via the corresponging getter. The method returns 'true' if color options have been found and 'false' otherwise. It should be used in either of the two following contexts. <p>
     As the first context, consider a calling method public boolean color(Vertex vertex, int color) that tries to color a vertex in the specified color and returns true if the process was successful. The method may only color the vertex in the specified color if no adjacent vertex already has this color. The current method can be used to check whether the color is among the color options available for the specified vertex.<p>
     As the second context, consider a calling method public int[] findIndicesOfVerticesWithFewestColorOptions(Vertex[] vertices, int[] colors) that is part of the heuristical search for the chromatic number of a graph. The calling method can implement the current method in order to loop through the specified array of vertices and return the indices of vertices with fewest color options.
     @param vertex the vertex for which color options should be computed
     @return whether or not color options have been found for the specified vertex
     */
    public int findColorOptionsForSingleVertex(Vertex vertex) {
        if (debug) System.out.println("findColorOptionsForSingleVertex()");
        // Eliminate the color option for the default color
        vertex.colorOptions = colors;
        if (Auxilaries.containsInt(colors, 0)) {
            vertex.colorOptions = Auxilaries.removeInt(colors,0);
        } else {
            vertex.colorOptions = colors;
        }
        // Loop through the current vertex's adjacent vertices for as long as there are color options to eliminate.
        int i = 0;
        while (vertex.colorOptions.length > 0 && i < vertex.adjacentVertices.length) {
            
            // Loop through the color options for the current vertex
            boolean didRemoveColorOption = false;
            int j = 0;
            while (!didRemoveColorOption && j < vertex.colorOptions.length) {
                // Eliminate color option when occupied by the current adjacent vertex
                int[] colorOptions = vertex.colorOptions;
                if (colorOptions[j] == vertex.adjacentVertices[i].color) {
                    vertex.colorOptions = Auxilaries.removeInt(colorOptions, colorOptions[j]);
                    didRemoveColorOption = true;
                }
                // Go to the next color option
                j++;
            }
            
            // Go to the next vertex
            i++;
        }
        
        return vertex.colorOptions.length;
    }
    
    private Vertex[] appendVertex(Vertex[] oldVertices, Vertex vertex) {
        if (debug == true) System.out.println("appendVertex()");
        
        Vertex[] newVertices = new Vertex[oldVertices.length + 1];
        for (int i = 0; i < oldVertices.length; i++) { newVertices[i] = oldVertices[i]; }
        newVertices[oldVertices.length] = vertex;
        
        return newVertices;
    }
    
    public Vertex[] removeVertex(Vertex[] oldVertices, Vertex vertex) {
        if (debug) System.out.println("removeVertex() " + vertex);
        
        Vertex[] newVertices = new Vertex[oldVertices.length-1];
        boolean vertexWasFound = false;
        for (int i = 0; i < oldVertices.length; i++) {
            if (oldVertices[i] == vertex) vertexWasFound = true;
            if (oldVertices[i] != vertex && !vertexWasFound) newVertices[i] = oldVertices[i];
            else if (oldVertices[i] != vertex && vertexWasFound) newVertices[i-1] = oldVertices[i];
        }
        
        return newVertices;
    }
}
