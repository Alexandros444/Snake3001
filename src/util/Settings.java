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
	public String cursorImagePath;
	public String crosshairImagePath; //, difficultyImagePath;

	/**
	 * Instanz der Klasse erstellt Standard-Werte und lädt Einstellungen
	 */
	public Settings() {
		// Initailisiert Standard-Werte 
		STANDARD_NAMES = new String[] {"DISPLAY_WIDTH","DISPLAY_HEIGHT","NORMAL_SCORE","FAST_SCORE","TUNNEL_SCORE","CROSSHAIR_FRAME","IS_FULLSCREEN","PIXELSIZE","CURSOR","CAVE_EFFECT","ACID_EFFECT"};
		STANDARD_VALUES = new String[] {""+960,""+540,""+0,""+0,""+0,""+0,""+false,""+3,""+0,""+false,""+false};

		// Erstellt neue Instanz der Einstellungen--> Einstellungen werden geladen
		config = new SettingsLoader(STANDARD_NAMES,STANDARD_VALUES);
		
		// Variablen werden aus Einstellungen geladen 
		displayWidth = config.getInt("DISPLAY_WIDTH");
		displayHeight = config.getInt("DISPLAY_HEIGHT");
		normalScore = config.getInt("NORMAL_SCORE");
		fastScore = config.getInt("FAST_SCORE");
		tunnelScore = config.getInt("TUNNEL_SCORE");
		crosshairFrame = config.getInt("CROSSHAIR_FRAME");
		isFullscreen = config.getBoolean("IS_FULLSCREEN");
		pixelSize = config.getInt("PIXELSIZE");
		cursorFrame = config.getInt("CURSOR");
		isCaveEffectEnabled = config.getBoolean("CAVE_EFFECT");
		isAcidEffectEnabled = config.getBoolean("ACID_EFFECT");
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
		config.setValue("NORMAL_SCORE",""+normalScore);
		config.setValue("FAST_SCORE",""+fastScore);
		config.setValue("TUNNEL_SCORE",""+tunnelScore);
		config.setValue("DISPLAY_WIDTH",""+displayWidth);
		config.setValue("DISPLAY_HEIGHT",""+displayHeight);
		config.setValue("IS_FULLSCREEN",""+isFullscreen);
		config.setValue("PIXELSIZE",""+pixelSize);
		config.setValue("CURSOR",""+cursorFrame);
		config.setValue("CAVE_EFFECT",""+isCaveEffectEnabled);
		config.setValue("ACID_EFFECT",""+isAcidEffectEnabled);
		// speichert die gesetzten Werte
		config.saveToFile();
	}

}
