import java.awt.Color;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JSlider;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JTextField;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.GridLayout;

public class TestGraph {
	//Frame size parameters
	private static final int FRAME_WIDTH = 1280;
	private static final int FRAME_HEIGHT = 720;
	//Initialize the GameMode variable, game
	public static GameMode game;
	//Initialize the Graph variable and set the min and max number of vertices
	private static Graph graph;
	private static final int minVertices = 1;
	private static final int maxVertices = 30;
	
	//Initialize the array of default colors
	private static Color[] colors = {
                        new Color(244,67,54),
                        new Color(233,30,99),
                        new Color(156,39,176),
                        new Color(103,58,183),
                        new Color(26,35,126),

                        new Color(33,150,243),
                        new Color(129,212,250),
                        new Color(0,188,212),
                        new Color(0,150,136),
                        new Color(76,175,80),

                        new Color(139,195,74),
                        new Color(205,220,57),
                        new Color(255,235,59),
                        new Color(255,193,7),
                        new Color(255,152,0),

                        new Color(255,138,101),
                        new Color(121,85,72),
                        new Color(158,158,158),
                        new Color(96,125,139),
                        new Color(33,33,33),

                        new Color(183,28,28),
                        new Color(74,20,140),
                        new Color(0,77,64),
                        new Color(130,119,23),
                        new Color(224,224,224),
                      };
	
	//Declare the frame:
	private static JFrame frame;
	//Initialize all the panels:
	protected static JPanel mainPanel = new JPanel();
	public static JPanel GameChooserScreen = new JPanel();
	public static JPanel GameScreen = new JPanel();
	public static JPanel PauseScreen = new JPanel();
	public static JPanel GameEndScreen = new JPanel();
	
	//Create the cardLayout
	protected static CardLayout CL = new CardLayout();
	//Initiate the variables for the font
	public static String fontName = "SansSerif Plain";
	public static int fontStyle = Font.BOLD;
	
	//Initialize all the labels, sliders, etc. that need to be modified during program execution
		//GameScreen items
	private static SimulatorV2 canvas;
	private static JPanel colorPalette = new JPanel();
	private static JButton[] buttons = new JButton[colors.length];
	private static JLabel timer = new JLabel("00:00");
	private static JSlider vertexSlider = new JSlider();
	private static JSlider edgeSlider = new JSlider();
	private static JLabel verticesLabel = new JLabel("VERTICES: ");
	private static JLabel edgeLabel = new JLabel("EDGES: ");
		//PauseScreen items
	private static JLabel time = new JLabel("Time taken: 00:00");
	private static JLabel userColors = new JLabel("You used 0 colors");
	private static JLabel percentVerticesColored = new JLabel("You colored 00/00 vertices");
		//GameEndScreen items
	private static JLabel time2 = new JLabel("Time taken: 00:00");
	private static JLabel userColors2 = new JLabel("You used 0 colors");
	private static JLabel percentVerticesColored2 = new JLabel("You colored 00/00 vertices");
	private static JLabel chromaNum = new JLabel("The chromatic number was: 0");
	private static JLabel[] conclusion = new JLabel[2];
	
	public static void main (String[] args) {
		//Create the frame
		frame = new JFrame("Graph Colouring Game");
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 		
		//Put window in the middle of the screen
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		//Add the cardlayout to the mainPanel
		mainPanel.setLayout(CL);
		frame.add(mainPanel);
		
		//Create all the panels
		createPanels();
		
		//Shows the GameChooserScreen
		CL.show(mainPanel, "GameChooserScreen");
		
		//Show the frame and set its layout manager to null
		frame.setVisible(true);
		frame.setLayout(null);
	}
	
	/** Create the panels to switch between
			- GameChooserScreen JPanel, the panel which proposes the User the three different GameModes, and the User then selects one
			- PauseScreen JPanel, the panel which is showed when the User pauses the game
			- GameEndScreen JPanel, the panel which is showed once the game is finished by the User (or the User interrupts the Game)
			- GameScreen JPanel, the panel which is the actual Game (contains the Canvas showing the Graph and the toolbar)
	*/
	public static void createPanels() {
		//GAMECHOOSERSCREEN PANEL
		//Create the font
		Font myFont = new Font(fontName, fontStyle, 15);
		
		//Add the Buttons to GameChooserScreen panel
		//Button mode1
		JButton mode1 = new JButton("Game mode 1");
		mode1.setBounds(170, FRAME_HEIGHT-250, 150, 80);
		mode1.setForeground(new Color(62,118,236));
		mode1.setFont(myFont);
		//When mode1 button is clicked, the display should change to GameScreen Panel
		mode1.addActionListener(new gameModeListener(1));
		
		GameChooserScreen.add(mode1);

		//Button mode2
		JButton mode2 = new JButton("Game mode 2");
		mode2.setBounds(565,FRAME_HEIGHT-250,150,80);
		mode2.setForeground(new Color(238,16,16));
		mode2.setFont(myFont);
		//When mode2 button is clicked, the display should change to GameScreen Panel
		mode2.addActionListener(new gameModeListener(2));
		
		GameChooserScreen.add(mode2);

		//Button mode3
		JButton mode3 = new JButton("Game mode 3");
		JLabel introduction = new JLabel("<html>Please select the game mode you want to play.<html>");
		mode3.setBounds(960,FRAME_HEIGHT-250,150,80);
		mode3.setForeground(new Color(23,154,19));
		mode3.setFont(myFont);
		//When mode3 button is clicked, the display should change to GameScreen Panel
		mode3.addActionListener(new gameModeListener(3));
		
		GameChooserScreen.add(mode3);

		//Add the text to the GameChooserScreen panel
		//Introduction text, tells the User to choose a gameMode
		introduction.setBounds((FRAME_WIDTH/2)-225,20,450,80);
		introduction.setForeground(Color.black);
		introduction.setFont(new Font(fontName, fontStyle, 18));
		GameChooserScreen.add(introduction);

		//Description for gameMode 1
		JLabel description1 = new JLabel("<html>Game mode 1<br> <br>To The Bitter End<br>   <br>Description :<br>You will have to colour the graph as quickly as possible.<br>You will not be allowed to finish until the minimum number of colours has been reached.<html>");
		description1.setBounds(95,140,300,250);
		description1.setForeground (new Color(62,118,236));
		description1.setFont(new Font(fontName, fontStyle, 12));
		GameChooserScreen.add(description1);
		
		//Description for gameMode 2
		JLabel description2 = new JLabel("<html>Game mode 2<br> <br>Best Upper Bound<br>   <br>Description :<br>You will be given a fixed amount of time.<br>You will have to find a colouring with as few colours as possible in the given time.<html>");
		description2.setBounds(490,130,300,250);
		description2.setForeground (new Color(238,16,16));
		description2.setFont(new Font(fontName, fontStyle, 12));
		GameChooserScreen.add(description2);

		//Description for gameMode 3
		JLabel description3 = new JLabel("<html>Game mode 3<br> <br>Random Order<br>   <br>Description :<br>You will have to pick the colours of the vertices in the order that the computer generated randomly for the vertices.<br>Once the colour of a vertex has been chosen, it cannot be change again.<br>Your goal is to use as few colours as possible.<html>");
		description3.setBounds(885,150,300,250);
		description3.setForeground (new Color(23,154,19));
		description3.setFont(new Font(fontName, fontStyle, 12));
		GameChooserScreen.add(description3);

		GameChooserScreen.setLayout(null);

		//Add GameChooserScreen panel to the mainPanel
		mainPanel.add(GameChooserScreen, "GameChooserScreen");
		
		//-----------------------------------------------------------------------------------------------------
		
		//GAME SCREEN (the main Panel)
		//Create the JLabel timer, which should show the time
		timer.setFont(new Font(fontName, fontStyle, 16));
		timer.setBounds(435, 10, 60, 30);
		//GameScreen.add(timer);
		
		//Create the canvas (to show the Graph)
		canvas = new SimulatorV2(930, 720);
		
		//Create the toolbar
		JPanel toolbar = new JPanel();
		toolbar.setPreferredSize(new Dimension(350, 720));
        toolbar.setLayout(null);
		
		//Create a JPanel for the color Palette
		colorPalette.setBounds(25, 0, 300, 300);
		colorPalette.setLayout(new GridLayout(5, 5));
		
		//Create all the buttons
		for (int i = 0; i < colors.length; i ++) {
			//Create the button, set its original color and add it a listener
			buttons[i] = new JButton();
			buttons[i].setBackground(colors[i]);
			buttons[i].addActionListener(new ColorListener(i));
			
			//Add it to the colorPalette JPanel
			colorPalette.add(buttons[i]);
		}
		
        //Create the slider Panel containing the sliders
        JPanel sliderPanel = new JPanel();
		sliderPanel.setBounds(0, 300, 350, 400);
		sliderPanel.setLayout(null);
		
		//Compute the starting values of the number of vertices and number of edges
		int numVertices = (int)(Math.random() * (maxVertices-minVertices)) + minVertices;
		int numEdges = (int)(Math.random() * ((numVertices*(numVertices-1)/2) - numVertices)) + numVertices;
		
        //Create the label for the first slider
		verticesLabel.setText("VERTICES: " + numVertices);
		verticesLabel.setFont(myFont);
		verticesLabel.setBounds(125, 10, 120, 30);
		sliderPanel.add(verticesLabel);
		//Create the first slider (for the number of Vertices)
		vertexSlider.setMaximum(30);
		vertexSlider.setMinimum(1);
		vertexSlider.addChangeListener(new vertexSliderListener());
		vertexSlider.setValue(numVertices);
		vertexSlider.setPaintTicks(true);
		vertexSlider.setPaintLabels(true);
		vertexSlider.setMinorTickSpacing(1);
		vertexSlider.setMajorTickSpacing(5);
		vertexSlider.setBounds(30, 40, 300, 60);
		sliderPanel.add(vertexSlider, BorderLayout.EAST);
		
		//Create the label for the second slider
		edgeLabel.setText("EDGES: " + numEdges);
		edgeLabel.setFont(myFont);
		edgeLabel.setBounds(135, 100, 100, 30);
		sliderPanel.add(edgeLabel);
		//Create the second slider (for the number of Edges)
		edgeSlider.setMaximum((vertexSlider.getMaximum()*(vertexSlider.getMaximum()-1))/2);
		edgeSlider.setMinimum(0);
		edgeSlider.setValue(numEdges);
		edgeSlider.addChangeListener(new edgeSliderListener());
		edgeSlider.setPaintTicks(true);
		edgeSlider.setPaintLabels(true);
		edgeSlider.setMinorTickSpacing(1);
		edgeSlider.setMajorTickSpacing(50);
		edgeSlider.setMaximum((numVertices * (numVertices-1))/2);
		edgeSlider.setBounds(30, 130, 300, 60);
		sliderPanel.add(edgeSlider, BorderLayout.EAST);
		
		//Create the Graph handling buttons
		//Create the openGraph button
		JButton openGraph = new JButton("Load Graph");
		openGraph.setFont(new Font(fontName, fontStyle, 14));
		openGraph.setBounds(10, 510, 120, 40);
		openGraph.addActionListener(new openGraphListener());
		toolbar.add(openGraph);
		//Create the generateGraph button
		JButton generateGraph = new JButton("Generate Graph");
		generateGraph.setFont(new Font(fontName, fontStyle, 14));
		generateGraph.setBounds(140, 510, 180, 40);
		generateGraph.addActionListener(new generateGraphListener());
		toolbar.add(generateGraph);
		
		//Create the Hint buttons
		//Create the "VertexHint" button
		JButton vertexHint = new JButton("Vertex Hint");
		vertexHint.setFont(myFont);
		vertexHint.setBounds(40, 580, 120, 40);
		vertexHint.addActionListener(new hintListener("vertex"));
		toolbar.add(vertexHint);
		//Create the "ColorHint" button
		JButton colorHint = new JButton("Color Hint");
		colorHint.setFont(myFont);
		colorHint.setBounds(190, 580, 120, 40);
		colorHint.addActionListener(new hintListener("color"));
		toolbar.add(colorHint);
		
		//Create the "Pause" button
		JButton pause = new JButton("Pause");
		pause.setBounds(145, 630, 80, 40);
		pause.setFont(myFont);
		//This listener will change the currently shown Panel when the User presses the Button
		pause.addActionListener(new panelChangeListener("PauseScreen", CL, mainPanel));
		toolbar.add(pause);
		
		toolbar.add(colorPalette);
		toolbar.add(sliderPanel);
		
		GameScreen.setLayout(new BorderLayout());
        GameScreen.add(toolbar, BorderLayout.EAST);
        GameScreen.add(canvas, BorderLayout.WEST);
		
		//Add GameScreen panel to mainPanel
		mainPanel.add(GameScreen, "GameScreen");
		
		//--------------------------------------------------------------------
		
		//PAUSESCREEN
		PauseScreen.setBackground(Color.black);
		
		//Then, create all the components
		//First, create the text components, showing the game stats (time, number colors used by User, chromatic Number)
		//Create the label for the Time the User took
		time.setBounds(490, 100, 300, 30);
		time.setFont(new Font(fontName, fontStyle, 18));
		time.setForeground(Color.white);
		PauseScreen.add(time);
		
		//Create the label for the number of colors used by the User
		userColors.setBounds(490, 130, 300, 30);
		userColors.setFont(new Font(fontName, fontStyle, 18));
		userColors.setForeground(Color.white);
		PauseScreen.add(userColors);
		
		//Create the label showing the pourcentage of vertices colored
		percentVerticesColored.setBounds(450, 160, 340, 30);
		percentVerticesColored.setFont(new Font(fontName, fontStyle, 18));
		percentVerticesColored.setForeground(Color.white);
		PauseScreen.add(percentVerticesColored);
		
		//Then, create buttons "Retry", "New Game", "Quit", "Continue"
		//Create button "Retry"
		JButton retry = new JButton("Retry");
		retry.setBounds(136, 400, 150, 80);		//need to set to some coordinates
		retry.addActionListener(new retryListener());
		PauseScreen.add(retry);

		//Create button "New Game"
		JButton newGame = new JButton("New Game");
		newGame.setBounds(422, 400, 150,80);
		newGame.addActionListener(new newGameListener());
		PauseScreen.add(newGame);

		//Create button "Quit"
		JButton quit = new JButton("Quit");
		quit.setBounds(708, 400, 150, 80);
		quit.addActionListener(new panelChangeListener("GameChooserScreen"));
		PauseScreen.add(quit);
		
		//Create button "Continue"
		JButton cont = new JButton("Continue");
        cont.setBounds(1014, 400, 150, 80);
		cont.addActionListener(new continueListener());
		PauseScreen.add(cont);
		
		//Set the layout manager to null
		PauseScreen.setLayout(null);
		
		//Add PauseScreen to mainPanel
		mainPanel.add(PauseScreen, "PauseScreen");
		
		//----------------------------------------------------------------------------
		
		//GAMEENDSCREEN
		GameEndScreen.setBackground(Color.black);
			
		//Then, create all the components
		//First, create the text components, showing the game stats (time, number colors used by User, chromatic Number)
		//Create the label for the Time the User took
		time2.setBounds(490, 100, 300, 30);
		time2.setFont(new Font(fontName, fontStyle, 18));
		time2.setForeground(Color.white);
		GameEndScreen.add(time2);
		
		//Create the label for the number of colors used by the User
		userColors2.setBounds(490, 130, 300, 30);
		userColors2.setFont(new Font(fontName, fontStyle, 18));
		userColors2.setForeground(Color.white);
		GameEndScreen.add(userColors2);
		
		//Create the label for the chromatic number
		chromaNum.setBounds(490, 160, 300, 30);
		chromaNum.setFont(new Font(fontName, fontStyle, 18));
		chromaNum.setForeground(Color.white);
		GameEndScreen.add(chromaNum);
		
		//Create the label showing the pourcentage of vertices colored
		percentVerticesColored.setBounds(450, 190, 320, 30);
		percentVerticesColored.setFont(new Font(fontName, fontStyle, 18));
		percentVerticesColored.setForeground(Color.white);
		PauseScreen.add(percentVerticesColored);

		//Create the label for the conclusion of the game (did the User manage to reach the chromatic number ?)
		conclusion[0] = new JLabel("Great !");
		conclusion[1] = new JLabel("You managed to only use as many colours as the chromatic number.");
		conclusion[0].setBounds(550, 250, 100, 30);
		conclusion[1].setBounds(400, 280, 500, 30);
		for (int i = 0; i < conclusion.length; i ++) {
			conclusion[i].setForeground(Color.green);
			conclusion[i].setFont(new Font(fontName, Font.PLAIN, 16));
			GameEndScreen.add(conclusion[i]);
		}

		//Then, create buttons "Retry", "New Game" and "Quit"
		//Create button "Retry"
		JButton retry2 = new JButton("Retry");
		retry2.setBounds(285, 400, 150, 80);
		retry2.addActionListener(new retryListener());
		GameEndScreen.add(retry2);

		//Create button "New Game"
		JButton newGame2 = new JButton("New Game");
		newGame2.setBounds(565, 400, 150,80);
		newGame2.addActionListener(new newGameListener());
		GameEndScreen.add(newGame2);

		//Create button "Quit"
		JButton quit2 = new JButton("Quit");
		quit2.setBounds(845, 400, 150, 80);
		quit2.addActionListener(new panelChangeListener("GameChooserScreen"));
		GameEndScreen.add(quit2);

		//Set the layout manager to null
		GameEndScreen.setLayout(null);
		
		//Add EndGameScreen to mainPanel
		mainPanel.add(GameEndScreen, "GameEndScreen");
	}
	
	/** Listener for the color buttons of the color Palette
	*/
	static class ColorListener implements ActionListener {
		private int colorIndex;
		
		/** Constructor specifying the index of the color represented by this button
		*/
		public ColorListener (int colorIndex) {
			this.colorIndex = colorIndex;
		}
		
		/** On click of this color button, it modifies the currently selected color
		*/
		public void actionPerformed (ActionEvent event) {
			SimulatorV2.currentColor = colorIndex;
		}
	}
	
	/** Listener for the vertex slider when the selected value changes
	*/
	static class vertexSliderListener implements ChangeListener {
		public void stateChanged (ChangeEvent e) {
			int numVertices = vertexSlider.getValue();
			int maxNumEdges = (numVertices * (numVertices-1)) / 2;
			
			//Modify the scale and range of the slider
			edgeSlider.setMaximum(maxNumEdges);
			
			//Show the current number of vertices
			verticesLabel.setText("VERTICES: " + numVertices);
		}
	}
	
	/** Listener for the edge slider when the selected value changes
	*/
	static class edgeSliderListener implements ChangeListener {
		public void stateChanged (ChangeEvent e) {
			int numEdges = edgeSlider.getValue();
			
			//Show the current number of vertices
			edgeLabel.setText("EDGES: " + numEdges);
		}
	}
	
	/** Listener for the Retry button
	*/
	static class retryListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			game.resetTimer();							//Reset the timer
			//If the game was over, then set it back to false
			if (game.gameOver) game.gameOver = false;
			//Reset the color of each vertex
			canvas.reset();
			
			//Show the GameScreen, and start the timer again
			CL.show(mainPanel, "GameScreen");
			game.startTimer();
		}
	}
	
	/** Listener for the newGame button
	*/
	static class newGameListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			game.resetTimer();							//Reset the timer at 0
			if (game.gameOver) game.gameOver = false;
			graph = null;								//Reset the graph at null
			
			//Create a new GameMode object from the same class as previously
			if (game instanceof GameMode1) {
				game = new GameMode1();
			}
			else if (game instanceof GameMode2) {
				game = new GameMode2();
			}
			else {
				game = new GameMode3();
			}
			
			//Remove the old simulator
			GameScreen.remove(canvas);
			
			//Reset the simulator / create a new one
			canvas = new SimulatorV2(930, 720);
			
			//And add it to the JPanel
			GameScreen.add(canvas, BorderLayout.WEST);
			
			
			
			//Show the GameScreen again
			CL.show(mainPanel, "GameScreen");
		}
	}
	
	/** Listener for the Continue button
	*/
	static class continueListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			//Show the GameScreen again, and continue the timer
			CL.show(mainPanel, "GameScreen");
			game.startTimer();
		}
	}
	
	/** Listener for the Quit button
	*/
	static class quitListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			//Stop the timer
			game.pauseTimer();
			//Reset the graph
			graph = null;
			
			//Reset the simulator
			canvas = new SimulatorV2(930, 720);
		}
	}
	
	/** Creates a listener class for the openGraph button
	*/
	static class openGraphListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			//Uses a JFileChooser for the User to select a .txt file to load the Graph described in the txt file
			JFileChooser chooser = new JFileChooser();
			if (chooser.showOpenDialog(GameScreen) == JFileChooser.APPROVE_OPTION) {
				//The User selected a file, then we call readGraph with this file
				File graphFile = chooser.getSelectedFile();
				String[] inputFile = {graphFile.getPath()};
				graph = ReadGraphV2.readGraph(inputFile);
				
				if (graph.n < 50)
					graph.chromaticNumber = ChromaticNumberV2.chromaticNum(graph);
				
				game.currentGraph = graph;
				canvas.showGraph(graph, game instanceof GameMode3);
				game.startTimer();
			}
			//Else, the operation was cancelled, the User closed the window without selecting a file
		}
	}
	
	/** Creates a listener class for the generateGraph button
	*/
	static class generateGraphListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			//First, retrieve the values of the slider for the number of vertices and the value of the slider for the number of Edges
			int numVertices = vertexSlider.getValue();
			int numEdges = edgeSlider.getValue();
			
			graph = game.generateGraph(numVertices, numEdges);
			graph.chromaticNumber = ChromaticNumberV2.chromaticNum(graph);
			
			game.currentGraph = graph;
			canvas.showGraph(graph, game instanceof GameMode3);
			game.startTimer();
		}
	}
	
	/** Creates a listener class for the Hint buttons
	*/
	static class hintListener implements ActionListener {
		private String hintType;
		
		public hintListener (String type) {
			hintType = type;
		}
		
		public void actionPerformed (ActionEvent e) {
			if (hintType.equals("vertex")) {
				game.vertexHint();
			}
			else {
				game.colorHint();
			}
		}
	}
	
	/** Create a listener class for the GameModeSelection buttons
	*/
	static class gameModeListener implements ActionListener {
		private int gameModeNumber;
		
		/** Constructor specifying the number of the button (and GameMode class associated with it)
		*/
		public gameModeListener (int n) {
			gameModeNumber = n;
		}
		
		public void actionPerformed (ActionEvent e) {
			//Create a gameMode according to the button the User clicked on
			if (gameModeNumber == 1) {			//The User clicked on GameMode1 button
				game = new GameMode1();
			}
			else if (gameModeNumber == 2) {		//The User clicked on GameMode2 button
				game = new GameMode2();
			}
			else {								//The User clicked on GameMode3 button
				game = new GameMode3();
			}
			
			//Update the display of the timer
			game.updateTimeJLabel();
			//Switch to the GameScreen panel
			CL.show(mainPanel, "GameScreen");
		}
	}

	/** This method recolors JButton button with the color of the color Palette at index colorIndex
		@param button, the JButton to be recolored
		@param colorIndex, the index of the currently selected color
	*/
	public static void colorButton(JButton button, int colorIndex) {
		button.setBackground(colors[colorIndex]);
		button.repaint();
	}
	
	/** This method should update the time JLabel, which shows the time
		@param newTime, the String that should be shown in the JLabel
	*/
	public static void updateTime(String newTime) {
		timer.setText(newTime);
	}
	
	/** This method should update the Text of all the JLabels in the PauseScreen before it is shown
		These JLabels include:
			- time, which shows the time the User has used so far
			- userColors, which is the number of colors the User used
			- percentVerticesColored, which states how many vertices out of the number of Vertices he has already colored
	*/
	public static void updatePauseScreen() {
		int userCols;
		int numColoredVertices;
		int numVertices;
		if (graph != null) {
			userCols = graph.colors.length-1;
			numColoredVertices = graph.coloredVertices.length;
			numVertices = graph.coloredVertices.length + graph.blankVertices.length;
		}
		else {
			userCols = 0;
			numColoredVertices = 0;
			numVertices = 0;
		}
		time.setText("Time taken: " + game.TimeToString(game.timeElapsed));
		userColors.setText("You used " + userCols + " colors");
		percentVerticesColored.setText(String.format("You colored %02d/%02d vertices", numColoredVertices, numVertices));
	}
	
	/** This method should update the Text of all the JLabels in the GameEndScreen before it is shown
		These JLabels include:
			- time2, which shows the time the User took to finish the Game
			- userColors2, which is the number of colors the User used
			- chromaNum, the chromatic number
			- percentVerticesColored, which states how many vertices out of the number of Vertices he did color
			- conclusion[0] and conclusion[1] which tell the User whether he/she won or lost
	*/
	public static void updateGameEndScreen() {
		int userCols = graph.colors.length-1;
		int chromaNumber = graph.getChromaticNumber();
		
		time2.setText("Time taken: " + game.TimeToString(game.timeElapsed));
		userColors2.setText("You used " + userCols + " colors");
		percentVerticesColored.setText(String.format("You colored %02d/%02d vertices", graph.coloredVertices.length, graph.coloredVertices.length + graph.blankVertices.length));
		
		if (graph.n < 50) {
			chromaNum.setText("The chromatic number was: " + chromaNumber);
			if (userCols == chromaNumber) {
				conclusion[0].setText("Great !");
				conclusion[1].setText("You managed to only use as many colours as the chromatic number");
			}
			else {
				conclusion[0].setText("Bad luck !");
				conclusion[1].setText("You used " + (userCols-chromaNumber) + " more colors than were needed");
			}
		}
	}
}