package graphics.guiRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import graphics.Texture;
import graphics.Vao;

/**
 * Der Renderer für das Gui unseres Programms.<br>
 * Enthält bisher nur einen einfachen Test, um zu schauen, ob der GuiShader funktioniert.
 * 
 * @author Ben
 */
public class GuiRenderer {
	
	private GuiShader shader;
	
	private Vao testVao;
	private Texture testTexture;
	
	/**
	 * Erstellt einen neuen Gui-Renderer.
	 */
	public GuiRenderer() {
		shader = new GuiShader();
		// erstellt ein Vao mit den Eckpunkten eines Vierecks, auf dem das Texture dargestellt werden soll
		testVao = new Vao(new float[]{-0.1f,-0.1f,-0.1f,0.1f,0.1f,-0.1f,-0.1f,0.1f,0.1f,0.1f,0.1f,-0.1f},new float[]{0,0,0,1,1,0,0,1,1,1,1,0});
		// erstellt ein neues Texture und lädt eine einfach Test-Grafik da rein
		testTexture = new Texture();
		testTexture.bufferData(2,2,new int[] {0xff000000,0xffff0000,0xff00ff00,0xff0000ff});
	}
	
	/**
	 * Rendert bisher nur ein einfaches Viereck als Test, soll aber später die ganze Gui rendern.
	 * 
	 * @param width Breite in Pixeln
	 * @param height Höhe in Pixeln
	 */
	public void render(int width, int height) {
		// bindet den Haupt-Framebuffer und startet den Gui-Shader
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,0);
		GL11.glViewport(0,0,width,height);
		shader.start();
		
		// rendert das Viereck mit dem Texture
		testTexture.bind();
		testVao.bind();
		testVao.render();
	}
	
	
	/**
	 * Löscht den Renderer und gibt genutzte Ressourcen frei.
	 */
	public void destroy() {
		shader.destroy();
		testTexture.destroy();
		testVao.destroy();
	}
	
}
