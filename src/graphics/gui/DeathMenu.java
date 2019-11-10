package graphics.gui;

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
	
	private ButtonComponent exitButton;
	private TextComponent testText;	
	
	private boolean isCloseRequested;
	
	public DeathMenu(Font font, int score) { 
		super(0,0,0x80000000,0,0);
		super.setPosition(POSITION_FULL);
		ContainerComponent container = new ContainerComponent(0,0);
		container.setWidthMode(WIDTH_AUTO);
		container.setHeightMode(HEIGHT_AUTO);
		container.setPosition(POSITION_CENTER);
		super.addComponent(container);
		
		testText = new TextComponent("Score: "+score,font);
		testText.setScale(3);
		testText.setOffset(0,-40);
		testText.setPosition(POSITION_FULL);
		container.addComponent(testText);
		
		exitButton = new ButtonComponent(200, 50, "Exit", font);
		exitButton.setOffset(4,4);
		container.addComponent(exitButton);
	}
	
	/**
	 * Methode zum Updaten des Start-Menüs
	 */
	public void update() {
		if(exitButton.wasClicked()) {
			// Programm beenden
			isCloseRequested = true;
		}
	}
	
	public boolean isExitRequested() {
		return isCloseRequested;
	}

} 