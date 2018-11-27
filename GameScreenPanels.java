import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.*;

 public class GameScreenPanels
{
    public static void main(String[] args)
    {
      JFrame frame = new JFrame("MainMenu");
      JPanel mainGame = new JPanel();
      JPanel userMenu = new JPanel();
      JPanel graphShow = new JPanel();
      JButton pause = new JButton("Pause");

      final int FRAME_WIDTH = 1280;
      final int FRAME_HEIGHT = 720;
      
      //main panel where both the userMenu and game panel will be on. when pushed on the game mode button, this is the panel that we have to switch to.
      mainGame.add(userMenu);
      mainGame.setLayout(new BorderLayout());
      mainGame.add(userMenu, BorderLayout.EAST);
      mainGame.add(graphShow, BorderLayout.WEST);

      //the panel which the user uses for colors, edges, vertecis, pause, and hint
      userMenu.setPreferredSize(new Dimension(350, 720));
      userMenu.setBackground(Color.blue);
      userMenu.setLayout(null);
      pause.setBounds(100, 550, 150, 35);
      userMenu.add(pause);

      //the panel where the graph will be shown
      graphShow.setPreferredSize(new Dimension(930, 720));
      graphShow.setBackground(Color.red);

      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
      frame.add(mainGame);
      
      frame.setVisible(true);
    }
 }
