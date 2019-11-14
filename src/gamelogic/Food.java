package gamelogic;

import util.math.Matrix3f;
import util.math.Vector3f;

/**
 * Die Klasse für das Essen was die Schlange wachsen lässt.<br>
 * 
 *  @author Jakopo
 */

public class Food {
	
	public static final float BASE_RADIUS = 0.025f;
	
	public Vector3f position;
	public float radius;
	public Matrix3f rotation;
	
	/**
	 * Erstellt eine neues Essen
	 */
	public Food() {
		this.radius = -BASE_RADIUS;
		//setzt das Essen auf eine zufällige Position
		float a = (float) (Math.random()-0.5);
		float b = (float) (Math.random()-0.5);
		float c = (float) (Math.random()-0.5);
			
		position = new Vector3f(a,b,c);
			
		rotation = new Matrix3f();
		rotation.rotate(0, 45, 45);
	}
	
	/**
	 * Gibt Distanz zwischen dem Mittepunkt des Essens und einer beliebigen Position zurück
	 *
	 * @param a beliebige Position
	 * @return Distanz von Korn zu a 
	 * 
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
	*/	
	/**
	 * Updated das Essen
	 * @param deltaTime Zeit seit letztem Frame
	 */ 
	public void update(float deltaTime) {
		radius += (BASE_RADIUS-radius)*(1-Math.pow(0.998f,deltaTime*(1e-6f)));
		rotation.rotate(1.5f * ((deltaTime)*(1e-7f)), 1.5f * ((deltaTime)*(1e-7f)), 0);
	}
}
