package graphics;
/*
 * Fps Zähler
 * 
 * @author Alex
 */


public class FpsCounter {

	// Variablen für vergangene Zeit, Frames und die Frames/Sekunde
	private int startTime;
	private int deltaTime = 0;
	private short frames = 0;
	public short fps;
	
	// Konstruktor
	public FpsCounter() {
		startTime = (int) System.currentTimeMillis();
	}
	
	// Wird jeden Frame aufgerufen und aktualisiert die Zeit, und alle 4 sekunden die Fps
	public void update() {
		deltaTime = ((int)System.currentTimeMillis()) - startTime;
		frames++;
		
		// überprüfen ob 4 sekunde oder (4000ms) um sind, und aktualisiert dann die Fps
		if(deltaTime >= 4000) {
			startTime = (int) System.currentTimeMillis();
			fps = (short) (frames/(deltaTime/1000));
			System.out.println(fps);
			deltaTime = 0;
			frames = 0;
		}
	}
	
}
