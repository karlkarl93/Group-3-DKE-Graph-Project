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

class Simulator extends JPanel {
    
    public static void main(String[] args) {
        // 1. Set up Graph and simulator
        Graph graph = new Graph(Edge.DEFAULT_EDGE_ARRAY(), 18);
        final int FRAMEWIDTH = 930;
        final int FRAMEHEIGHT = 720;
        Simulator simulator = new Simulator(graph, FRAMEWIDTH, FRAMEHEIGHT);

        // Make a window
        JFrame frame = new JFrame();
        frame.setTitle("Simulator");
        frame.setSize(FRAMEWIDTH, FRAMEHEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(simulator);

        frame.setVisible(true);
    }
    
    // MARK: Fields
    double canvasWidth;
    double canvasHeight;
    double[][] Positions;
    double[][] Projection;
    Edge[] edges;
    int currentColor = 0;
    
    Color[] colorPallette = {
    new Color(242,215,238),
    new Color(211,188,192),
    new Color(165,102,139),
    new Color(105,48,109),
    new Color(14,16,61)
    };

    protected Simulator(Graph graph, double canvasWidth, double canvasHeight) {
        this.edges = graph.edges;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        //positionVerticesRandomly(20,70,19);
        positionVerticesInCircle(graph.vertices.length);
        
        class VertexRecolorListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                if (!(e.getSource() instanceof VertexButton)) { return; }
                
                VertexButton button = (VertexButton) e.getSource();
                if (graph.checkIfColorIsLegalForVertex(button.vertex, currentColor)) {
                    button.setBackground(colorPallette[currentColor]);
                    button.repaint();
                    graph.setColor(button.vertex, currentColor);
                } else {
                    System.out.println("Impossible to color vertex with color " + currentColor);
                    if (currentColor < colorPallette.length -1) currentColor ++;
                    else currentColor = 0;
                }
            }
        }
        

        Projection = Positions; //project(Positions);
        this.setLayout(null);
        for (int i = 1; i < Projection[0].length; i++) {
            VertexButton button = new VertexButton(graph.vertices[i]);
            button.setBackground(Color.lightGray);
            button.addActionListener(new VertexRecolorListener());
            button.setBounds((int) (canvasWidth/2 + Projection[0][i] - 15), (int) (canvasHeight/2 +Projection[1][i] - 15), 30, 30);
            this.add(button);
        }
    }

    private void positionVerticesInCircle(int vertexCount) {
        Positions =  new double[4][vertexCount];
        double theta = 0;
        int radius = 300;
        
        for (int i = 1; i < vertexCount; i++) {
            
            Positions[0][i] = radius * Math.cos(theta);
            Positions[1][i] = radius * Math.sin(theta);
            theta += 2 * Math.PI / (vertexCount - 1);
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
        
        // Uncomment the following line when transformations or translations are performed on the vertex positions
        //Projection = project(Positions);
        
        for (int i = 0; i < edges.length; i++) {
            Edge edge = edges[i];
            //System.out.println(Projection[0][edge.u]);
            Point2D.Double pointU = new Point2D.Double(canvasWidth/2 + Projection[0][edge.u], canvasHeight/2 + Projection[1][edge.u]);
            Point2D.Double pointV = new Point2D.Double(canvasWidth/2 + Projection[0][edge.v], canvasHeight/2 + Projection[1][edge.v]);
            Line2D.Double line = new Line2D.Double(pointU, pointV);
            g2D.draw(line);
        }
    }

    protected static double[][] rotate(double[][] D, double x, double y) {
        double[][] X = {
            {1, 0, 0, 0},
            {0, Math.cos(x), -1*Math.sin(x), 0},
            {0, Math.sin(x),  Math.cos(x), 0},
            {0, 0, 0, 1}};
        
        
        double[][] Y = {
            {Math.cos(y), 0, Math.sin(y), 0},
            {0, 1, 0, 0},
            {-1*Math.sin(y), 0, Math.cos(y), 0},
            {0, 0, 0, 1}};
        
        return Auxilaries.matrixMultiply(Y, Auxilaries.matrixMultiply(X,D));
    }
    
    protected static double[][] project (double[][] D) {
        // Set observer coordinates
        double b = 0; double c = 0; double d = 5;
        
        // Set projection matrix
        double[][] P = {
            {1, 0, (-b/d), 0},
            {0, 1, (-c/d), 0},
            {0, 0, 0     , 0},
            {0, 0, -1/d  , 0}};
        
        // Project onto plane
        D = Auxilaries.matrixMultiply(P, D);
        double[][] newD = new double[2][D[0].length];
        for (int j = 0; j < D[0].length; j++) {
            newD[0][j] = D[0][j];
            newD[1][j] = D[1][j];
        }
        for (int j = 0; j < D[0].length; j++) {
            System.out.println("One entry " + j);
            newD[0][j] = newD[0][j] / 1 - 1/d;
            newD[1][j] = newD[1][j] / 1 - 1/d;
        }
        System.out.println("Length during call: " + newD[0].length);
        return newD;
    }
}
