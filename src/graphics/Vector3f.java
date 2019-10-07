package graphics;

/**
 *
 * Hilfsklasse für andere Klassen, welche mit Vektoren arbeiten
 * 
 * @author Alexander
 */
public class Vector3f {
	// 3 dimensionaler Vektor --> 3 Variablen
	public float x, y, z;

	/**
	 * Erstellt neuen Vektor mit 3 Werten<br>
	 * 
	 * Setzt die Werte eines Vektors auf (x,y,z)
	 * 
	 * @param x x-Wert
	 * @param y y-Wert
	 * @param z z-Wert
	 */
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Erstellt neuen Vektor mit 3 Werten<br>
	 * 
	 * Setzt alle Werte des Vektors auf i
	 * 
	 * @param i Wert für x, y und z
	 */
	public Vector3f(float i) {
		this(i, i, i);
	}

	/**
	 * Erstellt neuen Vektor mit 3 Werten<br>
	 * 
	 * Setzt alle Werte des Vektors auf 0
	 */
	public Vector3f() {
		this(0, 0, 0);
	}

	/**
	 * Multipliziert den Vektor mit einer Zahl
	 * 
	 * @param s Faktor
	 */
	public void scale(float s) {
		x *= s;
		y *= s;
		z *= s;
	}

	/**
	 * Gibt einen neuen Vektor mit identischen Werten zurück
	 * 
	 * @return Vektorkopie
	 */
	public Vector3f copy() {
		return new Vector3f(x, y, z);
	}

	/**
	 * Die länge des Vektors wird zurückgegeben
	 * @return Länge
	 */
	public float getLength() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Setze die Länge des Vektors, wobei die Richtung beibehalten wird
	 * 
	 * @param l neue Länge
	 */
	public void setLength(float l) {
		scale(l / getLength());
	}

	/**
	 * Addiert den Vektor mit dem übergebenem Vektor
	 * 
	 * @param v zu addierender Vektor
	 */
	public void add(Vector3f v) {
		x += v.x;
		y += v.y;
		z += v.z;
	}
	
	/**
	 * Ändert den Vektor durch Multiplikation mit der Matrix.<br>
	 * Kann genutzt werden, um den Vektor "in Richtung der Matrix" zu drehen, bzw. die in der Matrix gespeicherte Drehung auf ihn anzuwenden.
	 * @param matrix die Matrix
	 */
	
	public void apply(Matrix3f matrix) {
		float[] mValues = matrix.toFloatArray();
		float newX = x * mValues[0] + y * mValues[3] + z * mValues[6];
		float newY = x * mValues[1] + y * mValues[4] + z * mValues[7];
		float newZ = x * mValues[2] + y * mValues[5] + z * mValues[8];
		x = newX;
		y = newY;
		z = newZ;
	}

}
