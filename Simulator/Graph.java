
class Graph {
    boolean debug = true;
    /**
     Graph is a class that structures all information about the graph, including the vertices, the edges, their positions when displayed on the screen etc.. This class also includes methods to find an estimate of the chromatic number of the graph through heuristical processing.
     @author Tim Dick
     @author Others
     @version %I%,%G%
     */
    protected Vertex[] vertices;
    protected Edge[] edges;
    protected Vertex[] coloredVertices = new Vertex[0];
    protected Vertex[] blankVertices;
    protected int[] colors = {0};
    
    protected static Graph DEFAULT_GRAPH() {
        return new Graph(Edge.DEFAULT_EDGE_ARRAY(), 18); // There are 18 default edges
    }
    
    protected Graph(Edge[] edges, int vertexCount) {
        this.edges = edges;
        // 1. Initialise the arrays with references to vertices
        // 1.1 Initialise the vertices arrays such that one null object will remain at index 0
        blankVertices = new Vertex[vertexCount + 1];
        vertices = new Vertex[vertexCount + 1];
        
        
        // 1.2 Create default vertices
        vertices = new Vertex[vertexCount+1];
        for (int i = 1; i < vertices.length; i ++) {
            vertices[i] = new Vertex();
        }
        
        blankVertices = vertices;
        
        // 1.3 Loop through the edges and add the corresponding vertices the arrays of adjacent vertices for each vertex.
        for (int i = 0; i < edges.length ; i++) {
            vertices[edges[i].u].appendAdjacentVertex(vertices[edges[i].v]);
            vertices[edges[i].v].appendAdjacentVertex(vertices[edges[i].u]);
        }
    }
    
    
    
    protected boolean checkIfColorIsLegalForVertex(Vertex vertex, int color) {
        boolean isLegalColor = true;
        int i = 0;
        while (isLegalColor && i < vertex.adjacentVertices.length) {
            if (vertex.adjacentVertices[i].color == color) isLegalColor = false;
            i++;
        }
        return isLegalColor;
    }
    
    protected int[] sortColorsByProminence(Vertex[] vertices) {
        // Loop through vertices and increment the rank of a color when a vertex has this color
        int[] colorRanks = new int[vertices.length];
        
        for (int i = 1; i < vertices.length; i++) {
            if (vertices[i].color != Vertex.DEFAULT_BLANK_COLOR) colorRanks[vertices[i].color]++;
        }
        // Loop through the color rank array and sort the indices by value in descending order
        colorRanks = Auxilaries.sortIntsInDescendingOrder(colorRanks);
        
        // Remove zeros trailing zeros that occur when their are less colors than vertices
        colorRanks = Auxilaries.removeZeros(colorRanks);
        return colorRanks;
    }
    
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
        
        // 4. Append its reference to blankVertices
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
    }
    
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
    
    protected Vertex[] findVerticesWithFewestColorOptions(Vertex[] vertices) {
        if (debug) System.out.println("findVerticesWithFewestColorOptions()");
        /** This method finds the vertices that have the fewest color options among the vertices that have been passed as a parameter. As a side effect it mutates the color options of these vertices. The method should be used as part of a heuristical search that tries to identify the best vertices to color next. The calling method can decrease processing time by passing a subset of vertices rather than the entire array of vertices of the graph. For example, it can pass the vertices with the highest degree.
         @param vertices the vertices that should be checked for their color options
         @return the indices of the vertices that have the fewest color options
         */
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
    private int findColorOptionsForSingleVertex(Vertex vertex) {
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
        if (debug == true) System.out.println("removeVertex()");
        
        Vertex[] newVertices = new Vertex[oldVertices.length-1];
        boolean vertexWasFound = false;
        for (int i = 0; i < oldVertices.length; i++) {
            if (oldVertices[i] != vertex && !vertexWasFound) newVertices[i] = oldVertices[i];
            else if (oldVertices[i] != vertex && vertexWasFound) newVertices[i-1] = oldVertices[i];
            if (oldVertices[i] == vertex) vertexWasFound = true;
        }
        
        return newVertices;
    }
}
