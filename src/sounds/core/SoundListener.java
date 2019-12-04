package sounds.core;

import org.lwjgl.openal.AL10;

import util.math.Vector3f;

/**
 * Die Klasse für OpenAL-Listener.<br>
 * Wird genutzt, um festzulegen, wo sich der Spieler befindet und welche Klänge er dadurch wie laut hört.
 * <br><br>
 * Da es in OpenAL immer nur einen Listener gleichzeitig gibt, dient diese Klasse weniger als Listener-Objekt und mehr als Sammlung von Methoden zur Interaktion mit dem OpenAL-Listener - der Einfachheit halber ist sie aber trotzdem objektorientiert geschrieben.
 * 
 * @author Ben
 */
public class SoundListener {
	
	/**
	 * Erstellt einen neuen Listener
	 */
	public SoundListener() {
		this(new Vector3f());
	}
	
	/**
	 * Erstellt einen neuen Listener
	 * @param position Startposition als Ortsvektor
	 */
	public SoundListener(Vector3f position) {
		setPosition(position);
		setSpeed(new Vector3f(0,0,0));
	}
	
	/**
	 * Setzt die Position des Listeners
	 * @param position Ortsvektor
	 */
	public void setPosition(Vector3f position) {
		AL10.alListener3f(AL10.AL_POSITION,position.x,position.y,position.z);
	}
	
	/**
	 * Setzt die Geschwindigkeit des Listeners
	 * @param speed Bewegungsvektor
	 */
	public void setSpeed(Vector3f speed) {
		AL10.alListener3f(AL10.AL_VELOCITY,speed.x,speed.y,speed.z);
	}
	
}
