package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Properties;

/**
 * Klasse zum Speichern und laden der Einstellungn
 * 
 * @author Alex
 */
public class SettingsLoader {

	// Variablen Deklaration
	private Properties config;
	private static final String FILE_PATH = "saves/config.cfg";
	private static final String DIR_PATH = "saves";
	InputStream is = null;
	OutputStream os = null;
	
	private boolean exitLoading;
	
	// Strings für die Standard-Namen und Standard-Werte
	private static String[] STANDARD_NAMES;
	private static String[] STANDARD_VALUES;

	/**
	 * Lädt die Einsellungen
	 */
	public SettingsLoader(String[] names, String[] values) {
		STANDARD_NAMES = names;
		STANDARD_VALUES = values;

		loadPreferences();
	}

	// lädt Einstellungen und synchronisiert sie
	private void loadPreferences() {
		if(exitLoading)return;
		// Instanz der Java-Klasse Properties
		config = new Properties();
		try {
			// erstellt Input-Stream aus Datei
			is = new FileInputStream(FILE_PATH);
			// Lädt Input-Stream in Config-Datei
			config.load(is);
			// checkt ob alle notwendigen StandardEinstellungen enthalten sind
			checkIntegrety();
		} catch (IOException io) {
			// Es ist noch keine Datei da oder es gab Problem beim laden, erstes wird hier direkt behoben
			System.out.println("no file found, creating new standard File");
			// neue Datei erstellen
			createPreferenceFile();
			// setzt die Standard-Werte
			setStandardPrefernces();
			// Speichert die Einsellungen
			saveToFile();
			// lädt die Einsellungen erneut(Break nach einem Durchgang um Dauer-Schleife zu vermeiden)
			exitLoading = true;
			loadPreferences();
		}
	}

	// Überprüft die Datei auf Richtigkeit
	private void checkIntegrety() {
		// überprüft ob alle notwendigen Werte vorhanden sind
		boolean isInteger = true;
		for (String name : STANDARD_NAMES) {
			if (!config.containsKey(name)) {
				isInteger = false;
			}
		}
		// wenn nicht werden die StandardWerte gesetzt
		if (!isInteger) {
			setStandardPrefernces();
		}
		// überprüft ob die Datei bearbeitet wurde bzw. ob mehr Einträge vorhanden sind
		// als gebraucht werden
		if (config.size()>STANDARD_NAMES.length) {
			String[] remove = new String[config.size()-STANDARD_NAMES.length];
			int i = 0;
			for (Object nameInConfigFile : config.keySet()) {
				boolean found = Arrays.asList(STANDARD_NAMES).contains(nameInConfigFile);
				if (!found) {
					remove[i] = nameInConfigFile.toString();
					i++;
				}
			}
			// Schreibt vor nicht gebrauchte oder falsch geschriebene Werte ein \\
			for (String name : remove) {
				if (!name.contains("\\")) {
					config.setProperty("\\"+name,getString(name));
					config.remove(name);
				}
			}
		}
	}

	// setzt die Werte auf ihren Standard-Wert
	private void setStandardPrefernces() {
		System.out.println("standard");
		int i = 0;
		for (String name : STANDARD_NAMES) {
			newValue(name,STANDARD_VALUES[i]);
			i++;
		}
	}

	// fügt einen neuen Eintrag zu den Einstellungen hinzu
	private void newValue(String name, String value) {
		config.setProperty(name,value);
	}

	/**
	 * Ändert den Wert eines vorhandenen Eintrages in den Einstellungen
	 * 
	 * @param name  Name des Eintrages
	 * @param value Wert des Eintrages
	 */
	public void setValue(String name, String value) {
		if (config.containsKey(name)) {
			config.setProperty(name,value);
		} else {
			System.err.print(name);
			System.out.println(" is undefined in config");
		}
	}

	/**
	 * Gibt den zum Eintrag passenden Wert aus
	 * 
	 * @param name Name des Eintrags
	 * @return int Wert des Eintrags
	 */
	public int getInt(String name) {
		int temp = 0;
		try {
			temp = Integer.parseInt(config.getProperty(name));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(name+" not found in config");
		}
		return temp;
	}

	/**
	 * Gibt den zum Eintrag passenden String aus
	 * 
	 * @param name Name des Eintrags
	 * @return String Wert des Eintrags
	 */
	public String getString(String name) {
		String temp = "";
		try {
			temp = config.getProperty(name);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(name+" not found in config");
		}
		return temp;
	}

	/**
	 * Gibt den zum Eintrag passenden Wert aus
	 * 
	 * @param name Name des Eintrags
	 * @return float Wert des Eintrags
	 */
	public float getFloat(String name) {
		float temp = 0;
		try {
			temp = Float.parseFloat(config.getProperty(name));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(name+" not found in config");
		}
		return temp;
	}

	/**
	 * Gibt den zum Eintrag passenden Wert aus
	 * 
	 * @param name Name des Eintrags
	 * @return boolean Wert des Eintrags
	 */
	public boolean getBoolean(String name) {
		boolean temp = false;
		try {
			temp = Boolean.parseBoolean(config.getProperty(name));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(name+" not found in config");
		}
		return temp;
	}

	/**
	 * speichert die Einstellungen, schreibt sie in eine Datei
	 */
	public void saveToFile() {
		try {
			os = new FileOutputStream(FILE_PATH);
			config.store(os,null);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Fehler beim Speichern der Spieldaten");
		}
	}

	// erstellt neue config.cfg Datei im angegebenen ordner(pfad)
	private void createPreferenceFile() {
		try {
			new File(DIR_PATH).mkdirs();
			File file = new File(FILE_PATH);
			file.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fehler beim Ersellen der Config-Datei");
		}
	}
}
