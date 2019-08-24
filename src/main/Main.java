package main;

import org.lwjgl.opengl.GL11;

import graphic.Display;
import graphic.Vao;

/**
 * Die Klasse mit der Main-Methode unseres Programms.<br>
 * 
 * Bisher nur als Beispiel f�r den Umgang mit Eclipse, Git  und Javadoc gedacht.
 * 
 * @author Ben
 * @author Alex
 */

public class Main {
	
	/**
	 * Die Main-Methode selbst. Sie �ffnet ein Fenster.
	 * 
	 * @param args Kommandozeilenparameter
	 */
	
	public static void main(String[] args) {
		// Erstellt ein neues Fenster
		Display display = new Display(960,540,"TITEL");
		// Setzt die Hintergrundfarbe auf Magenta
		GL11.glClearColor(1,0,1,0);
		
		// Erstellt ein neues VAO mit den Eckpunkten eines Dreiecks
		// A(-0.5|-0.5) = Ecke links unten
		// B(0.5|-0.5) = Ecke rechts unten
		// C(0|0.5) = Ecke rechts oben
		Vao vao = new Vao(new float[] {-0.5f,-0.5f,0.5f,-0.5f,0,0.5f});
		
		while(!display.isCloseRequested()) {
			// Setzt den Inhalt des Fensters auf die Hintergrundfarbe zur�ck
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			
			// Rendert das Dreieck
			vao.render();
			
			// Updated den Bildschirm
			display.update();
		}
		
		// L�scht das Dreieck und schlie�t das Fenster
		vao.destroy();
		display.close();
	}
	
}
