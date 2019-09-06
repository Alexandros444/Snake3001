package main;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import graphics.Display;
import graphics.Matrix3f;
import graphics.Vao;
import graphics.Vector3f;
import graphics.raymarcher.RayMarcherShader;

/**
 * Die Klasse mit der Main-Methode unseres Programms.<br>
 * 
 * Bisher nur als Beispiel für den Umgang mit Eclipse, Git  und Javadoc gedacht.
 * 
 * @author Ben
 * @author Alex
 */

public class Main {
	
	/**
	 * Die Main-Methode selbst. Sie öffnet ein Fenster.
	 * 
	 * @param args Kommandozeilenparameter
	 */
	
	public static void main(String[] args) {
		// Erstellt ein neues Fenster
		Display display = new Display(960,540,"SNAKE 3001");
		// Setzt die Hintergrundfarbe auf Magenta
		GL11.glClearColor(1,0,1,0);
		
		// Erstellt ein neues VAO mit den Eckpunkten eines Dreiecks
		// A(-0.5|-0.5) = Ecke links unten
		// B(0.5|-0.5) = Ecke rechts unten
		// C(0|0.5) = Ecke rechts oben
		Vao vao = new Vao(new float[] {-0.8f,-0.8f,0.8f,-0.8f,0,0.8f});
		RayMarcherShader shader = new RayMarcherShader();
		shader.start();
		// neue Matrix als Blickrichtung wird erstellt
		Matrix3f viewMatrix = new Matrix3f();
		
		//positionsvektor wird erstellt
		Vector3f position = new Vector3f();
		
		while(!display.isCloseRequested()) {
			// Setzt den Inhalt des Fensters auf die Hintergrundfarbe zurück
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			
			// Setzt den zu rendernden Bereich (bei Fenstergrößenänderungen wichtig)
			GL11.glViewport(0, 0, display.getWidth(), display.getHeight());
			
			// dreht die Sichtmatrix je nach Tasteninput und lädt sie in den Shader
			if (display.isKeyPressed(GLFW.GLFW_KEY_W) || display.isKeyPressed(GLFW.GLFW_KEY_UP) ) {
				viewMatrix.rotate(-0.25f, 0, 0);
			}
			if (display.isKeyPressed(GLFW.GLFW_KEY_S) || display.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
				viewMatrix.rotate(0.25f, 0, 0);
			}
			if (display.isKeyPressed(GLFW.GLFW_KEY_A) || display.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
				viewMatrix.rotate(0, -0.25f, 0);
			}
			if (display.isKeyPressed(GLFW.GLFW_KEY_D) || display.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
				viewMatrix.rotate(0, 0.25f, 0);
			}
			if (display.isKeyPressed(GLFW.GLFW_KEY_Q)) {
				viewMatrix.rotate(0, 0, 0.25f);
			}
			if (display.isKeyPressed(GLFW.GLFW_KEY_E)) {
				viewMatrix.rotate(0, 0, -0.25f);
			}
			shader.loadViewMatrix(viewMatrix);
			
			Vector3f movement = new Vector3f (0,0,0.01f);
			movement.apply(viewMatrix);
			position.add(movement);
			shader.loadPosition(position);
			
			// Rendert das Dreieck
			vao.render();
			
			// Updated den Bildschirm
			display.update();
		}
		
		// Löscht das Dreieck und schließt das Fenster
		shader.destroy();
		vao.destroy();
		display.close();
	}
	
}
