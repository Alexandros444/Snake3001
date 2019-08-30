package graphics;

/**
 * 
 * @Author Jakopo
 *
 */

public class Matrix3f {
	private float m00, m01, m02, m10, m11, m12, m20, m21, m22;
	
	/**
	 * erstellt eine neue Identitätsmatrix
	 */
	public Matrix3f() {
		m00 = 1;
		m11 = 1;
		m22 = 1;
	}
/**
 * setzt die Matrix auf eine Rotation um die angegebene Achse um "rotation" Grad
 * @param rotation Winkel in Grad
 */
	public void setRotationMatrixX(float rotation) {
		m00 = 1;
		m01 = 0;
		m02 = 0;
		m10 = 0;
		m11 = (float) Math.cos(Math.toRadians(rotation));
		m12 = (float) Math.sin(Math.toRadians(rotation));
		m20 = 0;
		m21 = -(float) Math.sin(Math.toRadians(rotation));
		m22 = (float) Math.cos(Math.toRadians(rotation));
	}

	public void setRotationMatrixY(float rotation) {
		m00 = (float) Math.cos(Math.toRadians(rotation));
		m01 = 0;
		m02 = -(float) Math.sin(Math.toRadians(rotation));
		m10 = 0;
		m11 = 1;
		m12 = 0;
		m20 = (float) Math.sin(Math.toRadians(rotation));
		m21 = 0;
		m22 = (float) Math.cos(Math.toRadians(rotation));
	}

	public void setRotationMatrixZ(float rotation) {
		m00 = (float) Math.cos(Math.toRadians(rotation));
		m01 = (float) Math.sin(Math.toRadians(rotation));
		m02 = 0;
		m10 = (float) Math.sin(Math.toRadians(rotation));
		m11 = (float) Math.cos(Math.toRadians(rotation));
		m12 = 0;
		m20 = 0;
		m21 = 0;
		m22 = 1;
	}
/**
 * kopiert die Matrix 
 * @return Kopie der Matrix
 */
	public Matrix3f copy() {
		Matrix3f matrix = new Matrix3f();
		matrix.m00 = this.m00;
		matrix.m01 = this.m01;
		matrix.m02 = this.m02;
		matrix.m10 = this.m10;
		matrix.m11 = this.m11;
		matrix.m12 = this.m12;
		matrix.m20 = this.m20;
		matrix.m21 = this.m21;
		matrix.m22 = this.m22;
		return matrix;
	}
/**
 * multipliziert die Matrix 
 * @param B => andere Matrix
 */
	public void multiply(Matrix3f B) {
		Matrix3f A = this.copy();
		m00 = A.m00 * B.m00 + A.m10 * B.m01 + A.m20 * B.m02;
		m10 = A.m00 * B.m10 + A.m10 * B.m11 + A.m20 * B.m12;
		m20 = A.m00 * B.m20 + A.m10 * B.m21 + A.m20 * B.m22;
		m01 = A.m01 * B.m00 + A.m11 * B.m01 + A.m21 * B.m02;
		m11 = A.m01 * B.m10 + A.m11 * B.m11 + A.m21 * B.m12;
		m21 = A.m01 * B.m20 + A.m11 * B.m21 + A.m21 * B.m22;
		m02 = A.m02 * B.m00 + A.m12 * B.m01 + A.m22 * B.m02;
		m12 = A.m02 * B.m10 + A.m12 * B.m11 + A.m22 * B.m12;
		m22 = A.m02 * B.m20 + A.m12 * B.m21 + A.m22 * B.m22;

	}
/**
 * rotier die Matrix um die drei Variablen in x, y und z Richtung
 * @param rx Rotation in x Richtung in Grad
 * @param ry Rotation in y Richtung in Grad
 * @param rz Rotation in z Richtung in Grad
 */
	public void rotate(float rx, float ry, float rz) {
		Matrix3f rotationMatrixX = new Matrix3f();
		Matrix3f rotationMatrixY = new Matrix3f();
		Matrix3f rotationMatrixZ = new Matrix3f();

		rotationMatrixX.setRotationMatrixX(rx);
		rotationMatrixY.setRotationMatrixY(ry);
		rotationMatrixZ.setRotationMatrixZ(rz);

		this.multiply(rotationMatrixX);
		this.multiply(rotationMatrixY);
		this.multiply(rotationMatrixZ);
	}
/**
 * Matrix als Liste ausgeben
 * @return Liste
 */
	public float[] toFloatArray() {
		return new float[] { m00, m01, m02, m10, m11, m12, m20, m21, m22 };

	}

}
