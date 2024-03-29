package graphics.gui;

import gamelogic.World;
import graphics.gui.engine.ContainerComponent;
import graphics.gui.engine.components.BoxComponent;
import graphics.gui.engine.components.TextButtonComponent;
import graphics.gui.engine.components.TextComponent;
import graphics.gui.engine.fonts.Font;
import util.Settings;

/**
 * Menu das sich beim Tod der Schlange �ffnet
 * 
 * @author Alex
 */

public class DeathMenu extends BoxComponent {
	
	private TextButtonComponent exitButton;
	private TextButtonComponent retryButton;
	
	private boolean isCloseRequested;
	private boolean isRestartRequested;
	
	public DeathMenu(Font font, World world, Settings settings) { 
		super(0,0,0x80000000,0,0);
		super.setPosition(POSITION_FULL);
		ContainerComponent container = new ContainerComponent(0,0);
		container.setWidthMode(WIDTH_AUTO);
		container.setHeightMode(HEIGHT_AUTO);
		container.setPosition(POSITION_CENTER);
		super.addComponent(container);
		
		if(!world.hasSecondSnake) {
			TextComponent scoreText = new TextComponent("Score: "+world.score,font);
			scoreText.setPosition(POSITION_CENTER_FLOW);
			scoreText.setOffset(0,15);
			scoreText.setScale(4);
			container.addComponent(scoreText);		
		
			int gameModeScore = settings.normalScore;	
			if(world.gameMode==1) {
				gameModeScore = settings.fastScore;
			}
			if(world.gameMode==2){
				gameModeScore = settings.tunnelScore;
			}
		
			TextComponent highscoreText = new TextComponent("Highscore: "+gameModeScore,font);
			highscoreText.setPosition(POSITION_CENTER_FLOW);
			highscoreText.setOffset(0,40);
			highscoreText.setScale(3);
			container.addComponent(highscoreText);		
		
			//Highscore erreicht
			if(gameModeScore<world.score) {
				scoreText.setText("New Highscore:");
				scoreText.setOffset(20,20);
				scoreText.setScale(4);
				highscoreText.setText(""+world.score);
				highscoreText.setOffset(20,50);
				highscoreText.setScale(5);
			}
		}else {
			TextComponent winText = new TextComponent("",font);
			winText.setPosition(POSITION_CENTER_FLOW);
			winText.setOffset(0,15);
			winText.setScale(4);
			container.addComponent(winText);
			
			if(world.getGameResult() == World.RESULT_PLAYER_1_WINS) {
				winText.setText("Player 1 wins!");
			}else if(world.getGameResult() == World.RESULT_PLAYER_2_WINS) {
				winText.setText("Player 2 wins!");
			}else {
				if(world.score>world.secondScore) {
					winText.setText("Player 1 wins!");
				}
				else if(world.secondScore>world.score) {
					winText.setText("Player 2 wins!");
				}
				else {
					winText.setText("Draw");
				}
				
			}
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
	 * Methode zum Updaten des Start-Men�s
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