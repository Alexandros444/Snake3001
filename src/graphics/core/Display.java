package graphics.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWImage.Buffer;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.stb.STBImage;

import util.Settings;
import util.StaticUtils;

/**
 * Klasse zum erstellen eines Displays, hier wird unser Spiel angezeigt!
 * 
 * @author Alex
 */

public class Display {
	private Settings settings;
	private long windowID;
	private boolean isFullscreenMode;
	private int[] beforeFullscreenBounds = new int[4];
	
	
	/**
	 * Erstellt neues Display mit der Breite W, der Höhe H, und dem Titel titel
	 * 
	 * @param w Breite
	 * @param h Höhe
	 * @param title Titel
	 *
	 */
	public Display(Settings values, String title) {		
		this.settings = values;
		GLFWErrorCallback.createPrint(System.err).set();
		if (!GLFW.glfwInit()) {
			throw new IllegalStateException("GLFW kaputt");
		}
		windowID = GLFW.glfwCreateWindow(values.displayWidth, values.displayHeight, title, 0, 0);
		if (windowID == 0) {
			throw new IllegalStateException("Fenster kaputt");
		}
		GLFW.glfwMakeContextCurrent(windowID);
		GL.createCapabilities();
		if(settings.displayFullscreen) {
			toggleFullscreenMode();
		}		
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
		saveSettings();
		GLFW.glfwDestroyWindow(windowID);
		GLFW.glfwTerminate();
	}
	
	/**
	 * Übergibt Einstellungen des Displays an die Einstellungen wo sie gespeichert werden
	 */
	private void saveSettings(){
		settings.displayFullscreen = isFullscreenMode;
		if(!isFullscreenMode) {
			settings.displayWidth = getWidth();
			settings.displayHeight = getHeight();
		}
	}
	
	/**
	 * Liefert die Breite des Fensters.
	 * 
	 * @return Breite in Pixeln
	 */
	public int getWidth() {
		IntBuffer w = BufferUtils.createIntBuffer(1);	
		GLFW.glfwGetFramebufferSize(windowID,w,null);
		return w.get(0);
	}
	
	/**
	 * Liefert die Höhe des Fensters.
	 * 
	 * @return Höhe in Pixeln
	 */
	public int getHeight() {
		IntBuffer h = BufferUtils.createIntBuffer(1);	
		GLFW.glfwGetFramebufferSize(windowID,null,h);
		return h.get(0);
	}
	
	/**
	 * Liefert die X-Position der Maus.
	 * 
	 * @return X-Position relativ zur oberen linken Ecke in Pixeln
	 */
	public float getMouseX() {
		DoubleBuffer x = BufferUtils.createDoubleBuffer(1);	
		GLFW.glfwGetCursorPos(windowID,x,null);
		return (float)x.get(0);
	}
	
	/**
	 * Liefert die Y-Position der Maus.
	 * 
	 * @return Y-Position relativ zur oberen linken Ecke in Pixeln
	 */
	public float getMouseY() {
		DoubleBuffer y = BufferUtils.createDoubleBuffer(1);	
		GLFW.glfwGetCursorPos(windowID,null,y);
		return (float)y.get(0);
	}
	
	/**
	 * Gibt zurück, ob die linke Maustaste gedrückt ist.
	 * 
	 * @return ob die linke Maustaste gedrückt ist
	 */
	public boolean isLeftMouseDown() {
		return GLFW.glfwGetMouseButton(windowID,GLFW.GLFW_MOUSE_BUTTON_LEFT)==GLFW.GLFW_PRESS;
	}
	
	/**
	 * Gibt zurück, ob die rechte Maustaste gedrückt ist.
	 * 
	 * @return ob die rechte Maustaste gedrückt ist
	 */
	public boolean isRightMouseDown() {
		return GLFW.glfwGetMouseButton(windowID,GLFW.GLFW_MOUSE_BUTTON_RIGHT)==GLFW.GLFW_PRESS;
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
	 * @param path Pfad relativ zum Projektordner
	 */
	public void setWindowIcon(String path){
		IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);
		try {
			ByteBuffer fileContents = StaticUtils.ioResourceToByteBuffer("res/icon.png");
			ByteBuffer pixels = STBImage.stbi_load_from_memory(fileContents,widthBuffer,heightBuffer,comp,4);
			GLFWImage image = GLFWImage.malloc();
			image.set(widthBuffer.get(0),heightBuffer.get(0),pixels);
			Buffer buffer = GLFWImage.malloc(1);
			buffer.put(0,image);		
			GLFW.glfwSetWindowIcon(windowID,buffer);
		} catch (IOException e) {
			System.err.println("Display-Icon \""+path+"\" konnte nicht geladen werden!");
			e.printStackTrace();
		}			
	}

	/**
	 * Schaltet den Vollbildmodus an und aus.
	 */
	public void toggleFullscreenMode(){
		if(isFullscreenMode) {
			GLFW.glfwSetWindowMonitor(windowID, 0, beforeFullscreenBounds[0], beforeFullscreenBounds[1], beforeFullscreenBounds[2], beforeFullscreenBounds[3], GLFW.GLFW_DONT_CARE);
			isFullscreenMode = false;
		}else {
			setBeforePosition();
			GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			GLFW.glfwSetWindowMonitor(windowID,GLFW.glfwGetPrimaryMonitor(), 0, 0, vidMode.width(), vidMode.height(), vidMode.refreshRate());
			isFullscreenMode = true;
		}
		
	}
	
	/**
	 * Speichert die aktuelle Position und Größe des Fensters, sodass sie später wiederhergestellt werden kann.
	 */
	private void setBeforePosition() {
		IntBuffer tempX = BufferUtils.createIntBuffer(1);
		IntBuffer tempY = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowPos(windowID, tempX, tempY);
		beforeFullscreenBounds[0] = tempX.get(0);
		beforeFullscreenBounds[1] = tempY.get(0);
		GLFW.glfwGetWindowSize(windowID, tempX, tempY);
		beforeFullscreenBounds[2] = tempX.get(0);
		beforeFullscreenBounds[3] = tempY.get(0);
	}
	

}