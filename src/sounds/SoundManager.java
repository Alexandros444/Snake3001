package sounds;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;

import sounds.core.SoundBuffer;
import sounds.core.SoundListener;
import sounds.core.SoundSource;

/**
 * Klasse für den Umgang mit Klängen und Musik.<br>
 * Erstellt bisher einen OpenAL-Kontext, und gibt auf diesem dann endlos <code>Snake_Test.ogg<code> wieder.
 * @author Ben
 */
public class SoundManager {
	
	private long device;
	private long context;
	
	private SoundListener listener;
	private SoundBuffer musicBuffer;
	private SoundSource musicSource;
	
	/**
	 * Erstellt einen neuen OpenAL-Kontext und startet die Musikwiedergabe.
	 */
	public SoundManager() {
		device = ALC10.alcOpenDevice((ByteBuffer)null);
		if (device==0) {
			throw new RuntimeException("Failed to open the default OpenAL device.");
		}
		ALCCapabilities deviceCapabilities = ALC.createCapabilities(device);
		context = ALC10.alcCreateContext(device,(IntBuffer)null);
		if (context==0) {
			throw new RuntimeException("Failed to create OpenAL context.");
		}
		ALC10.alcMakeContextCurrent(context);
		AL.createCapabilities(deviceCapabilities);
		
		listener = new SoundListener();
		musicBuffer = new SoundBuffer("res/Snake_Test.ogg");
		musicSource = new SoundSource(true,false);
		musicSource.setBuffer(musicBuffer);
		musicSource.play();
	}
	
	/**
	 * Gibt den Audio-Listener zurück
	 * @reurn Audio-Listener
	 */
	public SoundListener getAudioListener() {
		return listener;
	}
	
	/**
	 * Gibt die Audio-Quelle der Spielmusik zurück
	 * @return Audio-Quelle der Spielmusik
	 */
	public SoundSource getMusicSource() {
		return musicSource;
	}
	
	/**
	 * Löscht alles wieder, um genutzte Ressourcen freizugeben.
	 */
	public void destroy() {
		ALC10.alcDestroyContext(context);
		ALC10.alcCloseDevice(device);
	}
	
}
