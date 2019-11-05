package graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import gamelogic.World;
import graphics.gui.GameGui;
import graphics.gui.PauseMenu;
import graphics.gui.engine.ContainerComponent;
import graphics.gui.engine.KeyInput;
import graphics.gui.engine.MouseEvent;
import graphics.gui.engine.fonts.Font;
import graphics.gui.engine.fonts.MonospaceFont;
import graphics.gui.renderer.GuiShader;
import util.Settings;

/**
 * Der Renderer für das Gui unseres Programms.<br>
 * Enthält bisher nur ein paar einfache Anzeigen als Test, um zu schauen, ob das Gui-Komponentensystem funktioniert
 * 
 * @author Ben
 */
public class GuiRenderer {
	
	private GuiShader shader;
	private ContainerComponent container;
	private GameGui gameGui;
	private Font font;
	private Settings settings;
	
	private KeyInput pauseKey;
	private PauseMenu pauseMenu;
	private boolean isPauseMenuOpen;
	
	/**
	 * Erstellt einen neuen Gui-Renderer.
	 */
	public GuiRenderer(Settings settings, KeyInput pauseKey) {
		this.settings = settings;
		this.pauseKey = pauseKey;
		shader = new GuiShader();
		font = new MonospaceFont("res/font/ascii.png");
		
		// erstellt den Container und fügt ihm das "GameGui" mit FPS-Counter, Punktzahl und Fadenkreuz hinzu
		container = new ContainerComponent(640,480);
		gameGui = new GameGui(font,settings);
		container.addComponent(gameGui);
		
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	/**
	 * Rendert bisher nur ein einfaches Viereck als Test, soll aber später die ganze Gui rendern.
	 * 
	 * @param width Breite in Pixeln
	 * @param height Höhe in Pixeln
	 */
	public void render(int width, int height, World world, MouseEvent mouseEvent) {
		// bindet den Haupt-Framebuffer und bereitet den Gui-Shader vor
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,0);
		GL11.glViewport(0,0,width,height);
		shader.start();
		shader.loadScreenSize(width,height);

		if(!isPauseMenuOpen&&pauseKey.wasKeyPressed()) {
			isPauseMenuOpen = true;
			pauseMenu = new PauseMenu(font,settings);
			container.addComponent(pauseMenu);
			world.pause();
		}else if (isPauseMenuOpen && (pauseMenu.isCloseRequested())||pauseKey.wasKeyPressed()){
			isPauseMenuOpen = false;
			container.removeComponent(pauseMenu);
			pauseMenu.destroy();
			pauseMenu = null;
			world.unpause();
		}
		
		// passt die Größe des Containers an
		container.setSize(width,height);

		// verarbeitet Maus-Events
		container.receiveMouseEvent(true,mouseEvent);
		
		// updated den Container
		container.update();
		
		// rendert den Container mit allen Elementen
		container.render(shader);
	}

	/**
	 * Zeigt die gegebene Punktzahl als Text an
	 * 
	 * @param score Punktzahl
	 */
	public void displayScore(int score) {
		gameGui.displayScore(score);
	}
	
	/**
	 * Löscht den Renderer und gibt genutzte Ressourcen frei.
	 */
	public void destroy() {
		shader.destroy();
		container.destroy();
		font.destroy();
	}
	
}
