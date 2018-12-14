/** Corresponds to the GameMode2 from the Graph Colouring Game - Best Upper Bound
    Description:
        You will be given a fixed amount of time.
        You will have to find a colouring with as few colours as possible in the given time.
*/
public class GameMode2 extends GameMode {
    //This variable represents the number of seconds given to the User to solve the graph
    private final int time = 300;
    //
    //IDEA: calculate time in function of the Graph's complexity ?
    //    Something like (int)(180 + (Math.random() * numVertices * (1 + numMaxEdges/numEdges)))
    //
    //where numMaxEdges is numVertices*(numVertices-1), the maximum number of edges for this number of vertices
    
    /** Should compute the timer (descending in this GameMode) to show using the integer timeElapsed inherited from GameMode, which counts the number of seconds elapsed
        Should then use this value to call a method of TestGraph to modify the text of the label
            Use the inherited method TimeToString(), which given an int representin the seconds, returns a string of format mm:ss (m for minutes, s for seconds)

            Added functionality of GameMode2:
                Should set gameOver to true once the timer hits 0
        
        Is called each time timeElapsed is incremented by 1
    */
    public void updateTimeJLabel () {
        TestGraph.updateTime(TimeToString(time - timeElapsed));
    }
    
    /** Computes if the Game is over and sets this value to gameOver (true if the game is over, otherwise false)
        In this GameMode, the game is over if the timer runs out (when the leftover time is 0, but this case is already covered by updateTimeJLabel
            or when the User has coloured all the vertices (!!!! only case this method must cover !!!!)
    */
    public void isGameOver() {
        if ((timeElapsed >= time) || (currentGraph.blankVertices.length == 0)) {
            gameOver = true;
            TestGraph.updateGameEndScreen();
            TestGraph.CL.show(TestGraph.mainPanel, "GameEndScreen");
        }
    }
}