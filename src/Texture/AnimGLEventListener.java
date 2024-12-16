package Texture;

import static Texture.Stair.imagesIdx;
import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.media.opengl.*;

import java.util.BitSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.glu.GLU;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class AnimGLEventListener implements GLEventListener, KeyListener {

    final String assetsFolderName = "Texture";
    int direction=1;
    int maxWidth = 6000;
    int maxHeight = 6000;

    int spdDown;
    String textureNames[] = {"run\\0.png","run\\1.png","run\\2.png","run\\3.png","Back.png", "jumping\\0.png", "jumping\\1.png", "idle\\0.png", "idle\\1.png", "idle\\2.png", "idle\\3.png", "sidejump\\0.png", "sidejump\\1.png", "sidejump\\2.png", "edge\\0.png", "edge\\1.png", "edge\\2.png", "stairs\\0.png", "stairs\\1.png", "stairs\\2.png", "stairs\\3.png", "stairs\\4.png", "stairs\\5.png", "stairs\\6.png", "stairs\\7.png", "stairs\\8.png"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];


    
    int frame=0;
    int cameraHeight = 0;
    boolean startCameraMotion = false;
    
    String gameStatus = "playing";
    int pausedFrame=0;
    
    int score=0;
    
    int soundFrame = -14*60;
    //--------------------
    
    Clip clip;
    int gameOver=-1;
    
    int BGScrollSpead = 20;
    Stair stair[] = new Stair[100];
    
    Character man = new Character();
    int timer=30;
    




    public void init(GLAutoDrawable gld) {
        
        //game = new Anim();

        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    //This Will Clear The Background Color To Black
        
        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);	
        gl.glGenTextures(textureNames.length, textures, 0);
        
        for(int i = 0; i < textureNames.length; i++){
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "\\" + textureNames[i] , true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

//                mipmapsFromPNG(gl, new GLU(), texture[i]);
                new GLU().gluBuild2DMipmaps(
                    GL.GL_TEXTURE_2D,
                    GL.GL_RGBA, // Internal Texel Format,
                    texture[i].getWidth(), texture[i].getHeight(),
                    GL.GL_RGBA, // External format from image,
                    GL.GL_UNSIGNED_BYTE,
                    texture[i].getPixels() // Imagedata
                    );
            } catch( IOException e ) {
              System.out.println(e);
              e.printStackTrace();
            }
        }
        
        
        for(int i = 0; i < 100; i++){
            stair[i] = new Stair(i);
        }
        

        man.x = maxWidth/2;
        man.y = stair[0].hight-4;
        
        File soundFile = null;
//        try {
//            URL soundURL =  Texture.sounds.sound.class.getResource("icy_tower_theme.wav");
//            Path relativePath = Paths.get(soundURL.toURI());
//            soundFile = relativePath.toFile();
//        } catch (URISyntaxException ex) {
//            Logger.getLogger(AnimGLEventListener.class.getName()).log(Level.SEVERE, null, ex);
//        }
        soundFile = Texture.sounds.sound.getSoundFile("icy_tower_theme.wav");
        
        AudioInputStream audioIn;
        try {            
            audioIn = AudioSystem.getAudioInputStream(soundFile);
            DataLine.Info info =new DataLine.Info(Clip.class, audioIn.getFormat());
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioIn);

        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(AnimGLEventListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnimGLEventListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(AnimGLEventListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public void display(GLAutoDrawable gld) {
        
        if(frame-soundFrame > 14*60){
            
            clip.start();
            soundFrame = frame;
        }
        
        frame++;
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity(); 
        DrawBackground(gl);

        
        
        if(gameStatus.equals("gameOver")){
            man.y=-1000;
            man.draw(gl, maxWidth, maxHeight, textures);
            
            TextRenderer textRenderer = new TextRenderer(new Font("Courier New", Font.BOLD, 120));
            textRenderer.beginRendering(1000, 1000);
            textRenderer.setColor(Color.WHITE);
            textRenderer.setSmoothing(true);

            textRenderer.draw("Game Over", 180, 550);
            textRenderer.setColor(Color.WHITE);
            textRenderer.endRendering();
            System.out.println("lose");
            
            //TextRenderer textRenderer = new TextRenderer(new Font("Courier New", Font.BOLD, 120));
            textRenderer.beginRendering(1000, 1000);
            textRenderer.setColor(Color.YELLOW);
            textRenderer.setSmoothing(true);

            textRenderer.draw("Score : " + score, 140, 350);
            textRenderer.setColor(Color.WHITE);
            textRenderer.endRendering();
//            System.out.println("lose");
            
            
        }else{
        
        
            man.isColid = false;
            man.onEdge = false;

            for(int i=0; i<100; i++){
                drawStair(gl, stair[i].x , stair[i].y ,stair[i].lvl , stair[i].length);
            }

            
            man.draw(gl, maxWidth, maxHeight, textures);

            TextRenderer textRenderer = new TextRenderer(new Font("Courier New", Font.BOLD, 60));
            textRenderer.beginRendering(1000, 1000);
            textRenderer.setColor(Color.WHITE);
            textRenderer.setSmoothing(true);

            textRenderer.draw(""+timer, 900, 900);
            textRenderer.setColor(Color.white);
            textRenderer.endRendering();
        }
        
        if(gameStatus.equals("playing")){

         
            
           for(int i=0; i<100; i++){
                if(man.x>stair[i].st && man.x<stair[i].nd && Math.abs(man.y - stair[i].hight)<man.spdDown){
                    man.isColid = true;
                    man.y = stair[i].hight;
                    score = Math.max(score, stair[i].lvl * 10);
                    
                    if((man.x>stair[i].st && man.x<stair[i].st+150)){
                        man.onEdge = true;
                        man.direction=-1;
                    }
                    
                    if(man.x<stair[i].nd && man.x>stair[i].nd-150){
                        man.onEdge = true;
                        man.direction=1;
                    }
                }
                
                if(cameraHeight>stair[i].hight){
                    stair[i] = new Stair(Stair.lastStairLvl+1);
                }
            }
            
           man.fall();
           
           if(man.y<cameraHeight-150){
               gameStatus = "gameOver";
           }
           
            if(startCameraMotion && frame%60==0){
                timer--;
                if(timer==0){
                    BGScrollSpead+=10;
                    timer=30;
                }
            }
            
            TextRenderer textRenderer2 = new TextRenderer(new Font("Courier New", Font.BOLD, 50));
            textRenderer2.beginRendering(1000, 1000);
            textRenderer2.setColor(Color.YELLOW);
            textRenderer2.setSmoothing(true);

            textRenderer2.draw("Score : " + score, 10, 10);
            textRenderer2.setColor(Color.white);
            textRenderer2.endRendering();
            
        }else if(gameStatus.equals("paused")){
            TextRenderer textRenderer2 = new TextRenderer(new Font("Courier New", Font.BOLD, 60));
            textRenderer2.beginRendering(1000, 1000);
            textRenderer2.setColor(Color.WHITE);
            textRenderer2.setSmoothing(true);

            textRenderer2.draw("Paused", 425, 500);
            textRenderer2.setColor(Color.white);
            textRenderer2.endRendering();
            
            gameStatus = "paused";
        }        


        try {
            handleKeyPress();
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(AnimGLEventListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnimGLEventListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(AnimGLEventListener.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }
    
    
    
    public void drawStair(GL gl, int x, int y, int lvl, int length){
        
        x -= maxWidth/4 - 15;
        y -= maxHeight/4;
        
        gl.glPushMatrix();
        gl.glTranslated( x/(maxWidth/2.0) , y/(maxHeight/2.0) , 0);
        gl.glScaled(0.5, 0.5, 1);
        DrawSprite(gl, 10, 0, Stair.imagesIdx[lvl/100 % imagesIdx.length][0], 1f);
        DrawSprite(gl, 600*(length+1)-100, 0, Stair.imagesIdx[lvl/100 % imagesIdx.length][2], 1f);
        for(int i=1; i<=length; i++)
        DrawSprite(gl, 600*i, 0, Stair.imagesIdx[lvl/100 % imagesIdx.length][1], 1f);
        
                gl.glPopMatrix();
        
        gl.glDisable(GL.GL_BLEND);
    }
    
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
    
    public void DrawSprite(GL gl,int x, int y, int index, float scale){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
            gl.glTranslated( x/(maxWidth/2.0) - 0.9, y/(maxHeight/2.0) - 0.9, 0);
            gl.glScaled(0.1*scale, 0.1*scale, 1);
            
            gl.glBegin(GL.GL_QUADS);
            // Front Face
                gl.glTexCoord2f(0.0f, 0.0f);
                gl.glVertex3f(-1.0f, -1.0f, -1.0f);
                gl.glTexCoord2f(1.0f, 0.0f);
                gl.glVertex3f(1.0f, -1.0f, -1.0f);
                gl.glTexCoord2f(1.0f, 1.0f);
                gl.glVertex3f(1.0f, 1.0f, -1.0f);
                gl.glTexCoord2f(0.0f, 1.0f);
                gl.glVertex3f(-1.0f, 1.0f, -1.0f);
            gl.glEnd();
        gl.glPopMatrix();
        
        gl.glDisable(GL.GL_BLEND);
    }

    
    public void DrawBackground(GL gl){
        gl.glEnable(GL.GL_BLEND);	
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[4]);	// Turn Blending On
        double bgtrans = cameraHeight/((maxHeight/2.0));
        gl.glTranslated( 0, -bgtrans, 0);

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
            // Front Face

            int e = ((int)(bgtrans)/2)*2;

            gl.glTexCoord2f(0.0f, 0.0f+e);
            gl.glVertex3f(-1.0f, -1.0f+e, -1.0f);
            gl.glTexCoord2f(1.0f, 0.0f+e);
            gl.glVertex3f(1.0f, -1.0f+e, -1.0f);
            gl.glTexCoord2f(1.0f, 1.0f+e);
            gl.glVertex3f(1.0f, 1.0f+e, -1.0f);
            gl.glTexCoord2f(0.0f, 1.0f+e);
            gl.glVertex3f(-1.0f, 1.0f+e, -1.0f);


            gl.glTexCoord2f(0.0f, 2.0f+e);
            gl.glVertex3f(-1.0f, 1.0f+e, -1.0f);
            gl.glTexCoord2f(1.0f, 2.0f+e);
            gl.glVertex3f(1.0f, 1.0f+e, -1.0f);
            gl.glTexCoord2f(1.0f, 3.0f+e);
            gl.glVertex3f(1.0f, 3.0f+e, -1.0f);
            gl.glTexCoord2f(0.0f, 3.0f+e);
            gl.glVertex3f(-1.0f, 3.0f+e, -1.0f);

        gl.glEnd();
        gl.glPopMatrix();
        
        gl.glDisable(GL.GL_BLEND);
    }
    
    /*
     * KeyListener
     */


    public void handleKeyPress() throws UnsupportedAudioFileException, IOException, LineUnavailableException{

        
                
        boolean up = isKeyPressed(KeyEvent.VK_UP);
        boolean right = isKeyPressed(KeyEvent.VK_RIGHT);
        boolean down = isKeyPressed(KeyEvent.VK_DOWN);
        boolean left = isKeyPressed(KeyEvent.VK_LEFT);
        
        boolean space = isKeyPressed(KeyEvent.VK_SPACE);
        
        boolean P = isKeyPressed(KeyEvent.VK_P);
        
        if(gameStatus.equals("gameOver")){
            AnimGLEventListener1.l.add(new AnimGLEventListener1.pair (menu.s , score));
            if(space){
                Anim.animator.stop();
                Anim.gamePlay.setVisible(false);
                new Anim2().setVisible(true);

            }
            
        }else{

        if(P && frame-pausedFrame>15){
            //pause = !pause;
            if(gameStatus.equals("paused")){
                gameStatus="playing";
            }else{
                gameStatus="paused";
            }
            
            pausedFrame = frame;
            //System.err.println(pause);
        }
        

        if(gameStatus.equals("playing")){
            
            man.keyPressed(up||space, right, down, left, frame);
            
           

            if(man.y>4000){
                startCameraMotion = true;
            }

            if(startCameraMotion){

               int te = Math.max(BGScrollSpead, (man.y-5000-cameraHeight));
               int H = (int) (te / (1+Math.exp(-(0.05)*(frame-man.jumpFrame-60))));
               H = Math.max(H, BGScrollSpead);
               cameraHeight+=H;           
            }
        }
        
        }
    }
    
    

    public BitSet keyBits = new BitSet(256);
 
    @Override 
    public void keyPressed(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.set(keyCode);
    } 
 
    @Override 
    public void keyReleased(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.clear(keyCode);
    } 
 
    @Override 
    public void keyTyped(final KeyEvent event) {
        // don't care 
    } 
 
    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }
}
