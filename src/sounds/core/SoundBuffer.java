package sounds.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;

import util.StaticUtils;

/**
 * Die Klasse für OpenAL-Audio-Buffer.<br>
 * Kann Audiodateien speichern und später über SoundSources wiedergeben.
 * <br><br>
 * In großen Teilen adaptiert von <a href="https://lwjglgamedev.gitbooks.io/3d-game-development-with-lwjgl/content/chapter22/chapter22.html">lwjglgamedev.gitbooks.io</a>.
 * 
 * @author Ben
 */
public class SoundBuffer {
	
	public final int id;
	
	/**
	 * Lädt einen neuen SoundBuffer aus einer .ogg-Audiodatei
	 * 
	 * @param path Pfad relativ zu <code>res/</code>
	 */
	public SoundBuffer(String path) {
		id = AL10.alGenBuffers();
		STBVorbisInfo info = STBVorbisInfo.malloc();
		try {
			ShortBuffer pcm = readVorbis(path,info);
			AL10.alBufferData(id,(info.channels()==1?AL10.AL_FORMAT_MONO16:AL10.AL_FORMAT_STEREO16),pcm,info.sample_rate());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Löscht den SoundBuffer, um Speicher freizugeben
	 */
	public void destroy() {
		AL10.alDeleteBuffers(id);
	}
	
	/**
	 * Lädt einen ShortBuffer mit Sample-Werten aus einer .ogg-Audiodatei.
	 * 
	 * @param path Pfad relativ zu <code>res/</code>
	 * @param info Info-Objekt, in das die Dateiinformationen kopiert werden sollen
	 * @return ShortBuffer mit Sample-Werten
	 * @throws IOException falls die Datei nicht geladen werden konnte
	 */
	private static ShortBuffer readVorbis(String path, STBVorbisInfo info) throws IOException {
		ByteBuffer vorbis = StaticUtils.ioResourceToByteBuffer(path);
		IntBuffer error = BufferUtils.createIntBuffer(1);
		long decoder = STBVorbis.stb_vorbis_open_memory(vorbis,error,null);
		if (decoder==0) {
			throw new IOException("Failed to open Ogg file. Error code: "+error.get(0));
		}
		STBVorbis.stb_vorbis_get_info(decoder,info);
		int channels = info.channels();
		ShortBuffer pcm = BufferUtils.createShortBuffer(STBVorbis.stb_vorbis_stream_length_in_samples(decoder)*channels);
		STBVorbis.stb_vorbis_get_samples_short_interleaved(decoder,channels,pcm);
		STBVorbis.stb_vorbis_close(decoder);
		return pcm;
	}
	
}
