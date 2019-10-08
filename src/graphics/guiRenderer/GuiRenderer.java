package graphics.guiRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import graphics.Matrix3f;
import graphics.gui.Font;
import graphics.gui.GuiComponent;
import graphics.gui.ImageComponent;
import graphics.gui.MonospaceFont;
import graphics.gui.TextComponent;

/**
 * Der Renderer für das Gui unseres Programms.<br>
 * Enthält bisher nur einen einfachen Test, um zu schauen, ob der GuiShader funktioniert.
 * 
 * @author Ben
 */
public class GuiRenderer {
	
	private GuiShader shader;
	
	private GuiComponent crosshairs;
	private TextComponent scoreText, fpsText;
	private Font font;
	
	/**
	 * Erstellt einen neuen Gui-Renderer.
	 */
	public GuiRenderer() {
		shader = new GuiShader();
		font = new MonospaceFont("res/font/ascii.png");
		// erstellt eine neue Gui-Komponente aus dem Bild des Fadenkreuzes
		crosshairs = new ImageComponent("res/crosshairs1.png");
		scoreText = new TextComponent("null", font);
		fpsText = new TextComponent("null", font);
		
		
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	/**
	 * Rendert bisher nur ein einfaches Viereck als Test, soll aber später die ganze Gui rendern.
	 * 
	 * @param width Breite in Pixeln
	 * @param height Höhe in Pixeln
	 */
	public void render(int width, int height) {
		// bindet den Haupt-Framebuffer und bereitet den Gui-Shader vor
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,0);
		GL11.glViewport(0,0,width,height);
		shader.start();
		shader.loadScreenSize(width,height);
		
		// positioniert und skaliert den ScoreText
		Matrix3f transform = new Matrix3f();
		transform.m20 = width/128;
		transform.m21 = height/64;
		scoreText.setTransform(transform);
		scoreText.setScale((width+height)/460);
		
		transform.m20 = width-fpsText.getWidth();
		transform.m21 = 0;
		fpsText.setTransform(transform);
		
		// positioniert das Fadenkreuz
		transform.m20 = width/2;
		transform.m21 = height/2;
		crosshairs.setTransform(transform);
		
		// rendert das Fadenkreuz
		crosshairs.render(shader);
		
		// rendert den fpsText 
		fpsText.render(shader);
		
		// rendert den scoreText
		scoreText.render(shader);
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
	 * Zeigt die FPS als Text an
	 * 
	 * @param fps
	 */
	public void displayFPS(int fps) {
		fpsText.setText(""+fps);
	}
	
	/**
	 * Löscht den Renderer und gibt genutzte Ressourcen frei.
	 */
	public void destroy() {
		shader.destroy();
		crosshairs.destroy();
		scoreText.destroy();
	}
	
	
}
