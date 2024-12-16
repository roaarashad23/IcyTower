
package Texture;


import java.awt.Font;
import java.awt.Label;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


public class X1 extends JFrame implements KeyListener{

    Label l1 = new Label("one");
    
    
 

    public X1() {

        this.setSize(500, 500);
        this.addKeyListener(this);
        
      //  l1.setSize(10, 10);
        l1.setFont(new Font("",15,20));
       // l1.setLocation(400,400);
        l1.setText("ahmed           50");
        l1.setVisible(true);
        this.add(l1);
        
            JLabel l1 = new JLabel("my text", SwingConstants.CENTER);


    }

    public static void main(String args[]) {
        new X1().setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
      if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
          this.setVisible(false);
          System.exit(0);
      }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
