import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class VertexButton extends JButton
{
    Vertex vertex;
    public VertexButton(Vertex vertex)
    {
        this.vertex = vertex;
        setPreferredSize(new Dimension(30,30));
        //setBorder(null);
    }
    
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(getBackground());
        g2D.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
        //g2D.setStroke(new BasicStroke(4));
        //g2D.drawOval(2, 2, getSize().width - 4, getSize().height - 4);
    }

    public static void main(String[] args)
    {
        JButton b1 = new RoundButton("test");
        JButton b2 = new RoundButton("Test2");
        
        JFrame frame = new JFrame("Round Buttons");
        JPanel panel = new JPanel();
        b1.setBackground(Color.white);
        b2.setBackground(Color.white);
        frame.add(panel);
        panel.add(b1);
        frame.setSize(100, 100);
        frame.setVisible(true);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        b1.addActionListener(new ActionListener()
                            {
            public void actionPerformed(ActionEvent actionEvent)
            {
                if (actionEvent.getSource() instanceof JButton) {
                    JButton b = (JButton) actionEvent.getSource();
                    Color initialBackground = b.getBackground();
                    Color background = Color.red;
                    System.out.println("Action heard");
                    if (background != null)
                    {
                        System.out.println("Action performed");
                        b.setBackground(background);
                        b.repaint();
                    }
                }
            }
        });
    }
}
