package simuladorv2;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix3;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;

import Utils.CubeModel;
import Utils.FPCameraController;
import Utils.Matrix3f;
import Utils.Matrix4f;
import Utils.OpenGLHelper;
import Utils.ShaderProgram;

public class TorreControl extends Dibujable{
	
	private final CubeModel cubeModel = new CubeModel();   
	private int vbo_vcube;
    private int vbo_ncube;
    private int vbo_tcube;
    private int cubeVao;
    private int ebocube;
    private int uKdAttribute;
    private int uKaAttribute;
    private int useTextures;
    private int uniModel;
    private int uNMatrixAttribute;
	 
    public TorreControl(int px, int py, int pz, ShaderProgram shaderProgram) {
	        super(px, py, pz);
	        
	        int posAttrib = shaderProgram.getAttributeLocation("aVertexPosition");
	        int vertexNormalAttribute = shaderProgram.getAttributeLocation("aVertexNormal");
	        int texCoordsAttribute = shaderProgram.getAttributeLocation("aVertexTexCoord");
	      	        
	        //-----------------------CUBE MODEL----------------------------------------//
	        cubeVao = glGenVertexArrays();
	        glBindVertexArray(cubeVao);
	        
	        vbo_vcube = cubeModel.createVerticesBuffer();
	        glBindBuffer(GL_ARRAY_BUFFER, vbo_vcube);
	        glEnableVertexAttribArray(posAttrib);
	        glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 0, 0);

	        vbo_ncube = cubeModel.createNormalsBuffer();
	        glBindBuffer(GL_ARRAY_BUFFER, vbo_ncube);
	        glEnableVertexAttribArray(vertexNormalAttribute);
	        glVertexAttribPointer(vertexNormalAttribute, 3, GL_FLOAT, false, 0, 0);

	        glBindBuffer(GL_ARRAY_BUFFER, vbo_vcube);
	        glEnableVertexAttribArray(posAttrib);
	        glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 0, 0);

	        glBindBuffer(GL_ARRAY_BUFFER, vbo_ncube);
	        glEnableVertexAttribArray(vertexNormalAttribute);
	        glVertexAttribPointer(vertexNormalAttribute, 3, GL_FLOAT, false, 0, 0);

	        vbo_tcube = cubeModel.createTextCoordsBuffer();
	        glBindBuffer(GL_ARRAY_BUFFER, vbo_tcube);
	        glEnableVertexAttribArray(texCoordsAttribute);
	        glVertexAttribPointer(texCoordsAttribute, 2, GL_FLOAT, false, 0, 0);

	        ebocube = cubeModel.createIndicesBuffer();
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
        System.out.println("Torre situada en coordenada (" + x + 
             				" " + y + " " + z +")");
        
        glBindBuffer(GL_ARRAY_BUFFER, vbo_vcube);
    	glBindBuffer(GL_ARRAY_BUFFER, vbo_tcube);
    	glBindBuffer(GL_ARRAY_BUFFER, vbo_ncube);
    	glBindVertexArray(cubeVao);
    	glUniform1i(useTextures, 1);
    	glUniform3f(uKaAttribute, 1.0f, 1.0f, 1.0f);
        glUniform3f(uKdAttribute, 0.8f, 0.8f, 0.8f);
        int uniTex1 = shaderProgram.getUniformLocation("Texture1");
           	
        drawbases(shaderProgram, openGLHelper, uniTex1);
        drawbody(shaderProgram, openGLHelper, uniTex1);
        drawcontrol(shaderProgram, openGLHelper, uniTex1);
        angle += angularVelocity*openGLHelper.getDeltaTime();
        drawantenna(shaderProgram, openGLHelper, uniTex1);
                	
    }
    
    public void drawbases(ShaderProgram shaderProgram, OpenGLHelper openGLHelper, int uniTex1){
    	
        shaderProgram.setUniform(uniTex1, 6);
        //-----------------------------------------------------------------------------------//
    	Matrix4f model = Matrix4f.scale(2.5f, 0.2f, 2.0f);
        model = Matrix4f.translate(-3, 1, -6).multiply(model);
        glUniformMatrix4(uniModel, false, model.getBuffer());

        Matrix3f normalMatrix = model.multiply(openGLHelper.getViewMatrix()).toMatrix3f().invert();
        normalMatrix.transpose();
        glUniformMatrix3(uNMatrixAttribute, false, normalMatrix.getBuffer());
    	
        glDrawElements(GL_TRIANGLES, cubeModel.getIndicesLength(), GL_UNSIGNED_INT, 0);
        //-----------------------------------------------------------------------------------//
        model = Matrix4f.scale(1.5f, 0.2f, 2.0f);
        model = Matrix4f.translate(-3, 5, -6).multiply(model);
        glUniformMatrix4(uniModel, false, model.getBuffer());

        normalMatrix = model.multiply(openGLHelper.getViewMatrix()).toMatrix3f().invert();
        normalMatrix.transpose();
        glUniformMatrix3(uNMatrixAttribute, false, normalMatrix.getBuffer());
    	
        glDrawElements(GL_TRIANGLES, cubeModel.getIndicesLength(), GL_UNSIGNED_INT, 0);
        //-----------------------------------------------------------------------------------//
        model = Matrix4f.scale(2.5f, 0.2f, 2.0f);
        model = Matrix4f.translate(-3, 9, -6).multiply(model);
        glUniformMatrix4(uniModel, false, model.getBuffer());

        normalMatrix = model.multiply(openGLHelper.getViewMatrix()).toMatrix3f().invert();
        normalMatrix.transpose();
        glUniformMatrix3(uNMatrixAttribute, false, normalMatrix.getBuffer());
    	
        glDrawElements(GL_TRIANGLES, cubeModel.getIndicesLength(), GL_UNSIGNED_INT, 0);
    }
    
    public void drawbody(ShaderProgram shaderProgram, OpenGLHelper openGLHelper, int uniTex1) {
        
    	shaderProgram.setUniform(uniTex1, 4);
        //------------------------------------------------------------------------------------//
        Matrix4f model = Matrix4f.scale(1.5f, 1.8f, 2.0f);
        model = Matrix4f.translate(-3, 3, -6).multiply(model);
        glUniformMatrix4(uniModel, false, model.getBuffer());

        Matrix3f normalMatrix = model.multiply(openGLHelper.getViewMatrix()).toMatrix3f().invert();
        normalMatrix.transpose();
        glUniformMatrix3(uNMatrixAttribute, false, normalMatrix.getBuffer());

        glDrawElements(GL_TRIANGLES, cubeModel.getIndicesLength(), GL_UNSIGNED_INT, 0);
        //--------------------------------------------------------------------------------------//
        model = Matrix4f.scale(1.5f, 1.8f, 2.0f);
        model = Matrix4f.translate(-3, 7, -6).multiply(model);
        glUniformMatrix4(uniModel, false, model.getBuffer());

        normalMatrix = model.multiply(openGLHelper.getViewMatrix()).toMatrix3f().invert();
        normalMatrix.transpose();
        glUniformMatrix3(uNMatrixAttribute, false, normalMatrix.getBuffer());

        glDrawElements(GL_TRIANGLES, cubeModel.getIndicesLength(), GL_UNSIGNED_INT, 0);
    }
           
    public void drawcontrol(ShaderProgram shaderProgram, OpenGLHelper openGLHelper, int uniTex1) {
        
    	shaderProgram.setUniform(uniTex1, 5);

        Matrix4f model = Matrix4f.scale(2.5f, 1.8f, 2.0f);
        model = Matrix4f.translate(-3, 11, -6).multiply(model);
        glUniformMatrix4(uniModel, false, model.getBuffer());

        Matrix3f normalMatrix = model.multiply(openGLHelper.getViewMatrix()).toMatrix3f().invert();
        normalMatrix.transpose();
        glUniformMatrix3(uNMatrixAttribute, false, normalMatrix.getBuffer());

        glDrawElements(GL_TRIANGLES, cubeModel.getIndicesLength(), GL_UNSIGNED_INT, 0);
        
    }
    
    private float angle;
    private static final float angularVelocity = 10.f;
    
    public void drawantenna(ShaderProgram shaderProgram, OpenGLHelper openGLHelper, int uniTex1){
    	shaderProgram.setUniform(uniTex1, 6);
    	
        Matrix4f model = Matrix4f.scale(0.2f, 1.0f, 0.2f);
        model = Matrix4f.translate(-3, 14, -6).multiply(model);
        glUniformMatrix4(uniModel, false, model.getBuffer());

        Matrix3f normalMatrix = model.multiply(openGLHelper.getViewMatrix()).toMatrix3f().invert();
        normalMatrix.transpose();
        glUniformMatrix3(uNMatrixAttribute, false, normalMatrix.getBuffer());

        glDrawElements(GL_TRIANGLES, cubeModel.getIndicesLength(), GL_UNSIGNED_INT, 0);
        //----------------------------------------------------------------------------------------//    
        shaderProgram.setUniform(uniTex1, 9);
        model = Matrix4f.rotate(angle, 0f, 0.5f, 0f);
        model = Matrix4f.scale(1.0f, 1.0f, 1.0f).multiply(model);;
        model = Matrix4f.translate(-3, 16, -6).multiply(model);
        glUniformMatrix4(uniModel, false, model.getBuffer());

        normalMatrix = model.multiply(openGLHelper.getViewMatrix()).toMatrix3f().invert();
        normalMatrix.transpose();
        glUniformMatrix3(uNMatrixAttribute, false, normalMatrix.getBuffer());

        glDrawElements(GL_TRIANGLES, cubeModel.getIndicesLength(), GL_UNSIGNED_INT, 0);
    }

}
