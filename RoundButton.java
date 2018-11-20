import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.*;

public class RoundButton extends JButton
{
    public RoundButton(String label)
    {
        super(label);
       

        // create a circle instead of an ovalform
        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width, size.height);
        setPreferredSize(size);

        // not painting the background, so we can make a custom (round) background
        setContentAreaFilled(false);

    }

    // paint the round background white
    protected void paintComponent(Graphics g)
    {
        g.setColor(Color.white);
        g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
        super.paintComponent(g);

    }

    // paint the border of the button black
    protected void paintBorder(Graphics g)
    {
        g.setColor(Color.black);
        g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
    
    }

    // hit detection
   Shape shape;
    public boolean contains(int x, int y)
    {
        // if button changed size, make a new shape object
        if (shape == null || !shape.getBounds().equals(getBounds()))
        {
            shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        }
        return shape.contains(x, y);
    
    }

    // test it 
    public static void main(String[] args)
    {
       JButton b = new RoundButton("I'm a button, change my mind");
        b.setBackground(Color.white);

        JFrame frame = new JFrame("BUTTONNNSSSS");
        frame.add(b);
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}