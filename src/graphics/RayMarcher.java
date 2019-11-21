package graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import gamelogic.World;
import graphics.core.Texture;
import graphics.core.Vao;
import graphics.raymarcher.RayMarcherShader;
import util.math.Vector3f;

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
	
	private boolean useCaveEffect = false;
	
	/**
	 * Erstellt einen neuen RayMarcher-Renderer.
	 */
	public RayMarcher() {
		// erstellt den entsprechenden Shader
		shader = new RayMarcherShader(useCaveEffect);
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
	public void render(World world, int width, int height) {
		
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
		shader.loadViewMatrix(world.viewDirection);
		shader.loadPosition(world.cameraPosition);
		if(world.hasSnake) {
			shader.loadSnake(world.snake.snakePositions);
			shader.loadSnakeSphereRadius(world.snake.sphereRadius);
		}else {
			shader.loadSnake(new Vector3f[0]);
			shader.loadSnakeSphereRadius(-0.05f);
		}
		if(world.hasSecondSnake) {
			shader.loadSecondSnake(world.secondSnake.snakePositions);
			shader.loadSecondSnakeSphereRadius(world.secondSnake.sphereRadius);
		}else {
			shader.loadSecondSnake(new Vector3f[0]);
			shader.loadSecondSnakeSphereRadius(-0.05f);
		}
		shader.loadFoodPosition(world.food.position);
		shader.loadFoodRadius(world.food.radius);
		shader.loadFoodRotation(world.food.rotation);
		shader.loadGridWidth(world.gridWidth);
		
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
	 * Aktiviert den "Cave Effect"
	 */
	public void enableCaveEffect() {
		useCaveEffect = true;
		reloadShader();
	}
	
	/**
	 * Aktiviert den "Cave Effect"
	 */
	public void disableCaveEffect() {
		useCaveEffect = false;
		reloadShader();
	}
	
	/**
	 * Lädt und kompiliert den Shader neu, um Grafikeffekte anzuwenden
	 */
	private void reloadShader() {
		shader.destroy();
		shader = new RayMarcherShader(useCaveEffect);
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
