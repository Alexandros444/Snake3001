package main;

/**
 * Klasse für zu speichernde Werte
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

	/**
	 * Instanz der Klasse erstellt Standard-Werte und lädt Einstellungen
	 */
	public Settings() {
		// Initailisiert Standard-Werte 
		STANDARD_NAMES = new String[] {"DISPLAY_WIDTH","DISPLAY_HEIGHT","SNAKE_SCORE"};
		STANDARD_VALUES = new String[] {""+960,""+540,""+0};

		// Erstellt neue Instanz der Einstellungen--> Einstellungen werden geladen
		config = new SettingsLoader(STANDARD_NAMES,STANDARD_VALUES);
		
		// Variablen werden aus Einstellungen geladen 
		displayWidth = config.getInt("DISPLAY_WIDTH");
		displayHeight = config.getInt("DISPLAY_HEIGHT");
		snakeScore = config.getInt("SNAKE_SCORE");
	}

	/**
	 * Speichert die Werte von Values in Einstellungen(Settings)
	 */
	public void save() {
		// setzt die Werte
		config.setValue("DISPLAY_WIDTH",""+displayWidth);
		config.setValue("DISPLAY_HEIGHT",""+displayHeight);
		config.setValue("SNAKE_SCORE",""+snakeScore);
		// speichert die gesetzten Werte
		config.saveToFile();
	}

}
