import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.*;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.DefaultBoundedRangeModel;
import java.util.Hashtable;


public class ProjectFrame2
{
    private static final int x = 1280;
    private static final int y = 720;

    JFrame ProjectFrame = new JFrame();
    Font myFont = new Font("SansSerif Plain", Font.BOLD, 15);
    JButton mode1 = new JButton("Game mode 1");
    JButton mode2 = new JButton("Game mode 2");
    JButton mode3 = new JButton("Game mode 3");
    JButton quit = new JButton("Quit");
    JButton pause = new JButton("Pause");
    JLabel introduction = new JLabel("<html>Please select the game mode you want to play.<html>");
    JLabel description1 = new JLabel("<html>Game mode 1<br> <br>To The Bitter End<br>   <br>Description :<br>You will have to colour the graph as quickly as possible.<br>You will not be allowed to finish until the minimum number of colours has been reached.<html>");
    JLabel description2 = new JLabel("<html>Game mode 2<br> <br>Best Upper Bound<br>   <br>Description :<br>You will be given a fixed amount of time.<br>You will have to find a colouring with as few colours as possible in the given time.<html>");
    JLabel description3 = new JLabel("<html>Game mode 3<br> <br>Random Order<br>   <br>Description :<br>You will have to pick the colours of the vertices in the order that the computer generated randomly for the vertices.<br>Once the colour of a vertex has been chosen, it cannot be change again.<br>Your goal is to use as few colours as possible.<html>");
    JPanel MainPanel = new JPanel();
    JPanel GameChooser = new JPanel();
    JPanel GameScreen = new JPanel();
    JPanel PauseScreen = new JPanel();
    
    JPanel userMenu = new JPanel();
    JPanel graphShow = new JPanel();
    JButton hint = new JButton("I'm lost, give me a hint");
    
    CardLayout CL = new CardLayout();

    public ProjectFrame2()
    {
        //MainPanel Layout + add MainPanel to MainFrame
        MainPanel.setLayout(CL);
        GameScreen.setLayout(null);
        ProjectFrame.add(MainPanel);

        //GAMECHOOSER PANEL
        //Add the Buttons to GameChooser panel
        mode1.setBounds(170,y-250,150,80);
        mode1.setForeground(new Color(62,118,236));
        mode1.setFont(myFont);
        GameChooser.add(mode1);
        //GameChooser.setLayout(null);

        mode2.setBounds(565,y-250,150,80);
        mode2.setForeground(new Color(238,16,16));
        mode2.setFont(myFont);
        GameChooser.add(mode2); 
        //GameChooser.setLayout(null);

        mode3.setBounds(960,y-250,150,80);
        mode3.setForeground(new Color(23,154,19));
        mode3.setFont(myFont);
        GameChooser.add(mode3);
        //GameChooser.setLayout(null);  

        //Add the text to the GameChooser panel
        introduction.setBounds((x/2)-225,20,450,80);
        introduction.setForeground(Color.black);
        introduction.setFont(new Font("SansSerif Plain", Font.BOLD, 18));
        GameChooser.add(introduction);
            //it was here

        description1.setBounds(95,140,300,250);
        description1.setForeground (new Color(62,118,236));
        description1.setFont(new Font("SansSerif Plain", Font.BOLD, 12));
        GameChooser.add(description1);
        
        description2.setBounds(490,130,300,250);
        description2.setForeground (new Color(238,16,16));
        description2.setFont(new Font("SansSerif Plain", Font.BOLD, 12));
        GameChooser.add(description2);

        description3.setBounds(885,150,300,250);
        description3.setForeground (new Color(23,154,19));
        description3.setFont(new Font("SansSerif Plain", Font.BOLD, 12));
        GameChooser.add(description3);

        GameChooser.setLayout(null);

        //Add GameChooser panel to the MainPanel
        MainPanel.add(GameChooser, "GameChooser");

        //Add GameScreen panel to MainPanel
        MainPanel.add(GameScreen, "GameScreen");

        //Add PauseScreen to MainPanel
        MainPanel.add (PauseScreen, "PauseScreen");

        //Add GameScreen to MainPanel
        MainPanel.add(GameScreen, "GameScreen");
        
        //GAME SCREEN PANEL
        //main panel where both the userMenu and game panel will be on. when pushed on the game mode button, this is the panel that we have to switch to.
        GameScreen.add(userMenu);
        GameScreen.setLayout(new BorderLayout());
        GameScreen.add(userMenu, BorderLayout.EAST);
        GameScreen.add(graphShow, BorderLayout.WEST);

        //the panel which the user uses for colors, edges, vertecis, pause, and hint
        userMenu.setPreferredSize(new Dimension(350, 720));
        userMenu.setBackground(Color.blue);
        userMenu.setLayout(null);
        pause.setBounds(100, 550, 150, 35);
        hint.setBounds(80, 400, 200, 35);
        userMenu.add(pause);
        userMenu.add(hint);

        //SLIDERS
        JPanel sliderPanel = new JPanel();
        JLabel label = new JLabel("Number of vertices is: ");
        JLabel label2 = new JLabel("Number of edges is: ");
        JSlider slider1 = new JSlider(0, 40);
        JSlider slider2 = new JSlider(0, 60);

        slider1.setMajorTickSpacing(10);
        slider1.setMinorTickSpacing(5);
        slider1.setPaintTicks(true);
        slider1.setPaintLabels(true);
        
        slider2.setMajorTickSpacing(15);
        slider2.setMinorTickSpacing(5);
        slider2.setPaintTicks(true);
        slider2.setPaintLabels(true);

        Hashtable position = new Hashtable();
        position.put(0, new JLabel("0"));
        position.put(20, new JLabel("20"));
        position.put(40, new JLabel("40"));
        position.put(60, new JLabel("60"));

        slider1.setLabelTable(position);
        slider2.setLabelTable(position);

        slider1.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e) 
            {
                label.setText("Number of vertices is: " + ((JSlider)e.getSource()).getValue());
            }
        });

        slider2.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e) 
            {
                label2.setText("Number of edges is: " + ((JSlider)e.getSource()).getValue());
            }
        });
        
        JButton accept = new JButton("Accept");
        //accept.addActionListener(newActionListener(){})}; accept the input from the sliders
        sliderPanel.add(slider1, BorderLayout.NORTH);
        sliderPanel.add(label, BorderLayout.NORTH);
        sliderPanel.add(slider2, BorderLayout.CENTER);
        sliderPanel.add(label2, BorderLayout.CENTER);
        sliderPanel.add(accept, BorderLayout.EAST);
        userMenu.add(sliderPanel);
        sliderPanel.setBounds(50, 200, 250, 150);
        
        //the panel where the graph will be shown
        graphShow.setPreferredSize(new Dimension(930, 720));
        graphShow.setBackground(Color.red);

        //PAUSESCREEN PANEL
        JLabel timeEl = new JLabel("<html>Time Elapsed: <html>");
        timeEl.setBounds(490,150,200,35);
        timeEl.setFont(new Font("SansSerif Plain", Font.BOLD, 12));
        
        JLabel usedCol = new JLabel("<html># Of Used Colors: <html>");
        usedCol.setBounds(490,200,200,35);
        usedCol.setFont(new Font("SansSerif Plain", Font.BOLD, 12));
        
        JLabel vertCol = new JLabel("<html># Of Vertices Colored: <html>");
        vertCol.setBounds(490,250,200,35);
        vertCol.setFont(new Font("SansSerif Plain", Font.BOLD, 12));
        
        quit.setBounds(x-160,y-150,120,80);
        quit.setForeground(new Color(238,16,16));
        quit.setFont(myFont);

        JButton cont = new JButton("Continue");
        cont.setBounds(170,y-150,120,80);
        cont.setForeground(new Color(23,154,19));
        cont.setFont(myFont);
        
        PauseScreen.setLayout(null);
        PauseScreen.add(timeEl);
        PauseScreen.add(usedCol);
        PauseScreen.add(vertCol);
        PauseScreen.add(quit);
        PauseScreen.add(cont);
        
        //Lets the MainPanel be the panel where we can display and switch
        //the different panels on. First, we want to show the panel with
        //the different gamemodes.
        CL.show(MainPanel, "GameChooser");

        //When mode1 button is pressed, the panel with the different gamemodes
        //will be switched with the gamescreen panel.
        mode1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                CL.show(MainPanel, "GameScreen");
            }
        });

        //When pause button is pressed, the gamescreen panel will be switched
        //with the pause screen panel.
        pause.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                CL.show(MainPanel, "PauseScreen");
            }
        });

        cont.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                CL.show(MainPanel, "GameScreen");
            }
        });

        //When quit button is pressed, the pause screen panel will be switched
        //with the game chooser screen again and then the user can choose another
        //game mode.
        quit.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                CL.show(MainPanel, "GameChooser");
            }
        });

        ProjectFrame.setTitle("Introduction Board");
        ProjectFrame.setSize(x,y);
        ProjectFrame.getContentPane().setBackground(Color.white);
        ProjectFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ProjectFrame.setVisible(true);
        ProjectFrame.setResizable(false);
        ProjectFrame.setLocationRelativeTo(null);
    }
}