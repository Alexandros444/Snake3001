package graphics.gui;

import gamelogic.World;
import graphics.gui.engine.ContainerComponent;
import graphics.gui.engine.components.BoxComponent;
import graphics.gui.engine.components.ButtonComponent;
import graphics.gui.engine.components.ImageComponent;
import graphics.gui.engine.components.TextComponent;
import graphics.gui.engine.fonts.Font;
import util.Settings;

/**
 * Die Klasse für das Menü, von dem aus beim Spielstart der Spielmodus ausgewählt werden kann
 * 
 * @author Ben
 */
public class GameModeMenu extends BoxComponent {
	
	private ButtonComponent normalButton;
	private ButtonComponent fastButton;
	private ButtonComponent tunnelButton;
	
	private boolean isStartRequested;
	private int selectedMode;
	
	/**
	 * Erstellt ein neues GameModeMenu
	 * 
	 * @param font die Schriftart für die Buttons
	 */
	public GameModeMenu(Font font, Settings settings) {
		super(0,0,0x80000000,0,0);
		super.setPosition(POSITION_FULL);
		ContainerComponent container = new ContainerComponent(0,0);
		container.setWidthMode(WIDTH_AUTO);
		container.setHeightMode(HEIGHT_AUTO);
		container.setPosition(POSITION_CENTER);
		container.setFlowDirection(FLOW_LEFT_TO_RIGHT);
		super.addComponent(container);
		
		normalButton = createButton("Normal Mode",font,"res/mode_normal.png",settings.normalScore);
		fastButton = createButton("Fast Mode",font,"res/mode_fast.png",settings.fastScore);
		tunnelButton = createButton("Tunnel Mode",font,"res/mode_tunnel.png",settings.tunnelScore);
		container.addComponent(normalButton);
		container.addComponent(fastButton);
		container.addComponent(tunnelButton);
	}
	
	/**
	 * Updated das Menü
	 */
	public void update() {
		if (normalButton.wasClicked()) {
			isStartRequested = true;
			selectedMode = World.MODE_NORMAL;
		}
		if (fastButton.wasClicked()) {
			isStartRequested = true;
			selectedMode = World.MODE_FAST;
		}
		if (tunnelButton.wasClicked()) {
			isStartRequested = true;
			selectedMode = World.MODE_TUNNEL;
		}
	}
	
	/**
	 * Gibt zurück, ob ein Spielmodus ausgewählt wurde
	 * @return ob ein Spielmodus ausgewählt wurde
	 */
	public boolean isStartRequested() {
		return isStartRequested;
	}
	
	/**
	 * Gibt den ausgewählten Spielmodus zurück, oder den Default-Spielmodus falls noch keiner ausgewählt ist
	 * @return eine der Konstanten <code>World.MODE_NORMAL</code>, <code>World.MODE_FAST</code> und <code>World.MODE_TUNNEL</code>
	 */
	public int getSelectedMode() {
		return selectedMode;
	}
	
	/**
	 * Erstellt einen neuen GameMode-Button
	 * 
	 * @param text Beschriftung des Buttons
	 * @param font Schriftart
	 * @param imagePath Dateipfad des Hintergrundbilds relativ zu <code>res/</code>, also z.B. <code>res/icon.png</code>
	 * @return den neuen Button
	 */
	private static ButtonComponent createButton(String text, Font font, String imagePath, int score) {
		ButtonComponent button = new ButtonComponent(168,168,0xff000000,0x4040bfff,0x40ffffff,4);
		button.setOffset(16,16);
		ImageComponent image = new ImageComponent(imagePath);
		image.setPosition(POSITION_FULL);
		image.setOffset(4,4);
		image.setTransparency(0.5f);
		button.addComponent(image);
		TextComponent textComponent = new TextComponent(text,font);
		textComponent.setPosition(POSITION_CENTER);
		textComponent.setScale(2);
		button.addComponent(textComponent);
		TextComponent scoreText = new TextComponent("Score: "+score,font);
		scoreText.setPosition(POSITION_CENTER_TOP);
		scoreText.setScale(2);
		scoreText.setOffset(0,-30);
		scoreText.setTransparency(0.5f);
		button.addComponent(scoreText);
		return button;
	}
	
}
