package Texture;



import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.media.opengl.*;

import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.glu.GLU;

public class AnimGLEventListener1 implements GLEventListener {

    final String assetsFolderName = "Texture";
    String textureNames[] = {"topTen.png"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];

    int maxWidth=6000, maxHeight=6000;

    public void init(GLAutoDrawable gld) {

        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    //This Will Clear The Background Color To Black
        
        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);	
        gl.glGenTextures(textureNames.length, textures, 0);
        
        for(int i = 0; i < textureNames.length; i++){
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "\\" + textureNames[i] , true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

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
        

        
        BufferedReader br;
        try {
            URL url =  this.getClass().getResource("scoreBoard");
            Path relativePath = Paths.get(url.toURI());
            br = new BufferedReader(new FileReader(relativePath.toFile()));
            
            
            while (br.ready()) {
                
            String name = br.readLine();
            int n = Integer.parseInt(br.readLine());
            l.add(new AnimGLEventListener1.pair (name ,n ));

            
        }
        br.close();
        BufferedWriter bw = new BufferedWriter(new FileWriter(relativePath.toFile()));

        for (AnimGLEventListener1.pair p : l) {
            bw.append("" + p.name + "\n" + p.score + "\n");
        }
        bw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnimGLEventListener1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnimGLEventListener1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(AnimGLEventListener1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    static TreeSet<pair> l = new TreeSet<pair>();

    static class pair implements Comparable<pair> {

        String name;
        int score;

        pair(String name, int score) {
            this.name = name;
            this.score = score;
        }

        @Override
        public int compareTo(pair o) {
            return o.score - this.score;
        }

    }

    static class player {

        private int maxScore;
        private String name;

        public player(String name) {
            this.name = name;
            maxScore = 0;
        }

        public int maximizeScore(int score) {
            return this.maxScore = Math.max(this.maxScore, score);
        }

        /**
         * @return the Max_score
         */
        public int getMaxScore() {
            return this.maxScore;
        }

        /**
         * @return the Name
         */
        public String getName() {
            return name;
        }

        /**
         * @param Name the Name to set
         */
        public void setName(String name) {
            this.name = name;
        }

    }
    
    public void display(GLAutoDrawable gld) {
        
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity(); 
        
        DrawSprite(gl, 2700, 2700, 0, 10);
        int y=620;
        for(pair p : l){
            String name = p.name;
            int n = p.score;
            
            TextRenderer textRenderer = new TextRenderer(new Font("Courier New", Font.BOLD, 30));
            textRenderer.beginRendering(1000, 1000);
            textRenderer.setColor(Color.BLACK);
            textRenderer.setSmoothing(true);


            textRenderer.draw(name + " : " + n, 520, y);
            textRenderer.setColor(Color.WHITE);
            textRenderer.endRendering();
            y-=50;
        }


        
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

    
}
