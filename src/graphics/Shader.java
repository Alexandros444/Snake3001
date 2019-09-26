package graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

/**
 * Basis-Klasse für OpenGL-Shader.<br>
 * Shader werden genutzt, um dem GPU zu sagen wie genau verschiedene Objekte gerendert werden sollen - deshalb brauchen wir zum Beispiel verschiedene Shader fürs GUI, das Spiel selber, und eventuell für Post-Processing-Effekte, falls wir mal welche einführen.
 * <br><br>
 * Diese Klasse hier enthält wichtige Basismethoden, die von den Shadern dann benutzt werden können - die Shader sollen also alle diese Klasse hier erweitern.
 * Sie können dann mit z.B. <code>super.xyz()</code> auf die Methoden dieser Klasse zugreifen.
 * <br><br>
 * Shader bestehen jeweils aus einem Vertex- und einem Fragment-Shader.
 * Der Vertex-Shader wird einmal pro Eckpunkt jedes gerenderten Modells ausgeführt und bestimmt, wo dieser Punkt gerendert werden soll.
 * Der Fragment-Shader wird anschließend einmal pro Pixel der entstehenden Dreiecke ausgeführt und bestimmt, welche Farbe dieser Punkt erhalten soll.
 * 
 * @author Ben
 */

public abstract class Shader {
	
	private final int programID;
	private final int vertexShaderID;
	private final int fragmentShaderID;
	
	/**
	 * Erstellt einen neuen Shader aus den übergebenen Vertex- und Fragment-Shader-Dateien.
	 * <br>
	 * Ruft intern <code>bindAttributes</code> der Kind-Klasse auf, um die Vertex-Buffer der zu rendernden Modelle den richtigen Vertex-Shader-Inputs zuzuweisen.
	 * @param vertexFile Dateipfad des Vertex-Shaders
	 * @param fragmentFile Dateipfad des Fragment-Shaders
	 */
	public Shader(String vertexFile, String fragmentFile) {
		vertexShaderID = loadShader(vertexFile,GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile,GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID,vertexShaderID);
		GL20.glAttachShader(programID,fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getUniformLocations();
	}
	
	/**
	 * Bindet den Shader an den OpenGL-Kontext, sodass spätere OpenGL-Funktionsaufrufe sich auf diesen Shader beziehen.
	 * <br><br>
	 * Der Shader wird dann u.A. zum Rendern benutzt, bis {@link #stop()} aufgerufen wird oder ein anderer Shader gebunden wird.
	 */
	public void start() {
		GL20.glUseProgram(programID);
	}
	
	/**
	 * Löst den Shader vom OpenGL-Kontext los.<br>
	 * Er wird dann nicht mehr zum rendern benutzt bis er wieder mit {@link #start()} an den Kontext gebunden wird.
	 */
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	/**
	 * Muss von Kind-Klassen implementiert werden und wird automatisch im Konstruktor aufgerufen.<br>
	 * Hier werden die einzelnen VAO-Attribute mit den Inputs des Vertex-Shaders verlinkt,
	 * d.h. es kann zum Beispiel festgelegt werden, dass die Daten im Array <code>0</code> des VAOs dem Shader als "<code>position</code>" übergeben werden.
	 * <br><br>
	 * Diese Festlegung erfolgt am besten über Aufrufe der Funktion {@link #bindAttribute(int attribute, String variableName)}.
	 */
	protected abstract void bindAttributes();
	
	/**
	 * Verlinkt eine Liste in VAOs mit einem Input des Vertex-Shaders.
	 * 
	 * @param attribute Listenindex
	 * @param variableName Name des Vertex-Shader-Inputs.
	 */
	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	/**
	 * Muss von Kind-Klassen implementiert werden und wird automatisch im Konstruktor aufgerufen.<br>
	 * Hier werden die einzelnen Uniform-IDs geladen, sodass mit diesen später Werte in die Uniforms geladen werden können.
	 * <br><br>
	 * Um die Uniform-IDs zu erhalten, sollte am besten {@link #getUniformLocation(String)} verwendet werden.
	 */
	protected abstract void getUniformLocations();
	
	/**
	 * Gibt die ID des entsprechenden Uniforms zurück.
	 * <br><br>
	 * Die ID kann genutzt werden, um Werte in den Uniform zu laden.
	 * @param name der im Shader verwendete Name des Uniforms
	 * @return die ID des Uniforms
	 */
	protected int getUniformLocation(String name) {
		return GL20.glGetUniformLocation(programID,name);
	}
	
	/**
	 * Lädt einen Vektor in einen Uniform.
	 * 
	 * @param location die ID des Uniforms
	 * @param value der zu ladende Vektor
	 */
	protected void loadVector3f(int location, Vector3f value) {
		GL20.glUniform3f(location,value.x,value.y,value.z);
	}
	
	/**
	 * Lädt eine Matrix in einen Uniform.
	 * 
	 * @param location die ID des Uniforms
	 * @param matrix die zu ladende Matrix
	 */
	protected void loadMatrix3f(int location, Matrix3f matrix) {
		GL20.glUniformMatrix3fv(location,false,matrix.toFloatArray());
	}
	
	/**
	 * Lädt einen Float in einen Uniform.
	 * 
	 * @param location die ID des Uniforms
	 * @param value der zu ladende Float
	 */
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location,value);
	}
	
	/**
	 * Lädt einen Integer in einen Uniform.
	 * 
	 * @param location die ID des Uniforms
	 * @param value der zu ladende Integer
	 */
	protected void loadInt(int location, int value) {
		GL20.glUniform1i(location,value);
	}
	
	/**
	 * Lädt ein Array aus Vektoren in einen Uniform.
	 * 
	 * @param location die ID des Uniforms
	 * @param vectors die zu ladenden Vektoren
	 */
	protected void loadVector3fArray(int location, Vector3f[] vectors) {
		float[] floatArray = new float[vectors.length*3];
		for (int i=0;i<vectors.length;i++) {
			floatArray[i*3] = vectors[i].x;
			floatArray[i*3+1] = vectors[i].y;
			floatArray[i*3+2] = vectors[i].z;
		}
		GL20.glUniform3fv(location,floatArray);
	}
	
	/**
	 * Löscht den Shader, um wieder Arbeitsspeicher im GPU freizugeben.
	 */
	public void destroy() {
		stop();
		GL20.glDetachShader(programID,vertexShaderID);
		GL20.glDetachShader(programID,fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	/**
	 * Lädt einen Vertex- oder Fragment-Shader aus einen Datei.
	 * 
	 * @param fileName Dateifad des Shaders relativ zum src-Ordner.
	 * @param type Der Typ des Shaders. Entweder <code>GL20.GL_VERTEX_SHADER</code> oder <code>GL20.GL_FRAGMENT_SHADER</code>.
	 * @return Die Id des geladenen Shaders.
	 */
	private static int loadShader(String fileName, int type) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine())!=null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		}catch(IOException e) {
			System.err.println("Shader `"+fileName+"` konnte nicht gefunden werden!");
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID,shaderSource);
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShaderi(shaderID,GL20.GL_COMPILE_STATUS)==GL11.GL_FALSE) {
			System.err.println("Shader `"+fileName+"` konnte nicht kompiliert werden!");
			System.out.println(GL20.glGetShaderInfoLog(shaderID,500));
			System.exit(-1);
		}
		return shaderID;
	}
	
}
