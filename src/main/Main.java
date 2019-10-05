package main;

import org.lwjgl.glfw.GLFW;

import gamelogic.Snake;
import graphics.Display;
import graphics.FpsCounter;
import graphics.Timer;
import graphics.guiRenderer.GuiRenderer;
import graphics.raymarcher.RayMarcher;

/**
 * Die Klasse mit der Main-Methode unseres Programms.<br>
 * 
 * @author Ben
 * @author Alex
 */

public class Main {
	
	/**
	 * Die Main-Methode selbst. Sie öffnet das Fenster und updated es regelmäßig.<br>
	 * Alles wichtige steht quasi hier.
	 * 
	 * @param args Kommandozeilenparameter
	 */
	
	public static void main(String[] args) {
		
		// Erstellt ein neues Fenster
		Display display = new Display(960,540,"SNAKE 3001");
		// Setzt das Fenster-Symbol
		display.setWindowIcon("res/icon.png");
		
		// Erstellt den Fps Zähler
		FpsCounter fps = new FpsCounter();
		
		// Erstellt den RayMarcher-Renderer
		RayMarcher gameRenderer = new RayMarcher();
		// Reduziert die Auflösung, um die FPS zu erhöhen
		gameRenderer.setPixelSize(3);
		
		// Erstellt den Gui-Renderer
		GuiRenderer guiRenderer = new GuiRenderer();
		
		// Initialisiert Schlange
		Snake snake = new Snake();
		
		Timer timer = new Timer();
		
		while(!display.isCloseRequested()) {	
			// updated die Schlange
			snake.update(display);
			
			if(display.isKeyPressed(GLFW.GLFW_KEY_F) && (timer.getTimeSec()>0)) {
				timer.reset();
				display.toggleFullscreeMode();
			}
			
			if ((snake.isAlive==false)&&display.isKeyPressed(GLFW.GLFW_KEY_ENTER)){
			    snake = new Snake();
			}
			
			// rendert und updated den Bildschirm
			gameRenderer.render(snake,display.getWidth(),display.getHeight());
			guiRenderer.displayScore(snake.getScore());
			guiRenderer.render(display.getWidth(),display.getHeight());
			display.update();
			fps.update();
			
		}
	
		// Beendet den Renderer und schließt das Fenster
		gameRenderer.destroy();
		guiRenderer.destroy();
		display.close();
	}
	
}
