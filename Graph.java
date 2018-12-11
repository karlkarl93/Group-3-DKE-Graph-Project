import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

public class Graph {
	/**
     Graph is a class that structures all information about the graph, including the vertices, the edges, their positions when displayed on the screen etc.. This class also includes methods to find an estimate of the chromatic number of the graph through heuristical processing.
     @author Tim Dick
	 @author Valentin Ringlet
     @author Others
     @version %I%,%G%
    */
	boolean debug = false;
	protected int n;
	protected int m;
	protected Edge[] edges;
	protected int notUsed;
	protected int chromaticNumber;
	protected Vertex[] vertices;
    protected Vertex[] coloredVertices = new Vertex[0];
    protected Vertex[] blankVertices;
    protected int[] colors = new int[0];
	
	/** Constructor for Graph objects, with two parameters:
		@param numVertices, the number of vertices in the graph
		@param e[], the array of edges, with contains all the edge connections of the graph
		@param notUsedVertices, the number of vertices that have no edge at all connecting them to another Vertex
	*/
	public Graph (Edge[] edges, int numVertices, int numEdges, int notUsedVertices) {
		this.edges = edges;
		n = numVertices;
		
		//Intialize the vertex arrays such that they start at index 1 (index 0 holds a null value)
		blankVertices = new Vertex[numVertices];
		vertices = new Vertex[numVertices + 1];
		
		//Fill the arrays with the default vertices
		for (int i = 1; i < vertices.length; i ++) {
			vertices[i] = new Vertex(i);
		}
		//Save the same into blankVertices
		for (int i = 0; i < blankVertices.length; i ++) {
			blankVertices[i] = vertices[i+1];
		}
		//System.arraycopy(vertices, 1, blankVertices, 0, blankVertices.length);
		
		//Save all the connections between vertices
		for (int i = 0; i < edges.length ; i++) {
			vertices[edges[i].u].appendAdjacentVertex(vertices[edges[i].v]);
			vertices[edges[i].v].appendAdjacentVertex(vertices[edges[i].u]);
		}
		
		m = numEdges;
		notUsed = notUsedVertices;
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
        int[] colorRanks = new int[TestGraph.colors.length];
        
        for (int i = 0; i < vertices.length; i++) {
			System.out.println(vertices[i] + ", color: " + vertices[i].color);
            if (vertices[i].color != Vertex.DEFAULT_BLANK_COLOR) colorRanks[vertices[i].color]++;
        }
		
		// Loop through the color rank array and retrieve the indexes of the colors used by the User so far in decreasing order of number of vertices colored (in that color)
        colorRanks = Auxilaries.sortIntsInDescendingOrder(colorRanks, colors);
        
		int[] result = new int[colorRanks.length];
		int length = 0;
		//Check if these colors are all usable (that is, that there exists at least 1 vertex that can be colored in that color)
		for (int i = 0; i < colorRanks.length; i ++) {
			boolean colorIsUsable = false;
			
			//Check if there are still vertices that can be colored in these colors
			int j = 0;
			while (j < blankVertices.length && !colorIsUsable) {
				if (checkIfColorIsLegalForVertex(blankVertices[j], colorRanks[i])) {
					colorIsUsable = true;
				}
				j++;
			}
			
			if (colorIsUsable) {
				result[length++] = colorRanks[i];
			}
		}
		
		//Reduce result to its actual length
		int[] newResult = new int[length];
		for (int i = 0; i < length; i ++) {
			newResult[i] = result[i];
		}
		result = newResult;
		
        return result;
    }
	
	protected void setColor(Vertex vertex, int color) {
        if (debug) System.out.println("SetColor() on vertex " + vertex + " to color " + color);
        //System.out.println("color == Vertex.DEFAULT_BLANK_COLOR: " + (color == Vertex.DEFAULT_BLANK_COLOR));
		
		
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
    
	/** Coloring a blank vertex
		Update blankVertices and 
	*/
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
	
	/** Searches 
	*/
	protected Vertex[] findVerticesWithHighestDegree(Vertex[] vertices) {
        if (debug) System.out.println("findVerticesWithHighestDegree()");
        
		// 1.1 Loop through the vertices once to find the highest degree
        int highestDegree = 0;
        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i] != null && vertices[i].adjacentVertices.length > highestDegree) {
				highestDegree = vertices[i].adjacentVertices.length;
			}
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
	
	
	
	// -------------------------------------------------------------
	
	/** Getter for the number of vertices
		@return n, the number of vertices of the graph
	*/
	public int getN () {
		return n;
	}
	
	/** Setter for the number of vertices
		Sets the private variable n to its parameter:
		@param newN, the new number of vertices to be saved into the variable n
	*/
	public void setN (int newN) {
		n = newN;
	}
	
	/** Getter for the number of edges
		@return m, the number of edges of the graph
	*/
	public int getM () {
		return m;
	}
	
	/** Setter for the number of edges
		Sets the private variable m to its parameter:
		@param newM, the new number of edges to be saved into the variable m
	*/
	public void setM (int newM) {
		m = newM;
	}
	
	/** Getter for the edge-array
		@return edges, the array containing all the edges of the graph
	*/
	public Edge[] getEdges () {
		return edges;
	}
	
	/** Getter for the number of vertices without connections
		@return notUsed, the number of vertices that have no edge connecting them to any other vertex
	*/
	public int getNotUsed () {
		return notUsed;
	}
	
	/** Setter for the number of vertices without connection at all
		@param newNotUsed, the new value for notUsed
	*/
	public void setNotUsed (int newNotUsed) {
		notUsed = newNotUsed;
	}
	
	/** Getter for the chromatic Number
		@return the current value of chromaticNumber
	*/
	public int getChromaticNumber () {
		return chromaticNumber;
	}
	
	/** Setter for the chromatic number.
		@param chromaNum, the new value of variable chromaticNumber
	*/
	public void setChromaticNumber (int chromaNum) {
		chromaticNumber = chromaNum;
	}
	
	/** Provide a nice way to display a Graph
		@return a String displaying the Graph's characteristics
	*/
	public String toString() {
		return getClass().getName() + "[n= " + n + "][m= " + m + "][notUsed= " + notUsed + "]";
	}
}

