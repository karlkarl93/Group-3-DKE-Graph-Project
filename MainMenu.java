import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.Component;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.border.*;
import javax.swing.*;

public class MainMenu2
{
    // making new buttons, panels, and frames
    JButton b1 = new JButton("Start");
    JButton b2 = new JButton("Saves");
    JButton b3 = new JButton("Exit");
    JButton b4 = new JButton("To The Bitter End");
    JButton b5 = new JButton("Best Upper Bound");
    JButton b6 = new JButton("Random Order");
    JButton b7 = new JButton("Back");
    JFrame MainFrame = new JFrame("Main Menu");
    JFrame ModesFrame = new JFrame("Modes Chooser");
    JPanel panelcont = new JPanel();
    JPanel p1 = new JPanel();
    JPanel p2 = new JPanel();
    // making a layout
    CardLayout cl = new CardLayout();

    // setting bounds, colors, labels, etc.
    public MainMenu2()
    {
        panelcont.setLayout(cl);
        p1.setLayout(null);
        
        JLabel l1 = new JLabel("Graph Coloring!");
        l1.setFont(new Font("Arial", Font.BOLD, 30));
        l1.setBounds(150, 100, 300, 35);

        JLabel l2 = new JLabel("Welcome to: ");
        l2.setFont(new Font("Arial", Font.BOLD, 20));
        l2.setBounds(150, 20, 200, 35);

        b1.setBackground(Color.green);
        b2.setBackground(Color.orange);
        b3.setBackground(Color.red);

        b1.setBounds(100, 250, 100, 35);
        b2.setBounds(200, 250, 100, 35);
        b3.setBounds(300, 250, 100, 35);
        panelcont.add(p1, "1");
        panelcont.add(p2, "2");

        p1.add(b1);
        p1.add(b2);
        p1.add(b3);
        p1.add(l1);
        p1.add(l2);

        p2.setLayout(null);
        
        JLabel l3 = new JLabel("Please Choose A Game Mode");
        l3.setFont(new Font("Arial", Font.BOLD, 15));
        l3.setBounds(125, 100, 250, 35);

        b4.setBackground(Color.green);
        b5.setBackground(Color.orange);
        b6.setBackground(Color.red);

        b4.setBounds(150, 200, 200, 35);
        b5.setBounds(150, 250, 200, 35);
        b6.setBounds(150, 300, 200, 35);
        b7.setBounds(150, 350, 200, 35);

        p2.add(b4);
        p2.add(b5);
        p2.add(b6);
        p2.add(b7);
        p2.add(l3);
        
        // letting the layout only show p1 with the main buttons
        cl.show(panelcont, "1");
        
        // action listener for the start button
        b1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                cl.show(panelcont, "2");
            }
        });

        // action listener for the back button
        b7.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
               cl.show(panelcont, "1");
            }
        });

        // adding the layout to the frame
        MainFrame.add(panelcont);
        MainFrame.setVisible(true);
        MainFrame.setSize(500, 500);
        MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    // test it
    public static void main(String[] args)
    {
        MainMenu2 Menu = new MainMenu2();

    }
}