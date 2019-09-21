package gamelogic;

import graphics.Matrix3f;
import graphics.Vector3f;

/**
 * Die Klasse für das Essen was die Schlange wachsen lässt.<br>
 * 
 *  @author Jakopo
 */

public class Food {
	
	public static final float BASE_RADIUS = 0.025f;
	
	public Vector3f foodPosition;
	public float radius;
	public Matrix3f foodRotation;
	
	/**
	 * Erstellt eine neues Essen
	 */
	
		public Food() {
			this.radius = 0;
			//setzt das Essen auf eine zufällige Position
			float a = (float) (Math.random()-0.5);
			float b = (float) (Math.random()-0.5);
			float c = (float) (Math.random()-0.5);
			
			foodPosition = new Vector3f(a,b,c);
			
			foodRotation = new Matrix3f();
			foodRotation.rotate(0, 45, 45);
		}
		
		/**
		 *gibt Distanz zwischen dem Mittepunkt des Essens 
		 * und einer beliebigen Position zurück
		 *
		 */ 
		
		public float distanceTo(Vector3f a) {
			Vector3f temp = a.copy();
			temp.scale(-1);
			temp.add(foodPosition);
			//sorgt für Kollision mit Essen aus anderen Kästen
			temp.x = ((temp.x+0.5f)%1+1)%1-0.5f;
			temp.y = ((temp.y+0.5f)%1+1)%1-0.5f;
			temp.z = ((temp.z+0.5f)%1+1)%1-0.5f;
			//gibt Distanz zwischen Kopf und essen zurück
			return temp.getLength();
		}
		
		/**
		 * upated das Essen
		 */ 
		public void update(float deltaTime) {
			radius += (BASE_RADIUS-radius)/10;
			foodRotation.rotate(1.5f * ((deltaTime)*(1e-7f)), 1.5f * ((deltaTime)*(1e-7f)), 0);
		}
}
