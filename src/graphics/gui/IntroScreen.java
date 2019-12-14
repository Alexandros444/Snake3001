package graphics.gui;

import graphics.gui.engine.ContainerComponent;
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
		
		ContainerComponent textContainer = new ContainerComponent(0,0);
		textContainer.setWidthMode(WIDTH_AUTO);
		textContainer.setHeightMode(HEIGHT_AUTO);
		textContainer.setPosition(POSITION_CORNER_BOTTOMLEFT);
		textContainer.setOffset(6,6);
		super.addComponent(textContainer);
		
		font = new MonospaceFont("res/font/ascii_dark.png");
		TextComponent text = new TextComponent("Made by undefined blockpasta",font);
		text.setOffset(6,6);
		text.setScale(2);
		text.setTransparency(0.5f);
		textContainer.addComponent(text);
		
		TextComponent text2 = new TextComponent("See Github.com/Alexandros444/Snake3001",font);
		text2.setOffset(6,6);
		text2.setScale(2);
		text2.setTransparency(0.5f);
		textContainer.addComponent(text2);
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
