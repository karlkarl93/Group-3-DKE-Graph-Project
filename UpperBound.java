import java.io.*;
import java.util.*;


public class UpperBound {
    
    public final static boolean DEBUG = false;
    
    public final static String COMMENT = "//";

    public static void main(String[] args) {
        Vertex[] vertices = readGraph(args);
        
        int provisionIndex = 1; // Referring to the current vertex that tries to disseminate its colour among the other vertices
        int numberOfVariableVertices = n;
        int upperBound = n;
        
        // Descend the upper bound
        while (numberOfVariableVertices > 1) { // trying to descend as long as there are vertices that have a variable colour
        
            if (!vertices[provisionIndex].isVariable) { provisionIndex ++ } // jumping to the next vertex if the current vertex is frozen, else try disseminating
            else {
                
                boolean didDisseminateColour = false;
                
                // Try to disseminate the colour of the current providing vertex
                for (int candidateIndex = 1; candidateIndex < vertices.length; candidateIndex++) {
                    
                    // Select the vertices that are variable and different from the current providing vertex
                    if (vertices[candidateIndex].isVariable && candidateIndex != provisionIndex) {
                    
                        // Check whether the candidate vertex can be recoloured
                        boolean vertexCanBeRecoloured = true;
                        int edgeIndex = 0;
                        
                        // Disqualify candidate if it edges with the providing vertex
                        while (vertexCanBeRecoloured && edgeIndex < vertices[provisionIndex].edges.length) {
                            if (candidateVertex == vertices[provisionIndex].edges[e] { vertexCanBeRecoloured = false; }
                            edgeIndex ++;
                        }
                        
                        // Disqualify candidate if it edges with a third vertex that has the same colour as the providing vertex
                        edgeIndex = 0;
                        while (vertexCanBeRecoloured && edge < vertices[candidateIndex].edges.length) {
                            int thirdVertexIndex = vertices[candidateIndex].edges[e];
                            if (vertices[thirdVertexIndex].colour == vertices[provisionIndex].colour) { vertexCanBeRecoloured = false; }
                            edgeIndex ++;
                        }
                        
                        // Recolour the candidate if possible
                        if vertexCanBeRecoloured {
                            vertices[candidateIndex].colour = vertices[provisionIndex].colour;
                            vertices[candidateIndex].isVariable = false;
                            numberOfVariableVertices --;
                            didDisseminateColour = true;
                        }
                    }
                }
                
                // Freeze the variable that tried to disseminate its colour
                vertices[candidateIndex].isVariable = false;
                numberOfVariableVertices
                
                // Reduce the upper bound if possible
                if (didDisseminateColour) {
                    numberOfVariableVertices--;
                }
            }
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
