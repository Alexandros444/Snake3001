package graphics.gui;

import graphics.gui.engine.components.BoxComponent;
import graphics.gui.engine.components.ImageComponent;
import graphics.gui.engine.components.TextComponent;
import graphics.gui.engine.fonts.Font;
import graphics.gui.engine.fonts.MonospaceFont;
import util.Timer;

/**
 * Klasse für den Introscreen bei Start des Programms
 * 
 * @author Max
 */
public class IntroScreen extends BoxComponent{

	private Timer timer;
	private ImageComponent image;
	
	private Font font;
	
	public IntroScreen() {
		super(0,0,0xffddeeee,0,0);
		super.setPosition(POSITION_FULL);
		
		image = new ImageComponent("res/icon.png");
		image.setPosition(POSITION_CENTER);
		super.addComponent(image);
		timer = new Timer();
		
		font = new MonospaceFont("res/font/ascii_dark.png");
		TextComponent text = new TextComponent("Github.com/Alexandros444/Snake3001",font);
		text.setPosition(POSITION_CORNER_BOTTOMLEFT);
		text.setOffset(6,6);
		text.setScale(2);
		text.setTransparency(0.5f);
		super.addComponent(text);
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
	
	public void destroy() {
		font.destroy();
		super.destroy();
	}
	
}
