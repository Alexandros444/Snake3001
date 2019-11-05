package graphics.gui;

import graphics.gui.engine.components.BoxComponent;
import graphics.gui.engine.components.ButtonComponent;
import graphics.gui.engine.components.TextComponent;
import graphics.gui.engine.fonts.Font;
import util.Settings;

/**
 * Bisher nur ein Platzhalter. Hier kommen später die Einstellungen rein.
 * <br><br>
 * @Alex wenn du das dann gemacht hast, denk dran auch die JavaDoc-Kommentare dazu zu ergänzen und den hier zu ersetzen
 */
public class SettingsGui extends BoxComponent {
	
	private ButtonComponent closeButton;
	
	public SettingsGui(Font font, Settings settings) {
		super(0,0,0xa0000000,0xbf808080,4);
		super.setPosition(POSITION_CENTER);
		super.setWidthMode(WIDTH_AUTO);
		super.setHeightMode(HEIGHT_AUTO);
		super.setInnerOffset(32,16);
		TextComponent testText = new TextComponent("Settings kommen dann hier rein",font);
		testText.setScale(2);
		testText.setOffset(0,8);
		super.addComponent(testText);
		closeButton = new ButtonComponent(150,50,"Close",font);
		super.addComponent(closeButton);
	}
	
	public boolean isCloseRequested() {
		return closeButton.wasClicked();
	}
	
}
