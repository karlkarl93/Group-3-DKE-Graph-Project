import java.util.Scanner;
import java.lang.Math;
import java.util.Random;

class Edge {
	int u;
	int v;
	
	public Edge (int x, int y) {
		u = x;
		v = y;
	}
}
	
public class RandomTest{
		
	public static void main (String[] args) {

		Scanner in = new Scanner(System.in);
		Random ran = new Random();
	
	System.out.println("Please type in the number of vertices/nodes you want to use:");
	int n = in.nextInt();
	
	System.out.println("Please type in the number of edges/connections you want it to have:");
	int c = in.nextInt();
	
	int counter = 1; // 1 means it has an edge

		int[][] edgeArray = new int[n][n];
		
	
		
	if ( c <= (n*(n-1))){
		if(c >= (n-1)){
			
			Edge[] edges = new Edge[c];
		while (counter <= c) {
		// max-min = range, +1 to include the last value ((max-min)+1), +min define start value (((max-min)+1)+min)
		// min = 1
		int x = ran.nextInt(n) + 1;
		int y = ran.nextInt(n) + 1;
		
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
		/*
			if ( edgeArray[x][y] ==0) {
				if (edgeArray[y][x] != 1) {
					if (x != y) {
						counter++;
						edgeArray[x][y] = 1;
								}
						}
				}
		}
		
		if (checkEdges(edgeArray)) {
				System.out.println("There is a vertex without a connection");
		}
		
		else{
		System.out.println("Result:");
		for(int i = 0; i < edgeArray.length; i++) {
			for (int j = 0; j < edgeArray[0].length; j++) {
				System.out.print(edgeArray[i][j]+ " " );
			}
			System.out.println();
		}
				
	}
	*/
		}
		
		//Show the result of edges
		for (int i=0; i < edges.length; i ++) {
			System.out.println("Edge #"+(i+1) + ": " + edges[i].u + " - " + edges[i].v);
		}
		
}
		else {
		System.out.println("Too few edges");
		}
	}
	
	else {
	System.out.println("Too many edges");
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
