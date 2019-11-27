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
	 * @param snake die zu rendernde Schlange
	 * @param width Breite in Pixeln
	 * @param height Höhe in Pixeln
	 */
	public void render(World world, int width, int height) {
		int innerWidth = width/pixelSize+1;
		int innerHeight = height/pixelSize+1;
		Framebuffer framebuffer = rayMarcher.render(world,innerWidth,innerHeight);
		// Kopiert das Ergebnis in den Haupt-Framebuffer
		framebuffer.bind(GL30.GL_READ_FRAMEBUFFER);
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER,0);
		GL30.glBlitFramebuffer(0,0,innerWidth,innerHeight,0,0,innerWidth*pixelSize,innerHeight*pixelSize,GL11.GL_COLOR_BUFFER_BIT,GL11.GL_NEAREST);
	}
	
	/**
	 * Setzt die Größe der Pixel, in denen gerendert werden soll
	 * @param pixelSize Größe der Pixel in Pixeln, lol
	 */
	public void setPixelSize(int pixelSize) {
		this.pixelSize = pixelSize;
	}
	
	/**
	 * Aktiviert den "Cave Effect"
	 */
	public void enableCaveEffect() {
		rayMarcher.enableCaveEffect();
	}
	
	/**
	 * Deaktiviert den "Cave Effect"
	 */
	public void disableCaveEffect() {
		rayMarcher.disableCaveEffect();
	}
	
	/**
	 * Aktiviert den "Acid Effect"
	 */
	public void enableAcidEffect() {
		rayMarcher.enableAcidEffect();
	}
	
	/**
	 * Deaktiviert den "Acid Effect"
	 */
	public void disableAcidEffect() {
		rayMarcher.disableAcidEffect();
	}
	
	/**
	 * Löscht den Renderer und gibt genutzte Ressourcen frei.
	 */
	public void destroy() {
		rayMarcher.destroy();
	}
	
}
