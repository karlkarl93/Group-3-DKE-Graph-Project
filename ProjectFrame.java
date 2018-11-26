import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.*;
import java.awt.*;

public class ProjectFrame extends JFrame {

private static final int x = 600;
private static final int y = 600;

  public static void main(String[] args) {
  JFrame ProjectFrame = new JFrame();
  ProjectFrame.setTitle("Introduction Board");
  ProjectFrame.setSize(x,y);
  ProjectFrame.getContentPane().setBackground(Color.white);
  ProjectFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  ProjectFrame.setLocationRelativeTo(null);


  Font myFont = new Font("SansSerif Plain", Font.BOLD, 15);



  JButton button1 = new JButton("Game mode 1");
  button1.setBounds((x/10),y-150,120,80);
  button1.setForeground(new Color(62,118,236));
  ProjectFrame.add(button1);
  button1.setFont(myFont);

  ProjectFrame.setLayout(null);


  JButton button2 = new JButton("Game mode 2");
  button2.setBounds((x/2)-50,y-150,120,80);
  ProjectFrame.add(button2);
  button2.setForeground(new Color(238,16,16));
  ProjectFrame.setLayout(null);
  button2.setFont(myFont);


  JButton button3 = new JButton("Game mode 3");
  button3.setBounds(x-160,y-150,120,80);
  button3.setForeground(new Color(23,154,19));
  ProjectFrame.add(button3);
  ProjectFrame.setLayout(null);
  button3.setFont(myFont);


  JLabel introduction = new JLabel();
  introduction.setText("<html>Please select the game mode you want to play.<html>");
  introduction.setBounds((x/2)-225,20,450,80);
  introduction.setForeground (Color.black);
  introduction.setFont(new Font("SansSerif Plain", Font.BOLD, 18));
  ProjectFrame.add(introduction);
  ProjectFrame.setLayout(null);


  JLabel description1 = new JLabel();
  description1.setText("<html>Game mode 1<br>   <br>Description :<br>You will have to colour the graph as quickly as possible.<br>You will not be allowed to finish until the minimum number of colours has been reached.<html>");
  description1.setBounds(x-540,105,150,250);
  description1.setForeground (new Color(62,118,236));
  description1.setFont(new Font("SansSerif Plain", Font.BOLD, 12));
  ProjectFrame.add(description1);
  ProjectFrame.setLayout(null);



  JLabel description2 = new JLabel();
  description2.setText("<html>Game mode 2<br>   <br>Description :<br>You will be given a fixed amount of time.<br>You will have to find a colouring with as few colours as possible in the given time.<html>");
  description2.setBounds((x/2)-40,90,150,250);
  description2.setForeground (new Color(238,16,16));
  description2.setFont(new Font("SansSerif Plain", Font.BOLD, 12));
  ProjectFrame.add(description2);


  JLabel description3 = new JLabel();
  description3.setText("<html>Game mode 3<br>   <br>Description :<br>You will have to pick the colours of the vertices in the order that the computer generated randomly for the vertices.<br>Once the colour of a vertex has been chosen, it cannot be change again.<br>Your goal is to use as few colours as possible.<html>");
  description3.setBounds(x-160,134,150,250);
  description3.setForeground (new Color(23,154,19));
  description3.setFont(new Font("SansSerif Plain", Font.BOLD, 12));
  ProjectFrame.add(description3);

  ProjectFrame.setVisible(true);
  ProjectFrame.setLayout(null);

}

}
