import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import javax.swing.JButton;
import javax.swing.JPanel;

/** This class is a model to use as parent for GameMode1, GameMode2 and GameMode3, the classes representing the different Games of this Graph Colouring Game
	It implements several methods, and some abstract ones (which need to be overwritten in the children)
*/
public abstract class GameMode {
	protected boolean gameOver = false;			//Boolean whether the game is finished or not
	protected Graph currentGraph;				//The graph of the game
	
	protected int timeElapsed = 0;				//The time elapsed (in integer secs)
	protected final int intervalMillis = 1000;	//The interval in millis at which actionPerformed should be called
	protected Timer t;							//The time object to count the seconds
	
	/** Constructor for GameMode.
		No parameters, all it does is prepare everything for the timer
	*/
	public GameMode () {
		//Initiate the Time listener and the Timer and start it
		TimeListener TL = new TimeListener();
		t = new Timer(intervalMillis, TL);
	}
	
	/** Starts the timer (or resumes it, if we previously entered the Pause screen)
	*/
	public void startTimer() {
		t.start();
	}
	
	/** Pauses the timer
	*/
	public void pauseTimer() {
		t.stop();
	}
	
	/** Reset the timer at 0 after stopping it
	*/
	public void resetTimer() {
		t.stop();
		timeElapsed = 0;
		updateTimeJLabel();
	}
	
	/** Class implementing the ActionListener interface, overwrites the actionPerformed method of ActionListener
		Used for the Timer
	*/
	class TimeListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			updateTime();
		}
	}
	
	/** This method is called every second to increase the seconds count (= time in secs) since the start of the game.
		When the game is over, it stops the timer.
	*/
	public void updateTime() {
		if (gameOver) {			//If the game is done, stop the timer
			t.stop();
		}
		timeElapsed ++;			//Increment the count of seconds
		
		isGameOver();
		
		updateTimeJLabel();		//Update the Label showing the time
	}
	
	/** Gives the User a hint about the next vertex to color.
			We need to have some visual effect to show the User which vertex to color next
	*/
	public void vertexHint() {
		//To be completed, see with Tim
	}
	
	/** Gives the User a hint concerning the next color to use.
			We need to have some visual effect to show the User which colour to choose
			(or select the color as the current color)
	*/
	public void colorHint() {
		//To be completed, see with Tim
	}
	
	/** Generates a Graph with random edge connections, with fixed n vertices and m edges
		@param n, the number of vertices
		@param m, the number of edges
	*/
	public Graph generateGraph (int n, int m) {
		Edge[] edges = new Edge[m];
		boolean[] seen = new boolean[n+1];
		
		if (m <= ((n*(n-1))/4)) {
			//If the number of edges to be generated is smaller or equal to half the maximum possible number of edges, then
			//we can generate which edges we want to have in our graph
		
			//counter represents the number of edges generated so far
			int counter = 0;
			while (counter < m) {
				int x = (int)(Math.random()*n) + 1;
				int y = (int)(Math.random()*n) + 1;
				
				//Check for validity
				boolean valid = true;
				
				//If they are the same, they are invalid
				if (x == y) {
					valid = false;
				}
				
				//Otherwise, if one of the previous edge is already the same, then it is also unvalid
				int i = 0;
				while ((i < counter-1) && valid) {
					if ((edges[i].u == x && edges[i].v == y) || (edges[i].v == x && edges[i].u == y)) {
						valid = false;
					}
					else {
						i ++;
					}
				}
				
				if (valid) {
					//Create the edge
					edges[counter] = new Edge(x, y);
					//Register that these vertices have edges
					seen[x] = true;
					seen[y] = true;
					//Increment counter
					counter ++;
				}
			}
		}
		else {
			//Otherwise, if there are more edges than half the maximum possible number of edges,
			//then, we can pick out which edges we don't want to take with us ...
			
			int maxEdges = (n*(n-1))/2;
			Edge[] allPossibleEdges = new Edge[maxEdges];
			int counter = 0;
			for (int i = 1; i < n; i ++) {
				for (int j = (i+1); j <= n; j ++) {
					allPossibleEdges[counter] = new Edge(i, j);
					counter ++;
				}
			}
			
			for (; maxEdges > m; maxEdges --) {
				int discard = (int)(Math.random() * maxEdges);
				
				//Copy the edge at index maxEdges-1 to index discard
				allPossibleEdges[discard] = allPossibleEdges[maxEdges-1];
			}
			
			//Copy the rest to edges
			System.arraycopy(allPossibleEdges, 0, edges, 0, m);
			
			//Set all vertex that has at least one edge connection to true in seen
			for (int i = 0; i < edges.length; i ++) {
				seen[edges[i].u] = true;
				seen[edges[i].v] = true;
			}
		}
		
		int notUsed = 0;
		for (int i = 1; i <= n; i ++) {
			if (! seen[i]) {
				notUsed ++;
			}
		}
		
		return new Graph(edges, n, m, notUsed);
	}
	
	/** Based on the count of secs, compute the mins:secs form for this count and return it
			@param secs, the number of secs the User took to end the Game (either by win or loss, which is irrelevant for this part of the program)
			
			@return a string with format mins:secs, such that mins*60+secs = int secs parameter given
	*/
	public String TimeToString(int secs) {
		int mins = (secs - (secs % 60))/60;
		int leftoverSecs = secs % 60;
		
		return String.format("%02d:%02d", mins, leftoverSecs);
	}
	
	/** The stopping condition of one game. These will be:
			- GameMode 1:
				the number of colours the User uses is reduced to the chromatic number
			- GameMode 2:
				Either the time the User had to finish to colour the Graph is elapsed (the User lost),
					or the User has coloured every vertex of the Graph
			- GameMode 3:
				The User has finished colouring the Graph in the given order,
					that is, he has coloured the last Vertex to be colored.
	*/
	public abstract void isGameOver ();
	
	/** Updates the showing of the JLabel time
			In GameMode2, it should show a descending time from a certain value
			In GameMode1 and 3, it should be ascending
	*/
	public abstract void updateTimeJLabel();
	
	//
	//
	// The following are all auxiliary methods for computing the chromatic number
	//
	//
	
	/** Checks if I can move item toMove from array[origin] to array[destination], that is, without having any element of array[destination] sharing an edge with toMove
		@return a boolean value, true if the move is valid, otherwise false
	*/
	public static boolean MoveIsPossible (Edge[] e, int[][] array, int toMove, int destination) {
		//As I should only need to move points from the 1st color to another one, I can assume that the position
		//of the point is array[0][toMove-1]
		boolean movePossible = true;
		int j = 0;
		
		//If the destination array is not empty, we need to check if we can add toMove in it, otherwise, it is a new color we create
		if (! arrayEmpty(array[destination])) {
			while ((j < e.length) && (movePossible)) {
				//For each edge, if the item to move is one of the two points, check if the other item is in the array I try to move the item to
				if ((e[j].u == toMove) || (e[j].v == toMove)) {
					if ((ArrayContains(array[destination], e[j].u)) || (ArrayContains(array[destination], e[j].v))) {
						movePossible = false;
					}
				}
				j ++;
			}
		}
		
		return movePossible;
	}
	
	/** Should move item toMove from array[origin] to array[destination].
		The User should have already checked if the move is possible (that is, doesn't bring together any edge-connected-points) using movePossible()
	*/
	public static int[][] Move(int[][] array, int origin, int toMove, int destination) {
		int i = 0;
		boolean notFoundToMove = true;
		//For each item in the origin array, while we have not found the item we need to move
		while ((i < array[origin].length) && (notFoundToMove)) {
			if (array[origin][i] == toMove) {
				//Once we find the item we need to move,
				notFoundToMove = false;
				
				//Deletes the original toMove
				array[origin][i] = 0;
				//And adds it in array[destination]
				Add(array[destination], toMove);
			}
			i ++;
		}
		
		//Return the modified array
		return array;
	}
	
	/** Adds the integer "element" to array, at the position element-1
	*/
	public static void Add (int[] array, int element) {
		array[element-1] = element;
	}
	
	/** Returns the number of color the array color composition uses (that is, the number of non-empty 1D-arrays inside "array"), where an empty array is filled with 0's, the default int value
		@return an integer, number of colors used by this color assignment
	*/
	public static int usedColors (int[][] array) {
		int i = 0;
		boolean empty = false;
		int numColors = 0;
		while ((i < array.length) && (! empty)) {
			if (! arrayEmpty(array[i])) {
				numColors ++;
				i ++;
			}
			else {
				empty = true;
			}
		}
		
		return numColors;
	}
	
	/** Returns a boolean indicating whether a given array of integers is empty (= filled with 0s) or not
		@return true if it is filled with only 0s, otherwise, false
	*/
	public static boolean arrayEmpty (int[] array) {
		boolean isEmpty = true;
		int i = 0;
		while ((i < array.length) && (isEmpty)) {
			if (array[i] != 0) {
				isEmpty = false;
			}
			else {
				i ++;
			}
		}
		
		return isEmpty;
	}
	
	/** Returns a boolean indicating whether an array of integers contains a given integer.
		@return true if the array contains the int, otherwise false
	*/
	public static boolean ArrayContains(int[] array, int x) {
		boolean found = false;
		int i = 0;
		
		while ((! found) && (i < array.length)) {
			if (array[i] == x) {
				found = true;
			}
			else {
				i ++;
			}
		}
		
		return found;
	}
}