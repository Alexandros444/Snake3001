package graphics.gui;

import graphics.gui.engine.ContainerComponent;
import graphics.gui.engine.components.BoxComponent;
import graphics.gui.engine.components.ButtonComponent;
import graphics.gui.engine.fonts.Font;
import util.Settings;

public class StartMenu extends BoxComponent {
	

	private ButtonComponent startButton;
	private ButtonComponent settingsButton;
	private ButtonComponent quitButton;
	
	private SettingsGui settingsGui;
	
	private boolean isStartRequested;
	private boolean isCloseRequested;
	private boolean areSettingsOpen;
	
	private Font font;
	private Settings settings;
	public boolean hasSettingsChanged;

	
	public StartMenu(Font font, Settings settings) { 
		super(0,0,0x80000000,0,0);
		super.setPosition(POSITION_FULL);
		ContainerComponent container = new ContainerComponent(0,0);
		container.setWidthMode(WIDTH_AUTO);
		container.setHeightMode(HEIGHT_AUTO);
		container.setPosition(POSITION_CENTER);
		super.addComponent(container);
		
		this.font = font;
		this.settings = settings;

		startButton = new ButtonComponent(200, 50, "Start", font);
		startButton.setOffset(4,4);
		container.addComponent(startButton);
		settingsButton = new ButtonComponent(200, 50, "Settings", font);
		settingsButton.setOffset(4,4);
		container.addComponent(settingsButton);
		quitButton = new ButtonComponent(200, 50, "Quit", font);
		quitButton.setOffset(4,4);
		container.addComponent(quitButton);
	}
	
	/**
	 * Methode zum Updaten des Start-Menüs
	 */
	public void update() {
		if(startButton.wasClicked()) {
			// Spielstart
			isStartRequested = true;
		}
		if(quitButton.wasClicked()) {
			// Programm beenden
			isCloseRequested = true;
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
	
	
	public boolean isStartRequested() {
		return isStartRequested;
	}
	public boolean isCloseRequested() {
		return isCloseRequested;
	}

} 

