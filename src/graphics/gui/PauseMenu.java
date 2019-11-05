package graphics.gui;

import graphics.gui.engine.ContainerComponent;
import graphics.gui.engine.components.BoxComponent;
import graphics.gui.engine.components.ButtonComponent;
import graphics.gui.engine.fonts.Font;

public class PauseMenu extends BoxComponent {
	
	private ButtonComponent continueButton;
	private ButtonComponent settingsButton;
	private ButtonComponent exitButton;
	private boolean isCloseRequested;
	
	public PauseMenu(Font font) {
		super(0,0,0x80000000,0,0);
		ContainerComponent container = new ContainerComponent(0,0);
		container.setWidthMode(WIDTH_AUTO);
		container.setHeightMode(HEIGHT_AUTO);
		container.setPosition(POSITION_CENTER);
		super.addComponent(container);
		
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
	
	public void update() {
		if(continueButton.wasClicked()) {
			isCloseRequested = true;
		}
	}
	
	public boolean isCloseRequested() {
		return isCloseRequested;
	}
	
	
}
