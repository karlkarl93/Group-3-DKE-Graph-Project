import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.Component;
import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.JColorChooser;

public class RoundButton3 extends JButton
{
  
    public RoundButton3(String label)
    {

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
        g.setColor(getBackground());
        g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
        super.paintComponent(g);

    }

    // paint the border of the button black
    protected void paintBorder(Graphics g)
    {
        g.setColor(Color.black);
        g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
    
    }


    // test it 
    public static void main(String[] args)
    {
        JButton b = new RoundButton3("test");

        JFrame frame = new JFrame("BUTTONNNSSSS");
        JPanel panel = new JPanel();
        b.setBackground(Color.white);
        frame.add(panel);
        panel.add(b);
        frame.setSize(800, 800);
        frame.setVisible(true);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        b.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                Color initialBackground = b.getBackground();
                Color background = JColorChooser.showDialog(null, "Change the color", initialBackground);
                    if (background != null)
                    {
                        b.setBackground(background);
                        b.repaint();
                    } 
            }
        });
         
    }
}