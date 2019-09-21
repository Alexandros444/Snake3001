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
	public int playTimeSec;
	
	private Timer timer;
	
	// Konstruktor
	public FpsCounter() {
		startTime = (int) System.currentTimeMillis();
		timer = new Timer();
	}
	
	// Wird jeden Frame aufgerufen und aktualisiert die Zeit, und alle 4 sekunden die Fps
	public void update() {
		deltaTime = ((int)System.currentTimeMillis()) - startTime;
		frames++;
		
		// überprüfen ob 4 sekunde oder (4000ms) um sind, und aktualisiert dann die Fps
		if(deltaTime >= 1000) {
			startTime = (int) System.currentTimeMillis();
			fps = (short) (frames/(deltaTime/1000));
			playTimeSec += deltaTime/1000;
			deltaTime = 0;
			frames = 0;
			
			// gibt die Fps und die vergangene Spielzeit alle 2 Sekunden aus
			if (timer.getTimeSec() > 2) {		
				timer.reset();
				System.out.println("Played Time: "+ playTimeSec);
				System.out.println(fps);
			}
		}		
	}

	
}
