import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;
import javax.swing.JPanel;

/** Listener class for the buttons who should change the shown Panel
*/
public class panelChangeListener implements ActionListener {
	private String panel;
	private static JPanel mainPanel;
	private static CardLayout CL;
	
	/** Default usual constructor
	*/
	public panelChangeListener (String panelToChangeTo) {
		panel = panelToChangeTo;
	}
	
	/** One-time declaration for the listener's static variables
	*/
	public panelChangeListener (String panelToChangeTo, CardLayout CL, JPanel mainPanel) {
		panel = panelToChangeTo;
		this.CL = CL;
		this.mainPanel = mainPanel;
	}
	
	/** Overwriting actionPerformed
	*/
	public void actionPerformed (ActionEvent event) {
		if (panel == "PauseScreen") {				//If we change to the PauseScreen, then update the PauseScreen statistics before
			TestGraph.game.pauseTimer();
			TestGraph.updatePauseScreen();
		}
		else if (panel == "GameEndScreen") {		//If we change to the GameEndScreen, then update the GameEndScreen statistics before
			TestGraph.game.pauseTimer();
			TestGraph.updateGameEndScreen();
		}
		
		CL.show(mainPanel, panel);
	}
}