/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Texture;

import javax.media.opengl.GL;


public class Character {
    
    
    int x=3000, y=2000;
    int speed=0, direction=1;
    int sprite=7, spriteIdx=0;
    int jumpingPower = 0;
    static int idle[] = {7, 8, 9, 10};
    static int run[] = {0, 1, 2, 3};
    static int jumping[] = {5, 6};
    static int sidejump[] = {11, 12, 13};
    static int edge[] = {14, 15};
    
    String status = "idle";
    
    int jumpFrame=0, hitWallFrame=0;
    boolean jump=false, jumpEnd=true;    
    boolean isColid = false;
    int spdDown;
    
    boolean onEdge=false;
    boolean comboJump = false;
    
    int rot=1;

    
    void fall(){
        jumpEnd=isColid;
        comboJump &= jump || (!jump && !isColid);

        if(!isColid && !jump){
            y-=spdDown;
            spdDown+=5;
            sprite = Character.sidejump[2];
        }else{
            spdDown=5;
        }
       
    }
    
    void keyPressed(boolean up, boolean right, boolean down, boolean left, int frame){
        

        
        if(x<=-75){
            x=-70;
            hitWallFrame = frame;
            if(Math.abs(speed)>10){
                speed*=-0.5;
                status="right";
                direction*=-1;
            }
            
        }
        
        if(x>=5550){
            x=5545;
            hitWallFrame = frame;
            if(Math.abs(speed)>10){
                speed*=-0.5;
                status="left";
                direction*=-1;
            }
        }
        
        
        if(jump && jumpingPower>0){
            y+=jumpingPower*8;
            jumpingPower--;
            if(jumpingPower==0){
                jump=false;
            }
        }
        
        if(frame-hitWallFrame < 15){
            right = left = false;
        }
        
        if(right&&left){
            
            if(!status.equals("idle")){
                speed/=1.04;
                if(Math.abs(speed)<10){
                    speed=0;
                    status = "idle";
                    spriteIdx=0;
                }
            }
            
        }else if(right){
            direction = 1;
            if(!status.equals("right")){
                status = "right";
                spriteIdx=0;
                speed = direction = 1;
            }
            
            speed+=2;            
            
        }else if(left){
            direction = -1;
            if(!status.equals("left")){
                status = "left";
                spriteIdx=0;
                speed = direction = -1;
            }
            
            speed-=2;
            
        }else{
            
            if(!status.equals("idle")){
                speed/=1.04;
                if(Math.abs(speed)<10){
                    speed=0;
                    status = "idle";
                    spriteIdx=0;
                }
            }
            
        }
        
        x+=speed;
        
        if(up && !jump && jumpEnd){
            
            if(status.equals("idle")){
                sprite = jumping[0];
            }else{
                sprite = sidejump[0];
            }
                        
            jumpEnd=false;
            jump=true; 
            jumpingPower = Math.abs(speed)/14 + 20;
            
            if(jumpingPower>25){
                jumpingPower += jumpingPower/4;
                comboJump = true;
            }
            
            jumpFrame = frame;
        }
        
        
        if(!jump)
        if(status.equals("idle")){
            if(onEdge){
                if(frame%10==0){
                    spriteIdx = (spriteIdx+1) % edge.length;
                    sprite = edge[spriteIdx];
                }
            }else{
            
                if(frame%10==0){
                    sprite = idle[spriteIdx];
                    spriteIdx = (spriteIdx+1) % idle.length;
                }
            
            }
            
        }else{            
            if(speed%5==0){
                sprite = run[spriteIdx];
                spriteIdx = (spriteIdx+1) % run.length;
            }
            
        }


        
    }
    
    public void draw(GL gl, int maxWidth, int maxHeight,  int textures[]){

        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[sprite]);	// Turn Blending On

        gl.glPushMatrix();

            gl.glTranslated( x/(maxWidth/2.0) - 0.9, y/(maxHeight/2.0) - 0.9, 0);
            gl.glScaled(0.1, 0.15, 1);
            if(comboJump && Anim.listener.gameStatus.equals("playing")){
                sprite=jumping[1];
                gl.glRotatef(rot, 0, 0, 1);
                rot+=20;
            }else{
                rot=1;
            }
            gl.glBegin(GL.GL_QUADS);
            // Front Face
                gl.glTexCoord2f(0.0f, 0.0f);
                gl.glVertex3f(-direction*1.0f, -1.0f, -1.0f);
                gl.glTexCoord2f(1.0f, 0.0f);
                gl.glVertex3f(direction*1.0f, -1.0f, -1.0f);
                gl.glTexCoord2f(1.0f, 1.0f);
                gl.glVertex3f(direction*1.0f, 1.0f, -1.0f);
                gl.glTexCoord2f(0.0f, 1.0f);
                gl.glVertex3f(-direction*1.0f, 1.0f, -1.0f);
            gl.glEnd();
        gl.glPopMatrix();
        
        gl.glDisable(GL.GL_BLEND);
    }
}
