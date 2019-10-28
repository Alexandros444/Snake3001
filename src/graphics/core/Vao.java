package graphics.core;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

/**
 * Klasse für Vertex Array Objects (VAOs).<br>
 * VAOs bestehen aus je einer Liste mit Eckpunkten bzw. deren Positionen und einer Liste mit deren Texturkoordinaten.
 * Diese Punkte werden beim Rendern dann zu Dreiecken zusammengesetzt, auf denen mithilfe der Texturkoordinaten die derzeit gebundene Textur dargestellt wird.
 * Sie enthalten damit alle Informationen, die benötigt werden um ein Objekt zu rendern.
 * 
 * @author Ben
 */

public class Vao {
	
	public final int id;
	private final int positionsVboID;
	private final int textureCoordsVboID;
	private final int length;
	
	/**
	 * Erstellt ein neues Vertex Array Object (VAO), das dann später gerendert werden kann.<br>
	 * Das VAO besteht aus einer Liste mit Eckpunkten bzw. deren Positionen und einer Liste mit deren Texturkoordinaten.<br>
	 * Beginnt beispielsweise die Liste der Positionen mit (1|2) und die der Texturkoordinaten mit (0|1),
	 * so wird der erste Eckpunkt an der Position (1|2) und mit der Farbe, die die Textur an der Stelle (0|1) hat, gezeichnet.<br>
	 * Die Texturkoordinaten erstrecken sich unabhängig von der Bildgröße von (0|0) bis (1|1).
	 * <br><br>
	 * Die Punkte werden beim Rendern je zu dritt zu Dreiecken verbunden. Dabei werden die Texturkoordinaten über das Dreieck interpoliert,
	 * d.h. der entsprechende Teil der Textur wird in dem Dreieck dargestellt.<br>
	 * Die Reihenfolge der Punkte in den Listen ist egal, solange<ol>
	 * <li>je drei aufeinanderfolgende Punkte ein Dreieck bilden (also Punkt 1, 2 und 3, Punkt 4, 5 und 6 usw.),</li>
	 * <li>die Punkte der Dreiecke jeweils in dem Uhrzeigersinn entgegengesetzter Reihenfolge angegeben werden, und</li>
	 * <li>die Punkte in beiden Listen in exakt der selben Reihenfolge angegebens werden.</li>
	 * </ol>
	 * Tritt ein Punkt dabei in zwei Dreiecken auf, muss er auch in beiden neu genannt werden - es kann nicht einfach auf die erste Nennung verwiesen werden.<br><br>
	 * 
	 * @param positions Float-Array mit den Eckpunkt-positionen. Sollte in der Form {x1,y1,x2,y2,x3,y3 ... } übergeben werden.
	 * @param textureCoords Float-Array mit den Textur-positionen der Eckpunkte. Sollte in der Form {x1,y1,x2,y2,x3,y3 ... } übergeben werden.
	 */
	public Vao(float[] positions, float[] textureCoords) {
		length = positions.length/2;
		id = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(id);
		positionsVboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,positionsVboID);
		GL30.glVertexAttribPointer(0,2,GL11.GL_FLOAT,false,0,0);
		GL30.glEnableVertexAttribArray(0);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER,positions,GL15.GL_STATIC_DRAW);
		
		textureCoordsVboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,textureCoordsVboID);
		GL30.glVertexAttribPointer(1,2,GL11.GL_FLOAT,false,0,0);
		GL30.glEnableVertexAttribArray(1);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER,textureCoords,GL15.GL_STATIC_DRAW);
	}
	
	/**
	 *  Erstellt ein Rechteck-Vao aus den gegebenen Abmessungen und Texturkoordinaten.
	 *  
	 * @param x1 kleinste x-Position
	 * @param y1 kleinste y-Position
	 * @param x2 größte x-Position
	 * @param y2 größte y-Position
	 * @param tx1 Texturkoordinate zu x1
	 * @param ty1 Texturkoordinate zu y1
	 * @param tx2 Texturkoordinate zu x2
	 * @param ty2 Texturkoordinate zu y2
	 */
	public Vao(float x1, float y1, float x2, float y2, float tx1, float ty1, float tx2, float ty2) {
		this(new float[]{x1,y1,x1,y2,x2,y1,x1,y2,x2,y2,x2,y1},new float[]{tx1,ty1,tx1,ty2,tx2,ty1,tx1,ty2,tx2,ty2,tx2,ty1});
	}
	
	/**
	 * Erstellt ein neues Rechteck-Vao mit den gegebenen Seitenlängen.<br>
	 * Die Texturkoordinaten erstrecken sich automatisch von 0 bis 1.
	 * 
	 * @param width
	 * @param height
	 */
	public Vao(int width, int height) {
		this(0,0,width,height,0,0,1,1);
	}
	
	/**
	 * Bindet das VAO an den OpenGL-Kontext.
	 */
	public void bind() {
		GL30.glBindVertexArray(id);
	}
	
	/**
	 * Rendert das VAO.<br>
	 * <b>Wichtig:</b> Dazu muss das VAO bereits an den OpenGL-Kontext gebunden sein. Ruft vorher am besten {@link #bind()} auf, um sicherzugehen.
	 */
	public void render() {
		GL30.glDrawArrays(GL11.GL_TRIANGLES,0,length);
	}
	
	/**
	 * Löscht das VAO, um den in der Grafikkarte belegten Speicher wieder freizugeben.
	 */
	public void destroy() {
		GL30.glDeleteVertexArrays(id);
		GL15.glDeleteBuffers(positionsVboID);
		GL15.glDeleteBuffers(textureCoordsVboID);
	}
	
}
