public class VerticesPositioner {
    
    public static void main(String[] args) {
        Vertex[] arrayNew = positionvertices(29,10, 5, 30, 30);
        for (int i = 0; i < arrayNew.length; i ++) {    // Vertex[] arrayNew a sauvgarde l'array qui est produite dans la méthode appelée par le "positionvertices"
            System.out.println("Point "+ (i+1) + " X: "+arrayNew[i].x + ", Y: " + arrayNew[i].y);
        }
    }
    
    /* This method displays the Vertices (depending on how many they are (nVertices)),
     where their coordonates are randomly choosen between a specific width and a specific height*/
    /* It also gives a minimum distance between each vertices to not make them to closer to each others */
    /* For the max distance , it depends of the numbr of vertices, we dont know yet if we will applmy it*/
    public static Vertex[] positionvertices(int maxDist, int minDist, int nVertices, int canvasWidth, int canvasHeight) {
        
        Vertex[] array =  new Vertex[nVertices];
        
        for (int i = 0; i<nVertices; i++) {
            
            int currentX = (int) (Math.random() * canvasWidth);                            //random
            int currentY = (int) (Math.random() * canvasHeight);
            
            for (int j = 0; j < i; j ++) {
                if ((distance(array[j], new Vertex(currentX, currentY)) < minDist) || (distance(array[j], new Vertex(currentX, currentY)) > maxDist)) {
                    currentX = (int) (Math.random() * canvasWidth);
                    currentY = (int) (Math.random() * canvasHeight);
                    j = 0;
                }
            }
            
            Vertex currentVertex = new Vertex(currentX, currentY);                                                                                                        // La on va creer le currentvertex (objet), et ce qu'il y a a la ligne en haut, c'est juste des random coordonés pour ce vertex
            array[i] = currentVertex;                                                                      // a la place de " currentVertex" on aurait bien pu écrire arry[i].x= current x , et la meme chose pour y
        }
        
        
        return array;
    }
    
    /** Computes the distance between two Vertex objects, and returns it
     */
    public static double distance(Vertex point1, Vertex point2) {
        double res = Math.sqrt(Math.pow(point2.x-point1.x, 2) + Math.pow(point2.y-point1.y, 2));
        return res;
    }
}
