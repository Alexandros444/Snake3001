package graphics.gui;

import graphics.gui.engine.ContainerComponent;
import graphics.gui.engine.components.BoxComponent;
import graphics.gui.engine.components.TextButtonComponent;
import graphics.gui.engine.components.TextComponent;
import graphics.gui.engine.fonts.Font;
import util.Settings;

/**
 * Menu das sich beim Tod der Schlange öffnet
 * 
 * @author Alex
 */

public class DeathMenu extends BoxComponent {
	
	private TextButtonComponent exitButton;
	private TextButtonComponent retryButton;
	private TextComponent scoreText,highscoreText;	
	
	private boolean isCloseRequested;
	private boolean isRestartRequested;
	
	public DeathMenu(Font font, int score, int gameMode, Settings settings) { 
		super(0,0,0x80000000,0,0);
		super.setPosition(POSITION_FULL);
		ContainerComponent container = new ContainerComponent(0,0);
		container.setWidthMode(WIDTH_AUTO);
		container.setHeightMode(HEIGHT_AUTO);
		container.setPosition(POSITION_CENTER);
		super.addComponent(container);
		
		scoreText = new TextComponent("Score: "+score,font);
		scoreText.setPosition(POSITION_CENTER_FLOW);
		scoreText.setOffset(0,15);
		scoreText.setScale(4);
		container.addComponent(scoreText);		
		
		int gameModeScore = settings.normalScore;	
		if(gameMode==1) {
			gameModeScore = settings.fastScore;
		}
		if(gameMode==2){
			gameModeScore = settings.tunnelScore;
		}
		
		highscoreText = new TextComponent("Highscore: "+gameModeScore,font);
		highscoreText.setPosition(POSITION_CENTER_FLOW);
		highscoreText.setOffset(0,40);
		highscoreText.setScale(3);
		container.addComponent(highscoreText);		
		
		//Highscore erreicht
		if(gameModeScore<score) {
			scoreText.setText("New Highscore:");
			scoreText.setOffset(20,20);
			scoreText.setScale(4);
			highscoreText.setText(""+score);
			highscoreText.setOffset(20,50);
			highscoreText.setScale(5);
		}
		
		retryButton = new TextButtonComponent(200, 50, "Retry", font);
		retryButton.setPosition(POSITION_CENTER_FLOW);
		retryButton.setOffset(4,4);
		container.addComponent(retryButton);
		
		exitButton = new TextButtonComponent(200, 50, "Exit", font);
		exitButton.setPosition(POSITION_CENTER_FLOW);
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
		if(retryButton.wasClicked()) {
			// Programm neustarten
			isRestartRequested = true;
		}
	}
	
	public boolean isExitRequested() {
		return isCloseRequested;
	}
	public boolean isRestartRequested() {
		return isRestartRequested; 
	}

} 