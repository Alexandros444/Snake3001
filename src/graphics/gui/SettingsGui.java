package graphics.gui;

import graphics.gui.engine.components.BoxComponent;
import graphics.gui.engine.components.TextButtonComponent;
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
	private TextButtonComponent closeButton, saveButton, crosshairButton, difficultyButton, pixelSizeButton;
	private ImageComponent crosshairImage, difficultyImage;
	private TextComponent crosshairText, difficultyText, pixelSizeText;
	private Settings settings;
	private TextComponent headlineText;
	
	
	public boolean hasSettingsChanged;
	public int crosshairFrame, difficultyFrame, pixelSize, maxPixelSize = 10, maxDifficulty = 3;
	/**
	 * Konstruktor
	 * 
	 * @param font Schriftart
	 * @param settings Einstellungn
	 */
	public SettingsGui(Font font, Settings settings) {
		// SuperKonstruktor
		super(600,500,0xf0000000,0xbf808080,4);
		super.setPosition(POSITION_CENTER);
		super.setWidthMode(WIDTH_STATIC);
		super.setHeightMode(HEIGHT_STATIC);
		super.setInnerOffset(160,240);
		
		this.settings = settings;
		crosshairFrame = settings.crosshairFrame;
		difficultyFrame = settings.difficulty;
		pixelSize = settings.pixelSize;

		// Komponenten
		headlineText= new TextComponent("Settings",font);
		headlineText.setScale(2);
		headlineText.setPosition(POSITION_CORNER_TOPLEFT);
		headlineText.setOffset(250,20);
		super.addComponent(headlineText);
		
		closeButton = new TextButtonComponent(125,40,"Close",font);
		closeButton.setPosition(POSITION_CORNER_BOTTOMRIGHT);
		closeButton.setOffset(12,10);
		super.addComponent(closeButton);
		
		saveButton = new TextButtonComponent(125,40,"Save",font);
		saveButton.setPosition(POSITION_CORNER_BOTTOMLEFT);
		saveButton.setOffset(12,10);
		super.addComponent(saveButton); 
		
		crosshairImage = new ImageComponent(settings.crosshairImagePath);
		crosshairImage.setPosition(POSITION_CORNER_TOPLEFT);
		crosshairImage.setOffset(26,32);
		super.addComponent(crosshairImage);
		
		crosshairButton = new TextButtonComponent(60,60,"",font);
		crosshairButton.setPosition(POSITION_CORNER_TOPLEFT);
		crosshairButton.setOffset(12,18);
		super.addComponent(crosshairButton);
		
		crosshairText = new TextComponent("Crosshair",font);
		crosshairText.setPosition(POSITION_CORNER_TOPLEFT);
		crosshairText.setOffset(84,39);
		crosshairText.setScale(2);
		super.addComponent(crosshairText);
		
		difficultyImage = new ImageComponent("res/difficulty"+difficultyFrame+".png");
		difficultyImage.setPosition(POSITION_CORNER_TOPLEFT);
		difficultyImage.setOffset(26,132);
		super.addComponent(difficultyImage);
		
		difficultyButton = new TextButtonComponent(60,60,"",font);
		difficultyButton.setPosition(POSITION_CORNER_TOPLEFT);
		difficultyButton.setOffset(12,118);
		super.addComponent(difficultyButton);
		
		difficultyText = new TextComponent("Difficulty",font);
		difficultyText.setPosition(POSITION_CORNER_TOPLEFT);
		difficultyText.setOffset(84,139);
		difficultyText.setScale(2);
		super.addComponent(difficultyText);
		
		//Intelligentes Bild hier einfügen
		
		pixelSizeButton = new TextButtonComponent(60,60,""+pixelSize,font);
		pixelSizeButton.setPosition(POSITION_CORNER_TOPLEFT);
		pixelSizeButton.setOffset(12,218);
		super.addComponent(pixelSizeButton);
		
		pixelSizeText = new TextComponent("Pixel Size",font);
		pixelSizeText.setPosition(POSITION_CORNER_TOPLEFT);
		pixelSizeText.setOffset(84,239);
		pixelSizeText.setScale(2);
		super.addComponent(pixelSizeText);
		
	}
	
	/**
	 * Methode zum Updaten des Settings-Menüs
	 */
	public void update() {
		if(crosshairButton.wasClicked()) {
			// Fadenkreuz wechseln
			crosshairFrame++;
			crosshairFrame %= settings.crosshairCount;
			crosshairImage.loadImage("res/crosshairs"+crosshairFrame+".png");
			saveButton.setBackgroundColor(0xFF222255);
		}
		
		if(difficultyButton.wasClicked()) {
			// Fadenkreuz wechseln
			difficultyFrame++;
			difficultyFrame %= maxDifficulty;
			difficultyImage.loadImage("res/difficulty"+difficultyFrame+".png");
			saveButton.setBackgroundColor(0xFF222255);
		}
		
		if(pixelSizeButton.wasClicked()) {
			pixelSize %= maxPixelSize;
			pixelSize++;
			pixelSizeButton.setText(""+pixelSize);
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
		settings.crosshairImagePathRenew();
		settings.difficulty = difficultyFrame;
		settings.pixelSize = pixelSize;
		settings.save();
	}
	
	
	/** 
	 * @return isCloseRequested Ob die Einstellungen geschlossen werden sollen
	 */
	public boolean isCloseRequested() {
		return closeButton.wasClicked();
	}
	
}
