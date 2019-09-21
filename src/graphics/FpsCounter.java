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
	
	private String[] timeMarkNames;
	private int[] timeMarks;
	
	// Konstruktor
	public FpsCounter() {
		startTime = (int) System.currentTimeMillis();
		
		timeMarkNames = new String[0];
		timeMarks = new int[0];
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
			
			if (getTimeMark("showStats") > 3) {		
				setTimeMark("showStats");
				System.out.println("Played Time: "+ playTimeSec);
				System.out.println(fps);
			}
		}		
	}

	/*
	 * Setzet eine Zeitmarke
	 * 
	 * wenn noch keine Zeitmarke mit dem Namen existiert wird sie erstellt
	 * 
	 * @param Name der Zeitmarke
	 */
	public void setTimeMark(String fromName) {
		// richtige Zeitmarke finden
		for (String loopName : timeMarkNames) {
			// Namen überprüfen
			if(loopName.equals(fromName)){
				// neue Zeitmarke auf jetzige Zeit setzen
				timeMarks[loopName.indexOf(loopName)] = playTimeSec;
				// Methode beenden
				return;
			}
		}
		// Wenn kein ZeitmarkenName existiert wird eine neue erstellt
		createNewTimeMark(fromName);
	}
	
	/*
	 * Gibt die vergangene Zeit zwischen letztem Aufruf der timeMark(Name) methode und der jetzigen Zeit aus
	 * 
	 * Setzt eine Zeitmarke wenn noch keine vorhanden ist
	 * 
	 * @param Name der Zeitmarke
	 */
	public int getTimeMark(String fromName) {
		int temp = 0;
		boolean found = false;
		for (String loopName : timeMarkNames) {
			// richtige Zeitmarke gefunden
			if(loopName.equals(fromName)){
				// Name existiert
				found = true;
				// Es gibt noch keinen Wert (Zeitmarke wurde noch nicht aufgerufen)
				if(timeMarks[loopName.indexOf(loopName)] == 0) {
					// temp auf Spielzeit setzen
					temp = playTimeSec;
				}else {
					// Es wurde schon eine Zeitmarke gesetzt
					// temp auf die Differenz zwischen letzter Zeitmarke und jetziger Zeit setzen
					temp = playTimeSec - timeMarks[loopName.indexOf(loopName)];
				}
			}
		}
		// Wenn kein ZeitmarkenName existiert
		if (!found) {
			// Erstelle neuen Zeitmarke
			setTimeMark(fromName);
		}
		return temp;
	}

	/*
	 * Erstellt neue Zeitmarke mit dem Parameter-Namen 
	 * 
	 * @param Name der Zeitmarke 
	 */
	private void createNewTimeMark(String fromName) {
		// Initialisierung von Hilfs-Arrays
		String[] tempNames = new String[timeMarkNames.length+1];
		int[] tempMarks = new int[timeMarks.length+1];
		// Übertragung der Namen
		for (int i = 0; i < timeMarkNames.length; i++) {
			tempNames[i] = timeMarkNames[i];
		}
		// Übertragung der Werte
		for (int i = 0; i < timeMarks.length; i++) {
			tempMarks[i] = timeMarks[i];
		}
		// Setzen des letzten Namens
		tempNames[timeMarkNames.length] = fromName;
		// setzt die (richtigen)Arrays auf die Hilfs-Array 
		timeMarkNames = tempNames;
		timeMarks = tempMarks;
		
	}
	
	
}
