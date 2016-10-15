/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuladorv2;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_POLYGON_OFFSET_FILL;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPolygonOffset;
import static org.lwjgl.opengl.GL11.glVertex3f;
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

import Utils.CubeModel;
import Utils.Matrix3f;
import Utils.Matrix4f;
import Utils.OpenGLHelper;
import Utils.PlaneModel;
import Utils.ShaderProgram;
import Utils.Texture;

public class Pista extends Dibujable
{
	private final PlaneModel roadModel = new PlaneModel(5, 1);
    private int vbo_vroad;
    private int vbo_nroad;
    private int vbo_troad;;
    private int roadVao;
    private int uniModel;
    private int eboroad;
    private int uNMatrixAttribute;    
    private int useTextures;
    private int uKdAttribute;
    private int uKaAttribute;
    
    public Pista(int px, int py, int pz, ShaderProgram shaderProgram) {
        super(px, py, pz);
        
        int posAttrib = shaderProgram.getAttributeLocation("aVertexPosition");
        int vertexNormalAttribute = shaderProgram.getAttributeLocation("aVertexNormal");
        int texCoordsAttribute = shaderProgram.getAttributeLocation("aVertexTexCoord");

     // ----------------------- ROAD MODEL -----------------------------//
        roadVao = glGenVertexArrays();
        glBindVertexArray(roadVao);

        vbo_vroad = roadModel.createVerticesBuffer();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_vroad);
        glEnableVertexAttribArray(posAttrib);
        glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 0, 0);

        vbo_nroad = roadModel.createNormalsBuffer();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_nroad);
        glEnableVertexAttribArray(vertexNormalAttribute);
        glVertexAttribPointer(vertexNormalAttribute, 3, GL_FLOAT, false, 0, 0);

        vbo_troad = roadModel.createTextCoordsBuffer();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_troad);
        glEnableVertexAttribArray(texCoordsAttribute);
        glVertexAttribPointer(texCoordsAttribute, 2, GL_FLOAT, false, 0, 0);

        eboroad = roadModel.createIndicesBuffer();
        glBindVertexArray(0);
        
        
        uniModel = shaderProgram.getUniformLocation("model");
        uNMatrixAttribute = shaderProgram.getUniformLocation("uNMatrix");
        useTextures = shaderProgram.getUniformLocation("useTextures");
        
        uKaAttribute = shaderProgram.getUniformLocation("Ka");
        uKdAttribute = shaderProgram.getUniformLocation("Kd");

    }
 
    @Override
    public void draw(ShaderProgram shaderProgram, OpenGLHelper openGLHelper)
    {
    	System.out.println("Pista situada en coordenada (" + x + 
                            " " + y + " " + z +")");
    	glBindBuffer(GL_ARRAY_BUFFER, vbo_vroad);
    	glBindBuffer(GL_ARRAY_BUFFER, vbo_nroad);    
    	glBindBuffer(GL_ARRAY_BUFFER, vbo_troad); 
    	glBindVertexArray(roadVao);
        glUniform3f(uKaAttribute, 1.0f, 1.0f, 1.0f);
        glUniform3f(uKdAttribute, 1.0f, 1.0f, 1.0f);
        glUniform1i(useTextures, 1);
        int uniTex1 = shaderProgram.getUniformLocation("Texture1");

    	drawmaintrack(shaderProgram, openGLHelper,uniTex1);
    	drawbacktrack(shaderProgram, openGLHelper,uniTex1);
    	
    }
    	
    public void drawmaintrack(ShaderProgram shaderProgram, OpenGLHelper openGLHelper, int uniTex1){
    		
    	shaderProgram.setUniform(uniTex1, 3);
        
        Matrix4f model = Matrix4f.scale(12.0f, 1.0f, 4.0f);
        model = Matrix4f.translate(10, 0, 0).multiply(model);
        glUniformMatrix4(uniModel, false, model.getBuffer());

        Matrix3f normalMatrix = model.multiply(openGLHelper.getViewMatrix()).toMatrix3f().invert();
        normalMatrix.transpose();
        glUniformMatrix3(uNMatrixAttribute, false, normalMatrix.getBuffer());
        // Enable polygon offset to resolve depth-fighting isuses
        glEnable(GL_POLYGON_OFFSET_FILL);
        glPolygonOffset(-2.0f, 4.0f);
        glDrawElements(GL_TRIANGLES, roadModel.getIndicesLength(), GL_UNSIGNED_INT, 0);
        glDisable(GL_POLYGON_OFFSET_FILL);
    	}
    
    public void drawbacktrack(ShaderProgram shaderProgram, OpenGLHelper openGLHelper, int uniTex1){
		
    	shaderProgram.setUniform(uniTex1, 8);

        Matrix4f model = Matrix4f.scale(11.0f, 1.0f, 5.0f);
        model = Matrix4f.translate(11, 0, -13).multiply(model);
        glUniformMatrix4(uniModel, false, model.getBuffer());

        Matrix3f normalMatrix = model.multiply(openGLHelper.getViewMatrix()).toMatrix3f().invert();
        normalMatrix.transpose();
        glUniformMatrix3(uNMatrixAttribute, false, normalMatrix.getBuffer());
        // Enable polygon offset to resolve depth-fighting isuses
        glEnable(GL_POLYGON_OFFSET_FILL);
        glPolygonOffset(-2.0f, 4.0f);
        glDrawElements(GL_TRIANGLES, roadModel.getIndicesLength(), GL_UNSIGNED_INT, 0);
        glDisable(GL_POLYGON_OFFSET_FILL);
    	}
        
        
}
