package graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import graphics.gui.engine.GuiComponent;
import graphics.gui.renderer.GuiShader;

/**
 * Der Renderer für das Gui unseres Programms.<br>
 * Enthält bisher nur ein paar einfache Anzeigen als Test, um zu schauen, ob das Gui-Komponentensystem funktioniert
 * 
 * @author Ben
 */
public class GuiRenderer {
	
	private GuiShader shader;
	
	/**
	 * Erstellt einen neuen Gui-Renderer.
	 */
	public GuiRenderer() {
		shader = new GuiShader();
		
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	/**
	 * Rendert eine Gui-Komponente
	 * 
	 * @param width Fensterbreite in Pixeln
	 * @param height Fensterhöhe in Pixeln
	 */
	public void render(int width, int height, GuiComponent component) {
		// bindet den Haupt-Framebuffer und bereitet den Gui-Shader vor
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,0);
		GL11.glViewport(0,0,width,height);
		shader.start();
		shader.loadScreenSize(width,height);
		
		// rendert den Container mit allen Elementen
		component.render(shader);
	}
	
	/**
	 * Löscht den Renderer und gibt genutzte Ressourcen frei.
	 */
	public void destroy() {
		shader.destroy();
	}
	
}
