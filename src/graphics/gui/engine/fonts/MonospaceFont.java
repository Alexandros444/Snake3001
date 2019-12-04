package graphics.gui.engine.fonts;

import graphics.core.Texture;

/**
 * Klasse für Schriftarten. Hält je eine Referenz auf eine Textur, in der alle
 * Buchstaben enthalten sind
 * 
 * @author Alex
 */
public class MonospaceFont implements Font {

	private Texture texture;

	/**
	 * @param path Pfad relativ zu <code>res/</code>
	 */
	public MonospaceFont(String path) {
		texture = new Texture(path);
	}
	
	/**
	 * Gibt die X Position des Zeichens in der Textur wieder
	 * (0-1) für textures-array
	 * 
	 * @param asciiCode Ascii-Code des Zeichens
	 * @return Position (0-1)
	 */
	public float getCharX(char asciiCode) {
		return asciiCode%16/16f;
	}

	/**
	 * Gibt die Y Position des Zeichens in der Textur wieder
	 * (0-1) für textures-array
	 * 
	 * @param asciiCode Ascii-Code des Zeichens
	 * @return Position (0-1)
	 */
	public float getCharY(char asciiCode) {
		return (float) Math.floor(asciiCode/16f)/16f-1/16;
	}

	/**
	 * Gibt die Breite des Zeichens in der Textur wieder
	 * (0-1) für textures-array
	 * 
	 * @param asciiCode Ascii-Code des Zeichens
	 * @return Position (0-1)
	 */
	public float getCharOffX(char asciiCode) {
		if (asciiCode=='%') {
			return (6f/8f)*1f/16f;
		}else {
			return (5f/8f)*1f/16f;
		}
	}

	/**
	 * Gibt die Höhe des Zeichens in der Textur wieder
	 * (0-1) für textures-array
	 * 
	 * @param asciiCode Ascii-Code des Zeichens
	 * @return Position (0-1)
	 */
	public float getCharOffY(char asciiCode) {
		return 1/16f;
	}

	/**
	 * Gibt die Breite des Zeichens in px wieder
	 * für positions-array
	 * 
	 * @param asciiCode Ascii-Code des Zeichens
	 * @return Breite Breite in px
	 */
	public float getCharWidth(char asciiCode) {
		return 5;
	}

	/**
	 * Gibt die Höhe des Zeichens in px wieder
	 * für positions-array
	 * 
	 *@param asciiCode Ascii-Code des Zeichens
	 *@return Höhe Höhe in px
	 */
	public float getCharHeight(char asciiCode) {
		return 8;
	}
	
	/**
	 * Gibt die geladene Textur wieder
	 * 
	 *@return Texture geladene Textur
	 */
	public Texture getTexture() {
		return texture;
	}
	
	public void destroy() {
		texture.destroy();
	}
	
}