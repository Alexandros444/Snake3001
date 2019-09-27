package graphics.guiRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import graphics.Matrix3f;
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
	private Matrix3f testTransform;
	
	/**
	 * Erstellt einen neuen Gui-Renderer.
	 */
	public GuiRenderer() {
		shader = new GuiShader();
		// erstellt ein Vao mit den Eckpunkten eines Vierecks, auf dem das Texture dargestellt werden soll
		testVao = new Vao(new float[]{-20f,-20f,-20f,20f,20f,-20f,-20f,20f,20f,20f,20f,-20f},new float[]{0,0,0,1,1,0,0,1,1,1,1,0});
		// erstellt ein neues Texture und lädt eine einfach Test-Grafik da rein
		testTexture = new Texture("res/crosshairs.png");
		testTransform = new Matrix3f();
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
		
		// ändert und lädt die Position des Vierecks
		//testTransform.rotate(0,0,-1);
		testTransform.m20 = width/2;
		testTransform.m21 = height/2;
		shader.loadTransformationMatrix(testTransform);
		
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
