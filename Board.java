
import javax.swing.*;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
public class Board extends JFrame{

public static void main(String [] args){
  //Board
    JFrame frame= new JFrame();
    frame.setSize(600,600);
    frame.setTitle("Game");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);

//Explanation
    JLabel label1= new JLabel();
    label1.setText("<html>Explanation mode 1:<br>To the bitter end: The player simply has to color<br>the graph as quickly as possible <br>The computer does not allow the player to finish until the minimum number of<br>colours has been reached<br><br>Explanation mode 2:<br>Best upper bound in a fixed time frame: The player is<br>givern a fixed amount of time and <br>they have to find a colouring with a few colours as possible in the given time<br><br>Explanation mode 3:<br>Random order: Here the computer generates a radom ordering<br>of the vertices and the players has<br>to pick the colours of the vertices in exactly that order.<br>Once the colour of a vertex has been chosen,<br>it cannot be changed again.<br>The goal for the player is to use<br>as few colours as possible</html>");
    frame.add(label1);
    frame.setVisible(true);



    String[] options= {"Mode 1", "Mode 2", "Mode 3"};
    JComboBox<String> combo= new JComboBox<String>(options);
    combo.setBounds(130,130, 130, 30);
    combo.setLocation(0,0);
    frame.add(combo);
    frame.setLayout(null);
    combo.setVisible(true);

//Button
    JButton button = new JButton("Press here");
    button.setBounds(100,80,200,30);
    button.setLocation(120, 0);
    frame.add(button);
    button.setVisible(true);
    frame.setLayout(null);
    button.setForeground(Color.pink);
    frame.setVisible(true);


// Acion Listenner
ActionListener listener = new Click();
button.addActionListener(listener);
}
}
