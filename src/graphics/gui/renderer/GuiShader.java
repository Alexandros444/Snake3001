package graphics.gui.renderer;

import graphics.core.Shader;
import util.math.Matrix3f;
import util.math.Vector2f;

public class GuiShader extends Shader {
	
	private static final String VERTEX_FILE = "graphics/gui/renderer/vertexShader.txt";
	private static final String FRAGMENT_FILE = "graphics/gui/renderer/fragmentShader.txt";
	
	private int screenSizeUniformID;
	private int transformationMatrixUniformID;
	private int transparencyUniformID;
	
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
	 * Lädt die IDs der verwendeten Uniforms.
	 */
	protected void getUniformLocations() {
		screenSizeUniformID = super.getUniformLocation("screenSize");
		transformationMatrixUniformID = super.getUniformLocation("transformationMatrix");
		transparencyUniformID = super.getUniformLocation("transparency");
	}
	
	/**
	 * Lädt die Größe des Gui-Koordinatensystems in den Shader.
	 * <br><br>
	 * Die Größe wird dort genutzt, um die im Gui-System genutzten Pixel-Koordinaten in OpenGL-Koordinaten (von -1 bis 1) umzurechnen.
	 * 
	 * @param width die Breite des Pixel-Koordinatensystems
	 * @param height die Höhe des Pixel-Koordinatensystems
	 */
	public void loadScreenSize(int width, int height) {
		super.loadVector2f(screenSizeUniformID,new Vector2f(width,height));
	}
	
	/**
	 * Lädt die Transformation, mit der das Objekt gerendert werden soll<br>
	 * In der Matrix sind Position, Rotation, Größe usw. kodiert
	 * 
	 * @param matrix Transformationmatrix
	 */
	public void loadTransformationMatrix(Matrix3f matrix) {
		super.loadMatrix3f(transformationMatrixUniformID,matrix);
	}
	
	public void loadTransparency(float transparency) {
		super.loadFloat(transparencyUniformID, transparency);
	}
	
}
