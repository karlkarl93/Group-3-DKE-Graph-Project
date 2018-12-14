/** Corresponds to GameMode3 from the Graph Colouring Game - Random Order
    Description:
        You will have to pick the colours of the vertices in the order that the computer generated randomly for the vertices.
        Once the colour of a vertex has been chosen, it cannot be change again.
        Your goal is to use as few colours as possible.
*/
public class GameMode3 extends GameMode {
    /** Should compute the timer (ascending in this GameMode) to show using the integer timeElapsed inherited from GameMode, which counts the number of seconds elapsed
        Should then use this value to call a method of TestGraph to modify the text of the label
            Use the inherited method TimeToString(), which given an int representin the seconds, returns a string of format mm:ss (m for minutes, s for seconds)

        Is called each time timeElapsed is incremented by 1
    */
    public void updateTimeJLabel () {
        TestGraph.updateTime(TimeToString(timeElapsed));
    }
    
    /** Computes if the Game is over and sets this value to gameOver (true if the game is over, otherwise false)
        In this GameMode, the game is over once the User has coloured the last vertex
    */
    public void isGameOver() {
        if (currentGraph.blankVertices.length == 0) {
            gameOver = true;
            TestGraph.updateGameEndScreen();
            TestGraph.CL.show(TestGraph.mainPanel, "GameEndScreen");
        }
    }
}