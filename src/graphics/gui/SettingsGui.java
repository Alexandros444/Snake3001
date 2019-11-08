package graphics.gui;

import graphics.gui.engine.components.BoxComponent;
import graphics.gui.engine.components.ButtonComponent;
import graphics.gui.engine.components.ImageComponent;
import graphics.gui.engine.components.TextComponent;
import graphics.gui.engine.fonts.Font;
import util.Settings;

/**
 * Einstellungs Menü
 * <br><br>
 * @author Alex
 */
public class SettingsGui extends BoxComponent {
	// Variablen
	private ButtonComponent closeButton, saveButton, crosshairButton;
	private ImageComponent crosshairImage;
	private TextComponent crosshairText;
	private Settings settings;
	
	public boolean hasSettingsChanged;
	public int crosshairFrame, crosshairCount;
	/**
	 * Konstruktor
	 * 
	 * @param font Schriftart
	 * @param settings Einstellungn
	 */
	public SettingsGui(Font font, Settings settings) {
		// SuperKonstruktor
		super(0,0,0xf0000000,0xbf808080,4);
		super.setPosition(POSITION_CENTER);
		super.setWidthMode(WIDTH_AUTO);
		super.setHeightMode(HEIGHT_AUTO);
		super.setInnerOffset(160,240);
		
		this.settings = settings;
		crosshairFrame = settings.crosshairFrame;
		crosshairCount = settings.crosshairCount;
		
		TextComponent testText = new TextComponent("Settings kommen dann hier rein",font);
		testText.setScale(2);
		testText.setOffset(0,8);
		super.addComponent(testText);
		
		// Komponenten
		closeButton = new ButtonComponent(125,40,"Close",font);
		closeButton.setPosition(POSITION_CORNER_BOTTOMRIGHT);
		closeButton.setOffset(12,10);
		super.addComponent(closeButton);
		
		saveButton = new ButtonComponent(125,40,"Save",font);
		saveButton.setPosition(POSITION_CORNER_BOTTOMLEFT);
		saveButton.setOffset(12,10);
		super.addComponent(saveButton); 
		
		crosshairImage = new ImageComponent(settings.crosshairPath);
		crosshairImage.setPosition(POSITION_CORNER_TOPLEFT);
		crosshairImage.setOffset(26,32);
		super.addComponent(crosshairImage);
		
		crosshairButton = new ButtonComponent(60,60,"",font);
		crosshairButton.setPosition(POSITION_CORNER_TOPLEFT);
		crosshairButton.setOffset(12,18);
		super.addComponent(crosshairButton);
		
		crosshairText = new TextComponent("Crosshair",font);
		crosshairText.setPosition(POSITION_CORNER_TOPLEFT);
		crosshairText.setOffset(84,39);
		crosshairText.setScale(2);
		super.addComponent(crosshairText);
	}
	
	/**
	 * Methode zum Updaten des Settings-Menüs
	 */
	public void update() {
		if(crosshairButton.wasClicked()) {
			// Fadenkreuz wechseln
			crosshairFrame++;
			crosshairFrame %= crosshairCount;
			crosshairImage.reloadImage("res/crosshairs"+crosshairFrame+".png");
			saveButton.setBackgroundColor(0xFF222255);
		}
		if(saveButton.wasClicked()) {
			// Einstellungen Speichern
			saveButton.setBackgroundColor(0x80000000);
			saveSettings();
		}
	}
	
	/**
	 * Speichert die Werte in Datei
	 */
	private void saveSettings() {
		hasSettingsChanged = true;
		settings.crosshairFrame = crosshairFrame;
		settings.crosshairPathRenew();
		settings.save();
	}
	
	
	/** 
	 * @return isCloseRequested Ob die Einstellungen geschlossen werden sollen
	 */
	public boolean isCloseRequested() {
		return closeButton.wasClicked();
	}
	
}
