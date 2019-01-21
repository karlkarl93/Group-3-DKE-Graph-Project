/*ClusterFactor for vertices = sum of degrees of adjacent vertices
ClusterFactor for edges = size of the intersection of adjacent vertices of the two vertices linked by the edge
ClusterFactor range of all clusters in a graph = the range of vertex cluster factors of all vertices that are linked by non-zero edges
Edges with a clusterFactor of 0 = Edges at the verge of clusters (either not connected to anything or connected to a cluster of connecting two clusters)
Intercluster edges: Edges with ClusterFactor equal to zero and ClusterFactors for both of their vertices that are in the range of cluster factors for all culsters in the graph
*/

public class InterClusterVertexDetector {
    
    public static void main(String[] args) {
        Graph g = ReadGraphV2.readGraph(args);
        System.out.println("Cluster First Colouring");
        clusterVerticesFirstColouring(g);
        
        System.out.println("Intercluster-Vertex First Colouring");
        interClusterVerticesFirstColoring(g);
    }
    
    protected static void clusterVerticesFirstColouring(Graph g) {
        g.resetColouring();
        Vertex[] interClusterVertices = detectInterClusterVertices(g);
        
        // Loop through all vertices and mark the ones that are interclusterVertices
        Vertex[] remainingVertices = new Vertex[g.vertices.length - 1 - interClusterVertices.length];
        int insertedVertices = 0;
        for (int i = 1; i < g.vertices.length; i++) {
            int j = 0;
            boolean isApproved = true;
            while (j < interClusterVertices.length && isApproved) {
                if (g.vertices[i] == interClusterVertices[j]) isApproved = false;
                j++;
            }
            if (isApproved) {
                remainingVertices[insertedVertices] = g.vertices[i];
                insertedVertices ++;
            }
        }
        
        g.blankVertices = remainingVertices;
        g.RLFcoloring();
        System.out.println("Colored vertices: " + g.coloredVertices.length);
        System.out.println("Initial upper bound: " + g.findAssignedColors(g.vertices).length);
        
        g.blankVertices = interClusterVertices;
        g.RLFcoloring();
        System.out.println("Final upper bound: " + g.findAssignedColors(g.vertices).length);
    }
    
    protected static void interClusterVerticesFirstColoring(Graph g) {
        
        g.resetColouring();
        
        // Find the vertices in the graph that do connect clusters and colour them
        g.blankVertices = detectInterClusterVertices(g);
        g.RLFcoloring();
        System.out.println("Colored vertices: " + g.coloredVertices.length);
        System.out.println("Initial upper bound: " + g.findAssignedColors(g.vertices).length);
        
        // Find the vertices that are still blank
        Vertex[] tmpBlankVertices = new Vertex[g.vertices.length];
        int blankVertexCount = 0;
        for (int i = 1; i < g.vertices.length; i++) {
            if (g.vertices[i].color == Vertex.DEFAULT_BLANK_COLOR) {
                tmpBlankVertices[i] = g.vertices[i];
                blankVertexCount ++;
            }
        }
        
        g.coloredVertices = g.blankVertices;
        g.blankVertices = new Vertex[blankVertexCount];
        int j = 0;
        for (int i = 1; i < tmpBlankVertices.length; i++) {
            if (tmpBlankVertices[i] != null) {
                g.blankVertices[j] = tmpBlankVertices[i];
                j++;
            }
        }
        g.RLFcoloring();
        System.out.println("Final upper bound: " + g.findAssignedColors(g.vertices).length);
        
    }
    
	protected static Vertex[] detectInterClusterVertices(Graph graph) {
        
        // Compute the cluster factor for each vertex as the sum of degrees of its adjacent vertices
        int[] vertexClusterFactors = new int[graph.vertices.length];
        for (int i = 1; i < graph.vertices.length; i++) {
            for (int j = 0; j < graph.vertices[i].adjacentVertices.length; j++) {
                vertexClusterFactors[i] += graph.vertices[i].adjacentVertices[j].adjacentVertices.length;
            }
        }
        
        // Compute the cluster factors for the edges and the range of cluster factors of the vertices that are part of clusters.
        int[] edgeClusterFactors = new int[graph.edges.length];
        int minVertexClusterFactor = Integer.MAX_VALUE; // Only when an edge has a cluster index equal to zero its vertices can lower this value
        for (int i = 0; i < graph.edges.length; i++) {
            Vertex u = graph.vertices[graph.edges[i].u];
            Vertex v = graph.vertices[graph.edges[i].v];
            
            // Increment the cluster factor of each edge when its vertices share some adjacent vertices
            for (int j = 0; j < u.adjacentVertices.length; j++) {
                int k = 0;
                boolean isIntersectingVertexIndex = false;
                while (!isIntersectingVertexIndex && k < v.adjacentVertices.length) {
                    if (u.adjacentVertices[j].vertexIndex == v.adjacentVertices[k].vertexIndex) {
                        edgeClusterFactors[i] += 1;
                        isIntersectingVertexIndex = true;
                    }
                    k++;
                }
            }
            
            // If the cluster factor for the current edge is larger than zero it means that it is part of a cluster. The range of cluster factors of the vertices that are part of a cluster is updated accordingly.
            if (edgeClusterFactors[i] > 0) {
                if (vertexClusterFactors[u.vertexIndex] < minVertexClusterFactor) minVertexClusterFactor = vertexClusterFactors[u.vertexIndex];
                
                if (vertexClusterFactors[v.vertexIndex] < minVertexClusterFactor) minVertexClusterFactor = vertexClusterFactors[v.vertexIndex];
                
            }
        }
        
        // Loop again through the cluster factors of edges and select those that are outside of clusters. They have a cluster factor of zero. Identify each of them for which both of its vertices have a cluster factor inside the range of cluster factors for vertices that are INSIDE of a cluster. Collect the references to these vertices in an array
        boolean[] isInterClusterVertex = new boolean[graph.vertices.length];
        int interClusterVertexCount = 0;
        for (int i = 0; i < edgeClusterFactors.length; i++) {
            
            if (edgeClusterFactors[i] == 0) {
                Vertex u = graph.vertices[graph.edges[i].u];
                Vertex v = graph.vertices[graph.edges[i].v];
                
                // Check if the cluster factors of BOTH vertices of the current edge are in the range of  cluster factors of vertices that are inside clusters. Prepare to transfer them into an array.
                if (vertexClusterFactors[u.vertexIndex] > minVertexClusterFactor &&
                    vertexClusterFactors[v.vertexIndex] > minVertexClusterFactor) {
                    if (!isInterClusterVertex[u.vertexIndex]) {
                        isInterClusterVertex[u.vertexIndex] = true;
                        interClusterVertexCount += 1;
                    }
                    if (!isInterClusterVertex[v.vertexIndex]) {
                        isInterClusterVertex[v.vertexIndex] = true;
                        interClusterVertexCount += 1;
                    }
                }
            }
        }
        
        Vertex[] interClusterVertices = new Vertex[interClusterVertexCount];
        for (int i = 1; i < isInterClusterVertex.length; i++) {
            if (isInterClusterVertex[i]) {
                interClusterVertices[interClusterVertexCount-1] = graph.vertices[i];
                interClusterVertexCount --;
            }
        }
        
        return interClusterVertices;
    }
    
    /** One of the upperBound methods
     It starts by having all vertex take a different color, then tries to "reunite" those vertices who are not connected by making a certain vertex give its colour to others
     */
    public static int upperBoundTopDown (Vertex[] vertices) {
        
        // Provide initial unique color to each vertex
        for (int i = 1; i < vertices.length; i ++) {
            System.out.println("Vertex: " + i + " is object: " + vertices[i]);
            if (vertices[i] != null) vertices[i].color = i;
        }
        
        int provisionIndex = 1; // referring to the current vertex that tries to provide its colour to the other vertices
        int numberOfVariableVertices = vertices.length; // referring to the vertices that have not yet provided or inherited their colour
        int upperBound = vertices.length;
        
        // Descend the upper bound
        while (numberOfVariableVertices > 1) { // trying to descend as long as there are vertices that have a variable colour
            System.out.println("This is the vertex " + vertices[provisionIndex]);
            if (!vertices[provisionIndex].isVariable) { provisionIndex ++; } // jumping to the next vertex if the current vertex is frozen
            else {
                
                boolean didProvideColour = false;
                
                // Try to disseminate the colour of the current providing vertex
                for (int candidateIndex = 1; candidateIndex < vertices.length; candidateIndex++) {
                    
                    // Select the vertices that are variable and different from the current providing vertex. They are identified as candidates to inherit the colour of the current providing vertex
                    if (vertices[candidateIndex].isVariable && candidateIndex != provisionIndex) {
                        
                        // Check whether the candidate vertex can be recoloured
                        boolean vertexCanBeRecoloured = true;
                        int edgeIndex = 0;
                        
                        // Disqualify candidate if it edges with the providing vertex
                        while (vertexCanBeRecoloured && edgeIndex < vertices[provisionIndex].adjacentVertices.length) {
                            if (candidateIndex == vertices[provisionIndex].adjacentVertices[edgeIndex].vertexIndex) { vertexCanBeRecoloured = false; }
                            edgeIndex ++;
                        }
                        
                        // Disqualify candidate if it edges with a third vertex that has the same colour as the providing vertex
                        edgeIndex = 0;
                        while (vertexCanBeRecoloured && edgeIndex < vertices[candidateIndex].adjacentVertices.length) {
                            int thirdVertexIndex = vertices[candidateIndex].adjacentVertices[edgeIndex].vertexIndex;
                            if (vertices[thirdVertexIndex].color == vertices[provisionIndex].color) { vertexCanBeRecoloured = false; }
                            edgeIndex ++;
                        }
                        
                        // Recolour the candidate if possible
                        if (vertexCanBeRecoloured) {
                            vertices[candidateIndex].color = vertices[provisionIndex].color;
                            vertices[candidateIndex].isVariable = false;
                            upperBound --;
                            numberOfVariableVertices --;
                            didProvideColour = true;
                        }
                    }
                }
                
                // Freeze the variable that tried to provide its colour
                vertices[provisionIndex].isVariable = false;
                numberOfVariableVertices --;
            }
        }
        
        //System.out.println("Upper bound: " + upperBound);
        return upperBound;
    }
}
