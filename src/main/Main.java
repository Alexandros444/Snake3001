package main;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import gamelogic.Snake;
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
		Vao vao = new Vao(new float[] {-1,-1,-1,1,1,-1,-1,1,1,1,1,-1});
		// Erstellt und aktiviert den RayMarcher-Shader
		RayMarcherShader shader = new RayMarcherShader();
		shader.start();
		// Initialisiert Schlange
		Snake snake = new Snake();
		// Setzt das Fenster-Symbol
		display.setWindowIcon("res/icon.png");
		
		while(!display.isCloseRequested()) {	
			
			// Setzt den Inhalt des Fensters auf die Hintergrundfarbe zurück
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			
			// Setzt den zu rendernden Bereich (bei Fenstergrößenänderungen wichtig)
			GL11.glViewport(0, 0, display.getWidth(), display.getHeight());
			
			// dreht die Sichtmatrix je nach Tasteninput und lädt sie in den Shader
			snake.update(display);
			shader.loadViewMatrix(snake.viewDirection);
			shader.loadPosition(snake.cameraPosition);
			
			// lädt eine einfache Schlange als Beispiel in den Shader
			shader.loadSnake(snake.arrayVecsSnake);
			
			// Gibt das Seitenverhältnis des Fensters an den Shader weiter
			float ratio = (float)display.getWidth()/display.getHeight();
			shader.loadScreenRatio(ratio);
			
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
