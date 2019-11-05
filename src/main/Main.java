package main;

import org.lwjgl.glfw.GLFW;

import gamelogic.World;
import graphics.GuiRenderer;
import graphics.RayMarcher;
import graphics.core.Display;
import graphics.gui.InputHandler;
import graphics.gui.engine.KeyInput;
import util.Settings;

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
		KeyInput keyInputF = inputHandler.getKeyInput(GLFW.GLFW_KEY_F);
		
		// Erstellt den RayMarcher-Renderer
		RayMarcher gameRenderer = new RayMarcher();
		// Reduziert die Auflösung, um die FPS zu erhöhen
		gameRenderer.setPixelSize(3);
		
		// Erstellt den Gui-Renderer
		GuiRenderer guiRenderer = new GuiRenderer(settings,inputHandler.getKeyInput(GLFW.GLFW_KEY_ESCAPE));
		
		
		World world = new World(settings);
		
		// GAME LOOP läuft solange das Fenster nicht geschlossen ist
		while(!display.isCloseRequested()) {	
			// updated die Schlange
			world.update(display);
			
			// Fullscreen an/aus-schalten
			if(keyInputF.wasKeyPressed()) {
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
			guiRenderer.render(display.getWidth(),display.getHeight(),world,inputHandler.getCurrentMouseEvent());
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
