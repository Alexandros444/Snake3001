package graphics.gui;

import gamelogic.World;
import graphics.gui.engine.ContainerComponent;
import graphics.gui.engine.components.BoxComponent;
import graphics.gui.engine.components.ButtonComponent;
import graphics.gui.engine.components.TextComponent;
import graphics.gui.engine.fonts.Font;

/**
 * Menu das sich beim Tod der Schlange öffnet
 * 
 * @author Alex
 */

public class DeathMenu extends BoxComponent {
	
	private ButtonComponent quitButton;
	private TextComponent testText;	
	
	private boolean isCloseRequested;
	
	public DeathMenu(Font font, World world) { 
		super(0,0,0x80000000,0,0);
		super.setPosition(POSITION_FULL);
		ContainerComponent container = new ContainerComponent(0,0);
		container.setWidthMode(WIDTH_AUTO);
		container.setHeightMode(HEIGHT_AUTO);
		container.setPosition(POSITION_CENTER);
		super.addComponent(container);
		
		testText = new TextComponent("Score: "+world.score,font);
		testText.setScale(3);
		testText.setOffset(0,-40);
		testText.setPosition(POSITION_FULL);
		container.addComponent(testText);
		
		quitButton = new ButtonComponent(200, 50, "Quit", font);
		quitButton.setOffset(4,4);
		container.addComponent(quitButton);
	}
	
	/**
	 * Methode zum Updaten des Start-Menüs
	 */
	public void update() {
		if(quitButton.wasClicked()) {
			// Programm beenden
			isCloseRequested = true;
		}
	}
	
	
	public boolean isCloseRequested() {
		return isCloseRequested;
	}

} 