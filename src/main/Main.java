package main;

import org.lwjgl.glfw.GLFW;

import gamelogic.Snake;
import gamelogic.World;
import graphics.Display;
import graphics.InputHandler;
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
		
		// Erstellt Config-Datei, Spielstände werden geladen
		Settings settings = new Settings();
		
		// Erstellt ein neues Fenster
		Display display = new Display(settings,"SNAKE 3001");
		display.setWindowIcon("res/icon.png");
		// erstellt InputHandler zum verarbeiten der Inputs auf das Fenster
		InputHandler inputHandler = new InputHandler(display);
		
		// Erstellt den RayMarcher-Renderer
		RayMarcher gameRenderer = new RayMarcher();
		// Reduziert die Auflösung, um die FPS zu erhöhen
		gameRenderer.setPixelSize(3);
		
		// Erstellt den Gui-Renderer
		GuiRenderer guiRenderer = new GuiRenderer(settings);
		
		
		World world = new World(settings);
		
		// Initialisiert einen Timer der die Zeit stoppt
		Timer timer = new Timer();
		
		// GAME LOOP läuft solange das Fenster nicht geschlossen ist
		while(!display.isCloseRequested()) {	
			// updated die Schlange
			world.update(display);
			
			// Fullscreen an/aus-schalten
			if(display.isKeyPressed(GLFW.GLFW_KEY_F) && (timer.getTimeSec()>0)) {
				// momentan nur im 1 sekunden abstand, später mit Key-Handler
				timer.reset();
				display.toggleFullscreenMode();
			}
			
			// Überprüfen ob Schlange gestorben ist, wenn ja Spiel neu-Starten 
			if ((world.snake.isAlive==false)&&display.isKeyPressed(GLFW.GLFW_KEY_ENTER)){
			    world.respawnSnake();
			}
			
			// Spiel wird gerendert
			gameRenderer.render(world,display.getWidth(),display.getHeight());
			// Punktzahl wird geupdated
			guiRenderer.displayScore(world.score);
			// Gui wird gerendert
			guiRenderer.render(display.getWidth(),display.getHeight(),inputHandler.getCurrentMouseEvent());
			// Display und Inputs werden aktualisiert
			display.update();
			inputHandler.update();
		}
	
		// Beendet den Renderer und schließt das Fenster
		gameRenderer.destroy();
		guiRenderer.destroy();
		display.close();
		settings.save();		
	}
	
}
