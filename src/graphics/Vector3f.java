package graphics;

public class Vector3f {
	
	public float x,y,z;
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;	
	}
	
	public Vector3f(float i) {
		x = i;
		y = i;
		z = i;
	}
	
	public Vector3f() {
		x = 0;
		y = 0;
		z = 0;
	}
	
	public void scale(float s) {
		x *= s;
		y *= s;
		z *= s;			
	}
	
	public Vector3f copy() {
		return new Vector3f(x,y,z);
	}

	public float getLength() {
		return (float) Math.sqrt((x*x)+(y*y)+(z*z));
	}
	
	public void setLength(float l) {
		float length = getLength();
		x /= length*l;
		y /= length*l;
		z /= length*l;
	}
	
	public void add(Vector3f v) {
		x += v.x;
		y += v.y;
		z += v.z;	
	}
	
}
