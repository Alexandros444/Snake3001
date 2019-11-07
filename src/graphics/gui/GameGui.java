package graphics.gui;

import graphics.gui.engine.ContainerComponent;
import graphics.gui.engine.GuiComponent;
import graphics.gui.engine.components.ImageComponent;
import graphics.gui.engine.components.TextComponent;
import graphics.gui.engine.fonts.Font;
import util.Settings;

/**
 * Enth�lt alles, was im Spiel durchgehend angezeigt wird - also bisher Fadenkreuz, Punktzahl und FPS.
 * 
 * @author Ben
 */
public class GameGui extends ContainerComponent {

	private ImageComponent crosshairs;
	private TextComponent scoreText;
	private FpsCounter fpsCounter;
	
	/**
	 * Erstellt alle Komponenten
	 * 
	 * @param font Schriftart f�r die Punktzahl- und Fps-Texte
	 * @param settings Einstellungen, f�r Fadenkreuz
	 */
	public GameGui(Font font, Settings settings) {
		super(0,0);
		super.setPosition(POSITION_FULL);
		
		// Erstellt das Fadenkreuz
		crosshairs = new ImageComponent("res/"+settings.guiRendererCrosshair+".png");
		crosshairs.setPosition(GuiComponent.POSITION_CENTER);
		
		// Erstellt den Text f�r die Punktzahl
		scoreText = new TextComponent("", font);
		scoreText.setPosition(GuiComponent.POSITION_CORNER_TOPLEFT);
		scoreText.setOffset(24,24);
		scoreText.setScale(3);
		
		// Erstellt die FPS-Anzeige
		fpsCounter = new FpsCounter(font);
		fpsCounter.setPosition(GuiComponent.POSITION_CORNER_TOPRIGHT);
		fpsCounter.setOffset(6,6);
		fpsCounter.setScale(2);
		
		// f�gt alle Komponenten zu sich hinzu
		super.addComponent(crosshairs);
		super.addComponent(scoreText);
		super.addComponent(fpsCounter);
	}
	
	/**
	 * Aktualisiert die angezeigte Punktzahl.
	 * 
	 * @param score derzeitige Punktzahl
	 */
	public void displayScore(int score) {
		scoreText.setText("Score: "+score);
	}
	
}