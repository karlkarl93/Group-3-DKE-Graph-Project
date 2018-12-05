import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;






public class ColorPalette extends JFrame{
  public static void main(String[] args){

    //To make the color of the buttons visible on Mac Os
    try {
      UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
    } catch (Exception e) {
            e.printStackTrace();
    }

    Color[] colors = {
                        new Color(244,67,54),
                        new Color(233,30,99),
                        new Color(156,39,176),
                        new Color(103,58,183),
                        new Color(26,35,126),

                        new Color(33,150,243),
                        new Color(129,212,250),
                        new Color(0,188,212),
                        new Color(0,150,136),
                        new Color(76,175,80),

                        new Color(139,195,74),
                        new Color(205,220,57),
                        new Color(255,235,59),
                        new Color(255,193,7),
                        new Color(255,152,0),

                        new Color(255,138,101),
                        new Color(121,85,72),
                        new Color(158,158,158),
                        new Color(96,125,139),
                        new Color(33,33,33),

                        new Color(183,28,28),
                        new Color(74,20,140),
                        new Color(0,77,64),
                        new Color(130,119,23),
                        new Color(224,224,224),
                      };

    JButton[] buttons = new JButton[colors.length];

    class ColorListener implements ActionListener {
      public void actionPerformed(ActionEvent event) {
        for (int i = 0; i < buttons.length; i++) {
          if(event.getSource() == buttons[i]) {

              System.out.println("Button " + (i+1) + " Clicked with color" + buttons[i].getBackground());
            }
          }
        }
      }




    ActionListener listener = new ColorListener();


    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(5, 5));

    for(int i =0; i<colors.length; i++) {
      JButton button = new JButton();
      button.setBackground(colors[i]);
      buttons[i]=button;

      buttonPanel.add(button);
      button.addActionListener(listener);


    }







/*
    JButton button1 = new JButton();
    JButton button2 = new JButton();
    JButton button3 = new JButton();
    JButton button4 = new JButton();
    JButton button5 = new JButton();
    JButton button6 = new JButton();
    JButton button7 = new JButton();
    JButton button8 = new JButton();
    JButton button9 = new JButton();
    JButton button10 = new JButton();
    JButton button11 = new JButton();
    JButton button12 = new JButton();
    JButton button13 = new JButton();
    JButton button14 = new JButton();
    JButton button15 = new JButton();
    JButton button16 = new JButton();
    JButton button17 = new JButton();
    JButton button18 = new JButton();
    JButton button19 = new JButton();
    JButton button20 = new JButton();
    JButton button21 = new JButton();
    JButton button22 = new JButton();
    JButton button23 = new JButton();
    JButton button24 = new JButton();
    JButton button25 = new JButton();
    JButton button26 = new JButton();
    JButton button27 = new JButton();
    JButton button28 = new JButton();
    JButton button29 = new JButton();
    JButton button30 = new JButton();











    button1.setBackground(new Color(244,67,54));
    button2.setBackground(new Color(233,30,99));
    button3.setBackground(new Color(156,39,176));
    button4.setBackground(new Color(103,58,183));
    button5.setBackground(new Color(26,35,126));
    button6.setBackground(new Color(33,150,243));
    button7.setBackground(new Color(129,212,250));
    button8.setBackground(new Color(0,188,212));
    button9.setBackground(new Color(0,150,136));
    button10.setBackground(new Color(76,175,80));
    button11.setBackground(new Color(139,195,74));
    button12.setBackground(new Color(205,220,57));
    button13.setBackground(new Color(255,235,59));
    button14.setBackground(new Color(255,193,7));
    button15.setBackground(new Color(255,152,0));
    button16.setBackground(new Color(255,138,101));
    button17.setBackground(new Color(121,85,72));
    button18.setBackground(new Color(158,158,158));
    button19.setBackground(new Color(96,125,139));
    button20.setBackground(new Color(33,33,33));          //BLACK
    button21.setBackground(new Color(183,28,28));
    button22.setBackground(new Color(74,20,140));
    button23.setBackground(new Color(0,77,64));
    button24.setBackground(new Color(130,119,23));
    button25.setBackground(new Color(224,224,224));
 */



/*
    buttonPanel.add(button1);
    buttonPanel.add(button2);
    buttonPanel.add(button3);
    buttonPanel.add(button4);
    buttonPanel.add(button5);
    buttonPanel.add(button6);
    buttonPanel.add(button7);
    buttonPanel.add(button8);
    buttonPanel.add(button9);
    buttonPanel.add(button10);
    buttonPanel.add(button11);
    buttonPanel.add(button12);
    buttonPanel.add(button13);
    buttonPanel.add(button14);
    buttonPanel.add(button15);
    buttonPanel.add(button16);
    buttonPanel.add(button17);
    buttonPanel.add(button18);
    buttonPanel.add(button19);
    buttonPanel.add(button20);
    buttonPanel.add(button21);
    buttonPanel.add(button22);
    buttonPanel.add(button23);
    buttonPanel.add(button24);
    buttonPanel.add(button25);
*/



    JFrame frame = new JFrame();
    frame.setTitle("Color Palette");
    frame.setSize(300, 300);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.add(buttonPanel);
    frame.setVisible(true);


  }
}
