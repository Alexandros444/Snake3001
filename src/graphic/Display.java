package graphic;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

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
	 * Schließt das Fenster 
	 */
	
	public void close() {
		GLFW.glfwDestroyWindow(windowID);
		GLFW.glfwTerminate();
	}
}
