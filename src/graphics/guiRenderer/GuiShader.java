package graphics.guiRenderer;

import graphics.Shader;

public class GuiShader extends Shader {
	
	private static final String VERTEX_FILE = "graphics/guiRenderer/vertexShader.txt";
	private static final String FRAGMENT_FILE = "graphics/guiRenderer/fragmentShader.txt";

	/**
	 * Lädt den Shader.
	 * <br><br>
	 * Er muss anschließend noch mit <code>.start()</code> an den OpenGL-Kontext gebunden werden, damit er benutzt werden kann.
	 */
	public GuiShader() {
		super(VERTEX_FILE,FRAGMENT_FILE);
	}
	
	/**
	 * Legt die Inputs für den Vertex-Shader fest.
	 */
	protected void bindAttributes() {
		super.bindAttribute(0,"position");
		super.bindAttribute(1,"textureCoords");
	}
	
	/**
	 * Lädt die IDs der verwendeten Uniforms..
	 */
	protected void getUniformLocations() {
		
	}
	
}
