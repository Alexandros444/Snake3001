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
	 * @param x
	 * @param y
	 * @param z
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
	 * @param i
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
	 * Multipliziert den Vektor mit s
	 * 
	 * @param s
	 */
	public void scale(float s) {
		x *= s;
		y *= s;
		z *= s;
	}

	/**
	 * Gibt einen neuen Vektor mit identischen Werten zurück
	 * 
	 * @return Vector3f Vektorkopie
	 */
	public Vector3f copy() {
		return new Vector3f(x, y, z);
	}

	/**
	 * Die länge des Vektors wird zurückgegeben
	 * 
	 * @return float länge
	 */
	public float getLength() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Setze die Länge des Vektors auf l, wobei die Richtung beibehalten wird
	 * 
	 * @param l
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

	public void apply(Matrix3f matrix) {
		float[] mValues = matrix.toFloatArray();
		float newX = x * mValues[0] + y * mValues[1] + z * mValues[2];
		float newY = x * mValues[3] + y * mValues[4] + z * mValues[5];
		float newZ = x * mValues[6] + y * mValues[7] + z * mValues[8];
		x = newX;
		y = newY;
		z = newZ;
	}

}
