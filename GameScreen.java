import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;

public class GameScreen extends JPanel{

  public static void main(String [] args){

    JFrame frame= new JFrame();
    JPanel panel= new JPanel();

    JSlider slider1= new JSlider();
    slider1.setMaximum(30);
    slider1.setMinimum(0);
    slider1.setValue(15);
    slider1.setPaintTicks(true);
    slider1.setPaintLabels(true);
    slider1.setMinorTickSpacing(1);
    slider1.setMajorTickSpacing(5);
    slider1.setPreferredSize(new Dimension(300,30));
    slider1.setBounds(1000,300,250,50);
    panel.setLayout(null);
    panel.add(slider1, BorderLayout.EAST);
    panel.setVisible(true);


    JSlider slider2= new JSlider();
    slider2.setMaximum((slider1.getMaximum()*(slider1.getMaximum()-1))/2);
    slider2.setMinimum(0);
    slider2.setValue(5);
    slider2.setPaintTicks(true);
    slider2.setPaintLabels(true);
    slider2.setMinorTickSpacing(50);
    slider2.setMajorTickSpacing(100);
    slider2.setPreferredSize(new Dimension(300,30));
    slider2.setBounds(1000,425,250,50);
    panel.setLayout(null);
    panel.add(slider2, BorderLayout.EAST);
    panel.setVisible(true);

    JLabel label2= new JLabel();
    panel.setLayout(null);
    label2.setText("EDGES");
    label2.setBounds(new Rectangle(new Point(1100,400),label2.getPreferredSize()));
    panel.add(label2);
    panel.setVisible(true);


    JLabel label= new JLabel();
    panel.setLayout(null);
    label.setText("VERTICES");
    label.setBounds(new Rectangle(new Point(1100,260),label.getPreferredSize()));
    panel.add(label);
    panel.setVisible(true);


    JButton button= new JButton("Select");
    button.setBounds(100,80,200,30);
    button.setLocation(1025, 350);
    panel.add(button);
    button.setVisible(true);
    panel.setLayout(null);
    button.setForeground(Color.pink);
    frame.setVisible(true);

    JButton button2= new JButton("Select");
    button2.setBounds(42,80,200,30);
    button2.setLocation(1025, 475);
    panel.add(button2);
    button2.setVisible(true);
    panel.setLayout(null);
    button2.setForeground(Color.pink);
    frame.setVisible(true);

    frame.add(panel);
    frame.setSize(1280,720);
    frame.setTitle("Game");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);


}
}
