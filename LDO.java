import java.util.*;
public class LDO{
  private int vertices;
  private int edges;
  private Map<Integer, int[]> connections;
/* vertices= number of vertices
   edges= number of edges
   v= on vertex in the edge
*/
public LDO(Graph g){
  this.max= max;
  vertices = g.getN();
  edges = g.getEdges().length;
  if(vertices<0) throw new IllegalArgumentException("The number of vertices must be positive");

  connections = MapConnections.connections(g);
  }

public int degree(int index){
  return connections.get(index).length;
}
/*
public int highestDegree(int index){
  Vertex max= blankVertices[0];
  for(index=2; index<= blankVertices.length; index++){
    if(degree(index)>degree(max){
      max= index;
    }
  }
  return max;
}

public int DescendingOrder(int index){

}
*/
