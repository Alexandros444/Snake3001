package graphics.core;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

public class Framebuffer {
	
	private int id;
	
	/**
	 * Erstellt einen neuen Framebuffer und bindet ihn an den OpenGL-Kontext.
	 */
	public Framebuffer() {
		id = GL30.glGenFramebuffers();
		bind();
	}
	
	/**
	 * Bindet den Framebuffer an den OpenGL-Kontext, sodass sich weitere Funktionsaufrufe auf ihn beziehen, bis ein anderer Buffer gebunden wird.
	 */
	public void bind() {
		GL15.glBindBuffer(GL30.GL_FRAMEBUFFER,id);
	}
	
	/**
	 * Bindet den Framebuffer an den OpenGL-Kontext, sodass sich weitere Funktionsaufrufe auf ihn beziehen, bis ein anderer Buffer gebunden wird.
	 * @param target <code>GL30.GL_FRAMEBUFFER</code>, <code>GL30.GL_READ_FRAMEBUFFER</code> oder <code>GL30.GL_DRAW_FRAMEBUFFER</code>
	 */
	public void bind(int target) {
		GL30.glBindBuffer(target,id);
	}
	
	/**
	 * Fügt eine Textur zum Framebuffer hinzu, sodass in diese gerendert werden kann.
	 * @param texture die Textur
	 * @param attachment Anhangspunkt des Framebuffers, an den die Textur angehängt werden soll, wie z.B. <code>GL30.GL_COLOR_ATTACHMENT0</code>
	 */
	public void attachTexture(Texture texture, int attachment) {
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER,attachment,texture.id,0);
	}
	
	/**
	 * Löscht den Framebuffer, um genutzte Ressourcen freizugeben.<br>
	 * Die zugehörigen Texturen werden nicht gelöscht und können weiter verwendet werden.
	 */
	public void destroy() {
		GL30.glDeleteFramebuffers(id);
	}
	
}
