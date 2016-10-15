/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuladorv2;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.GL_TEXTURE4;
import static org.lwjgl.opengl.GL13.GL_TEXTURE5;
import static org.lwjgl.opengl.GL13.GL_TEXTURE6;
import static org.lwjgl.opengl.GL13.GL_TEXTURE7;
import static org.lwjgl.opengl.GL13.GL_TEXTURE8;
import static org.lwjgl.opengl.GL13.GL_TEXTURE9;
import static org.lwjgl.opengl.GL13.GL_TEXTURE10;
import static org.lwjgl.opengl.GL13.GL_TEXTURE11;
import static org.lwjgl.opengl.GL13.GL_TEXTURE12;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindFragDataLocation;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import Utils.Drawable;
import Utils.FPCameraController;
import Utils.OpenGLHelper;
import Utils.ShaderProgram;
import Utils.Texture;

/**
 * 
 * @author Alba
 */
public class Simuladorv2 implements Drawable {

	private Aeropuerto aero = null;
	private ShaderProgram shaderProgram;
	private final OpenGLHelper openGLHelper = new OpenGLHelper(
			"Simulador v2.0", new FPCameraController(-5, -8, -18));
	private Simuladorv2 sim = null;
	private int uKdAttribute;
	private int uKaAttribute;
	private Texture maintrackTexture, towerbaseTexture, grasstexture,
			towerbodyTexture, towercontrolTexture, skyTexture, minortrackTexture, angarTexture,
			antennaTexture, piecesTexture;
	private int uniTex1, uniTex2, uniTex3;

	public static void main(String[] args) {
		// TODO code application logic here
		// Es aconsejable crear una clase para probar y manejar. El compilador
		// me decía que requería métodos estáticos (pero hay que intentar
		// evadirlos todo lo posible).
		Simuladorv2 sim = new Simuladorv2();
		sim.run();
	}

	public void run() {
		System.out
				.println("Hello LWJGL " + Sys.getVersion() + "!. Simuladorv2");
		openGLHelper.initGL("VS_ADS_Texture.vs", "FS_ADS_Texture4.fs");
		shaderProgram = openGLHelper.getShaderProgram();
		prepareTextures();
		aero = new Aeropuerto(0, 0, 0, shaderProgram);
		aero.add(new Skybox(0, 0, 0, shaderProgram));
		aero.add(new Pista(0, 0, 0, shaderProgram));
		aero.add(new TorreControl(0, 0, 0, shaderProgram));
		aero.add(new Angar(0, 0, 0, shaderProgram));
		aero.add(new Avion(1, 0, -14, shaderProgram));
		aero.add(new Avion(-1, 0, 0, shaderProgram));
		initLights();
		openGLHelper.run(this);

	}

	private void initLights() {
		int uLightPositionAttribute = shaderProgram
				.getUniformLocation("LightPosition");
		int uLightIntensityAttribute = shaderProgram
				.getUniformLocation("LightIntensity");

		uKaAttribute = shaderProgram.getUniformLocation("Ka");
		uKdAttribute = shaderProgram.getUniformLocation("Kd");

		int uShininessAttribute = shaderProgram.getUniformLocation("Shininess");

		glUniform4f(uLightPositionAttribute, 2.0f, 4.0f, 2.0f, 1.0f);
		glUniform3f(uLightIntensityAttribute, 0.7f, 0.7f, 0.7f);

		glUniform3f(uKaAttribute, 1.0f, 1.0f, 1.0f);
		glUniform3f(uKdAttribute, 0.8f, 0.8f, 0.8f);

		glUniform1f(uShininessAttribute, 18.0f);
	}

	private void prepareTextures() {

		glActiveTexture(GL_TEXTURE0);
		grasstexture = Texture.loadTexture("mygrass.jpg");
		uniTex1 = shaderProgram.getUniformLocation("Texture1");
		shaderProgram.setUniform(uniTex1, 0);

		glActiveTexture(GL_TEXTURE1);
		Texture texture = Texture.loadTexture("baserock.jpg");
		uniTex2 = shaderProgram.getUniformLocation("Texture2");
		shaderProgram.setUniform(uniTex2, 1);

		glActiveTexture(GL_TEXTURE2);
		texture = Texture.loadTexture("deadalpha.png");
		uniTex3 = shaderProgram.getUniformLocation("Texture3");
		shaderProgram.setUniform(uniTex3, 2);

		glBindVertexArray(0);

		glActiveTexture(GL_TEXTURE3);
		maintrackTexture = Texture.loadTexture("maintrack.jpg");

		glActiveTexture(GL_TEXTURE4);
		towerbodyTexture = Texture.loadTexture("towerbody.jpg");

		glActiveTexture(GL_TEXTURE5);
		towercontrolTexture = Texture.loadTexture("towercontrol.jpg");

		glActiveTexture(GL_TEXTURE6);
		towerbaseTexture = Texture.loadTexture("towerbase.jpg");

		glActiveTexture(GL_TEXTURE7);
		skyTexture = Texture.loadTexture("nightsky.jpg");

		glActiveTexture(GL_TEXTURE8);
		minortrackTexture = Texture.loadTexture("minortrack.jpg");

		glActiveTexture(GL_TEXTURE9);
		antennaTexture = Texture.loadTexture("antenna.png");

		glActiveTexture(GL_TEXTURE10);
		angarTexture = Texture.loadTexture("angar.jpg");

		glActiveTexture(GL_TEXTURE11);
		piecesTexture = Texture.loadTexture("pieces.jpg");
	}
	
	@Override
	public void draw() {
		aero.draw(shaderProgram, openGLHelper);
		// TODO Auto-generated method stub

	}
}
