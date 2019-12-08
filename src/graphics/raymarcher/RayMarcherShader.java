package graphics.raymarcher;

import gamelogic.Snake;
import graphics.core.Shader;
import util.math.Matrix3f;
import util.math.Vector3f;

/**
 * Der Shader zum Rendern in 3D, mithilfe von RayMarching.<br>
 * Der eigentliche Rendering-Prozess findet im Fragmentshader statt, wo aus den Koordinaten jedes Pixels jeweils die Farbe bestimmt wird - diese Klasse enthält bloß Code, um den Shader zu initialisieren und Werte in ihn zu laden.
 * 
 * @author Ben
 */

public class RayMarcherShader extends Shader{
	
	private static final String VERTEX_FILE = "graphics/raymarcher/vertexShader.txt";
	private static final String FRAGMENT_FILE = "graphics/raymarcher/fragmentShader.txt";
	
	private int viewMatrixUniformID;
	private int positionUniformID;
	private int screenRatioUniformID;
	private int snakePositionsUniformID;
	private int snakeLengthUniformID;
	private int snakeRadiusUniformID;
	private int snakeColorUniformID;
	private int foodPositionUniformID;
	private int foodRadiusUniformID;
	private int foodRotationUniformID;
	private int gridWidthUniformID;
	private int secondSnakePositionsUniformID;
	private int secondSnakeLengthUniformID;
	private int secondSnakeRadiusUniformID;
	private int secondSnakeColorUniformID;
	private int fovUniformID;
	private int reflectivityUniformID;
	
	/**
	 * Lädt den Shader.
	 * <br><br>
	 * Er muss anschließend noch mit <code>.start()</code> an den OpenGL-Kontext gebunden werden, damit er benutzt werden kann.
	 */
	public RayMarcherShader(boolean useCaveEffect, boolean useAcidEffect) {
		super(VERTEX_FILE,FRAGMENT_FILE,(useCaveEffect?"#define EFFECT_CAVE\n":"")+(useAcidEffect?"#define EFFECT_ACID\n":""));
	}
	
	/**
	 * Legt die Inputs für den Vertex-Shader fest.
	 */
	protected void bindAttributes() {
		super.bindAttribute(0,"position");
	}
	
	/**
	 * Lädt die IDs der verwendeten Uniforms.
	 */
	protected void getUniformLocations() {
		viewMatrixUniformID = super.getUniformLocation("viewDirection");
		positionUniformID = super.getUniformLocation("cameraPosition");
		screenRatioUniformID = super.getUniformLocation("screenRatio");
		snakePositionsUniformID = super.getUniformLocation("snakePositions");
		snakeLengthUniformID = super.getUniformLocation("snakeLength");
		snakeRadiusUniformID = super.getUniformLocation("snakeSphereRadius");
		snakeColorUniformID = super.getUniformLocation("snakeColor");
		foodPositionUniformID = super.getUniformLocation("foodPosition");
		foodRadiusUniformID = super.getUniformLocation("foodRadius");
		foodRotationUniformID = super.getUniformLocation("foodRotation");
		gridWidthUniformID = super.getUniformLocation("gridWidth");
		secondSnakePositionsUniformID = super.getUniformLocation("secondSnakePositions");
		secondSnakeLengthUniformID = super.getUniformLocation("secondSnakeLength");
		secondSnakeRadiusUniformID = super.getUniformLocation("secondSnakeSphereRadius");
		secondSnakeColorUniformID = super.getUniformLocation("secondSnakeColor");
		fovUniformID = super.getUniformLocation("fov");
		reflectivityUniformID = super.getUniformLocation("reflectivity");
	}
	
	/**
	 * Setzt des Seitenverhältnis des zu rendernden Bilds
	 * 
	 * @param ratio Seitenverhältnis in Breite/Höhe
	 */
	public void loadScreenRatio(float ratio) {
		super.loadFloat(screenRatioUniformID,ratio);
	}
	
	/**
	 * Lädt die Matrix als Sichtrichtung.
	 * 
	 * @param matrix die Sichtmatrix.
	 */
	public void loadViewMatrix(Matrix3f matrix) {
		super.loadMatrix3f(viewMatrixUniformID,matrix);
	}
	
	/**
	 * Setzt die Position der Kamera auf den übergebenen Ortsvektor.
	 * 
	 * @param position Ortsvektor
	 */
	public void loadPosition(Vector3f position) {
		super.loadVector3f(positionUniformID,position);
	}
	
	/**
	 * Lädt die Schlange als erste Schlange.
	 * 
	 * @param snake die Schlange
	 */
	public void loadSnake(Snake snake) {
		if (snake!=null) {
			super.loadVector3fArray(snakePositionsUniformID,snake.snakePositions);
			super.loadInt(snakeLengthUniformID,snake.snakePositions.length);
			super.loadFloat(snakeRadiusUniformID,snake.sphereRadius);
			super.loadVector3f(snakeColorUniformID,snake.color);
		}else {
			super.loadInt(snakeLengthUniformID,0);
			super.loadFloat(snakeRadiusUniformID,-0.05f);
		}
	}
	
	/**
	 * Lädt die Schlange als zweite Schlange.
	 * 
	 * @param snake die Schlange
	 */
	public void loadSecondSnake(Snake snake) {
		if (snake!=null) {
			super.loadVector3fArray(secondSnakePositionsUniformID,snake.snakePositions);
			super.loadInt(secondSnakeLengthUniformID,snake.snakePositions.length);
			super.loadFloat(secondSnakeRadiusUniformID,snake.sphereRadius);
			super.loadVector3f(secondSnakeColorUniformID,snake.color);
		}else {
			super.loadInt(secondSnakeLengthUniformID,0);
			super.loadFloat(secondSnakeRadiusUniformID,-0.05f);
		}
	}
	
	/**
	 * Lädt die Position des Essens.
	 * 
	 * @param position Ortsvektor
	 */
	public void loadFoodPosition(Vector3f position) {
		super.loadVector3f(foodPositionUniformID,position);
	}
	
	/**
	 * Lädt die Größe des Essens.
	 * 
	 * @param radius Radius
	 */
	public void loadFoodRadius(float radius) {
		super.loadFloat(foodRadiusUniformID, radius);
	}
	
	/**
	 * Lädt die Matrix zum Drehen des Essens.
	 * 
	 * @param matrix Rotationsmatrix
	 */
	public void loadFoodRotation(Matrix3f matrix) {
		super.loadMatrix3f(foodRotationUniformID, matrix);
	}
	
	/**
	 * Lädt die Dicke des Gitters
	 * 
	 * @param gridWidth Dicke der Gitterstäbe
	 */
	public void loadGridWidth(float gridWidth) {
		super.loadFloat(gridWidthUniformID,gridWidth);
	}
	
	/**
	 * Lädt das Field of View
	 * 
	 * @param fov FOV Skalar
	 */
	public void loadFOV(float fov) {
		super.loadFloat(fovUniformID,fov);
	}
	
	/**
	 * Lädt die Stärke der Reflektionen
	 * 
	 * @param reflectivity Stärke der Reflektionen
	 */
	public void loadReflectivity(float reflectivity) {
		super.loadFloat(reflectivityUniformID,reflectivity);
	}
}

