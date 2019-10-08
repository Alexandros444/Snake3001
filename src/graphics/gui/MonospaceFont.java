package graphics.gui;

import graphics.Texture;

/**
 * Klasse für Schriftarten. Hält je eine Referenz auf eine Textur, in der alle
 * Buchstaben enthalten sind
 * 
 * @author Alex
 */
public class MonospaceFont implements Font {

	public Texture texture;

	/**
	 * @param path Pfad relativ zu <code>res/res</code>
	 */
	public MonospaceFont(String path) {
		loadTexture(path);
	}
	
	public void loadTexture(String path) {
		texture = new Texture(path);
	}

	public Texture getTexture() {
		return texture;
	}

	public float getCharX(int asciiCode) {
		return asciiCode%16/16f;
	}

	public float getCharY(int asciiCode) {
		return (float) Math.floor(asciiCode/16f)/16f-1/16;
	}

	public float getCharOffX(int asciiCode) {
		return 1/16f;
	}

	public float getCharOffY(int asciiCode) {
		return 1/16f;
	}

	public float getCharWidth(int asciiCode) {
		return 8;
	}

	public float getCharHeight(int asciiCode) {
		return 8;
	}

	public float getPadding(int asciiCode) {
		return 6;
	}

}