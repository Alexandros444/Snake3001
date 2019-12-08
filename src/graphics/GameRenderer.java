package graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import gamelogic.World;
import graphics.core.Framebuffer;
import graphics.raymarcher.RayMarcher;

/**
 * Der Renderer für das gesamte Spiel. Greift auf die Klasse {@link RayMarcher} zurück, um die 3D-Inhalte zu rendern.
 * 
 * @author Ben
 */
public class GameRenderer {
	
	private RayMarcher rayMarcher;

	private int pixelSize = 1;
	
	/**
	 * Erstellt einen neuen Renderer.
	 * @param useCaveEffect ob der Cave Effekt benutzt werden soll
	 * @param useAcidEffect ob der Acid Effekt benutzt werden soll
	 */
	public GameRenderer(boolean useCaveEffect, boolean useAcidEffect) {
		rayMarcher = new RayMarcher(useCaveEffect,useAcidEffect);
	}
	
	/**
	 * Rendert die gesamte Szene
	 * 
	 * @param world die zu rendernde Welt
	 * @param width Breite in Pixeln
	 * @param height Höhe in Pixeln
	 */
	public void render(World world, int width, int height) {
		int innerWidth = width/pixelSize+1;
		int innerHeight = height/pixelSize+1;
		if (!world.hasSecondSnake) {
			Framebuffer framebuffer = rayMarcher.render(innerWidth,innerHeight,world.snake,null,world.viewDirection,world.cameraPosition,world,false);
			// Kopiert das Ergebnis in den Haupt-Framebuffer
			framebuffer.bind(GL30.GL_READ_FRAMEBUFFER);
			GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER,0);
			GL30.glBlitFramebuffer(0,0,innerWidth,innerHeight,0,0,innerWidth*pixelSize,innerHeight*pixelSize,GL11.GL_COLOR_BUFFER_BIT,GL11.GL_NEAREST);
		}else {
			Framebuffer framebuffer = rayMarcher.render(innerWidth/2,innerHeight,world.snake,world.secondSnake,world.viewDirection,world.cameraPosition,world,false);
			// Kopiert das Ergebnis in den Haupt-Framebuffer
			framebuffer.bind(GL30.GL_READ_FRAMEBUFFER);
			GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER,0);
			GL30.glBlitFramebuffer(0,0,innerWidth/2,innerHeight,0,0,(innerWidth/2)*pixelSize,innerHeight*pixelSize,GL11.GL_COLOR_BUFFER_BIT,GL11.GL_NEAREST);
			Framebuffer framebuffer2 = rayMarcher.render(innerWidth/2,innerHeight,world.secondSnake,world.snake,world.secondViewDirection,world.secondCameraPosition,world,false);
			// Kopiert das Ergebnis in den Haupt-Framebuffer
			framebuffer2.bind(GL30.GL_READ_FRAMEBUFFER);
			GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER,0);
			GL30.glBlitFramebuffer(0,0,innerWidth/2,innerHeight,(innerWidth/2)*pixelSize,0,width,innerHeight*pixelSize,GL11.GL_COLOR_BUFFER_BIT,GL11.GL_NEAREST);
		}
	}
	
	/**
	 * Rendert die gesamte Szene in eine Bilddatei
	 * 
	 * @param path Dateipfad relativ zum Projektordner bzw. relativ zu dem Ordner mit der Jar-Datei
	 * @param width Breite in Pixeln
	 * @param height Höhe in Pixeln
	 * @param world die zu rendernde Welt
	 */
	public void renderToFile(String path, int width, int height, World world) {
		rayMarcher.renderToFile(path,width,height,world.snake,null,world.viewDirection,world.cameraPosition,world);
	}
	
	/**
	 * Setzt die Größe der Pixel, in denen gerendert werden soll
	 * @param pixelSize Größe der Pixel in Pixeln, lol
	 */
	public void setPixelSize(int pixelSize) {
		this.pixelSize = pixelSize;
	}
	
	/**
	 * Setzt die Größe des FOVs
	 * @param scale
	 */
	public void setFOV(float scale) {
		rayMarcher.setFOV(scale);
	}
	
	/**
	 * Setzt die Stärke der Reflektionen
	 * @param reflectivity Stärke der Reflektionen
	 */
	public void setReflectivity(float reflectivity) {
		rayMarcher.setReflectivity(reflectivity);
	}
	

	/**
	 * Lädt den Shader mit den entsprechenden Effekten
	 * 
	 * @param useCaveEffect ob der "Cave Effect" aktiv sein soll
	 * @param useAcidEffect ob der "Acid Effect" aktiv sein soll
	 */
	public void applyEffects(boolean useCaveEffect, boolean useAcidEffect) {
		rayMarcher.applyEffects(useCaveEffect,useAcidEffect);
	}
	
	/**
	 * Löscht den Renderer und gibt genutzte Ressourcen frei.
	 */
	public void destroy() {
		rayMarcher.destroy();
	}
	
}
