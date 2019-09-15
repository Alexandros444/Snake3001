package gamelogic;

import org.lwjgl.glfw.GLFW;

import graphics.Display;
import graphics.Matrix3f;
import graphics.Vector3f;

public class Snake {
	// Deklarieren der Variablen
	public Vector3f cameraPosition, movement;
	public Matrix3f viewDirection;

	public Vector3f[] arrayVecsSnake;
	
	private float rotationSpeed = 2f;
	private float movementSpeed = 0.01f;	
	
	// Konstruktor, Initialisiert die Variablen
	public Snake(){
	    cameraPosition = new Vector3f();
	    viewDirection = new Matrix3f();
	    movement = new Vector3f();
	    arrayVecsSnake = new Vector3f[18];
	    
	    //Startposition der Kugeln des SchlangenSchwanzes
		for (int i = 0; i < arrayVecsSnake.length; i++) {
			arrayVecsSnake[i] = cameraPosition.copy();
		}
	}
	// Updated die Bewegung der Schlange
	public void update(Display display) {
		// dreht die Sichtmatrix je nach Tasteninput und lädt sie in den Shader

		// dreht Sichtmatrix nach oben					
		if (display.isKeyPressed(GLFW.GLFW_KEY_W) || display.isKeyPressed(GLFW.GLFW_KEY_UP) ) {
			viewDirection.rotate(-rotationSpeed, 0, 0);
		}
		// dreht Sichtmatrix nach unten
		if (display.isKeyPressed(GLFW.GLFW_KEY_S) || display.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
			viewDirection.rotate(rotationSpeed, 0, 0);
		}
		// dreht Sichtmatrix nach links
		if (display.isKeyPressed(GLFW.GLFW_KEY_A) || display.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
			viewDirection.rotate(0, -rotationSpeed, 0);
		}
		// Dreht Sichtmatrix nach rechts 
		if (display.isKeyPressed(GLFW.GLFW_KEY_D) || display.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
			viewDirection.rotate(0, rotationSpeed, 0);
		}
		// Rotiert Sichtmatrix nach links
		if (display.isKeyPressed(GLFW.GLFW_KEY_Q)) {
			viewDirection.rotate(0, 0, rotationSpeed);
		}
		// Rotiert Sichtmatrix nach rechts
		if (display.isKeyPressed(GLFW.GLFW_KEY_E)) {
			viewDirection.rotate(0, 0, -rotationSpeed);
		}
		//wenn Leertaste gedrückt dann stop
		if (!display.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
			// Setzt den BewegungsVektor zurück
			movement.x = 0;
			movement.y = 0;
			movement.z = movementSpeed;
			// dreht den BewegungsVektor durch die SichtMatrix
			movement.apply(viewDirection);
			// addiert den BewegungsVektor zum Kamera-Positions-Vektor 
			cameraPosition.add(movement);
		}
		// ruft die Methode zum Updaten der Positionen auf
		updateSnakePositions();
	}

	// updated die Vektoren für die Kugeln des SchlangenSchwanzes
	private void updateSnakePositions() {
		arrayVecsSnake[0] = cameraPosition.copy();
		for (int i=1;i<arrayVecsSnake.length;i++){
		    Vector3f delta = arrayVecsSnake[i-1].copy();
			Vector3f temp = arrayVecsSnake[i].copy();
			temp.scale(-1f);
		    delta.add(temp);
		    delta.setLength(delta.getLength()-0.1f);
		    arrayVecsSnake[i].add(delta);
		}
	}
	
	
}