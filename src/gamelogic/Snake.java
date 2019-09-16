package gamelogic;

import org.lwjgl.glfw.GLFW;

import graphics.Display;
import graphics.Matrix3f;
import graphics.Vector3f;

public class Snake {
	// Deklarieren der Variablen
	public Vector3f cameraPosition, movement;
	public Matrix3f viewDirection;
	public boolean isAlive = true;

	public Vector3f[]  snakePositions;
	
	private float rotationSpeed = 2f;
	private float movementSpeed = 0.01f;

	
	// Konstruktor, Initialisiert die Variablen
	public Snake(){
	    cameraPosition = new Vector3f(0,0,10f);  
	    viewDirection = new Matrix3f();
	    movement = new Vector3f();
	     snakePositions = new Vector3f[18];
	    
	    //Startposition der Kugeln des SchlangenSchwanzes
		for (int i = 0; i <  snakePositions.length; i++) {
			 snakePositions[i] = new Vector3f(); 
		}
	}

	// Updated die Bewegung der Schlange
	public void update(Display display) {
		if(isAlive==true) {
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
			if (isAlive==true && !display.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
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
			
			//checkt für jede Kugel ob man kollidiert
			for(int i=2;i<snakePositions.length;i++) {
				if(sphereDistance(snakePositions[0],snakePositions[i])<0) {
					isAlive=false;
				}
			}
		}
	}

	// updated die Vektoren für die Kugeln des SchlangenSchwanzes
	private void updateSnakePositions() {
		 snakePositions[0] = cameraPosition.copy();
		for (int i=1;i< snakePositions.length;i++){
		    Vector3f delta =  snakePositions[i-1].copy();
			Vector3f temp =  snakePositions[i].copy();
			temp.scale(-1f);
		    delta.add(temp);
		    delta.setLength(delta.getLength()-0.1f);
		     snakePositions[i].add(delta);
		}
		
	}
	//Kollisionsbedingung
	private float sphereDistance(Vector3f a, Vector3f b) { 
		Vector3f temp = a.copy();
		temp.scale(-1);
		temp.add(b);
		return temp.getLength()-0.1f;
	}
	
	
}