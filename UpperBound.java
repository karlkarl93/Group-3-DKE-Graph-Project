import java.io.*;
import java.util.*;


public class UpperBound {
    
    public final static boolean DEBUG = false;
    
    public final static String COMMENT = "//";
    
    private static Vertex[] vertices = new Vertex[0];
    
    private static int currentGraph = 0;
    
    public static void main(String[] args) {
        
        System.out.println("Graph   Shuffle     Lower Bound");
        
        if (args.length > 0) {
            vertices = readGraph(args);
            String bar = "";
            int lowestUpperBound = findUpperBound();
            for (int j = 1; j < lowestUpperBound; j++) {
                bar += "◼︎◼︎";
            }
            System.out.println("        " + "Yes" + "         " + bar + "\n");
        } else {
            
            for (int i = 1; i < 21; i++) {
                currentGraph = i;
                String fileName = "Graph_" + i +".txt";
                String[] array = {fileName};
                vertices = readGraph(array);
                
                
                int lowestUpperBound = vertices.length;
                lowestUpperBound = findUpperBound();
                
                String bar = "";
                
                for (int j = 1; j < lowestUpperBound; j++) {
                    bar += "◼︎◼︎";
                }
                
                System.out.println("        " + "Yes" + "         " + bar + "\n");
                
            }
        }
        
    }
    

    // Try to descend with varying vertices as first providers for colour
    public static int findUpperBound() {
        int lowestUpperBound = vertices.length;
        
        // Loop through all vertices and set them each of them as the first providing vertex for descending towards the lowest upper bound
        for (int startingProvisionIndex = 1; startingProvisionIndex < vertices.length; startingProvisionIndex++) {
            int currentUpperBound = descend(startingProvisionIndex);
            if (currentUpperBound < lowestUpperBound) {
                lowestUpperBound = currentUpperBound;
            }
            
            for (int i = 1; i < vertices.length; i++) {
                vertices[i].isVariable = true;
            }
        }
        
        return lowestUpperBound;
    }
    
    // Try to descend, starting with the vertex identified by the starting provision index
    public static int descend(int initialProvisionIndex) { // will have values from 1...vertices.count, e.g. 5
        
        int lowestUpperBound = vertices.length;
        
        // should go from 5 to 23
        for (int i = initialProvisionIndex; i < vertices.length; i++) {
            int currentUpperBound = disseminate(i, lowestUpperBound);
            if (currentUpperBound < lowestUpperBound) { lowestUpperBound = currentUpperBound; }
        }
        
        // then should go from 1 to 4
        for (int i = 1; i < initialProvisionIndex-1; i++) {
            int currentUpperBound = disseminate(i, lowestUpperBound);
            if (currentUpperBound < lowestUpperBound) { lowestUpperBound = currentUpperBound; }
        }
        
        String bar = "";
        if (initialProvisionIndex == 1) {
            for (int i = 1; i < lowestUpperBound; i++) {
                bar += "◼︎◼︎";
            }
            if (currentGraph < 10) {
                System.out.println(currentGraph + "       " + "No          " + bar);
            } else {
                System.out.println(currentGraph + "       " + "No         " + bar);
            }
        }
        
        return lowestUpperBound;
    }
    
    
    // Try to disseminate the colour of the vertex indexed by the provision index
    public static int disseminate(int provisionIndex, int currentUpperBound) {
        for (int candidateIndex = 1; candidateIndex < vertices.length; candidateIndex++) {
            
            // Select the vertices that are variable and different from the current providing vertex. They are identified as candidates to inherit the colour of the current providing vertex
            if (vertices[candidateIndex].isVariable && candidateIndex != provisionIndex) {
                
                // Check whether the candidate vertex can be recoloured
                boolean vertexCanBeRecoloured = true;
                int edgeIndex = 0;
                
                // Disqualify candidate if it edges with the providing vertex
                while (vertexCanBeRecoloured && edgeIndex < vertices[provisionIndex].edges.length) {
                    if (candidateIndex == vertices[provisionIndex].edges[edgeIndex]) { vertexCanBeRecoloured = false; }
                    edgeIndex ++;
                }
                
                // Disqualify candidate if it edges with a third vertex that has the same colour as the providing vertex
                edgeIndex = 0;
                while (vertexCanBeRecoloured && edgeIndex < vertices[candidateIndex].edges.length) {
                    int thirdVertexIndex = vertices[candidateIndex].edges[edgeIndex];
                    if (vertices[thirdVertexIndex].colour == vertices[provisionIndex].colour) { vertexCanBeRecoloured = false; }
                    edgeIndex ++;
                }
                
                // Recolour the candidate and freeze vertices
                if (vertexCanBeRecoloured) {
                    vertices[candidateIndex].colour = vertices[provisionIndex].colour;
                    vertices[candidateIndex].isVariable = false;
                    vertices[provisionIndex].isVariable = false;
                    currentUpperBound -= 1;
                }
            }
        }
        
        return currentUpperBound;
    }
    
    
    
    
    public static int descend(Vertex[] vertices, int startingVertexIndex, int endVertexIndex) {
        int provisionIndex = startingVertexIndex; // referring to the current vertex that tries to provide its colour to the other vertices
        int numberOfVariableVertices = vertices.length; // referring to the vertices that have not yet provided or inherited their colour
        int upperBound = vertices.length;
        
        long preRunTime = System.currentTimeMillis();
        
        // Descend the upper bound
        while (numberOfVariableVertices > 1) { // trying to descend as long as there are vertices that have a variable colour
            
            if (!vertices[provisionIndex].isVariable) { provisionIndex ++; } // jumping to the next vertex if the current vertex is frozen
            else {
                
                boolean didProvideColour = false;
                
                // Try to disseminate the colour of the current providing vertex
                
                
                // Freeze the variable that tried to provide its colour
                vertices[provisionIndex].isVariable = false;
                numberOfVariableVertices --;
            }
        }
        return upperBound;
    }
    
    public static Vertex[] readGraph(String[] args) {
        if( args.length < 1 )
        {
            System.out.println("Error! No filename specified.");
            System.exit(0);
        }
        
        
        String inputfile = args[0];
        
        boolean seen[] = null;
        
        //! n is the number of vertices in the graph
        int n = -1;
        
        //! m is the number of edges in the graph
        int m = -1;
        
        //! e will contain the edges of the graph
        ColEdge e[] = null;
        
        try     {
            FileReader fr = new FileReader(inputfile);
            BufferedReader br = new BufferedReader(fr);
            
            String record = new String();
            
            //! THe first few lines of the file are allowed to be comments, staring with a // symbol.
            //! These comments are only allowed at the top of the file.
            
            //! -----------------------------------------
            while ((record = br.readLine()) != null)
            {
                if( record.startsWith("//") ) continue;
                break; // Saw a line that did not start with a comment -- time to start reading the data in!
            }
            
            if( record.startsWith("VERTICES = ") )
            {
                n = Integer.parseInt( record.substring(11) );
                if(DEBUG) System.out.println(COMMENT + " Number of vertices = "+n);
            }
            
            seen = new boolean[n+1];
            
            record = br.readLine();
            
            if( record.startsWith("EDGES = ") )
            {
                m = Integer.parseInt( record.substring(8) );
                if(DEBUG) System.out.println(COMMENT + " Expected number of edges = "+m);
            }
            
            e = new ColEdge[m];
            
            for( int d=0; d<m; d++)
            {
                if(DEBUG) System.out.println(COMMENT + " Reading edge "+(d+1));
                record = br.readLine();
                String data[] = record.split(" ");
                if( data.length != 2 )
                {
                    System.out.println("Error! Malformed edge line: "+record);
                    System.exit(0);
                }
                e[d] = new ColEdge();
                
                e[d].u = Integer.parseInt(data[0]);
                e[d].v = Integer.parseInt(data[1]);
                
                seen[ e[d].u ] = true;
                seen[ e[d].v ] = true;
                
                if(DEBUG) System.out.println(COMMENT + " Edge: "+ e[d].u +" "+e[d].v);
                
            }
            
            String surplus = br.readLine();
            if( surplus != null )
            {
                if( surplus.length() >= 2 ) if(DEBUG) System.out.println(COMMENT + " Warning: there appeared to be data in your file after the last edge: '"+surplus+"'");
            }
            
        }
        catch (IOException ex)
        {
            // catch possible io errors from readLine()
            System.out.println("Error! Problem reading file "+inputfile);
            System.exit(0);
        }
        
        for( int x=1; x<=n; x++ )
        {
            if( seen[x] == false )
            {
                if(DEBUG) System.out.println(COMMENT + " Warning: vertex "+x+" didn't appear in any edge : it will be considered a disconnected vertex on its own.");
            }
        }
        
        //! At this point e[0] will be the first edge, with e[0].u referring to one endpoint and e[0].v to the other
        //! e[1] will be the second edge...
        //! (and so on)
        //! e[m-1] will be the last edge
        //!
        //! there will be n vertices in the graph, numbered 1 to n
        
        //! INSERT YOUR CODE HERE!
        
        // Compiles an array of vertex objects that store index, edges and colour information
        int[][] sortedEdges = new int[n+1][0];
        Vertex[] vertices = new Vertex[n+1];
        
        for (int i = 0; i < m; i++) { // looping through all edges in e
            int u = e[i].u;
            int v = e[i].v;
            sortedEdges[u] = append(v, sortedEdges[u]);
            sortedEdges[v] = append(u, sortedEdges[v]);
        }
        
        for (int v = 1; v < vertices.length; v++) {
            int[] edges = sortedEdges[v];
            vertices[v] = new Vertex(v,edges);
        }
        
        return vertices;
    }
    
    private static int[] append(int newElement, int[] oldArray) {
        if (oldArray.length == 0) {
            int[] tmp = {newElement};
            return tmp;
        }
        int[] newArray = new int[oldArray.length+1];
        for (int i = 0; i < oldArray.length; i++) {
            newArray[i] = oldArray[i];
        }
        newArray[newArray.length-1] = newElement;
        return newArray;
    }
    
}


class ColEdge
{
    int u;
    int v;
}

class Vertex {
    int index;
    int[] edges;
    boolean isVariable = true; // providing a property for the status of a vertex. Vertices freeze when they have provided other vertices wit their colour or when they have been provided with the colour of another vertex or when they have been processed entirely by the current method.
    int colour;
    
    // Constructor method that is called by 'new'
    public Vertex(int index, int[] edges) {
        this.index = index;
        this.edges = edges;
        colour = index;
    }
}
