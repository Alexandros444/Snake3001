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
	public int normalScore,fastScore,tunnelScore;
	public int crosshairFrame, crosshairCount = 5;
	public boolean isFullscreen;
	public boolean isCaveEffectEnabled;
	public boolean isAcidEffectEnabled;
	public int pixelSize;
	public int cursorFrame;
	public float fov;
	public float reflectivity;
	public String cursorImagePath;
	public String crosshairImagePath; //, difficultyImagePath;
	public boolean isMusicEnabled;

	/**
	 * Instanz der Klasse erstellt Standard-Werte und lädt Einstellungen
	 */
	public Settings() {
		// Initailisiert Standard-Werte 
		STANDARD_NAMES = new String[] {"DISPLAY_WIDTH","DISPLAY_HEIGHT","CROSSHAIR_FRAME","IS_FULLSCREEN","PIXELSIZE","CURSOR","CAVE_EFFECT","ACID_EFFECT","FOV","REFLECTIVITY","MUSIC"};
		STANDARD_VALUES = new String[] {""+960,""+540,""+0,""+false,""+3,""+0,""+false,""+false,""+1.0,""+0.5,""+false};

		// Erstellt neue Instanz der Einstellungen--> Einstellungen werden geladen
		config = new SettingsLoader(STANDARD_NAMES,STANDARD_VALUES);
		
		// Highscores werden geladen
		int[] scores = ScoresLoader.loadScores("saves/highscores.bin",3);
		normalScore = scores[0];
		fastScore = scores[1];
		tunnelScore = scores[2];
		
		// Variablen werden aus Einstellungen geladen 
		displayWidth = config.getInt("DISPLAY_WIDTH");
		displayHeight = config.getInt("DISPLAY_HEIGHT");
		crosshairFrame = config.getInt("CROSSHAIR_FRAME");
		isFullscreen = config.getBoolean("IS_FULLSCREEN");
		pixelSize = config.getInt("PIXELSIZE");
		cursorFrame = config.getInt("CURSOR");
		isCaveEffectEnabled = config.getBoolean("CAVE_EFFECT");
		isAcidEffectEnabled = config.getBoolean("ACID_EFFECT");
		fov = config.getFloat("FOV");
		reflectivity = config.getFloat("REFLECTIVITY");
		isMusicEnabled = config.getBoolean("MUSIC");
		crosshairImagePathRenew();
		curserImagePathRenew();
	}
	
	/**
	 * lädt den Pfad des Crosshairs neu falls er geändert wurde
	 */
	public void crosshairImagePathRenew() {
		crosshairImagePath = "res/crosshairs"+crosshairFrame+".png";
	}
	
	/**
	 * lädt den Pfad des Cursers neu falls er geändert wurde
	 */
	public void curserImagePathRenew() {
		cursorImagePath = "res/cursor"+cursorFrame+".png";
	}
	
	/**
	 * Speichert die Werte von Values in Einstellungen(Settings)
	 */
	public void save() {
		// setzt die Werte
		config.setValue("CROSSHAIR_FRAME",""+crosshairFrame);
		config.setValue("DISPLAY_WIDTH",""+displayWidth);
		config.setValue("DISPLAY_HEIGHT",""+displayHeight);
		config.setValue("IS_FULLSCREEN",""+isFullscreen);
		config.setValue("PIXELSIZE",""+pixelSize);
		config.setValue("CURSOR",""+cursorFrame);
		config.setValue("CAVE_EFFECT",""+isCaveEffectEnabled);
		config.setValue("ACID_EFFECT",""+isAcidEffectEnabled);
		config.setValue("FOV", ""+fov);
		config.setValue("REFLECTIVITY", ""+reflectivity);
		config.setValue("MUSIC",""+isMusicEnabled);
		// speichert die gesetzten Werte
		config.saveToFile();
		
		int[] scoresFromFile = ScoresLoader.loadScores("saves/highscores.bin",3);
		normalScore = Math.max(normalScore,scoresFromFile[0]);
		fastScore = Math.max(fastScore,scoresFromFile[1]);
		tunnelScore = Math.max(tunnelScore,scoresFromFile[2]);
		ScoresLoader.saveScores("saves/highscores.bin", new int[] {normalScore,fastScore,tunnelScore});
	}

}
