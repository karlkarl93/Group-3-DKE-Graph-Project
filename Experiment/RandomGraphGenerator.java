import java.util.Scanner;
import java.lang.Math;
import java.util.Random;

public class RandomGraphGenerator {
    
    public static void main(String[] args) {
        System.out.println(generate(5,6).vertices[1].identifier);
    }
    
    public static Graph generate(int edgeCount, int vertexCount) {
        
        Random ran = new Random();
        
        int counter = 1; // 1 means it has an edge
        
        int[][] edgeArray = new int[vertexCount][vertexCount];
        
        
        
        if ( edgeCount <= (vertexCount*(vertexCount-1))){
            if(edgeCount >= (vertexCount-1)){
                
                Edge[] edges = new Edge[edgeCount];
                while (counter <= edgeCount) {
                    // max-min = range, +1 to include the last value ((max-min)+1), +min define start value (((max-min)+1)+min)
                    // min = 1
                    int x = ran.nextInt(vertexCount) + 1;
                    int y = ran.nextInt(vertexCount) + 1;
                    
                    int i=0;
                    boolean notFound = true;
                    if (x == y) {
                        notFound = false;
                    }
                    
                    while (i < counter-1 && notFound) {
                        if ((edges[i].u == x && edges[i].v == y) || (edges[i].v == x && edges[i].u == y)) {
                            notFound = false;
                        }
                        else {
                            i ++;
                        }
                    }
                    
                    if (notFound) {
                        
                        edges[i] = new Edge(x, y);
                        counter ++;
                    }
                }
                return new Graph(edges, vertexCount);
            }
            else return null;
        }
        else {
            return null;
        }
    }
    
    public static boolean checkEdges(int edgeArray[][]) {
        int rows = 0;
        int colms = 0;
        
        for(int a = 0; a < edgeArray.length; a++) {
            for (int b = 0; b < edgeArray[0].length; b++) {
                rows += edgeArray[a][b];
            }
            
            System.out.println(rows);
            
            if (rows == 0) {
                for(int d = 0; d < edgeArray.length; d++) {
                    
                    colms += edgeArray[d][a];
                }
                if (colms == 0) {
                    return true;
                }
            }
            else {
                rows = 0;
            }
        }
        return false;
    }
}
