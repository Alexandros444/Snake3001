package graphics.raymarcher;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import gamelogic.Snake;
import graphics.Texture;
import graphics.Vao;

/**
 * Der 3D-Renderer unseres Programms, basierend auf RayMarching.<br>
 * Enthält Code zum Rendern von Schlange, Futter und Gitter.
 * 
 * @author Ben
 */
public class RayMarcher {
	
	private RayMarcherShader shader;
	private Vao vao;
	
	private int framebufferID;
	private Texture texture;

	private int pixelSize = 1;
	
	/**
	 * Erstellt einen neuen RayMarcher-Renderer.
	 */
	public RayMarcher() {
		// erstellt den entsprechenden Shader
		shader = new RayMarcherShader();
		shader.start();

		// Erstellt ein neues VAO mit den Eckpunkten eines Rechtecks
		vao = new Vao(new float[] {-1,-1,-1,1,1,-1,-1,1,1,1,1,-1},new float[12]);
		
		// erstellt einen neuen Framebuffer, in den gerendert werden kann
		framebufferID = GL30.glGenFramebuffers();
		texture = new Texture();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,framebufferID);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER,GL30.GL_COLOR_ATTACHMENT0,texture.id,0);

		// Setzt die Hintergrundfarbe auf Magenta
		GL11.glClearColor(1,0,1,0);
	}
	
	/**
	 * Rendert die gesamte Szene
	 * 
	 * @param snake die zu rendernde Schlange
	 * @param width Breite in Pixeln
	 * @param height Höhe in Pixeln
	 */
	public void render(Snake snake, int width, int height) {
		
		// Setzt den zu rendernden Bereich (bei Fenstergrößenänderungen wichtig)
		int innerWidth = width/pixelSize+1;
		int innerHeight = height/pixelSize+1;
		texture.resize(innerWidth,innerHeight);
		GL11.glViewport(0,0,innerWidth,innerHeight);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,framebufferID);
		
		// Setzt den Inhalt des Fensters auf die Hintergrundfarbe zurück
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		// lädt alle Infos in den Shader
		shader.start();
		shader.loadViewMatrix(snake.viewDirection);
		shader.loadPosition(snake.cameraPosition);
		shader.loadSnake(snake.snakePositions);
		shader.loadFoodPosition(snake.food.foodPosition);
		shader.loadFoodRadius(snake.food.radius);
		shader.loadFoodRotation(snake.food.foodRotation);
		
		// lädt das aktuelle Seitenverhältnis des Fensters in den Shader
		float ratio = (float)width/height;
		shader.loadScreenRatio(ratio);

		// Rendert das Viereck in den Framebuffer
		
		vao.bind();
		vao.render();
		
		// Kopiert das Ergebnis in den Haupt-Framebuffer
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER,framebufferID);
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER,0);
		GL30.glBlitFramebuffer(0,0,innerWidth,innerHeight,0,0,innerWidth*pixelSize,innerHeight*pixelSize,GL11.GL_COLOR_BUFFER_BIT,GL11.GL_NEAREST);
	}
	
	/**
	 * Setzt die Größe der Pixel, in denen das ganze gerendert werden soll
	 * 
	 * @param size Größe der Pixel in Pixeln :P
	 */
	public void setPixelSize(int size) {
		pixelSize = size;
	}
	
	/**
	 * Löscht den Renderer und gibt genutzte Ressourcen frei.
	 */
	public void destroy() {
		shader.destroy();
		vao.destroy();
		GL30.glDeleteFramebuffers(framebufferID);
		texture.destroy();
	}
	
}
