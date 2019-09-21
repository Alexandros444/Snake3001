package graphics;

/*
 * Timer Klasse zu stoppen von Zeit
 * 
 * @author Alex
 */

public class Timer {
	// StartZeit
	private long startTime;
	
	// Konstruktor
	public Timer() {
		reset();
	}
	
	/*
	 * Gibt die Zeit in Sekunden aus
	 */
	public int getTimeSec(){
		return (int) ((System.nanoTime()-startTime)*1e-9);
	}
	
	/*
	 * Gibt die Zeit in Millisekunden aus
	 */
	public float getTimeMil() {
		return (float) ((System.nanoTime()-startTime)*1e-6);
	}
	
	/*
	 * Gibt die Zeit in Nanosekunden aus
	 */
	public long getTimeNano() {
		return System.nanoTime() - startTime;
	}
	
	/*
	 * setzt die Startzeit neu
	 */
	public void reset() {
		startTime = System.nanoTime();
	}
		
	
}
