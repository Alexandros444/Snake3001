package graphics;

import graphics.gui.Font;
import graphics.gui.TextComponent;

/**
 * Fps Zähler zählt die Fps und zeigt sie an 
 * 
 * @author Alex
 */

public class FpsCounter extends TextComponent{

	// Variablen für vergangene Zeit, Frames und die Frames/Sekunde
	private int startTime;
	private int deltaTime = 0;
	private short frames = 0;
	private short fps;

	/**
	 * Erstellt den FPSCounter
	 * 
	 * @param font Schriftart der FPS
	 */
	public FpsCounter(Font font) {
		super("FPS",font);
		startTime = (int) System.currentTimeMillis();
	}
	
	/**
	 * aktualisiert Zeit, und alle 4 sekunden Fps
	 */
	public void update() {
		deltaTime = ((int)System.currentTimeMillis()) - startTime;
		frames++;
		
		// überprüfen ob 1 sekunde oder (1000ms) um sind, und aktualisiert dann die Fps
		if(deltaTime >= 1000) {
			startTime = (int) System.currentTimeMillis();
			fps = (short) (frames/(deltaTime/1000));
			deltaTime = 0;
			frames = 0;
			super.setText(fps+" FPS");
		}
	}
	
}