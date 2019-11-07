package graphics.gui;

import graphics.gui.engine.ContainerComponent;
import graphics.gui.engine.components.BoxComponent;
import graphics.gui.engine.components.ButtonComponent;
import graphics.gui.engine.fonts.Font;

public class StartMenu extends BoxComponent {
	

	private ButtonComponent startButton;
	private ButtonComponent settingsButton;
	private ButtonComponent quitButton;
	
	private boolean isStartRequested;
	private boolean isCloseRequested;
	
	private Font font;

	
	public StartMenu(Font font) { 
		super(0,0,0x80000000,0,0);
		super.setPosition(POSITION_FULL);
		ContainerComponent container = new ContainerComponent(0,0);
		container.setWidthMode(WIDTH_AUTO);
		container.setHeightMode(HEIGHT_AUTO);
		container.setPosition(POSITION_CENTER);
		super.addComponent(container);
		
		this.font = font;

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
	
	
	public void update() {
		if(startButton.wasClicked()) {
			isStartRequested = true;
		}
		if(quitButton.wasClicked()) {
			isCloseRequested = true;
		}
	}
	
	
	public boolean isStartRequested() {
		return isStartRequested;
	}
	public boolean isCloseRequested() {
		return isCloseRequested;
	}

} 

