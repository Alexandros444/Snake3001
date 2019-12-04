package graphics.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.stb.STBImage;

import util.StaticUtils;

/**
 * Klasse für OpengGL-Texturen. Können zum Speichern und späteren Rendern von Bildern genutzt werden.
 * 
 * @author Ben, Texture von Alex
 */
public class Texture {
	
	public final int id;
	
	
	private int width, height;
	
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
		bufferData(0,0,(int[])null);
	}
	
	/**
	 * Erstellt neue Textur aus Datei
	 * 
	 * @param path Dateipfad relativ zu <code>res/</code>
	 */
	public Texture(String path) {
		// ruft normalen Konstruktor auf
		this();
		// Placeholder Textur, für nicht geladene Texturen
		bufferData(2,2,new int[] {0xff000000,0xffff0000,0xff00ff00,0xff0000ff});
		 
		// Buffer für Daten der Textur(Breite, Höhe, PixelDaten)
		IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);
		try {
			// Buffer für Textur werden erstllt
			ByteBuffer fileContents = StaticUtils.ioResourceToByteBuffer(path);
			ByteBuffer pixels = STBImage.stbi_load_from_memory(fileContents,widthBuffer,heightBuffer,comp,4);
			// Textur wird aus Buffern erstellt
			bufferData(widthBuffer.get(0),heightBuffer.get(0),pixels);
				
		} catch (IOException e) {
			System.err.println("Bild konnte nicht geladen werden, Path: "+path);
			e.printStackTrace();
		}	
		
	}
	
	/**
	 * Erstellt eine neue Textur mit dem gegebenen Inhalt.<br><br>
	 * Hat den selben Effekt wie ein Aufruf von {@link #bufferData(int, int, int[])}
	 * 
	 * @param width Breite in Pixeln
	 * @param height Höhe in Pixeln
	 * @param pixels Pixel-Daten im Format 0xAABBGGRR (also Hexadezimal)
	 */
	public Texture(int width, int height, int[] pixels) {
		this();
		bufferData(width,height,pixels);
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
			bufferData(width,height,(int[])null);
		}
	}
	
	/**
	 * Setzt das Texture auf die gegebene Größe und den gegebenen Inhalt.
	 * 
	 * @param width Breite in Pixeln
	 * @param height Höhe in Pixeln
	 * @param pixels Pixel-Daten im RGBA8-Format - jeder Pixel wird durch einen Integer angegeben, wobei das insignifikanteste Byte den Farbwert für Rot angibt, das zweite für Grün usw.
	 */
	public void bufferData(int width, int height, int[] pixels) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,id);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D,0,GL11.GL_RGBA,width,height,0,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE,pixels);
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Setzt das Texture auf die gegebene Größe und den gegebenen Inhalt.
	 * 
	 * @param width Breite in Pixeln
	 * @param height Höhe in Pixeln
	 * @param pixels Pixel-Daten ByteBuffer-Magic
	 */
	public void bufferData(int width, int height, ByteBuffer pixels) {
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
	 * Gibt die Breite des Textures zurück.
	 * 
	 * @return Breite in Pixeln
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Gibt die Höhe des Textures zurück.
	 * 
	 * @return Höhe in Pixeln
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Speichert den Inhalt der Textur als png-Datei ab.
	 * @param path Dateipfad relativ zum Projektordner bzw. relativ zu dem Ordner, der die Jar-Datei enhält
	 */
	public void saveAsFile(String path) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,id);
		ByteBuffer pixels = BufferUtils.createByteBuffer(width*height*4);
		GL11.glGetTexImage(GL11.GL_TEXTURE_2D,0,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE,pixels);
		int[] ints = new int[width*height];
		for (int x=0;x<width;x++) {
			for (int y=0;y<height;y++) {
				int i = x+(height-y-1)*width;
				int a = pixels.get(i*4+3)&0xff;
				int r = Math.round((pixels.get(i*4)&0xff)*255f/a);
				int g = Math.round((pixels.get(i*4+1)&0xff)*255f/a);
				int b = Math.round((pixels.get(i*4+2)&0xff)*255f/a);
				ints[x+y*width] = (a<<24)+(r<<16)+(g<<8)+b;
			}
		}
		try {
			BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			bi.getRaster().setDataElements(0,0,width,height,ints);
			ImageIO.write(bi,"png",new File(path+".png"));
		} catch (IOException e) {
			System.out.println("Failed to write image!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Löscht das Texture, um Speicher freizugeben.
	 */
	public void destroy() {
		GL11.glDeleteTextures(id);
	}
	
}
