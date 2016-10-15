package simuladorv2;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix3;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import Utils.CubeModel;
import Utils.Matrix3f;
import Utils.Matrix4f;
import Utils.OpenGLHelper;
import Utils.ShaderProgram;

public class Skybox extends Dibujable {

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
	 
    public Skybox(int px, int py, int pz, ShaderProgram shaderProgram) {
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
    public void draw(ShaderProgram shaderProgram, OpenGLHelper openGLHelper){
       	
    	System.out.println("Skybox situado en coordenada (" + x + 
 				" " + y + " " + z +")");
    	
    	glBindBuffer(GL_ARRAY_BUFFER, vbo_vcube);
    	glBindBuffer(GL_ARRAY_BUFFER, vbo_tcube);
    	glBindBuffer(GL_ARRAY_BUFFER, vbo_ncube);
    	glBindVertexArray(cubeVao);
    	glUniform1i(useTextures, 1);
    	glUniform3f(uKaAttribute, 1.0f, 1.0f, 1.0f);
        glUniform3f(uKdAttribute, 0.8f, 0.8f, 0.8f);
    	int uniTex1 = shaderProgram.getUniformLocation("Texture1");
    	shaderProgram.setUniform(uniTex1, 7);
    	
        Matrix4f model = Matrix4f.scale(30.0f, 30.0f, 20.0f);
        model = Matrix4f.translate(10, 29, 0).multiply(model);
        glUniformMatrix4(uniModel, false, model.getBuffer());

        Matrix3f normalMatrix = model.multiply(openGLHelper.getViewMatrix()).toMatrix3f().invert();
        normalMatrix.transpose();
        glUniformMatrix3(uNMatrixAttribute, false, normalMatrix.getBuffer());

        glDrawElements(GL_TRIANGLES, cubeModel.getIndicesLength(), GL_UNSIGNED_INT, 0);
    }
}
