package graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import graphics.gui.FpsCounter;
import graphics.gui.PauseMenu;
import graphics.gui.engine.ContainerComponent;
import graphics.gui.engine.GuiComponent;
import graphics.gui.engine.KeyInput;
import graphics.gui.engine.MouseEvent;
import graphics.gui.engine.components.ImageComponent;
import graphics.gui.engine.components.TextComponent;
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
	private GuiComponent crosshairs;
	private TextComponent scoreText, fpsText;
	private ContainerComponent container;
	private Font font;
	
	private KeyInput pauseKey;
	private PauseMenu pauseMenu;
	private boolean isPauseMenuOpen;
	
	/**
	 * Erstellt einen neuen Gui-Renderer.
	 */
	public GuiRenderer(Settings settings, KeyInput pauseKey) {
		this.pauseKey = pauseKey;
		shader = new GuiShader();
		font = new MonospaceFont("res/font/ascii.png");
		// erstellt eine neue Gui-Komponente aus dem Bild des Fadenkreuzes
		crosshairs = new ImageComponent("res/"+settings.guiRendererCrosshair+".png");
		// erstellt zwei leere Textkomponenten für Punktzahl und FPS
		scoreText = new TextComponent("", font);
		fpsText = new FpsCounter(font);
		
		// legt die Positionen der Elemente fest
		crosshairs.setPosition(GuiComponent.POSITION_CENTER);
		
		scoreText.setPosition(GuiComponent.POSITION_CORNER_TOPLEFT);
		scoreText.setOffset(24,24);
		scoreText.setScale(3);
		
		fpsText.setPosition(GuiComponent.POSITION_CORNER_TOPRIGHT);
		fpsText.setOffset(6,6);
		fpsText.setScale(2);
		
		// erstellt den Container und fügt alle Elemente zu ihm zu
		container = new ContainerComponent(640,480);
		container.addComponent(crosshairs);
		container.addComponent(scoreText);
		container.addComponent(fpsText);
		
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	/**
	 * Rendert bisher nur ein einfaches Viereck als Test, soll aber später die ganze Gui rendern.
	 * 
	 * @param width Breite in Pixeln
	 * @param height Höhe in Pixeln
	 */
	public void render(int width, int height, MouseEvent mouseEvent) {
		// bindet den Haupt-Framebuffer und bereitet den Gui-Shader vor
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,0);
		GL11.glViewport(0,0,width,height);
		shader.start();
		shader.loadScreenSize(width,height);

		if(pauseKey.wasKeyPressed()) {
			if(!isPauseMenuOpen) {
				isPauseMenuOpen = true;
				pauseMenu = new PauseMenu(font);
				container.addComponent(pauseMenu);
			}else {
				isPauseMenuOpen = false;
				container.removeComponent(pauseMenu);
				pauseMenu.destroy();
				pauseMenu = null;
			}
		}
		
		// passt die Größe des Containers an
		container.setSize(width,height);
		if(isPauseMenuOpen) {
			pauseMenu.setSize(width,height);
		}

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
		scoreText.setText("Score: "+score);
	}
	
	/**
	 * Löscht den Renderer und gibt genutzte Ressourcen frei.
	 */
	public void destroy() {
		shader.destroy();
		crosshairs.destroy();
		scoreText.destroy();
		font.destroy();
	}
	
}
