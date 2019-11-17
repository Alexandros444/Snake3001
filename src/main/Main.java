package main;

import org.lwjgl.glfw.GLFW;

import graphics.core.Display;
import graphics.gui.MainGuiContainer;
import graphics.gui.engine.InputHandler;
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
		
		// Erstellt das Spiel und Gui
		MainGuiContainer guiContainer = new MainGuiContainer(settings,inputHandler.getKeyInput(GLFW.GLFW_KEY_ESCAPE));
		
		// GAME LOOP läuft solange das Fenster nicht geschlossen ist
		while(!display.isCloseRequested()&&!guiContainer.isCloseRequested()) {
			
			// Fullscreen an/aus-schalten
			if(guiContainer.hasFullscreenChanged) {
				guiContainer.hasFullscreenChanged = false;
				display.toggleFullscreenMode();
			}
			
			// Gui wird gerendert
			int width = display.getWidth();
			int height = display.getHeight();
			float scale = 0.001f*Math.min(width,height*2);
			guiContainer.setScale(scale,scale);
			guiContainer.setSize(width,height);
			guiContainer.receiveMouseEvent(true,inputHandler.getCurrentMouseEvent());
			
			guiContainer.update(display);
			guiContainer.render(width,height);
			
			// Display und Inputs werden aktualisiert
			display.update();
			inputHandler.update();
		}
	
		// Beendet den Renderer und schließt das Fenster
		settings.save();
		guiContainer.destroy();
		display.close();	
	}
	
}
