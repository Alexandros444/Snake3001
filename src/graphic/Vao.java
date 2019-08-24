package graphic;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

/**
 * 
 * @author Ben
 */

public class Vao {
	
	public final int id;
	private final int positionsVboID;
	private final int length;
	
	/**
	 * Erstellt ein neues Vertex Array Object, das dann später gerendert werden kann.
	 * 
	 * @param positions Float-Array mit den Eckpunkt-positionen. Sollte in der Form {x1,y1,x2,y2,x3,y3 ... } übergeben werden. 
	 */
	
	public Vao(float[] positions) {
		length = positions.length;
		id = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(id);
		positionsVboID = GL30.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,positionsVboID);
		GL30.glVertexAttribPointer(0,2,GL11.GL_FLOAT,false,0,0);
		GL30.glEnableVertexAttribArray(0);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER,positions,GL15.GL_STATIC_DRAW);
	}
	
	/**
	 * Rendert das Vertex Array Object.
	 */
	
	public void render() {
		GL30.glDrawArrays(GL11.GL_TRIANGLES,0,length);
	}
	
	/**
	 * Löscht das Vertex Array Object, um wieder Speicher im GPU freizugeben.
	 */
	
	public void destroy() {
		GL30.glDeleteVertexArrays(id);
		GL15.glDeleteBuffers(positionsVboID);
	}
	
}
