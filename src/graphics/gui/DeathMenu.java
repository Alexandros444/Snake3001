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
		scoreText.setScale(4);
		scoreText.setOffset(0,-100);
		scoreText.setPosition(POSITION_FULL);
		container.addComponent(scoreText);		
		
		String gameModeName = "Normal";
		int gameModeScore = settings.normalScore;	
		if(gameMode==1) {
			gameModeName = "Fast";
			gameModeScore = settings.fastScore;
		}
		if(gameMode==2){
			gameModeName = "Tunnel";
			gameModeScore = settings.tunnelScore;
		}
		
		highscoreText = new TextComponent(gameModeName+" Mode Score: "+gameModeScore,font);
		highscoreText.setScale(2);
		highscoreText.setOffset(0,-40);
		highscoreText.setPosition(POSITION_FULL);
		container.addComponent(highscoreText);		
		
		//Highscore erreicht
		if(gameModeScore<score) {
			scoreText.setText("New Highscore in "+gameModeName+" Mode");
			scoreText.setOffset(-250,-180);
			scoreText.setScale(5);
			highscoreText.setText(""+score);
			highscoreText.setOffset(75,-80);
			highscoreText.setScale(6);
		}
		
		retryButton = new TextButtonComponent(200, 50, "Retry", font);
		retryButton.setOffset(4,4);
		container.addComponent(retryButton);
		
		exitButton = new TextButtonComponent(200, 50, "Exit", font);
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