import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class ReadGraphV2 {
	public final static boolean DEBUG = false;
	public final static String COMMENT = "//";
	
	/** This method reads a graph from a file name or path that is given as a parameter
		@param String args[], the array of strings, which is typically of length 1 and contains the file Name (or path if needed)
	*/
	public static Graph readGraph(String args[]) {		
		//If the length of args is 0, then the User didn't specify a txt file
		if (args.length < 1) {
			System.out.println("Error! No filename specified.");
			System.exit(0);
		}
		
		//Initialize edges, n and m
		Edge[] edges = null;			//edges will contain the edges of the graph
		int n = -1;						//n is the number of vertices in the graph
		int m = -1;						//m is the number of eges in the graph
		boolean seen[] = new boolean[n+1];
		int notUsed = 0;
		
		Scanner in = new Scanner(System.in);
		
		String inputfile = args[0];
		
		try { 
			FileReader fr = new FileReader(inputfile);
			BufferedReader br = new BufferedReader(fr);

			String record = new String();
				
			//! The first few lines of the file are allowed to be comments, staring with a // symbol.
			//! These comments are only allowed at the top of the file.
				
			//! -----------------------------------------
			while ((record = br.readLine()) != null) {
				if (record.startsWith("//")) continue;
				break; // Saw a line that did not start with a comment -- time to start reading the data in!
			}
			
			if (record.startsWith("VERTICES = ")) {
				//Retrieve the number of vertices, and print it if (DEBUG == true)
				n = Integer.parseInt( record.substring(11) );					
				if(DEBUG) System.out.println(COMMENT + " Number of vertices = "+n);
			}
			
			seen = new boolean[n+1];
			record = br.readLine();
				
			if(record.startsWith("EDGES = ")) {
				//Retrieve the (expected) number of edges 
				m = Integer.parseInt( record.substring(8) );				
				if(DEBUG) System.out.println(COMMENT + " Expected number of edges = "+m);
			}

			edges = new Edge[m];	
			
			for (int d = 0; d < m; d++) {
				if(DEBUG) System.out.println(COMMENT + " Reading edge "+(d+1));
				
				record = br.readLine();
				String data[] = record.split(" ");
				if (data.length != 2) {
					System.out.println("Error! Malformed edge line: "+record);
					System.exit(0);
				}
				
				edges[d] = new Edge(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
				seen[edges[d].u] = true;
				seen[edges[d].v] = true;
				
				if(DEBUG) System.out.println(COMMENT + " Edge: "+ edges[d].u +" "+edges[d].v);
			}
			
			//If there is still some text after the m edges, then there is an error
			String surplus = br.readLine();
			if (surplus != null) {
				if( surplus.length() >= 2 ) System.out.println(COMMENT + " Warning: there appeared to be data in your file after the last edge: '"+surplus+"'");						
			}
			
			for (int x = 1; x <= n; x++) {
				if (seen[x] == false) {
					notUsed ++;
					if(DEBUG) System.out.println(COMMENT + " Warning: vertex "+x+" didn't appear in any edge : it will be considered a disconnected vertex on its own.");
				}
			}
		}
		catch (IOException ex) { 
			// catch possible io errors from readLine()
			System.out.println("Error! Problem reading file "+inputfile);
			System.exit(0);
		}

		//! At this point edges[0] will be the first edge, with edges[0].u referring to one endpoint and edges[0].v to the other
		//! edges[1] will be the second edge...
		//! (and so on)
		//! edges[m-1] will be the last edge
		//! 
		//! there will be n vertices in the graph, numbered 1 to n
		
		return new Graph(edges, n, m, notUsed, seen);
	}
}