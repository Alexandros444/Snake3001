package sounds.core;

import org.lwjgl.openal.AL10;

/**
 * Die Klasse für OpenAL-Audio-Quellen.<br>
 * Kann genutzt werden, um Klänge aus einem {@link SoundBuffer} wiederzugeben.
 * @author Ben
 */
public class SoundSource {
	
	public final int id;
	
	/**
	 * Erstellt eine neuen Audio-Quelle.
	 * @param loop ob sich der Ton wiederholen soll
	 * @param relative ob die Position relativ ist
	 */
	public SoundSource(boolean loop, boolean relative) {
		id = AL10.alGenSources();
		if (loop) {
			AL10.alSourcei(id,AL10.AL_LOOPING,AL10.AL_TRUE);
		}
		if (relative) {
			AL10.alSourcei(id,AL10.AL_SOURCE_RELATIVE,AL10.AL_TRUE);
		}
	}
	
	/**
	 * Ordnet der Quelle einen Buffer zu, der wiedergegeben werden soll.
	 * @param buffer der Buffer
	 */
	public void setBuffer(SoundBuffer buffer) {
		AL10.alSourcei(id,AL10.AL_BUFFER,buffer.id);
	}
	
	/**
	 * Startet die Wiedergabe des Klanges
	 */
	public void play() {
		AL10.alSourcePlay(id);
	}
	
	/**
	 * Pausiert die Wiedergabe des Klanges
	 */
	public void pause() {
		AL10.alSourcePause(id);
	}
	
	/**
	 * Stoppt die Wiedergabe des Klanges
	 */
	public void stop() {
		AL10.alSourceStop(id);
	}
	
	/**
	 * Löscht die Quelle, um Ressourcen freizugeben
	 */
	public void destroy() {
		AL10.alDeleteSources(id);
	}
	
}
