package gamelogic;

import org.lwjgl.glfw.GLFW;

import graphics.Display;
import graphics.Matrix3f;
import graphics.Vector3f;
import gamelogic.Food;

/**
 * Die Klasse für die Schlange.<br>
 * 
 *  @author Jakopo
 */
public class Snake {

	private static final int MAX_LENGTH = 32;
	
	public Vector3f cameraPosition, movement;
	public Matrix3f viewDirection;
	public boolean isAlive = true;
	public Vector3f[]  snakePositions;
	public Food food;
	
	private long lastFrame;
	
	private float rotationSpeed = 0.75f;
	private float movementSpeed = 0.0045f;

	private float sphereRadius = 0.05f;

	
	/**
	 * Erstellt eine neue Schlange
	 */
	public Snake(){
	    cameraPosition = new Vector3f(0,0,0.5f);  
	    viewDirection = new Matrix3f();
	    movement = new Vector3f();
	    snakePositions = new Vector3f[5];
	    food = new Food();
	    
	    //Startposition der Kugeln des SchlangenSchwanzes
	     for (int l = 0; l < snakePositions.length; l++) {
			 snakePositions[l] = new Vector3f(); 
		}
	     
	     lastFrame = System.nanoTime();
	}

	/**
	 * Updated und bewegt die Schlange
	 * 
	 * @param display Das Display, von dem aus Tastendrücke eingelesen werden sollen
	 */
	
	public void update(Display display) {
		// Vergangene Zeit wird berechnet
		float deltaTime = System.nanoTime()-lastFrame;
		lastFrame = System.nanoTime();
		
		// Überprüfen ob die Schlange noch lebt
		if(!isAlive)return;
		
		// STEUERUNG
		// dreht die Sichtmatrix je nach Tasteninput und lädt sie in den Shader
	
		// dreht Sichtmatrix nach oben					
		if (display.isKeyPressed(GLFW.GLFW_KEY_W) || display.isKeyPressed(GLFW.GLFW_KEY_UP) ) {
			viewDirection.rotate(-rotationSpeed* (deltaTime*(1e-7f)), 0, 0);
		}
		// dreht Sichtmatrix nach unten
		if (display.isKeyPressed(GLFW.GLFW_KEY_S) || display.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
			viewDirection.rotate(rotationSpeed* (deltaTime*(1e-7f)), 0, 0);
		}
		// dreht Sichtmatrix nach links
		if (display.isKeyPressed(GLFW.GLFW_KEY_A) || display.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
			viewDirection.rotate(0, -rotationSpeed* (deltaTime*(1e-7f)), 0);
		}
		// Dreht Sichtmatrix nach rechts 
		if (display.isKeyPressed(GLFW.GLFW_KEY_D) || display.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
			viewDirection.rotate(0, rotationSpeed* (deltaTime*(1e-7f)), 0);
		}
		// Rotiert Sichtmatrix nach links
		if (display.isKeyPressed(GLFW.GLFW_KEY_Q)) {
			viewDirection.rotate(0, 0, rotationSpeed* (deltaTime*(1e-7f)));
		}
		// Rotiert Sichtmatrix nach rechts
		if (display.isKeyPressed(GLFW.GLFW_KEY_E)) {
			viewDirection.rotate(0, 0, -rotationSpeed* (deltaTime*(1e-7f)));
		}
		//wenn Leertaste gedrückt dann stop
		if (!display.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
			// Setzt den BewegungsVektor zurück
			movement.x = 0;
			movement.y = 0;
			movement.z = movementSpeed;
			// Bestimmt Geschwindigkeit pro Frame
			movement.z = movementSpeed * (deltaTime*(1e-7f));
			// dreht den BewegungsVektor durch die SichtMatrix
			movement.apply(viewDirection);
			// addiert den BewegungsVektor zum Kamera-Positions-Vektor 
			cameraPosition.add(movement);
			
			food.update(deltaTime);
		}
		
		// bewegt die Schlange
		updateSnakePositions(); 
		
		//checkt für jede Kugel ob man kollidiert
		checkCollision();
		
		//falls Schlangenkopf Essen trifft dann neues Essen erstellen
		if(food.distanceTo(snakePositions[0])<sphereRadius) {   
			System.out.println("Korn gefressen!");
			System.out.println("Sch...Länge "+(snakePositions.length+1) );
			// Erweitert Schlangenlänge um 1
			addSphere();
			// platziert das Korn neu
			placeFood();
		}
	}
	
	/**
	 * Platziert ein neues Futterkorn und verlängert die Schlange
	 */
	private void placeFood() {
		boolean goodPosition = false;
		//solange keine gute Position gefunden wurde soll das Korn woanders erscheinen
		while(!goodPosition) {
			food = new Food();
			goodPosition = true;
			
			//Kontrolle ob Korn im Gitter landet
			if(gridDistance(food.foodPosition)<(1.5 * Food.BASE_RADIUS)) {
					goodPosition = false;
			}
			//Kontrolle ob Korn in der Schlange landet
			for(int i=0;i<snakePositions.length;i++) {
				if(food.distanceTo(snakePositions[i])<sphereRadius) {
					goodPosition = false;
				}
			}
		}
	}

	/**
	 * Fügt eine Kugel zur Schlange hinzu, es sei denn die Schlange hat bereits maximalLänge
	 */
	private void addSphere() {
		if(snakePositions.length<MAX_LENGTH) {
			Vector3f[] temp =  new Vector3f [snakePositions.length+1];
			for(int i = 0;i<snakePositions.length;i++) {
				temp[i] = snakePositions[i];
			}
			temp[snakePositions.length] = snakePositions[snakePositions.length-1].copy();
			snakePositions = temp;
		}
	}
	
	/**
	 * Überprüft ob die Schlange mit sich kollidiert
	 */
	private void checkCollision() {
		if(gridDistance(snakePositions[0])-sphereRadius<0) {
			isAlive=false;
			System.out.println("Du ficker bist gestorben");
		}
		for(int i=2;i<snakePositions.length;i++) {
			if(sphereDistance(snakePositions[0],snakePositions[i])<0) {
				isAlive=false;
				System.out.println("Du ficker bist gestorben");
				break;
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
	
	/**
	 * Gibt die Distanz des gegebenen Punktes zum Gitter zurück.
	 * 
	 * @param p Ortsvektor
	 * @return Distanz zum Gitter
	 */
	private static float gridDistance(Vector3f p) {
		float x = Math.max(0,Math.abs(Math.abs(p.x)%1-0.5f)-0.02f);
		float y = Math.max(0,Math.abs(Math.abs(p.y)%1-0.5f)-0.02f);
		float z = Math.max(0,Math.abs(Math.abs(p.z)%1-0.5f)-0.02f);
		return (float)Math.min(Math.sqrt(x*x+y*y),Math.min(Math.sqrt(y*y+z*z),Math.sqrt(z*z+x*x)));
	}
	
}