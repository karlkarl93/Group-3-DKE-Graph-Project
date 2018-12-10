import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.JLabel;

class SimulatorV2 extends JPanel {    
    // MARK: Fields
    protected int canvasWidth;
    protected int canvasHeight;
    protected double[][] Positions;
    protected Edge[] edges  = new Edge[0];
    protected static int currentColor = 0;
	protected Graph graph;
	protected VertexButton[] vertexButtons = new VertexButton[0];
	
	/** Default constructor, just sets the Size of this JPanel and adds the timer JLabel
		@param timer JLabel, the label showing the timer
	*/
	protected SimulatorV2 (int canvasWidth, int canvasHeight) {
		//Save the canvasWidth and height
		this.canvasWidth = canvasWidth;
		this.canvasHeight = canvasHeight;
		
		//Initialize this JPanel, set the Size
		this.setPreferredSize(new Dimension(canvasWidth, canvasHeight));
		
		//Repaint the graph to cover previous drawings
		this.repaint();
	}

	/** Shows the Graph
	*/
    protected void showGraph(Graph graph, boolean isGameMode3) {
		this.edges = graph.edges;
		this.graph = graph;
        
		//Position the vertices in circle
		positionVerticesInCircle(graph.vertices.length);
		this.setLayout(null);
		
		vertexButtons = new VertexButton[Positions[0].length];
		
		//Create all the buttons, and add them to this JPanel
		for (int i = 1; i < Positions[0].length; i++) {
			vertexButtons[i] = new VertexButton(graph.vertices[i]);
			vertexButtons[i].setBackground(Color.lightGray);
			
			if (! isGameMode3) {
				vertexButtons[i].addActionListener(new VertexRecolorListener());
			}
			else {
				vertexButtons[i].addActionListener(new RandomOrderVertexRecolorListener(i));
			}
			vertexButtons[i].setBounds((int) (canvasWidth/2 + Positions[0][i] - 15), (int) (canvasHeight/2 +Positions[1][i] - 15), 30, 30);
			this.add(vertexButtons[i]);
		}
		
		//To refresh the JPanel and actually show the graph
		this.repaint();
    }
	
	/** Listener for the VertexButtons
	*/
	class VertexRecolorListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			VertexButton button = (VertexButton) e.getSource();
			
			//Check if the coloring is legal
			if (graph.checkIfColorIsLegalForVertex(button.vertex, currentColor)) {
				//Recolor the button
				TestGraph.colorButton(button, currentColor);
				graph.setColor(button.vertex, currentColor);
				
				//Check if the game is over
				TestGraph.game.isGameOver();
			}
			//Else, do nothing
		}
	}
	
	/** Listener for the VertexButtons for GameMode3
	*/
	class RandomOrderVertexRecolorListener implements ActionListener {
		private int vertexIndex;
		private boolean vertexColored = false;
		
		public RandomOrderVertexRecolorListener (int x) {
			vertexIndex = x;
		}
		
		public void actionPerformed(ActionEvent e) {
			if ((vertexIndex == 1) || ((graph.vertices[vertexIndex-1].color != Vertex.DEFAULT_BLANK_COLOR) && !vertexColored)) {
				VertexButton button = (VertexButton) e.getSource();
				
				//Check if the coloring is legal
				if (graph.checkIfColorIsLegalForVertex(button.vertex, currentColor)) {
					//Recolor the button
					TestGraph.colorButton(button, currentColor);
					graph.setColor(button.vertex, currentColor);
					
					//Check if the game is over
					TestGraph.game.isGameOver();
					
					//Show the next Vertex to be colored
					showVertexToColor(vertexIndex+1);
					
					//Set vertexColored to true, as the User has colored the vertex and thus is not allowed to recolor it anymore
					vertexColored = true;
				}
				//Else, do nothing
			}
		}
	}
	
	/** Should use some visual effect to display the next Vertex to be colored in GameMode3
		@param vertexIndex, the index of the vertex that should be marekd
	*/
	public void showVertexToColor(int vertexIndex) {
		
	}
	
	/** Resets the color of all vertices
	*/
	public void reset() {
		//We can just say 0, because coloredVertices is progressively emptied from its elements, thus its length goes to 0
		while (0 < graph.coloredVertices.length) {
			graph.setColor(graph.coloredVertices[0], Vertex.DEFAULT_BLANK_COLOR);
		}
		
		for (int i = 1; i < vertexButtons.length; i ++) {
			vertexButtons[i].setBackground(Color.lightGray);
			vertexButtons[i].repaint();
		}
	}

    private void positionVerticesInCircle(int vertexCount) {
        Positions =  new double[4][vertexCount];
        double theta = 0;
        int radius = 300;
        
        for (int i = 1; i < vertexCount; i++) {
            
            Positions[0][i] = radius * Math.cos(theta);
            Positions[1][i] = radius * Math.sin(theta);
            theta += 2 * Math.PI / (vertexCount-1);
        }
    }
    
	
	private void positionVerticesRandomly(double maxDist, double minDist, int vertexCount) {
        
        Positions =  new double[4][vertexCount];
        
        for (int i = 1; i< vertexCount; i++) {
            int signX = randomSign(); int signY = randomSign();
            double currentX = signX * (Math.random() * canvasWidth/2);
            double currentY = signY * (Math.random() * canvasHeight/2);
            //double currentZ = 0; //canvasHeight/2 + (Math.random() * canvasHeight/2);
            
            for (int j = 1; j < i; j ++) {
                System.out.println("Inside For loop of position vertices");
                double adjacentX = Positions[0][j];
                double adjacentY = Positions[1][j];
                //double adjacentZ = 0; //Positions[2][j];
                if (distance(adjacentX, adjacentY, currentX, currentY) < minDist) {
                    currentX += signX * 50;
                    currentY += signY * 50;
                    j = 1;
                }
            }
            
            Positions[0][i] = currentX;
            Positions[1][i] = currentY;
            //Positions[2][i] = currentZ;
        }
        
    }
    
    private int randomSign() {
        boolean positive = (Math.random() > 0.5);
        int sign = -1;
        if (positive) sign = 1;
        return sign;
    }
    
    private double distance(double x1, double y1, /**double z1,*/ double x2, double y2 /**,double z2*/) {
        double res = Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2) /**+ Math.pow(z2-z1, 2)*/);
        return res;
    }
    
    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        
        for (int i = 0; i < edges.length; i++) {
            Edge edge = edges[i];
            //System.out.println(Projection[0][edge.u]);
            Point2D.Double pointU = new Point2D.Double(canvasWidth/2 + Positions[0][edge.u], canvasHeight/2 + Positions[1][edge.u]);
            Point2D.Double pointV = new Point2D.Double(canvasWidth/2 + Positions[0][edge.v], canvasHeight/2 + Positions[1][edge.v]);
            Line2D.Double line = new Line2D.Double(pointU, pointV);
            g2D.draw(line);
        }
    }
	
	/** Class for the VertexButtons
	*/
	public class VertexButton extends JButton {
		Vertex vertex;
		
		public VertexButton(Vertex vertex) {
			this.vertex = vertex;
			setPreferredSize(new Dimension(30,30));
			setBorder(null);
			setContentAreaFilled(false);
		}
		
		protected void paintComponent(Graphics g) {
			Graphics2D g2D = (Graphics2D) g;
			g2D.setColor(getBackground());
			g2D.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
			super.paintComponent(g);
			//g2D.setStroke(new BasicStroke(4));
			//g2D.drawOval(2, 2, getSize().width - 4, getSize().height - 4);
		}
		
		protected void paintOrder (Graphics g) {
			g.setColor(Color.black);
			g.drawOval(0, 0, getSize().width-1, getSize().height-1);
		}
	}
}