/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuladorv2;

import Utils.OpenGLHelper;
import Utils.ShaderProgram;
import Utils.Texture;

/**
 *
 * @author Alba
 */
/* 
 * No vamos a usar un objeto 'Dibujable' como tal, vamos a dibujar un avión, una
 * pista, una torre de control...
 */
public abstract class Dibujable {
    protected int x,y,z;
    
    public Dibujable(int px,int py, int pz){
        set_pos(px,py,pz);
    }
    
    public void set_pos(int px, int py, int pz){
        this.x = px;
        this.y = py;
        this.z = pz;
    }
    
    public void draw(ShaderProgram shaderProgram, OpenGLHelper openGLHelper)
    {
    System.out.println("Error");
    throw new UnsupportedOperationException("ESto es un error");
    //throw 
    }
    
}