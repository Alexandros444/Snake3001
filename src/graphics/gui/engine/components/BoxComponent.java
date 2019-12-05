package graphics.gui.engine.components;

import graphics.core.Texture;
import graphics.core.Vao;
import graphics.gui.engine.ContainerComponent;
import graphics.gui.renderer.GuiShader;

/**
 * Die Klasse für ContainerKomponenten mit eigenem Hintergrund.<br>
 * Der Hintergrund wird automatisch aus der gegebenen Farbe und Umrandung erstellt und hinter den Kindelementen gerendert.
 * 
 * @author Ben
 */
public class BoxComponent extends ContainerComponent {
	
	private Vao vao;
	private Texture texture;
	
	private int backgroundColor;
	private int borderColor;
	private int borderWidth;
	
	/**
	 * Erstell eine neue Boxkomponente
	 * 
	 * @param width Breite in Pixeln
	 * @param height Höhe in Pixeln
	 * @param backgroundColor Hintergrundfarbe im Format 0xaabbggrr (Hexadezimal)
	 * @param borderColor Farbe des Randes im Format 0xaabbggrr (Hexadezimal)
	 * @param borderWidth Breite des Randes in Pixeln
	 */
	public BoxComponent(int width, int height, int backgroundColor, int borderColor, int borderWidth) {
		super(width, height);
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
		this.borderWidth = borderWidth;
		vao = new Vao(width,height);
		texture = generateTexture(width,height,backgroundColor,borderColor,borderWidth);
	}
	
	/**
	 * Passt bei Größenänderungen den eigenen Hintergrund und die Anordnung der Kindelemente an.
	 */
	protected void onSizeChange() {
		super.onSizeChange();
		vao.destroy();
		texture.destroy();
		int width = super.getWidth();
		int height = super.getHeight();
		vao = new Vao(width,height);
		texture = generateTexture(width,height,backgroundColor,borderColor,borderWidth);
	}
	
	/**
	 * Setzt die Hintergrundfarbe
	 * 
	 * @param color Hintergrundfarbe im Format 0xaabbggrr (Hexadezimal)
	 */
	public void setBackgroundColor(int color) {
		backgroundColor = color;
		texture.destroy();
		texture = generateTexture(super.getWidth(),super.getHeight(),backgroundColor,borderColor,borderWidth);
	}
	
	/**
	 * Setzt die Farbe des Randes
	 * 
	 * @param color Farbe im Format 0xaabbggrr (Hexadezimal)
	 */
	public void setBorderColor(int color) {
		borderColor = color;
		texture.destroy();
		texture = generateTexture(super.getWidth(),super.getHeight(),backgroundColor,borderColor,borderWidth);
	}
	
	/**
	 * Rendert das Element und all seine Kindelemente.
	 * @param shader zum Rendern genutzter Shader
	 */
	public void render(GuiShader shader) {
		if (super.getVisibility()==VISIBILITY_VISIBLE) {
			shader.loadTransformationMatrix(super.getTotalTransform());
			shader.loadTransparency(super.getTotalTransparency());
			texture.bind();
			vao.bind();
			vao.render();
			super.render(shader);
		}
	}
	
	/**
	 * Löscht das Element und alle Kindelemente, um Ressourcen freizugeben.
	 */
	public void destroy() {
		vao.destroy();
		texture.destroy();
		super.destroy();
	}
	
	/**
	 * Erstellt eine neue Hintergrundtextur
	 * 
	 * @param width Breite in Pixeln
	 * @param height Höhe in Pixeln
	 * @param backgroundColor Hintergrundfarbe im Format 0xaabbggrr (Hexadezimal)
	 * @param borderColor Farbe des Randes im Format 0xaabbggrr (Hexadezimal)
	 * @param borderWidth Breite des Randes in Pixeln
	 * @return die Textur
	 */
	private static Texture generateTexture(int width, int height, int backgroundColor, int borderColor, int borderWidth) {
		int[] pixels = new int[width*height];
		for (int x=0;x<width;x++) {
			for (int y=0;y<height;y++) {
				if (x<borderWidth||y<borderWidth||x>=width-borderWidth||y>=height-borderWidth) {
					pixels[x+y*width] = borderColor;
				}else {
					pixels[x+y*width] = backgroundColor;
				}
			}
		}
		return new Texture(width,height,pixels);
	}
	
}
