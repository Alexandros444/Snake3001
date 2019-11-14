package util;

/**
 * Timer Klasse zu stoppen von Zeit
 * 
 * @author Alex
 */

public class Timer {
	
	private long startTime;
	
	/**
	 * Erstellt einen neuen Timer.
	 */
	public Timer() {
		reset();
	}
	
	/**
	 * Gibt die Zeit in Sekunden zurück
	 * @return Zeit in Sekunden
	 */
	public float getTime() {
		return (System.nanoTime()-startTime)*1e-9f;
	}
	
	/**
	 * Gibt die Zeit in Sekunden zurück
	 * @return Zeit auf volle Sekunden abgerundet
	 */
	public int getTimeSec(){
		return (int) ((System.nanoTime()-startTime)*1e-9);
	}
	
	/**
	 * Gibt die Zeit in Millisekunden zurück
	 * @return Zeit in Millisekunden
	 */
	public float getTimeMil() {
		return (float) ((System.nanoTime()-startTime)*1e-6);
	}
	
	/**
	 * Gibt die Zeit in Nanosekunden aus
	 * @return Zeit in Nanosekunden
	 */
	public long getTimeNano() {
		return System.nanoTime() - startTime;
	}
	
	/**
	 * setzt die Startzeit neu
	 */
	public void reset() {
		startTime = System.nanoTime();
	}
		
	
}
