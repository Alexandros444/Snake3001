package graphics.gui;

import graphics.gui.engine.components.BoxComponent;
import graphics.gui.engine.components.ImageComponent;
import graphics.gui.engine.components.TextButtonComponent;
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
	private TextButtonComponent closeButton, saveButton, crosshairButton, fullscreenButton, pixelSizeButton, cursorButton, caveButton;
	private ImageComponent crosshairImage, fullscreenImage, cursorImage;
	private TextComponent crosshairText, fullscreenText, pixelSizeText, cursorText, caveText;
	private Settings settings;
	private TextComponent headlineText;
	

	public boolean hasSettingsChanged;
	public int crosshairFrame, pixelSize, maxPixelSize = 10, cursorFrame;
	public boolean isFullscreen, isCaveEffect;
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
		pixelSize = settings.pixelSize;
		isFullscreen = settings.isFullscreen;
		cursorFrame = settings.cursorFrame;
		isCaveEffect = settings.isCaveEffect;
		
		// Komponenten
		headlineText= new TextComponent("Settings",font);
		headlineText.setScale(3);
		headlineText.setPosition(POSITION_CORNER_TOPLEFT);
		headlineText.setOffset(220,-30);
		super.addComponent(headlineText);
		
		
		closeButton = new TextButtonComponent(125,40,"Close",font);
		closeButton.setPosition(POSITION_CORNER_BOTTOMRIGHT);
		closeButton.setOffset(12,10);
		super.addComponent(closeButton);
		
		
		saveButton = new TextButtonComponent(125,40,"Apply",font);
		saveButton.setPosition(POSITION_CORNER_BOTTOMLEFT);
		saveButton.setOffset(12,10);
		super.addComponent(saveButton);
		
		//Crosshairs
		crosshairButton = new TextButtonComponent(60,60,"",font);
		crosshairButton.setPosition(POSITION_CORNER_TOPLEFT);
		crosshairButton.setOffset(12,18);
		
		crosshairImage = new ImageComponent(settings.crosshairImagePath);
		crosshairImage.setPosition(POSITION_CORNER_TOPLEFT);
		crosshairImage.setSize(40,40);
		crosshairImage.setOffset(10,10);

		crosshairButton.addComponent(crosshairImage);
		super.addComponent(crosshairButton);
		
		crosshairText = new TextComponent("Crosshair",font);
		crosshairText.setPosition(POSITION_CORNER_TOPLEFT);
		crosshairText.setOffset(84,39);
		crosshairText.setScale(2);
		super.addComponent(crosshairText);

		//Fullsceen
		fullscreenButton = new TextButtonComponent(60,60,"",font);
		fullscreenButton.setPosition(POSITION_CORNER_TOPLEFT);
		fullscreenButton.setOffset(12,118);

		fullscreenImage = new ImageComponent("res/fullscreen"+isFullscreen+".png");
		fullscreenImage.setPosition(POSITION_CORNER_TOPLEFT);
		fullscreenImage.setSize(40,40);
		fullscreenImage.setOffset(10,10);
		
		fullscreenButton.addComponent(fullscreenImage);
		super.addComponent(fullscreenButton);
		
		fullscreenText = new TextComponent("Fullscreen",font);
		fullscreenText.setPosition(POSITION_CORNER_TOPLEFT);
		fullscreenText.setOffset(84,139);
		fullscreenText.setScale(2);
		super.addComponent(fullscreenText);
		
		//PixelSize
		pixelSizeButton = new TextButtonComponent(60,60,""+pixelSize,font);
		pixelSizeButton.setPosition(POSITION_CORNER_TOPLEFT);
		pixelSizeButton.setOffset(12,218);
		super.addComponent(pixelSizeButton);
		
		pixelSizeText = new TextComponent("Pixel Size",font);
		pixelSizeText.setPosition(POSITION_CORNER_TOPLEFT);
		pixelSizeText.setOffset(84,239);
		pixelSizeText.setScale(2);
		super.addComponent(pixelSizeText);
		
		//CURSOR
		cursorButton = new TextButtonComponent(60,60,"",font);
		cursorButton.setPosition(POSITION_CORNER_TOPLEFT);
		cursorButton.setOffset(12,318);

		cursorImage = new ImageComponent(settings.cursorImagePath);
		cursorImage.setPosition(POSITION_CORNER_TOPLEFT);
		cursorImage.setSize(40,40);
		cursorImage.setOffset(10,10);
		
		cursorButton.addComponent(cursorImage);
		super.addComponent(cursorButton);
		
		cursorText = new TextComponent("Cursor",font);
		cursorText.setPosition(POSITION_CORNER_TOPLEFT);
		cursorText.setOffset(84,339);
		cursorText.setScale(2);
		super.addComponent(cursorText);
		
		//CAVE EFFECT
		caveButton = new TextButtonComponent(70,60,""+isCaveEffect,font);
		caveButton.setPosition(POSITION_CORNER_TOPLEFT);
		caveButton.setOffset(312,18);
		super.addComponent(caveButton);
		
		caveText = new TextComponent("Cave_Effect",font);
		caveText.setPosition(POSITION_CORNER_TOPLEFT);
		caveText.setOffset(384,39);
		caveText.setScale(2);
		super.addComponent(caveText);
		
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
		
		if(fullscreenButton.wasClicked()) {
			// Fadenkreuz wechseln
			if(isFullscreen) {
				isFullscreen = false;
			}else {
				isFullscreen = true;
			}
			fullscreenImage.loadImage("res/fullscreen"+isFullscreen+".png");
			saveButton.setBackgroundColor(0xFF222255);
		}
		
		if(pixelSizeButton.wasClicked()) {
			pixelSize %= maxPixelSize;
			pixelSize++;
			pixelSizeButton.setText(""+pixelSize);
			saveButton.setBackgroundColor(0xFF222255);
		}
		if(cursorButton.wasClicked()) {
			cursorFrame++;
			cursorFrame%=2;
			cursorImage.loadImage("res/cursor"+cursorFrame+".png");
			saveButton.setBackgroundColor(0xFF222255);
		}
		if(caveButton.wasClicked()) {
			if(isCaveEffect) {
				isCaveEffect = false;
			}else {
				isCaveEffect = true;
			}
			caveButton.setText(""+isCaveEffect);
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
		settings.pixelSize = pixelSize;
		settings.isFullscreen = isFullscreen;
		settings.cursorFrame = cursorFrame;
		settings.curserImagePathRenew();
		settings.isCaveEffect = isCaveEffect;
		settings.save();
	}
	
	
	/** 
	 * @return isCloseRequested Ob die Einstellungen geschlossen werden sollen
	 */
	public boolean isCloseRequested() {
		return closeButton.wasClicked();
	}
	
}
