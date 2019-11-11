package util;

/**
 * Speichert die derzeitigen Einstellungen
 * 
 * @author Alex
 */
public class Settings {
	// Variablen Deklaration
	private static String[] STANDARD_NAMES;
	private static String[] STANDARD_VALUES;

	private SettingsLoader config;

	public int displayWidth;
	public int displayHeight;
	public int snakeScore;
	public int crosshairFrame, crosshairCount = 5;
	public boolean displayFullscreen;
	public int difficulty;
	public int pixelSize;
	public String crosshairImagePath; //, difficultyImagePath;

	/**
	 * Instanz der Klasse erstellt Standard-Werte und lädt Einstellungen
	 */
	public Settings() {
		// Initailisiert Standard-Werte 
		STANDARD_NAMES = new String[] {"DISPLAY_WIDTH","DISPLAY_HEIGHT","SNAKE_SCORE","CROSSHAIR_FRAME","DISPLAY_FULLSCREEN","DIFFICULTY","PIXELSIZE"};
		STANDARD_VALUES = new String[] {""+960,""+540,""+0,""+0,""+false, ""+0,""+3};

		// Erstellt neue Instanz der Einstellungen--> Einstellungen werden geladen
		config = new SettingsLoader(STANDARD_NAMES,STANDARD_VALUES);
		
		// Variablen werden aus Einstellungen geladen 
		displayWidth = config.getInt("DISPLAY_WIDTH");
		displayHeight = config.getInt("DISPLAY_HEIGHT");
		snakeScore = config.getInt("SNAKE_SCORE");
		crosshairFrame = config.getInt("CROSSHAIR_FRAME");
		displayFullscreen = config.getBoolean("DISPLAY_FULLSCREEN");
		difficulty = config.getInt("DIFFICULTY");
		pixelSize = config.getInt("PIXELSIZE");
		crosshairImagePathRenew();
		//difficultyImagePathRenew();
	}
	
	/**
	 * lädt den Pfad des Crosshairs neu falls er geändert wurde
	 */
	public void crosshairImagePathRenew() {
		crosshairImagePath = "res/crosshairs"+crosshairFrame+".png";
	}
	/** AUSKOMMENTIERT WEIL IM MOMENT NICHT GEBRAUCHT
	 * lädt den Pfad des Difficulty Images neu falls er geändert wurde
	 */
	//public void difficultyImagePathRenew() {
	//	difficultyImagePath = "res/difficulty"+difficulty+".png";
	//}
	
	
	/**
	 * Speichert die Werte von Values in Einstellungen(Settings)
	 */
	public void save() {
		// setzt die Werte
		config.setValue("CROSSHAIR_FRAME",""+crosshairFrame);
		config.setValue("SNAKE_SCORE",""+snakeScore);
		config.setValue("DISPLAY_WIDTH",""+displayWidth);
		config.setValue("DISPLAY_HEIGHT",""+displayHeight);
		config.setValue("DISPLAY_FULLSCREEN",""+displayFullscreen);
		config.setValue("DIFFICULTY",""+difficulty);
		config.setValue("PIXELSIZE",""+pixelSize);
		// speichert die gesetzten Werte
		config.saveToFile();
	}

}
