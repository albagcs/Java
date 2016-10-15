/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuladorv2;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.GL_TEXTURE4;
import static org.lwjgl.opengl.GL13.GL_TEXTURE5;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix3;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GLContext;

import Utils.CubeModel;
import Utils.Matrix3f;
import Utils.Matrix4f;
import Utils.OpenGLHelper;
import Utils.ShaderProgram;
import Utils.SphereModel;
import Utils.Texture;
/**
 * @author Alba
 */
public class Avion extends Dibujable {
   
	private final SphereModel sphereModel = new SphereModel();
	private int vbo_vs;
    private int vbo_ns;
    private int vbo_ts;
    private int sphereVao;
    private int ebos;
    private int uKdAttribute;
    private int uKaAttribute;
    private int useTextures;
    private int uniModel;
    private int uNMatrixAttribute;
    
    public Avion(int px, int py, int pz, ShaderProgram shaderProgram) {
        super(px, py, pz);
        
        int posAttrib = shaderProgram.getAttributeLocation("aVertexPosition");
        int vertexNormalAttribute = shaderProgram.getAttributeLocation("aVertexNormal");
        int texCoordsAttribute = shaderProgram.getAttributeLocation("aVertexTexCoord");
      
        // ----------------------- SPHERE MODEL -----------------------------//
        sphereVao = glGenVertexArrays();
        glBindVertexArray(sphereVao);

        vbo_vs = sphereModel.createVerticesBuffer();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_vs);
        glEnableVertexAttribArray(posAttrib);
        glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 0, 0);

        vbo_ns = sphereModel.createNormalsBuffer();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_ns);
        glEnableVertexAttribArray(vertexNormalAttribute);
        glVertexAttribPointer(vertexNormalAttribute, 3, GL_FLOAT, false, 0, 0);

        ebos = sphereModel.createIndicesBuffer();
        glBindVertexArray(0);
              
        uniModel = shaderProgram.getUniformLocation("model");
        uNMatrixAttribute = shaderProgram.getUniformLocation("uNMatrix");
        useTextures = shaderProgram.getUniformLocation("useTextures");
               
        uKaAttribute = shaderProgram.getUniformLocation("Ka");
        uKdAttribute = shaderProgram.getUniformLocation("Kd");
                 
    }
	// Estamos sobreescribiendo el método del padre en todas las clases, en 
    // pista también
     
    private float angle;
    private static final float angularVelocity = 10.f;
    @Override
    public void draw(ShaderProgram shaderProgram, OpenGLHelper openGLHelper)
    {
        System.out.println("Avion situado en coordenada (" + x + 
             				" " + y + " " + z +")");
        
        glBindBuffer(GL_ARRAY_BUFFER, vbo_vs);
    	glBindBuffer(GL_ARRAY_BUFFER, vbo_ns);
    	
    	glBindVertexArray(sphereVao);
        glUniform3f(uKaAttribute, 0f, 0.8f, 1.0f);
        glUniform3f(uKdAttribute, 0.8f, 0.2f, 0.5f);
        int uniTex1 = shaderProgram.getUniformLocation("Texture1");
        glUniform1i(useTextures, 0);
        
        angle += angularVelocity*openGLHelper.getDeltaTime();
        drawbody(openGLHelper);
        drawings(openGLHelper);    
          
    }
       
    public void drawbody(OpenGLHelper openGLHelper) {
    	
    	Matrix4f model = Matrix4f.scale(4.0f, 1.2f, 1.0f);
    	model = Matrix4f.translate(9.0f*(1.0f + (float)Math.cos(angle/8)*-x), 1+y, 0+z).multiply(model);
        glUniformMatrix4(uniModel, false, model.getBuffer());
        
        Matrix3f normalMatrix = model.multiply(openGLHelper.getViewMatrix()).toMatrix3f().invert();
        normalMatrix.transpose();
        glUniformMatrix3(uNMatrixAttribute, false, normalMatrix.getBuffer());

        glDrawElements(GL_TRIANGLES, sphereModel.getIndicesLength(), GL_UNSIGNED_INT, 0);
        //--------------------------------------------------------------------------------------//
        model = Matrix4f.scale(3.0f, 1.2f, 1.0f);
    	model = Matrix4f.rotate(angle*5, 0f, 4.0f, 0).multiply(model);
    	model = Matrix4f.translate(9.0f*(1.0f + (float)Math.cos(angle/8)*-x), 4+y, 0+z).multiply(model);
        glUniformMatrix4(uniModel, false, model.getBuffer());
        
        normalMatrix = model.multiply(openGLHelper.getViewMatrix()).toMatrix3f().invert();
        normalMatrix.transpose();
        glUniformMatrix3(uNMatrixAttribute, false, normalMatrix.getBuffer());

        glDrawElements(GL_TRIANGLES, sphereModel.getIndicesLength(), GL_UNSIGNED_INT, 0);

    }
    
    public void drawings(OpenGLHelper openGLHelper) {
    	
    	Matrix4f model = Matrix4f.scale(1.0f, 0.5f, 5.0f);
    	model = Matrix4f.translate(9.0f*(1.0f + (float)Math.cos(angle/8)*-x), 1+y, 0+z).multiply(model);
        glUniformMatrix4(uniModel, false, model.getBuffer());
        
        Matrix3f normalMatrix = model.multiply(openGLHelper.getViewMatrix()).toMatrix3f().invert();
        normalMatrix.transpose();
        glUniformMatrix3(uNMatrixAttribute, false, normalMatrix.getBuffer());

        glDrawElements(GL_TRIANGLES, sphereModel.getIndicesLength(), GL_UNSIGNED_INT, 0);

    }
       
    
}
