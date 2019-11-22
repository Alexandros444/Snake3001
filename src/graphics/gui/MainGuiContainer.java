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
	
	private GameModeMenu gameModeMenu;
	private boolean isGameModeMenuOpen;
	
	private DeathMenu deathScreen;
	private boolean isDeathMenuOpen;
	
	private IntroScreen introScreen;
	private boolean isIntroScreenOpen;
	
	private boolean isFullscreen;
	public boolean hasFullscreenChanged;
	
	private float scaleX = 1; 
	private float scaleY = 1;
	
	private int playerCount;
	
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
		
		gameRenderer = new RayMarcher(settings.isCaveEffectEnabled,true);
		gameRenderer.setPixelSize(settings.pixelSize);
		guiRenderer = new GuiRenderer();
		world = new World();
		
		gameGui = new GameGui(font,settings);
		super.addComponent(gameGui);
		
		openStartMenu();
		
		//introScreen = new IntroScreen();
		//super.addComponent(introScreen);
		//isIntroScreenOpen = true;
		
		isFullscreen = settings.isFullscreen;
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
			if (isGameModeMenuOpen) {
				// GameModeMenu ist offen
				if (gameModeMenu.isStartRequested()) {
					world.setGameMode(gameModeMenu.getSelectedMode());
					closeGameModeMenu();
					display.toggleCursor();
					startGame();
				}else if(pauseKey.wasKeyPressed()) {
					closeGameModeMenu();
					openStartMenu();
				}
			}else {
				// Hauptmenü ist offen
				if(startMenu.isStartRequested()) {
					playerCount = startMenu.getPlayerCount();
					closeStartMenu();
					openGameModeMenu();
				}else if(startMenu.hasSettingsChanged) {
					applyChangedSettings(display);
				}
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
					display.toggleCursor();
				}
			}else {
				// Death-Menü ist nicht offen
				if(world.hasSnake&&world.snake.isAlive==false) {
					// Schlange ist gestorben
					openDeathScreen();
					saveScore();
					display.toggleCursor();
				}
				// Pausenmenü kann nur geöffnet werden wenn DeathMenü geschlossen ist
				if(isPauseMenuOpen) {
					if (pauseMenu.isContinueRequested()||pauseKey.wasKeyPressed()){
						// schließt das Pausenmenü wieder
						closePauseMenu();
						display.toggleCursor();
					}else if(pauseMenu.isExitRequested()) {
						// öffnet das Hauptmenü und setzt die Welt zurück
						saveScore();
						closePauseMenu();
						openStartMenu();
						world.reset();
					}else if(pauseMenu.hasSettingsChanged) {
						applyChangedSettings(display);
					}
				} else if(pauseKey.wasKeyPressed() || !display.isFocused()) {
					// öffnet das Pausenmenü
					openPauseMenu();
					display.toggleCursor();
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
	
	private void saveScore() {
		if(world.gameMode == World.MODE_NORMAL) {
			if(settings.normalScore<world.score) {
				settings.normalScore = world.score;			
			}
		}else if(world.gameMode == World.MODE_FAST) {
			if(settings.fastScore<world.score) {
				settings.fastScore = world.score;			
			}
		}else {
			if(settings.tunnelScore<world.score) {
				settings.tunnelScore = world.score;			
			}
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
	 * Schließt das Hauptmenü
	 */
	private void closeStartMenu() {
		super.removeComponent(startMenu);
		startMenu.destroy();
		startMenu = null;
	}
	
	/**
	 * Öffnet das GameModeMenu
	 */
	private void openGameModeMenu() {
		gameModeMenu = new GameModeMenu(font,settings);
		isGameModeMenuOpen = true;
		super.addComponent(gameModeMenu);
	}
	
	/**
	 * Schließt das GameModeMenu
	 */
	private void closeGameModeMenu() {
		isGameModeMenuOpen = false;
		super.removeComponent(gameModeMenu);
		gameModeMenu.destroy();
		gameModeMenu = null;
	}
	
	/**
	 * Startet das Spiel
	 */
	private void startGame() {
		hasGameStarted = true;
		world.spawnSnake();
		if(playerCount == 2){ 
			world.spawnSecondSnake(); 
		}
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
		deathScreen = new DeathMenu(font,world.score, world.gameMode,settings);
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
	private void applyChangedSettings(Display display) {
		gameGui.crosshairs.loadImage(settings.crosshairImagePath);
		gameRenderer.setPixelSize(settings.pixelSize);
		if(isFullscreen != settings.isFullscreen) {
			hasFullscreenChanged = true;
		}
		isFullscreen = settings.isFullscreen;
		if(settings.cursorFrame!=0) {
			display.setCursor(settings.cursorImagePath);
		}else {
			display.setStandardCursor();
		}
		if(settings.isCaveEffectEnabled) {
			gameRenderer.enableCaveEffect();
		}else {
			gameRenderer.disableCaveEffect();
		}
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
		return !hasGameStarted&&!isGameModeMenuOpen&&startMenu.isCloseRequested(); 
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
