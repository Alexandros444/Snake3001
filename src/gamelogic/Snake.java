package gamelogic;

import org.lwjgl.glfw.GLFW;

import graphics.Display;
import graphics.Matrix3f;
import graphics.Vector3f;

public class Snake {
	
	public Vector3f cameraPosition, movement;
	public Matrix3f viewDirection;
	public boolean isAlive = true;

	public Vector3f[]  snakePositions;
	
	private float rotationSpeed = 2f;
	private float movementSpeed = 0.01f;
	private float sphereRadius = 0.05f;

	
	/**
	 * Erstellt eine neue Schlange
	 */
	public Snake(){
	    cameraPosition = new Vector3f(0,0,0.5f);  
	    viewDirection = new Matrix3f();
	    movement = new Vector3f();
	     snakePositions = new Vector3f[32];
	    
	    //Startposition der Kugeln des SchlangenSchwanzes
		for (int i = 0; i <  snakePositions.length; i++) {
			 snakePositions[i] = new Vector3f(); 
		}
	}

	/**
	 * Updated und bewegt die Schlange
	 * 
	 * @param display Das Display, von dem aus Tastendrücke eingelesen werden sollen
	 */
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
			
			//checkt für jede Kugel ob man kollidiert
			for(int i=2;i<snakePositions.length;i++) {
				if(sphereDistance(snakePositions[0],snakePositions[i])<0) {
					isAlive=false;
				}
			}
		}
	}
	
	/**
	 * Updated die Positionen des Körpers der Schlange
	 */
	private void updateSnakePositions() {
		 snakePositions[0] = cameraPosition.copy();
		for (int i=1;i< snakePositions.length;i++){
		    Vector3f delta =  snakePositions[i-1].copy();
			Vector3f temp =  snakePositions[i].copy();
			temp.scale(-1f);
		    delta.add(temp);
		    if (delta.getLength()>2*sphereRadius){
			    delta.setLength(delta.getLength()-2*sphereRadius);
			    snakePositions[i].add(delta);
		    }
		}
		
	}
	
	/**
	 * Gibt die Distanz zwischen zwei Kugeln der Schlange zurück.
	 * 
	 * @param a Ortsvektor der ersten Kugel
	 * @param b OrtsVektor der zweiten Kugel
	 * @return Distanz zwischen den Kugeln
	 */
	private float sphereDistance(Vector3f a, Vector3f b) { 
		// setzt  temp auf den Gegenvektor von a und addiert dann b
		// temp ist dann b-a
		Vector3f temp = a.copy();
		temp.scale(-1);
		temp.add(b);
		// bringt alle Werte mit Modulo in den Bereich von -0.5f bis 0.5f
		// sorgt so für Kollision mit Schlangen aus anderen Kästen
		temp.x = (temp.x+10.5f)%1-0.5f;
		temp.y = (temp.y+10.5f)%1-0.5f;
		temp.z = (temp.z+10.5f)%1-0.5f;
		// gibt die Distanz zwischen den Mittelpunkten minus die Radien zurück
		return temp.getLength()-2*sphereRadius;
	}
	
	
}