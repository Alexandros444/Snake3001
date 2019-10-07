package graphics.guiRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import graphics.Matrix3f;
import graphics.gui.Font;
import graphics.gui.GuiComponent;
import graphics.gui.ImageComponent;
import graphics.gui.TextComponent;

/**
 * Der Renderer f�r das Gui unseres Programms.<br>
 * Enth�lt bisher nur einen einfachen Test, um zu schauen, ob der GuiShader funktioniert.
 * 
 * @author Ben
 */
public class GuiRenderer {
	
	private GuiShader shader;
	
	private GuiComponent crosshairs;
	private TextComponent text;
	
	/**
	 * Erstellt einen neuen Gui-Renderer.
	 */
	public GuiRenderer() {
		shader = new GuiShader();
		// erstellt eine neue Gui-Komponente aus dem Bild des Fadenkreuzes
		crosshairs = new ImageComponent("res/crosshairs1.png");
		text = new TextComponent("nothing", new Font("res/font/ascii.png"));
		
		// positioniert und skaliert den Text
		Matrix3f transform = new Matrix3f();
		transform.m20 = 24;
		transform.m21 = 24;
		text.setTransform(transform);
		text.setScale(3);
		
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	/**
	 * Rendert bisher nur ein einfaches Viereck als Test, soll aber sp�ter die ganze Gui rendern.
	 * 
	 * @param width Breite in Pixeln
	 * @param height H�he in Pixeln
	 */
	public void render(int width, int height) {
		// bindet den Haupt-Framebuffer und bereitet den Gui-Shader vor
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,0);
		GL11.glViewport(0,0,width,height);
		shader.start();
		shader.loadScreenSize(width,height);
		
		// berechnet und l�dt die Position der Komponente
		Matrix3f transform = new Matrix3f();
		transform.m20 = width/2;
		transform.m21 = height/2;
		crosshairs.setTransform(transform);
		
		// rendert das Fadenkreuz
		crosshairs.render(shader);
		
		// rendert den Text
		text.render(shader);
	}

	/**
	 * Zeigt die gegebene Punktzahl als Text an
	 * 
	 * @param score Punktzahl
	 */
	public void displayScore(int score) {
		text.setText("Score: "+score);
	}
	
	/**
	 * L�scht den Renderer und gibt genutzte Ressourcen frei.
	 */
	public void destroy() {
		shader.destroy();
		crosshairs.destroy();
		text.destroy();
	}
	
	
}
