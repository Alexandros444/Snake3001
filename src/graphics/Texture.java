package graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

/**
 * Klasse für OpengGL-Texturen. Können zum Speichern und späteren Rendern von Bildern genutzt werden.
 * 
 * @author Ben
 */
public class Texture {
	
	public final int id;
	
	private int width;
	private int height;
	
	/**
	 * Erstellt ein neues Texture
	 */
	public Texture() {
		// generiert ein neues Texture
		id = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,id);
		// setzt die Interpolationsmethode bei Größenänderungen auf Nearest-Neighbor, d.h. verpixelt
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_NEAREST);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MAG_FILTER,GL11.GL_NEAREST);
		// initialisiert Speicher des Textures
		bufferData(0,0,null);
	}
	
	/**
	 * Ändert die Größe des Textures, falls es noch nicht die richtige Größe hat.<br>
	 * Der Inhalt des Textures wird in dem Fall zurückgesetzt.
	 * 
	 * @param width Breite in Pixeln
	 * @param height Höhe in Pixeln
	 */
	public void resize(int width, int height) {
		if (this.width!=width||this.height!=height) {
			bufferData(width,height,null);
		}
	}
	
	/**
	 * Setzt das Texture auf die gegebene Größe und den gegebenen Inhalt.
	 * 
	 * @param width Breite in Pixeln
	 * @param height Höhe in Pixeln
	 * @param data Pixel-Daten im RGBA8-Format - jeder Pixel wird durch einen Integer angegeben, wobei das insignifikanteste Byte den Farbwert für Rot angibt, das zweite für Grün usw.
	 */
	public void bufferData(int width, int height, int[] pixels) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,id);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D,0,GL11.GL_RGBA,width,height,0,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE,pixels);
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Bindet das Texture an den OpenGL-Kontext, damit es zum Rendern benutzt werden kann.
	 */
	public void bind() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,id);
	}
	
	/**
	 * Löscht das Texture, um Speicher freizugeben.
	 */
	public void destroy() {
		GL11.glDeleteTextures(id);
	}
	
}
