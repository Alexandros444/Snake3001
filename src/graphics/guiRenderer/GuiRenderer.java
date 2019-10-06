package graphics.guiRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import graphics.Matrix3f;
import graphics.gui.Font;
import graphics.gui.GuiComponent;
import graphics.gui.ImageComponent;
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
	private TextComponent text;
	
	/**
	 * Erstellt einen neuen Gui-Renderer.
	 */
	public GuiRenderer() {
		shader = new GuiShader();
		// erstellt eine neue Gui-Komponente aus dem Bild des Fadenkreuzes
		crosshairs = new ImageComponent("res/crosshairs1.png");
		text = new TextComponent("nothing", new Font("res/font/ascii.png"));
		
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
		
		// berechnet und lädt die Position der Komponente
		Matrix3f transform = new Matrix3f();
		transform.m20 = width/2;
		transform.m21 = height/2;
		shader.loadTransformationMatrix(transform);
		
		// rendert das Fadenkreuz
		crosshairs.render();
		
		// ändert die Transformations-Martix und lädt sie
		transform.scale(2);
		transform.m20 = width/32;
		transform.m21 = height-height/16;
		shader.loadTransformationMatrix(transform);
		// rendert die Textbox
		text.render();
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
	 * Löscht den Renderer und gibt genutzte Ressourcen frei.
	 */
	public void destroy() {
		shader.destroy();
		crosshairs.destroy();
		text.destroy();
	}
	
	
}
