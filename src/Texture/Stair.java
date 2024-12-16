/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Texture;


public class Stair{
    
    int x, lvl, length;
    int st, nd, y, hight;
    
    static int lastStairLvl = 99;
    static int stairrate = 1000;
    static int imagesIdx[][] = {{17, 18, 19}, {20, 21, 22}, {23, 24, 25}};
    
    static int per[][] = {
        {0, 0, 5, 10, 15, 15, 20, 20, 10, 5},
        {5, 5, 10, 15, 20, 15, 12, 10, 8, 2},
        {7, 12, 18, 20, 15, 10, 10, 5, 2, 1}
    };
    
    
   AnimGLEventListener anim = new AnimGLEventListener();

    public Stair(int lvl) {        
        
        if(lvl==lastStairLvl+1){
            lastStairLvl++;
        }
        
        if(lvl%50==0){
            x=0;
            length=18;
        }else{
            //length = (int)(Math.random()*10) + 3;
            int r = (int)(Math.random()*100);
            int sum=0;
            for(int i=0; i<10; i++){
                if(r>=sum){
                    length=i+2;
                }
                sum += per[Math.min(lvl/100, per.length-1)][i];
            }
            
            x = (int)(Math.random()*(anim.maxWidth-(length+2)*300));
        }
        
        this.lvl = lvl; 
        this.y = lvl*stairrate;
        hight = y+285;
        st = x - 300;
        nd = x + (length+1)*300;
    }
            
}