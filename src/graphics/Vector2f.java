package graphics;

/**
 * Hilfsklasse für 2D-Vektoren
 */
public class Vector2f {
	
	public float x;
	public float y;
	
	/**
	 * Erstellt einen neuen Vektor mit den entsprechenden Werten
	 * 
	 * @param x x-Wert
	 * @param y y-Wert
	 */
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Erstellt einen neuen Vektor mit den Werten (0|0)
	 */
	public Vector2f() {
		this(0,0);
	}
	
}
