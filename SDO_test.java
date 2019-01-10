/**
 * @param color is the color of the Vertex
 * @param adjDegree is the degree of neighbooring vertices
 * @param satDegree is the degree of colored neighboors
 * @param number is the number of the vertex
 * @param vertices[] is the list with all the vertices
 * @param edges[] is the list with all the edges (from ReadGraphV2 i think?)
 * @param uncolored[] is the list with uncolored vertices --> is the same as vertices[]?
 * 
 * got all of it from: https://github.com/khaosminded/Dsatur/blob/master/src/tcss543/Dsatur.java
 * but i did change the names of variables etc.
*/


private int vertices[]; 
private int edges[]; 
private int uncolored[];
private int adjDegree = 0;


public class Vertex
{
    int color;
    int adjDegree;
    int satDegree;
    int number;

    public Vertex(int number, int adjDegree)
    {
        this.color = 0; // not colored, so start color = 0
        this.adjDegree = adjDegree; 
        this.satDegree = 0; // in beginning no saturation yet
        this.number = number; // number/ID of the vertex
    }
}

// for the graph
for (int i = 0; i < edges.length; i++)
{
    for (int j = 0; j < edges.lenght; j++)
    {
        if (edges[i][j])
        {
            adjDegree++;
        }
    }

    vertices.add(new Vertex(i, adjDegree));
}

uncolored.addAll(vertices);
// return this;


--------------------------------------------------------------------------------------------------------------------------------------------------
/**
 * @param maxSat is for the maximum saturation of a Vertex
 * @param maxAdj is for the maximum number of adjacent vertices
 * @param colorVert is the vertex to color 
 * @param sat is the number of saturation
 * @param adj is the number of adjacent vertices
*/

public class SDO
{
    public static int colorsNeeded()
    {
        int result = 0;

        while (!uncolored.isEmpty())
        {
            int maxSat  = Integer.MIN_VALUE;
            int maxAdj = Integer.MIN_VALUE;
            int colorVert = -1;

            //choose V with most saturation, if there are multiple vertices with the same degree, choose the one with the most neighboors (adj)
            for (Vertex v : uncolored)
            {
                int sat = v.satDegree;
                maxSat = sat > maxSat ? sat : maxSat    // if sat > maxSat, then new maxSat is sat, else maxSat stays the same
            }

            for (int i = 0; i < uncolored.size(); i++)
            {
                Vertex v = uncolored.get(i);
                int adj = v.adjDegree;
                if (v.satDegree == maxSat && maxAdj < adj)
                {
                    maxAdj = adj;
                    colorVert = i;
                }
            }

            // RLF for coloring? --> scrap next parts
------------------------------------------------------------------------------------------------------------------------

            // color current vertex
            Vertex curVert = uncolored.get(colorVert);
            boolean[] colored = new boolean[vertices.size()];
            for (int i = 0; i < colored.length; i++)
            {
                colored[i] = false;
            }

            int curVertNum = curVert.number;
            for (int i = 0; i < edges.length; i++)
            {
                if (edges.[curVertNum][i] && vertices.get(i).color >= 0)
                {
                    int colorIndex = verties.get(i).color;
                    colored[colorIndex] = true;
                }
            }

            for (int i = 0; i < colored.length; i++)
            {
                if (colored[i] == false)
                {
                    curVert.color = i;
                }
            }

            // reset list for next usage
            for (int i = 0; i < colored.length; i++)
            {
                colored[i] = false;
            }

            for (int i = 0; i < edges.lenght; i++)
            {
                if (edges[curVertNum][i])
                {
                    Vertex adj = vertices.get(i);
                    if (adj.color >= 0)
                    {
                      continue;  
                    }

                    for (int j = 0; i < edges.length; i++)
                    {
                        if (edges[i][j])
                        {
                            Vertex adj_adj = vertices.get(j);
                            if (adj_adj.color >= 0)
                            {
                                colored[adj_adj.color] = true;
                            }
                        }
                    }

                    int sat = 0;
                    for (boolean a : colored)
                    {
                        sat += (a) ? 1 : 0
                    }
                    //update saturation degree
                }   adj.satDegree = sat;
            }

            uncolored.remove(colorVert);
        }

        // go through colored array and sum up the number of colors used
        boolean[] colored = new boolean[vertices.size()];
        for (int i = 0; i < colored.length; i++)
        {
             // now what?
        }
        
       
    }
}