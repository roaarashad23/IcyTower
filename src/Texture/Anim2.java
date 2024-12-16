package Texture;

import java.awt.*;
import java.awt.event.*;
import javax.media.opengl.*;
import javax.swing.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Anim2 extends JFrame implements KeyListener {

    static Anim2 gamePlay;

    public static void main(String[] args) {
        //try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                try {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException ex) {
                    java.util.logging.Logger.getLogger(Anim2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    java.util.logging.Logger.getLogger(Anim2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    java.util.logging.Logger.getLogger(Anim2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    java.util.logging.Logger.getLogger(Anim2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
                break;
            }
        }

        gamePlay = new Anim2();
    }

    GLCanvas glcanvas;
    static AnimGLEventListener1 listener;

    public static void reSet() {

        gamePlay = new Anim2();

    }

    public Anim2() {

        listener = new AnimGLEventListener1();

        glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);
        glcanvas.addKeyListener(this);
        glcanvas.setSize(650, 650);

        getContentPane().add(glcanvas, BorderLayout.WEST);

        setTitle("Icytower");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 650);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        glcanvas.requestFocus();
    }

    @Override
    public void keyPressed(final KeyEvent event) {
    }

    @Override
    public void keyReleased(final KeyEvent event) {

        try {
            if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                new menu().setVisible(true);
                this.dispose();
            }
        } catch (Exception ex) {
            Logger.getLogger(Anim2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void keyTyped(final KeyEvent event) {
        // don't care 
    }
}
