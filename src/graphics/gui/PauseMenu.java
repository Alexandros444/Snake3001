package graphics.gui;

import graphics.gui.engine.ContainerComponent;
import graphics.gui.engine.components.BoxComponent;
import graphics.gui.engine.components.ButtonComponent;
import graphics.gui.engine.fonts.Font;
import util.Settings;

public class PauseMenu extends BoxComponent {
	
	private ButtonComponent continueButton;
	private ButtonComponent settingsButton;
	private ButtonComponent exitButton;
	private boolean isContinueRequested, isExitRequested;
	
	private Font font;
	private Settings settings;
	public SettingsGui settingsGui;
	private boolean areSettingsOpen;
	public boolean hasSettingsChanged;
	
	/**
	 * Konstruktor	
	 * 
	 * @param font Schriftart
	 * @param settings jetzige Einstellungen
	 */
	public PauseMenu(Font font, Settings settings) {
		super(0,0,0x80000000,0,0);
		super.setPosition(POSITION_FULL);
		ContainerComponent container = new ContainerComponent(0,0);
		container.setWidthMode(WIDTH_AUTO);
		container.setHeightMode(HEIGHT_AUTO);
		container.setPosition(POSITION_CENTER);
		super.addComponent(container);
		
		this.font = font;
		this.settings = settings;
		settingsGui = null;
		areSettingsOpen = false;
		
		continueButton = new ButtonComponent(200, 50, "Continue", font);
		continueButton.setOffset(4,4);
		container.addComponent(continueButton);
		settingsButton = new ButtonComponent(200, 50, "Settings", font);
		settingsButton.setOffset(4,4);
		container.addComponent(settingsButton);
		exitButton = new ButtonComponent(200, 50, "Exit", font);
		exitButton.setOffset(4,4);
		container.addComponent(exitButton);
	}
	
	
	/**
	 * Methode zum Updaten des Pause-Menüs
	 */
	public void update() {
		if(continueButton.wasClicked()) {
			// Spiel Wiederaufnehmen
			isContinueRequested = true;
		}
		if(exitButton.wasClicked()) {
			// Zurück zum Hauptmenü
			isExitRequested = true;
		}
		// SETTINGS-MENÜ
		if(areSettingsOpen) {
			// Einstellungen SIND offen
			settingsGui.update();
			if(settingsGui.hasSettingsChanged) {
				// Einstellungen wurden geändert
				hasSettingsChanged = true;
				settingsGui.hasSettingsChanged = false;
			}else {
				hasSettingsChanged = false;
			}
			if(settingsGui.isCloseRequested()) {
				// Einstellungen schließen
				areSettingsOpen = false;
				super.removeComponent(settingsGui);
				settingsGui.destroy();
				settingsGui = null;
			}
		}else if (settingsButton.wasClicked()) {
			// Einstllungen öffnen
			areSettingsOpen = true;
			settingsGui = new SettingsGui(font,settings);
			super.addComponent(settingsGui);
		} 		
	}
	
	/**
	 * Ob das spiel wiederaufgenommen werden soll
	 * 
	 * @return isContinueRequest
	 */
	public boolean isContinueRequested() {
		return isContinueRequested;
	}

	/**
	 * Ob das Menü geöffnet werden soll
	 * 
	 * @return isExitRequested
	 */
	public boolean isExitRequested() {
		return isExitRequested;
	}
	
}
