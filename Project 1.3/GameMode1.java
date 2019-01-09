/** Corresponds to GameMode1 from the Graph Colouring Game - To the bitter End
	Description: 
		You will have to colour the graph as quickly as possible.
		You will not be allowed to finish until the minimum number of colours has been reached. (= chromatic number)
*/
public class GameMode1 extends GameMode {
	/** Should compute the timer (ascending in this GameMode) to show using the integer timeElapsed inherited from GameMode, which counts the number of seconds elapsed
		Should then use this value to call a method of TestGraph to modify the text of the label. 
			Use the inherited method TimeToString(), which given an int representin the seconds, returns a string of format mm:ss (m for minutes, s for seconds)
		
		Is called each time timeElapsed is incremented by 1
	*/
	public void updateTimeJLabel () {
		TestGraph.updateTime(TimeToString(timeElapsed));
	}
	
	/** Computes if the Game is over and sets this value to gameOver (true if the game is over, otherwise false)
		In this GameMode, the game is only over if the User currently uses a number of colors equivalent to the chromatic number (and is thus not greater)
	*/
	public void isGameOver() {
		if ((currentGraph.colors.length == currentGraph.chromaticNumber) && (currentGraph.blankVertices.length == 0)) {
			gameOver = true;
			TestGraph.updateGameEndScreen();
			TestGraph.CL.show(TestGraph.mainPanel, "GameEndScreen");
		}
	}
}	