package graphics;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import org.lwjgl.stb.STBImage;
import org.lwjgl.glfw.GLFWImage.Buffer;

/**
 * Klasse zum erstellen eines Displays, hier wird unser Spiel angezeigt!
 * 
 * @author Alex diktiert von Ben(Dem Diktator)
 */

public class Display {
	private long windowID;

	/**
	 * Erstellt neues Display mit der Breite W, der Höhe H, und dem Titel titel
	 * 
	 * @param w Breite
	 * @param h Höhe
	 * @param title Titel
	 *
	 */

	public Display(int w, int h, String title) {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!GLFW.glfwInit()) {
			throw new IllegalStateException("GLFW kaputt");
		}
		windowID = GLFW.glfwCreateWindow(w, h, title, 0, 0);
		if (windowID == 0) {
			throw new IllegalStateException("Fenster kaputt");
		}
		GLFW.glfwMakeContextCurrent(windowID);
		GL.createCapabilities();
	}

	/**
	 * Updated das Display und checkt die Events
	 */
	
	public void update() {
		GLFW.glfwSwapBuffers(windowID);
		GLFW.glfwPollEvents();
	}
	
	/**
	 * @return Ob das Fenster geschlossen werden soll
	 */

	public boolean isCloseRequested() {
		return GLFW.glfwWindowShouldClose(windowID);
	}
	
	/**
	 * Schließt das Fenster.
	 */
	
	public void close() {
		GLFW.glfwDestroyWindow(windowID);
		GLFW.glfwTerminate();
	}
	
	/**
	 * Liefert die Breite des Fensters.
	 */
	
	public int getWidth() {
		IntBuffer w = BufferUtils.createIntBuffer(1);	
		GLFW.glfwGetFramebufferSize(windowID,w,null);
		return w.get(0);
	}
	
	/**
	 * Liefert die Höhe des Fensters.
	 */
	
	public int getHeight() {
		IntBuffer h = BufferUtils.createIntBuffer(1);	
		GLFW.glfwGetFramebufferSize(windowID,null,h);
		return h.get(0);
	}
	
	/**
	 * Gibt zurück ob eine taste gedrückt wurde
	 * @param key Tasten ID Format: GLFW_KEY_E 
	 * @return true oder false ob taste gedrückt ist
	 */
	
	public boolean isKeyPressed(int key) {
		int state = GLFW.glfwGetKey(windowID, key);
		if (state == GLFW.GLFW_PRESS) {
		    return true; 
		}
		return false;
	}
	
	/**
	 * Setzt das Fenster-Symbol
	 * 
	 * @param path relativ zum Projektordner
	 */
	public void setWindowIcon(String path){
		IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);
		ByteBuffer pixels = STBImage.stbi_load(path,widthBuffer,heightBuffer,comp,4);
		GLFWImage image = GLFWImage.malloc();
		image.set(widthBuffer.get(0),heightBuffer.get(0),pixels);
		Buffer buffer = GLFWImage.malloc(1);
		buffer.put(0,image);		
		GLFW.glfwSetWindowIcon(windowID,buffer);			
	}

}