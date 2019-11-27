package graphics.raymarcher;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import gamelogic.World;
import graphics.core.Framebuffer;
import graphics.core.Texture;
import graphics.core.Vao;
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
	
	private Framebuffer framebuffer;
	private Texture texture;
	
	private boolean useCaveEffect;
	private boolean useAcidEffect;
	
	/**
	 * Erstellt einen neuen RayMarcher-Renderer.
	 */
	public RayMarcher(boolean useCaveEffect, boolean useAcidEffect) {
		this.useCaveEffect = useCaveEffect;
		this.useAcidEffect = useAcidEffect;
		// erstellt den entsprechenden Shader
		shader = new RayMarcherShader(useCaveEffect,useAcidEffect);
		shader.start();

		// Erstellt ein neues VAO mit den Eckpunkten eines Rechtecks
		vao = new Vao(new float[] {-1,-1,-1,1,1,-1,-1,1,1,1,1,-1},new float[12]);
		
		// erstellt einen neuen Framebuffer, in den gerendert werden kann
		framebuffer = new Framebuffer();
		texture = new Texture();
		framebuffer.attachTexture(texture, GL30.GL_COLOR_ATTACHMENT0);

		// Setzt die Hintergrundfarbe auf Magenta
		GL11.glClearColor(1,0,1,0);
	}
	
	/**
	 * Rendert die gesamte Szene
	 * 
	 * @param snake die zu rendernde Schlange
	 * @param width Breite in Pixeln
	 * @param height Höhe in Pixeln
	 * @return Framebuffer mit dem Rendering-Ergebnis
	 */
	public Framebuffer render(World world, int width, int height) {
		// Setzt den zu rendernden Bereich (bei Fenstergrößenänderungen wichtig)
		texture.resize(width,height);
		GL11.glViewport(0,0,width,height);
		framebuffer.bind();
		
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
		
		return framebuffer;
	}
	
	/**
	 * Aktiviert den "Cave Effect"
	 */
	public void enableCaveEffect() {
		useCaveEffect = true;
		reloadShader();
	}
	
	/**
	 * Deaktiviert den "Cave Effect"
	 */
	public void disableCaveEffect() {
		useCaveEffect = false;
		reloadShader();
	}
	
	/**
	 * Aktiviert den "Acid Effect"
	 */
	public void enableAcidEffect() {
		useAcidEffect = true;
		reloadShader();
	}
	
	/**
	 * Deaktiviert den "Acid Effect"
	 */
	public void disableAcidEffect() {
		useAcidEffect = false;
		reloadShader();
	}
	
	/**
	 * Lädt und kompiliert den Shader neu, um Grafikeffekte anzuwenden
	 */
	private void reloadShader() {
		shader.destroy();
		shader = new RayMarcherShader(useCaveEffect,useAcidEffect);
	}
	
	/**
	 * Löscht den Renderer und gibt genutzte Ressourcen frei.
	 */
	public void destroy() {
		shader.destroy();
		vao.destroy();
		framebuffer.destroy();
		texture.destroy();
	}
	
}
