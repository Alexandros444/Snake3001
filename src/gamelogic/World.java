package gamelogic;

import org.lwjgl.glfw.GLFW;

import graphics.core.Display;
import util.Settings;
import util.math.Vector3f;

/**
 * Enthält alle Informationen zur Spielwelt, also die Schlange, das Essen, das Gitter usw.
 * 
 * @author Alexander
 */
public class World {
	
	public Snake snake;
	public Food food;
	public int score;
	
	private long lastFrame;
	
	private Settings settings;
	
	/**
	 * Initialisiert die Welt, mit ihren Koponenten
	 * 
	 * @param settings zu ladende Einstellungen
	 */
	public World(Settings settings) {
		this.settings = settings; 

		snake = new Snake();
		food = new Food();
		
	    lastFrame = System.nanoTime();
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
		
		if(snake.isAlive) {
			snake.update(display);
			food.update(deltaTime);
			checkFoodCollision();
			checkDeathCollision();
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
			deathEvent();
		}
		for(int i=2;i<snake.snakePositions.length;i++) {
			if(sphereDistance(snake.snakePositions[0],snake.snakePositions[i])<1.9*snake.sphereRadius) {
				deathEvent();			
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
			//Kontrolle ob Korn in der Schlange landet
			for(int i=0;i<snake.snakePositions.length;i++) {
				if(sphereDistance(snake.snakePositions[i], food.position)<snake.sphereRadius) {
					goodPosition = false;
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
	private static float gridDistance(Vector3f p) {
		float x = Math.max(0,Math.abs(Math.abs(p.x)%1-0.5f)-0.02f);
		float y = Math.max(0,Math.abs(Math.abs(p.y)%1-0.5f)-0.02f);
		float z = Math.max(0,Math.abs(Math.abs(p.z)%1-0.5f)-0.02f);
		return (float)Math.min(Math.sqrt(x*x+y*y),Math.min(Math.sqrt(y*y+z*z),Math.sqrt(z*z+x*x)));
	}
	
	/*
	 * respawnt die Schlange (setzt Werte der Schlange zurück)  
	 */
	public void respawnSnake(){
		snake = new Snake();
	}
	
	/**
	 * Speichert den Score und setzt den Status der Schlange auf tot
	 */
	public void deathEvent() {
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
		temp.x = (temp.x+10.5f)%1-0.5f;
		temp.y = (temp.y+10.5f)%1-0.5f;
		temp.z = (temp.z+10.5f)%1-0.5f;
		// gibt die Distanz zwischen den Mittelpunkten minus die Radien zurück
		return temp.getLength();
	}
	
}