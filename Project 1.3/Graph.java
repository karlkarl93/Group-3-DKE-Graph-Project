import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.Random;

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
	protected boolean[] seen;
	protected int chromaticNumber;
	protected Vertex[] vertices;
	protected Vertex[] coloredVertices = new Vertex[0];
	protected Vertex[] blankVertices;
	protected int[] colors = {0};

    protected static void main(String[] args) {
        Graph myGraph = ReadGraphV2.readGraph(args);
        
        myGraph.generateRandomColouring(myGraph.blankVertices);
        System.out.println("Colors used during random colouring: " + myGraph.colors.length);
        
        myGraph.localSearch(myGraph.vertices, myGraph.colors);
        System.out.println("Colors used after local search: " + myGraph.colors.length);
    }
    
    protected void generateRandomColouring(Vertex[] vertices) {
        // 1. Loop through the array of vertices
        for (int i = 0; i < vertices.length; i++) {
            // 2.
            findColorOptionsForSingleVertex(vertices[i]);
            if (vertices[i].colorOptions.length == 0) vertices[i].color = colors.length;
            else {
                Random random = new Random();
                vertices[i].color =  vertices[i].colorOptions[random.nextInt(vertices[i].colorOptions.length)];
            }
        }
    }
    
    protected boolean localSearch(Vertex[] vertices, int[] colors) {
        int[] sortedColors = sortColorsByProminence(vertices);
        Vertex[] targetVertices = findVerticesWithSpecificColor(vertices, sortedColors[sortedColors.length-1]);
        tryToColorVerticesInAlternativeColor(targetVertices);
        return true;
    }
    
    /**
     * This method is used to find the colors assigned to vertices passed as parameter.
     * @param vertices The vertices that should be searched through.
     */
    protected int[] findAssignedColors(Vertex[] vertices) {
        // 1. Construct an array holding as many elements as the maximum number of colors possible for the graph.
        int[] colors = new int[vertices.length];
        
        // 2. Loop through the vertices
        int insertedColors = 0;
        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i] != null) {
                // 3. Insert the color of the current vertex into the colors array if it has not been inserted yet
                if (!Auxilaries.containsInt(colors, vertices[i].color)) {
                    colors[insertedColors] = vertices[i].color;
                    insertedColors++;
                }
            }
        }
        
        // 4. Remove zeros that occur when there are less colors than vertices and return the result
        return Auxilaries.removeZeros(colors);
    }
    
    /**
     * This method sorts the colors by their frequency in the graph.
     * @param vertices The vertices for which the colors whould be sorted, e.g. all vertices of the graph or the adjacent vertices surrounding a particular vertex or interest.
     * @param the colors that should be sorted, e.g. all the colors of the graph or only those that are featured among a particular vertex's adjacent vertices.
     * @return the colors in sorted order
     */
    protected int[] sortColorsByFrequency(Vertex[] vertices, int[] colors) {
        
        // 1. Construct an array holding as many zeros as there are vertices in the graph (not counting the null object at index 0 of the vertices array). Each value in the array will describe the frequency of a color in the graph. Such a value is indexed by the the number that identifies a color.
        int[] colorRanks = new int[vertices.length-1];
        
        // 2. Loop through vertices and increment the rank of the vertex's color when it is in the colors array passed as argument
        for (int i = 1; i < vertices.length; i++) {
            int currentColor = vertices[i].color;
            // Loop through the colors array and increment the rank of the color when the current vertex has one such color
            if (vertices[i].color != Vertex.DEFAULT_BLANK_COLOR) {
                for (int j = 0; j < colors.length; j++) {
                    if (currentColor == colors[j]) colorRanks[vertices[i].color]++;
                }
            }
        }
        // 3. Loop through the color rank array and sort the indices by value in descending order. Note that the indices resemble the numbers by which the colors are identified.
        colorRanks = Auxilaries.sortIndicesByValueInDescendingOrder(colorRanks);
        
        // 4. Remove elements with value equal to zero. They occur when there are less used colors than there are vertices in the graph.
        colorRanks = Auxilaries.removeZeros(colorRanks);
        return colorRanks;
    }
    
    /**
     * This method is used to try to recolor the specified vertices in a different color.
     * @param vertices The vertices that need to be recolored. Note that only colored vertices may be passed to this method.
     */
    protected boolean tryToColorVerticesInAlternativeColor(Vertex[] vertices) {
        // 1. Loop through the vertices
        for (int i = 0; i < vertices.length; i++) {
            // 2. Idenfity the color options for the current vertex
            findColorOptionsForSingleVertex(vertices[i]);
            // 3.1 Handle the case in which the current color is the only possible color for the current vertex
            if (vertices[i].colorOptions.length == 1) {
                // 3.1.1 Unlock a color option for the current vertex by looping through the colors that are assigned to its adjacent vertices. Identify the adjacent vertices that share a color. Start with the group of adjacent vertices that has the rarest color among the adjacent vertices.
                int[] sortedColors = sortColorsByFrequency(vertices[i].adjacentVertices, findAssignedColors(vertices[i].adjacentVertices));
                int unlockedColorOption = Vertex.DEFAULT_BLANK_COLOR;
                int j = sortedColors.length-1;
                while (unlockedColorOption == Vertex.DEFAULT_BLANK_COLOR && j >= 0) {
                    // 3.1.1.1 Search for the rarest color among the current vertex's adjacent vertices
                    Vertex[] adjacentVerticesToBeRecolored = findVerticesWithSpecificColor(vertices[i].adjacentVertices, sortedColors[j]);
                    
                    // 3.1.1.2 Try to recolor these vertices in any color but the color to be avoided
                    boolean shouldTryToUnlockColorOption = true;
                    int k = 0;
                    while (shouldTryToUnlockColorOption && k < adjacentVerticesToBeRecolored.length) {
                        findColorOptionsForSingleVertex(adjacentVerticesToBeRecolored[k]);
                        adjacentVerticesToBeRecolored[k].colorOptions = Auxilaries.removeInt(adjacentVerticesToBeRecolored[k].colorOptions, vertices[i].color); // The color of the vertex of main interest
                        adjacentVerticesToBeRecolored[k].colorOptions = Auxilaries.removeInt(adjacentVerticesToBeRecolored[k].colorOptions, adjacentVerticesToBeRecolored[k].color); // The color of the adjacent vertex itself
                        if (adjacentVerticesToBeRecolored[k].colorOptions.length == 0) shouldTryToUnlockColorOption = false;
                        else if (adjacentVerticesToBeRecolored[k].colorOptions.length > 0) {
                            adjacentVerticesToBeRecolored[k].color = adjacentVerticesToBeRecolored[k].colorOptions[0];
                        }
                        k++; // Move to the next adjacent vertex with the current color
                    }
                    if (shouldTryToUnlockColorOption) unlockedColorOption = sortedColors[j];
                    j--; // Move to the next group of adjacent vertices that have the same color
                }
                
                // Recolor the current vertex if a color option was unlocked for it.
                if (unlockedColorOption != Vertex.DEFAULT_BLANK_COLOR) vertices[i].color = unlockedColorOption;
                else return false; // Return that there was at least one of the vertices passed as parameter that could not be succesfully recolored
            }
            // Handle the case in which the current vertex has more color options than the one currently assigned to it.
            else {
                findColorOptionsForSingleVertex(vertices[i]);
                vertices[i].colorOptions = Auxilaries.removeInt(vertices[i].colorOptions, vertices[i].color);
                vertices[i].color = vertices[i].colorOptions[1];
            }
        }
        // Return that all of the vertices passed as parameter were succesfully recolored
        return true;
    }
    
    /**
     * This method is used to find the vertices that have the specified color.
     * @param vertices The vertices that should be searched through.
     * @param color The color that a vertex has to be assigned to to be identified by this search.
     * @return An array holding the references to the vertices with the specified color.
     */
    protected Vertex[] findVerticesWithSpecificColor(Vertex[] vertices, int color) {
        // 1. Construct an array that can hold references to the colored vertices
        Vertex[] coloredVertices = new Vertex[0];
        
        // 2. Loop through the vertices passed as parameter and append its reference to the array of colored vertices if it has the specified color
        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i] != null) {
                if (vertices[i].color == color) appendVertex(coloredVertices, vertices[i]);
            }
        }
        
        // 3. Return the array holding the references to the colored vertices
        return coloredVertices;
    }
                           
	/** Constructor for Graph objects, with two parameters:
		@param numVertices, the number of vertices in the graph
		@param e[], the array of edges, with contains all the edge connections of the graph
		@param notUsedVertices, the number of vertices that have no edge at all connecting them to another Vertex
	*/
	public Graph (Edge[] edges, int numVertices, int numEdges, int notUsedVertices, boolean[] usedVertices) {
		this.edges = edges;
		n = numVertices;
		m = numEdges;
		notUsed = notUsedVertices;
		seen = usedVertices;

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
	}

	/** Colors the graph using LDO and RLF
	*/
	public void RLFcoloring() {
		int color = 0;
		while (blankVertices.length != 0){
			//Compute mostConnectedVertices using findVerticesWithHighestDegree
			Vertex[] mostConnectedVertices = findVerticesWithHighestDegree(blankVertices);
			for(int i =0; i<mostConnectedVertices.length; i++) {
				if(mostConnectedVertices[i].color == Vertex.DEFAULT_BLANK_COLOR) {
					colorRLF(mostConnectedVertices[i], color);
					color ++;
				}
			}
		}
	}

	/** Auxiliary method for RLFcoloring
	*/
	public void colorRLF(Vertex v, int colorSelected) {
    	setColor(v, colorSelected);
      	for(int i=0; i<blankVertices.length; i++) {
        	if (checkIfColorIsLegalForVertex(blankVertices[i], colorSelected)) {
            	setColor(blankVertices[i],colorSelected);
          	}
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

		for (int i = 0; i < vertices.length; i++) {
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

		//If there is at least one color that we can still use again, then
		if (length > 0) {
			//Reduce result to its actual length
			int[] newResult = new int[length];
			for (int i = 0; i < length; i ++) {
				newResult[i] = result[i];
			}
			result = newResult;
		}
		else {						//Otherwise, return an array with just the first color that has not been used yet
			//Create a new array of size 1 to contain the first color that is not used so far
			int[] newResult = new int[1];
			boolean colorFound = true;
			int i = 0;
			while (i < vertices.length && colorFound) {
				int j = 0;
				colorFound = false;
				while (j < colors.length && !colorFound) {
					if (colors[j] == i) {
						colorFound = true;
					}
					else {
						j ++;
					}
				}

				if (colorFound) i++;
			}

			if (i < vertices.length) newResult[0] = i;
			result = newResult;
		}
		
		return result;
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

				/*
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
				*/

		Vertex maxDegree = vertices[1];
		Vertex[] mostConnectedVertices = new Vertex[10];
		mostConnectedVertices[0] = maxDegree;
		int length = 1;
		for(int i=2; i<vertices.length; i++) {
			if(maxDegree.adjacentVertices.length < vertices[i].adjacentVertices.length) {
				mostConnectedVertices = new Vertex[10];
				mostConnectedVertices[0] = vertices[i];
				length = 1;
				maxDegree = vertices[i];
			}
			else if (maxDegree.adjacentVertices.length == vertices[i].adjacentVertices.length) {
				if(length < mostConnectedVertices.length) {
					Vertex[] newmCV = new Vertex[length*2];
					for(int j = 0; j < mostConnectedVertices.length; j++) {
						newmCV[j] = mostConnectedVertices[j];
					}
					mostConnectedVertices = newmCV;
				}
				
				mostConnectedVertices[length++]=vertices[i];
			}
		}
		
		Vertex[] result = new Vertex[length];
		for(int i = 0; i < mostConnectedVertices.length; i++) {
			result[i] = mostConnectedVertices[i];
		}

        return result;
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
