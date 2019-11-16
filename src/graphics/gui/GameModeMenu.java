package graphics.gui;

import gamelogic.World;
import graphics.gui.engine.ContainerComponent;
import graphics.gui.engine.components.BoxComponent;
import graphics.gui.engine.components.TextButtonComponent;
import graphics.gui.engine.fonts.Font;

/**
 * Die Klasse für das Menü, von dem aus beim Spielstart der Spielmodus ausgewählt werden kann
 * 
 * @author Ben
 */
public class GameModeMenu extends BoxComponent {
	
	private TextButtonComponent normalButton;
	private TextButtonComponent fastButton;
	private TextButtonComponent tunnelButton;
	
	private boolean isStartRequested;
	private int selectedMode;
	
	/**
	 * Erstellt ein neues GameModeMenu
	 * 
	 * @param font die Schriftart für die Buttons
	 */
	public GameModeMenu(Font font) {
		super(0,0,0x80000000,0,0);
		super.setPosition(POSITION_FULL);
		ContainerComponent container = new ContainerComponent(0,0);
		container.setWidthMode(WIDTH_AUTO);
		container.setHeightMode(HEIGHT_AUTO);
		container.setPosition(POSITION_CENTER);
		super.addComponent(container);
		
		normalButton = new TextButtonComponent(200, 50, "Normal Mode", font);
		normalButton.setOffset(4,4);
		container.addComponent(normalButton);
		fastButton = new TextButtonComponent(200, 50, "Fast Mode", font);
		fastButton.setOffset(4,4);
		container.addComponent(fastButton);
		tunnelButton = new TextButtonComponent(200, 50, "Tunnel Mode", font);
		tunnelButton.setOffset(4,4);
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
	
}
