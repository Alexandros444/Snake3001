package graphics.gui.engine.components;

import graphics.core.Texture;
import graphics.core.Vao;
import graphics.gui.engine.GuiComponent;
import graphics.gui.renderer.GuiShader;

/**
 * Die Klasse für Bild-Komponenten unseres Gui-Systems.
 * 
 * @author Ben
 */
public class ImageComponent extends GuiComponent {
	
	private final Vao vao;
	private Texture texture;
	
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
		this(path,2);
	}
	public ImageComponent(String path,float scale) {
		// ruft den GuiComponent-Konstruktor auf
		super(0,0);
		
		// lädt das Bild und passt eigene Größe an
		texture = new Texture(path);
		int width = (int)(scale*texture.getWidth());
		int height = (int)(scale*texture.getHeight());
		super.setSize(width,height);
		System.out.println("Width: "+width+", height: "+height);
		
		// erstellt Rechteck-Vao
		vao = new Vao(new float[]{0,0,0,height,width,0,0,height,width,height,width,0},new float[]{0,0,0,1,1,0,0,1,1,1,1,0});
	}	
	
	/**
	 * Rendert das Bild
	 */
	public void render(GuiShader shader) {
		shader.loadTransformationMatrix(super.getTotalTransform());
		shader.loadTransparency(super.getTotalTransparency());
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
	
	/**
	 * lädt das Bild neu
	 * 
	 * @param path Pfad relativ
	 */
	public void reloadImage(String path) {
		texture.destroy();
		texture = new Texture(path);
	}
	
}
