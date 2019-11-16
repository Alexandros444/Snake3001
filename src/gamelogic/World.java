package gamelogic;

import org.lwjgl.glfw.GLFW;

import graphics.core.Display;
import util.Settings;
import util.math.Matrix3f;
import util.math.Vector3f;

/**
 * Enthält alle Informationen zur Spielwelt, also die Schlange, das Essen, das Gitter usw.
 * 
 * @author Alexander
 */
public class World {
	
	public static final int MODE_NORMAL = 0;
	public static final int MODE_FAST = 1;
	public static final int MODE_TUNNEL = 2;
	
	private static final float SPEED_NORMAL = 0.005f;
	private static final float SPEED_FAST = 0.008f;
	
	private static final float GRID_WIDTH_NORMAL = 0.04f;
	private static final float GRID_WIDTH_TUNNEL = 0.6f;
	
	private int gameMode = MODE_NORMAL;
	
	public Snake snake;
	public Food food;
	public int score;
	
	public boolean isPaused;
	public boolean hasSnake;
	
	public Vector3f cameraPosition;
	public Matrix3f viewDirection;

	private float rotationSpeed = 0.75f;
	private float movementSpeed = SPEED_NORMAL;
	public float gridWidth = GRID_WIDTH_NORMAL;
	
	private long lastFrame;
	
	private Settings settings;
	
	/**
	 * Initialisiert die Welt, mit ihren Koponenten
	 * 
	 * @param settings zu ladende Einstellungen
	 */
	public World(Settings settings) {
		this.settings = settings;

		reset();
		isPaused = false;
		
	    lastFrame = System.nanoTime();
	}
	
	public void setGameMode(int gameMode) {
		if (this.gameMode!=gameMode) {
			this.gameMode = gameMode;
			
			if (gameMode==MODE_FAST) {
				movementSpeed = SPEED_FAST;
			}else {
				movementSpeed = SPEED_NORMAL;
			}
			if (gameMode==MODE_TUNNEL) {
				gridWidth = GRID_WIDTH_TUNNEL;
			}else {
				gridWidth = GRID_WIDTH_NORMAL;
			}
		}
	}
	
	/**
	 * Updatet die Welt und ihre Koponenten
	 * 
	 * @param display Display auf dem das Spiel läuft
	 */
	public void update(Display display) {
		float deltaTime = System.nanoTime()-lastFrame;
		lastFrame = System.nanoTime();
		
		if(display.isKeyPressed(GLFW.GLFW_KEY_G))placeFood();
		
		if(!(hasSnake&&!snake.isAlive)&&!isPaused) {
			if (hasSnake&&snake.isAlive) {
				if (display.isKeyPressed(GLFW.GLFW_KEY_W) || display.isKeyPressed(GLFW.GLFW_KEY_UP) ) {
					viewDirection.rotate(-rotationSpeed* (deltaTime*(1e-7f)), 0, 0);
				}
				if (display.isKeyPressed(GLFW.GLFW_KEY_S) || display.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
					viewDirection.rotate(rotationSpeed* (deltaTime*(1e-7f)), 0, 0);
				}
				if (display.isKeyPressed(GLFW.GLFW_KEY_A) || display.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
					viewDirection.rotate(0, -rotationSpeed* (deltaTime*(1e-7f)), 0);
				}
				if (display.isKeyPressed(GLFW.GLFW_KEY_D) || display.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
					viewDirection.rotate(0, rotationSpeed* (deltaTime*(1e-7f)), 0);
				}
				if (display.isKeyPressed(GLFW.GLFW_KEY_Q)) {
					viewDirection.rotate(0, 0, rotationSpeed* (deltaTime*(1e-7f)));
				}
				if (display.isKeyPressed(GLFW.GLFW_KEY_E)) {
					viewDirection.rotate(0, 0, -rotationSpeed* (deltaTime*(1e-7f)));
				}
			}
			//wenn Leertaste gedrückt dann stop
			if (!display.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
				// Setzt den BewegungsVektor zurück
				Vector3f movement = new Vector3f(0,0,movementSpeed);
				// Bestimmt Geschwindigkeit pro Frame
				movement.scale(deltaTime*(1e-7f));
				// dreht den BewegungsVektor durch die SichtMatrix
				movement.apply(viewDirection);
				// addiert den BewegungsVektor zum Kamera-Positions-Vektor 
				cameraPosition.add(movement);
			}
			food.update(deltaTime);
			if (hasSnake) {
				snake.update(cameraPosition,deltaTime);
				checkFoodCollision();
				checkDeathCollision();
			}
		}
	}
	/**
	 * Überprüft ob die Schlange mit Food kollidiert
	 */
	private void checkFoodCollision() {
		if(sphereDistance(snake.snakePositions[0], food.position)<snake.sphereRadius) {   
			score += 1;
			System.out.println("Punktzahl: "+score+", Länge: "+snake.snakePositions.length);
			// Erweitert Schlangenlänge um 1
			snake.addSphere();
			// platziert das Korn neu
			placeFood();
		}
	}

	/**
	 * Überprüft ob die Schlange mit sich kollidiert
	 */
	private void checkDeathCollision() {
		if(gridDistance(snake.snakePositions[0])-0.8f*snake.sphereRadius<0) {
			killSnake();
		}
		for(int i=2;i<snake.snakePositions.length;i++) {
			if(sphereDistance(snake.snakePositions[0],snake.snakePositions[i])<1.9*snake.sphereRadius) {
				killSnake();			
				break;
			}
		}		
	}
	/**
	 * Platziert ein neues Futterkorn
	 */
	private void placeFood() {
		boolean goodPosition = false;
		//solange keine gute Position gefunden wurde soll das Korn woanders erscheinen
		while(!goodPosition) {
			food = new Food();
			goodPosition = true;
			
			//Kontrolle ob Korn im Gitter landet
			if(gridDistance(food.position)<2*Food.BASE_RADIUS) {
				goodPosition = false;
			}
			if(hasSnake) {
				//Kontrolle ob Korn in der Schlange landet
				for(int i=0;i<snake.snakePositions.length;i++) {
					if(sphereDistance(snake.snakePositions[i], food.position)<snake.sphereRadius) {
						goodPosition = false;
					}
				}
			}
		}
	}
	/**
	 * Gibt die Distanz des gegebenen Punktes zum Gitter zurück.
	 * 
	 * @param p Ortsvektor
	 * @return Distanz zum Gitter
	 */
	private float gridDistance(Vector3f p) {
		float x = Math.max(0,Math.abs(Math.abs(p.x)%1-0.5f)-gridWidth/2);
		float y = Math.max(0,Math.abs(Math.abs(p.y)%1-0.5f)-gridWidth/2);
		float z = Math.max(0,Math.abs(Math.abs(p.z)%1-0.5f)-gridWidth/2);
		return (float)Math.min(Math.sqrt(x*x+y*y),Math.min(Math.sqrt(y*y+z*z),Math.sqrt(z*z+x*x)));
	}
	
	/**
	 * Setzt alles zurück, erstellt eine leere Welt ohne Schlange
	 */
	public void reset() {
	    viewDirection = new Matrix3f();
	    cameraPosition = new Vector3f(0,0,0.5f);
	    
		score = 0;
		snake = null;
		hasSnake = false;
		
		placeFood();
	}
	
	/**
	 * Spawnt die Schlange
	 */
	public void spawnSnake(){
		snake = new Snake();
		hasSnake = true;
	}
	
	/**
	 * Speichert den Score und setzt den Status der Schlange auf tot
	 */
	public void killSnake() {
		if(settings.snakeScore<score) {
			settings.snakeScore = score;
		}
		snake.isAlive=false;
		System.out.println("Du bist gestorben");
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
		temp.x = ((temp.x+0.5f)%1+1)%1-0.5f;
		temp.y = ((temp.y+0.5f)%1+1)%1-0.5f;
		temp.z = ((temp.z+0.5f)%1+1)%1-0.5f;
		// gibt die Distanz zwischen den Mittelpunkten minus die Radien zurück
		return temp.getLength();
	}
	
	public void pause() {
		isPaused = true;
	}
	
	public void unpause() {
		isPaused = false;
		lastFrame = System.nanoTime();
	}
}