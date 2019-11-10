package gamelogic;

import util.math.Vector3f;

/**
 * Die Klasse für die Schlange.<br>
 * 
 *  @author Jakopo
 */
public class Snake {

	private static final int MAX_LENGTH = 64;
	
	public boolean isAlive;
	public Vector3f[] snakePositions;

	public float sphereRadius = -0.1f;
	
	/**
	 * Erstellt eine neue Schlange
	 */
	public Snake(){
		
	    snakePositions = new Vector3f[5];
	    
	    //Startposition der Kugeln des SchlangenSchwanzes
	     for (int l = 0; l < snakePositions.length; l++) {
			 snakePositions[l] = new Vector3f(); 
		}
	     
	     isAlive = true;
	}

	/**
	 * Updated und bewegt die Schlange
	 * 
	 * @param display Das Display, von dem aus Tastendrücke eingelesen werden sollen
	 */
	
	public void update(Vector3f cameraPosition, float deltaTime) {
		sphereRadius += (0.05f-sphereRadius)*(1-Math.pow(0.999f,deltaTime*(1e-6f)));
		// bewegt die Schlange
		updateSnakePositions(cameraPosition); 
	}
	
	/**
	 * Fügt eine Kugel zur Schlange hinzu, es sei denn die Schlange hat bereits ihre Maximallänge erreicht
	 */
	public void addSphere() {
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
	 * Updated die Positionen des Körpers der Schlange
	 */
	private void updateSnakePositions(Vector3f cameraPosition) {
		float minDistance = 2*Math.max(sphereRadius,0.01f);
		snakePositions[0] = cameraPosition.copy();
		for (int i=1;i< snakePositions.length;i++){
		    Vector3f delta =  snakePositions[i-1].copy();
			Vector3f temp =  snakePositions[i].copy();
			temp.scale(-1f);
		    delta.add(temp);
		    if (delta.getLength()>minDistance){
			    delta.setLength(delta.getLength()-minDistance);
			    snakePositions[i].add(delta);
		    }
		}
	}
	
}