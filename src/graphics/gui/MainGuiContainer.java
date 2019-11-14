package graphics.gui;

import gamelogic.World;
import graphics.GuiRenderer;
import graphics.RayMarcher;
import graphics.core.Display;
import graphics.gui.engine.ContainerComponent;
import graphics.gui.engine.KeyInput;
import graphics.gui.engine.fonts.Font;
import graphics.gui.engine.fonts.MonospaceFont;
import util.Settings;
import util.math.Matrix3f;


/**
 * Klasse für den Haupt-Gui-Container unseres Spiels. Verwaltet sowohl das Spiel und den Renderer dazu, als auch das gesamte Gui.
 * 
 * @author Ben
 */
public class MainGuiContainer extends ContainerComponent {
	
	private Font font;
	private Settings settings;
	
	private RayMarcher gameRenderer;
	private GuiRenderer guiRenderer;
	private World world;
	
	private GameGui gameGui;

	private KeyInput pauseKey;
	private PauseMenu pauseMenu;
	private boolean isPauseMenuOpen;
	
	private StartMenu startMenu;
	private boolean hasGameStarted;
	
	private DeathMenu deathScreen;
	private boolean isDeathMenuOpen;
	
	private IntroScreen introScreen;
	private boolean isIntroScreenOpen;
	
	private float scaleX = 1; 
	private float scaleY = 1;
	/**
	 * Erstellt den Container
	 * 
	 * @param settings Einstellungs-Objekt
	 * @param pauseKey Taste für Pausenmenü
	 */
	public MainGuiContainer(Settings settings, KeyInput pauseKey) {
		super(0,0);
		this.settings = settings;
		this.pauseKey = pauseKey;
		font = new MonospaceFont("res/font/ascii.png");
		
		gameRenderer = new RayMarcher();
		gameRenderer.setPixelSize(settings.pixelSize);
		guiRenderer = new GuiRenderer();
		world = new World(settings);
		
		gameGui = new GameGui(font,settings);
		super.addComponent(gameGui);
		
		openStartMenu();
		
		introScreen = new IntroScreen();
		super.addComponent(introScreen);
		isIntroScreenOpen = true;
		
	}
	
	/**
	 * Updated das Spiel und Gui
	 * 
	 * @param display Fenster, durch dessen Tastendrücke die Schlange gesteuert wird
	 */
	public void update(Display display) {
		super.update();
		
		world.update(display);
		
		gameGui.displayScore(world.score);
		
		if(!hasGameStarted) {
			// Hauptmenü ist offen
			if(startMenu.isStartRequested()) {
				startGame();
			}else if(startMenu.hasSettingsChanged) {
				applyChangedSettings();
			}
		}else {
			// Spiel hat begonnen
			if(isDeathMenuOpen) {
				// Death-Menü ist offen
				if(deathScreen.isExitRequested()) {
					// schließt den DeathScreen und öffnet wieder das Hauptmenü
					closeDeathScreen();
					openStartMenu();
					world.reset();
					}
				else if(deathScreen.isRestartRequested()){ 
					restartGame();
				}
			}else {
				// Death-Menü ist nicht offen
				if(world.hasSnake&&world.snake.isAlive==false) {
					// Schlange ist gestorben
					openDeathScreen();
				}
				// Pausenmenü kann nur geöffnet werden wenn DeathMenü geschlossen ist
				if(isPauseMenuOpen) {
					if (pauseMenu.isContinueRequested()||pauseKey.wasKeyPressed()){
						// schließt das Pausenmenü wieder
						closePauseMenu();
					}else if(pauseMenu.isExitRequested()) {
						// öffnet das Hauptmenü und setzt die Welt zurück
						closePauseMenu();
						openStartMenu();
						world.killSnake();
						world.reset();
					}else if(pauseMenu.hasSettingsChanged) {
						applyChangedSettings();
					}
				} else if(pauseKey.wasKeyPressed()) {
					// öffnet das Pausenmenü
					openPauseMenu();
				}
			}
		}
		if(isIntroScreenOpen && introScreen.isFinished()) {
			isIntroScreenOpen = false;
			super.removeComponent(introScreen);
			introScreen.destroy();
			introScreen = null;
		}
	}
	
	/**
	 * Öffnet das Hauptmenü
	 */
	private void openStartMenu() {
		startMenu = new StartMenu(font,settings);
		hasGameStarted = false;
		super.addComponent(startMenu);
	}
	
	/**
	 * Schließt das Hauptmenü und startet das Spiel
	 */
	private void startGame() {
		hasGameStarted = true;
		super.removeComponent(startMenu);
		startMenu.destroy();
		startMenu = null;
		world.spawnSnake();
	}
	/**
	 * Schließt das deathMenu und startet das Spiel neu
	 */
	private void restartGame() {
		hasGameStarted = true; 
		closeDeathScreen();
		world.reset();
		world.spawnSnake();		 
	}
	
	/**
	 * Pausiert das Spiel und öffnet das Pausenmenü
	 */
	private void openPauseMenu() {
		isPauseMenuOpen = true;
		pauseMenu = new PauseMenu(font,settings);
		super.addComponent(pauseMenu);
		world.pause();
	}
	
	/**
	 * Schließt das Pausenmenü und de-pausiert das Spiel
	 */
	private void closePauseMenu() {
		isPauseMenuOpen = false;
		super.removeComponent(pauseMenu);
		pauseMenu.destroy();
		pauseMenu = null;
		world.unpause();
	}
	
	/**
	 * Öffnet den Deathscreen
	 */
	private void openDeathScreen() {
		isDeathMenuOpen = true;
		deathScreen = new DeathMenu(font,world.score);
		super.addComponent(deathScreen);
	}
	
	/**
	 * Schließt den Deathscreen
	 */
	private void closeDeathScreen() {
		isDeathMenuOpen = false;
		super.removeComponent(deathScreen);
		deathScreen.destroy();
		deathScreen = null;
	}
	
	/**
	 * Wendet Änderungen der Settings auf alles an
	 */
	private void applyChangedSettings() {
		gameGui.crosshairs.reloadImage(settings.crosshairImagePath);
		gameRenderer.setPixelSize(settings.pixelSize);
		world.updateMovementSpeed();
	}
	
	/**
	 * Rendert das Spiel und das Gui
	 * 
	 * @param width Breite in Pixeln
	 * @param height Höhe in Pixeln
	 */
	public void render(int width, int height) {
		gameRenderer.render(world,width,height);
		guiRenderer.render(width,height,this);
	}
	
	/**
	 * Gibt zurück, ob das Spiel geschlossen werden soll
	 * 
	 * @return ob das Spiel geschlossen werden soll
	 */
	public boolean isCloseRequested() {
		return !hasGameStarted&&startMenu.isCloseRequested(); 
	}
	
	/**
	 * Löscht alle zugehörigen Ressourcen, um wieder Speicher freizugeben
	 */
	public void destroy() {
		super.destroy();
		font.destroy();
		gameRenderer.destroy();
		guiRenderer.destroy();
	}
	
	public void setScale(float scaleX, float scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		Matrix3f scaleMatrix = new Matrix3f(); 
		 
		scaleMatrix.m00 *= scaleX;
		scaleMatrix.m11 *= scaleY;
		super.setInnerTransform(scaleMatrix);
	}
	
	public void setSize(int width, int height) {
		super.setSize((int)(width/scaleX),(int)(height/scaleY));
	}
}
