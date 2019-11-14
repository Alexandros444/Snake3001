package graphics.gui;

import graphics.gui.engine.components.BoxComponent;
import graphics.gui.engine.components.ImageComponent;
import util.Timer;

/**
 * Klasse für den Introscreen bei Start des Programms
 * 
 * @author Max
 */
public class IntroScreen extends BoxComponent{

	private Timer timer;
	private ImageComponent image;
	
	public IntroScreen() {
		super(0,0,0xffddeeee,0,0);
		super.setPosition(POSITION_FULL);
		
		image = new ImageComponent("res/icon.png");
		image.setPosition(POSITION_CENTER);
		super.addComponent(image);
		timer = new Timer();
	}
	
	public void update() {
		if (timer.getTime()>2) {
			super.setTransparency(3-timer.getTime());
		}
		super.update();
	}
	
	public boolean isFinished() {
		return timer.getTime()>3;
	}
	
}
