public class Vertex {
	protected double x, y, z = 0;
    protected final static int DEFAULT_BLANK_COLOR = -1;
    protected int color = DEFAULT_BLANK_COLOR;
    protected int[] colorOptions = {0};
    protected Vertex[] adjacentVertices = new Vertex[0];
	protected int vertexIndex;
	
	/** Constructor specifying the number of this vertex
	*/
	public Vertex(int x) {
		vertexIndex = x;
	}
	
	/** Add vertex Vertex to adjacentVertices[] and copy the old contents of adjactentVertices
	*/
    protected void appendAdjacentVertex(Vertex vertex) {
        Vertex[] newArray = new Vertex[adjacentVertices.length + 1];
        for (int i = 0; i < adjacentVertices.length; i++) {
            newArray[i] = adjacentVertices[i];
        }
        newArray[adjacentVertices.length] = vertex;
        adjacentVertices = newArray;
    }
    
	protected Vertex[] addAdjacentVertices(Vertex[] vertices, int[][] edgeData) {
        for (int i = 0; i < vertices.length; i++) {
            Vertex[] adjacentVertices = new Vertex[edgeData[i].length];
            for (int j = 0; j < adjacentVertices.length; j++) {
                adjacentVertices[j] = vertices[edgeData[i][j]];
            }
            
            vertices[i].adjacentVertices = adjacentVertices;
        }
        
        return vertices;
    }
	
	/** For debugging purposes
	*/
	public String toString() {
		return "Vertex #" + vertexIndex;
	}
}