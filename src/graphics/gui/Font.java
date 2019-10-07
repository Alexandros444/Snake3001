package graphics.gui;

import graphics.Texture;

/**
 * Klasse für Schriftarten. Hält je eine Referenz auf eine Textur, in der alle Buchstaben enthalten sind
 * 
 * @author Alex
 */
public class Font {

	public Texture texture;
	
	/**
	 * @param path Pfad relativ zu <code>res/res</code>
	 */
	public Font(String path) {
		texture = new Texture(path);
	}

}