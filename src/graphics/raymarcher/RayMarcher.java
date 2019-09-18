package graphics.raymarcher;

import org.lwjgl.opengl.GL11;

import gamelogic.Snake;
import graphics.Vao;

/**
 * Der 3D-Renderer unseres Programms, basierend auf RayMarching.<br>
 * Enthält Code zum Rendern von Schlange, Futter und Gitter.
 * 
 * @author Ben
 */
public class RayMarcher {
	
	public RayMarcherShader shader;
	public Vao vao;
	
	/**
	 * Erstellt einen neuen RayMarcher-Renderer.
	 */
	public RayMarcher() {
		// erstellt den entsprechenden Shader
		shader = new RayMarcherShader();
		shader.start();

		// Erstellt ein neues VAO mit den Eckpunkten eines Rechtecks
		vao = new Vao(new float[] {-1,-1,-1,1,1,-1,-1,1,1,1,1,-1});
		
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
		GL11.glViewport(0,0,width,height);
		
		// Setzt den Inhalt des Fensters auf die Hintergrundfarbe zurück
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		// lädt alle Infos in den Shader
		shader.loadViewMatrix(snake.viewDirection);
		shader.loadPosition(snake.cameraPosition);
		shader.loadSnake(snake.snakePositions);
		shader.loadFoodPosition(snake.food.foodPosition);
		shader.loadFoodRadius(snake.food.radius);
		
		// lädt das aktuelle Seitenverhältnis des Fensters in den Shader
		float ratio = (float)width/height;
		shader.loadScreenRatio(ratio);

		// Rendert das Viereck
		vao.render();
	}
	
	/**
	 * Löscht den Renderer und gibt genutzte Ressourcen frei.
	 */
	public void destroy() {
		shader.destroy();
		vao.destroy();
	}
	
}
