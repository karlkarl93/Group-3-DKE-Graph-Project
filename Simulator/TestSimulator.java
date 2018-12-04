import javax.swing.*;

public class TestSimulator {
    public static void main(String[] args) {
        Edge[] edges = {
            new Edge(1,6),
            new Edge(1,5),
            new Edge(2,5),
            new Edge(2,4),
            new Edge(3,4),
            new Edge(5,3),
            new Edge(6,8),
            new Edge(7,5),
            new Edge(8,1),
            new Edge(9,2),
            new Edge(10,8),
            new Edge(11,7),
            new Edge(12,3),
            new Edge(13,5),
            new Edge(14,6),
            new Edge(15,8),
            new Edge(16,2),
            new Edge(17,1),
            new Edge(18,4),
        };
        int vertexCount = 18;
        int frameWidth = 930;
        int frameHeight = 720;
        Graph graph = new Graph(edges, vertexCount);
        Simulator simulator = new Simulator(graph, frameWidth, frameHeight);
        
        // Make a window
        JFrame frame = new JFrame();
        frame.setTitle("Simulator");
        frame.setSize(frameWidth, frameHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(simulator);
        
        frame.setVisible(true);
    }
}
