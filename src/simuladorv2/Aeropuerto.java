/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuladorv2;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import Utils.*;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.util.ArrayList;
import java.nio.ByteBuffer;

import Utils.OpenGLHelper;

/**
 *
 * @author Alba
 */

// Aeropuerto está en un nivel superior a Pista y Avion, por lo tanto no es 
// necesario que herede, pues no sería un Dibujable
public class Aeropuerto extends Dibujable{
	
	private final PlaneModel planeModel = new PlaneModel(5, 5);
	private final CubeModel cubeModel = new CubeModel();
    private int planeVao;
    private int axiesVao;
    private int vbo_vp;
    private int vbo_np;
    private int vbo_tp;
    private int vbo_vax;
    private int vbo_nax;
    private int ebop;
    private int eboax;
    private int uKdAttribute;
    private int uKaAttribute;
    private int useTextures;
    private int uniModel;
    private int uNMatrixAttribute;
    
    ArrayList<Dibujable> MiobjsDibujables = null;
    // Cada clase tiene que tener un constructor, no es void, porque no retorna
    // nada

    public Aeropuerto(int px, int py, int pz,ShaderProgram shaderProgram) {
    	super(px, py, pz);
        MiobjsDibujables = new ArrayList<Dibujable>(); 
        
        int posAttrib = shaderProgram.getAttributeLocation("aVertexPosition");
        int vertexNormalAttribute = shaderProgram.getAttributeLocation("aVertexNormal");
        int texCoordsAttribute = shaderProgram.getAttributeLocation("aVertexTexCoord");
        
        //------------------------------- PLANE MODEL----------------------------------//
        planeVao = glGenVertexArrays();
        glBindVertexArray(planeVao);

        vbo_vp = planeModel.createVerticesBuffer();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_vp);
        glEnableVertexAttribArray(posAttrib);
        glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 0, 0);

        vbo_np = planeModel.createNormalsBuffer();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_np);
        glEnableVertexAttribArray(vertexNormalAttribute);
        glVertexAttribPointer(vertexNormalAttribute, 3, GL_FLOAT, false, 0, 0);

        vbo_tp = planeModel.createTextCoordsBuffer();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_tp);
        glEnableVertexAttribArray(texCoordsAttribute);
        glVertexAttribPointer(texCoordsAttribute, 2, GL_FLOAT, false, 0, 0);

        ebop = planeModel.createIndicesBuffer();
        glBindVertexArray(0);
        
        //------------------------AXIES MODEL-------------------------------//
        
        axiesVao = glGenVertexArrays();
        glBindVertexArray(axiesVao);
        
        vbo_vax = cubeModel.createVerticesBuffer();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_vax);
        glEnableVertexAttribArray(posAttrib);
        glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 0, 0);

        vbo_nax = cubeModel.createNormalsBuffer();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_nax);
        glEnableVertexAttribArray(vertexNormalAttribute);
        glVertexAttribPointer(vertexNormalAttribute, 3, GL_FLOAT, false, 0, 0);

        eboax = cubeModel.createIndicesBuffer();
        glBindVertexArray(0);
       
        uniModel = shaderProgram.getUniformLocation("model");
        uNMatrixAttribute = shaderProgram.getUniformLocation("uNMatrix");
        useTextures = shaderProgram.getUniformLocation("useTextures");
        
        uKaAttribute = shaderProgram.getUniformLocation("Ka");
        uKdAttribute = shaderProgram.getUniformLocation("Kd");
    }       
    /**
     *
     * @param objeto
     */
    public void add(Dibujable objeto){
        // Polimorfismo
        MiobjsDibujables.add(objeto);
        System.out.println("dibujable añadido");
    }
    
    public void draw(ShaderProgram shaderProgram, OpenGLHelper openGLHelper)
    {    	
    	int uniTex1 = shaderProgram.getUniformLocation("Texture1");
    	//drawAxies(openGLHelper);
    	drawfloor(shaderProgram, openGLHelper, uniTex1);    	
        
        // Dibújame todas las cosas del aeropuerto
        for (Dibujable dib: MiobjsDibujables)
        {
            dib.draw(shaderProgram, openGLHelper);
        }
    }
    
    public void drawAxies(OpenGLHelper openGLHelper){
    	
    	glBindBuffer(GL_ARRAY_BUFFER, vbo_vax);
    	glBindBuffer(GL_ARRAY_BUFFER, vbo_nax);    
    	glBindVertexArray(axiesVao);
        glUniform1i(useTextures, 0);

         for (int i = 0; i < 20; i++) {
             Matrix4f model = Matrix4f.scale(0.35f, 0.15f, 0.1f);
             model = Matrix4f.translate(i, 0, 0).multiply(model);
             glUniformMatrix4(uniModel, false, model.getBuffer());
             Matrix3f normalMatrix = model.multiply(openGLHelper.getViewMatrix()).toMatrix3f().invert();
             normalMatrix.transpose();
             glUniformMatrix3(uNMatrixAttribute, false, normalMatrix.getBuffer());
             glUniform3f(uKaAttribute, 0.9f, 0.9f, 0.0f);
             glUniform3f(uKdAttribute, 0.9f, 0.0f, 0.0f);
             glDrawElements(GL_TRIANGLES, cubeModel.getIndicesLength(), GL_UNSIGNED_INT, 0);

             model = Matrix4f.scale(0.1f, 0.35f, 0.1f);
             model = Matrix4f.translate(0, i, 0).multiply(model);
             glUniformMatrix4(uniModel, false, model.getBuffer());
             normalMatrix = model.multiply(openGLHelper.getViewMatrix()).toMatrix3f().invert();
             normalMatrix.transpose();
             glUniformMatrix3(uNMatrixAttribute, false, normalMatrix.getBuffer());
             glUniform3f(uKaAttribute, 0.0f, 0.9f, 0.0f);
             glUniform3f(uKdAttribute, 0.0f, 0.9f, 0.0f);
             glDrawElements(GL_TRIANGLES, cubeModel.getIndicesLength(), GL_UNSIGNED_INT, 0);

             model = Matrix4f.scale(0.1f, 0.1f, 0.35f);
             model = Matrix4f.translate(0, 0, 0 + i).multiply(model);
             glUniformMatrix4(uniModel, false, model.getBuffer());
             normalMatrix = model.multiply(openGLHelper.getViewMatrix()).toMatrix3f().invert();
             normalMatrix.transpose();
             glUniformMatrix3(uNMatrixAttribute, false, normalMatrix.getBuffer());
             glUniform3f(uKaAttribute, 0.0f, 0.0f, 0.9f);
             glUniform3f(uKdAttribute, 0.0f, 0.0f, 0.9f);
             glDrawElements(GL_TRIANGLES, cubeModel.getIndicesLength(), GL_UNSIGNED_INT, 0);
         }
    }
    
   public void drawfloor(ShaderProgram shaderProgram, OpenGLHelper openGLHelper, int uniTex1){
	   
	   shaderProgram.setUniform(uniTex1, 0);
       glBindVertexArray(planeVao);
       glUniform3f(uKaAttribute, 1.0f, 1.0f, 1.0f);
       glUniform3f(uKdAttribute, 1.0f, 1.0f, 1.0f);
       glUniform1i(useTextures, 3);

       Matrix4f model = Matrix4f.scale(30.0f, 1.0f, 20.0f);
       model = Matrix4f.translate(10, 0, 0).multiply(model);
       glUniformMatrix4(uniModel, false, model.getBuffer());

       Matrix3f normalMatrix = model.multiply(openGLHelper.getViewMatrix()).toMatrix3f().invert();
       normalMatrix.transpose();
       glUniformMatrix3(uNMatrixAttribute, false, normalMatrix.getBuffer());

       glDrawElements(GL_TRIANGLES, planeModel.getIndicesLength(), GL_UNSIGNED_INT, 0);
   }
   
  
    
}
