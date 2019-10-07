package graphics.gui;

import graphics.Texture;
import graphics.Vao;
import graphics.guiRenderer.GuiShader;

/**
 * Die Klasse für Bild-Komponenten unseres Gui-Systems.
 * 
 * @author Ben
 */
public class ImageComponent extends GuiComponent {
	
	private final Vao vao;
	private final Texture texture;
	
	/**
	 * Erstellt eine neue Bild-Komponente aus dem gegebenen Dateipfad.
	 * <br><br>
	 * <b>Achtung</b>: Bilder mit ungerader Breite oder Höhe funktionieren gerade noch nicht richtig, es kann vorkommen, dass am Rand eine Reihe Pixel abgeschnitten oder doppelt dargestellt wird.<br>
	 * Das liegt vermutlich an Rundungsfehlern in der Grafikkarte, wie wir das am besten beheben können ist derzeit noch nicht klar.
	 * 
	 * @param path Dateipfad relativ zu <code>res/res</code>
	 */
	public ImageComponent(String path) {
		// ruft den GuiComponent-Konstruktor auf
		super(0,0);
		
		// lädt das Bild und passt eigene Größe an
		texture = new Texture(path);
		int width = 2*texture.getWidth();
		int height = 2*texture.getHeight();
		super.setSize(width,height);
		System.out.println("Width: "+width+", height: "+height);
		
		// erstellt Rechteck-Vao
		float x = 0.5f*width;
		float y = 0.5f*height;
		vao = new Vao(new float[]{-x,-y,-x,y,x,-y,-x,y,x,y,x,-y},new float[]{0,0,0,1,1,0,0,1,1,1,1,0});
	}
	
	/**
	 * Rendert das Bild
	 */
	public void render(GuiShader shader) {
		shader.loadTransformationMatrix(super.getTotalTransform());
		texture.bind();
		vao.bind();
		vao.render();
	}
	
	/**
	 * Löscht das Bild, um Speicher freizugeben
	 */
	public void destroy() {
		vao.destroy();
		texture.destroy();
	}
	
}
