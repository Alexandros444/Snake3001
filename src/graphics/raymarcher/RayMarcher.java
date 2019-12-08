package graphics.raymarcher;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import gamelogic.Snake;
import gamelogic.World;
import graphics.core.Framebuffer;
import graphics.core.Texture;
import graphics.core.Vao;
import util.math.Matrix3f;
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
	
	private float fov;
	private float reflectivity = 0.5f;

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
		framebuffer.attachTexture(texture,GL30.GL_COLOR_ATTACHMENT0);

		// Setzt die Hintergrundfarbe auf Magenta
		GL11.glClearColor(1,0,1,0);
	}
	
	/**
	 * Rendert die gesamte Szene
	 * 
	 * @param width  Bildbreite in Pixeln
	 * @param height Bildhöhe in Pixeln
	 * @param snake1 Schlange, aus deren Perspektive gerendert werden soll
	 * @param snake2 zweite Schlange oder <code>null</code>
	 * @param viewDirection Blickrichtung der ersten Schlange
	 * @param cameraPosition Position des Kopfes der ersten Schlange
	 * @param world Welt für Umgebungsvariablen
	 * @return Framebuffer mit dem Rendering-Ergebnis
	 */
	public Framebuffer render(int width, int height, Snake snake1, Snake snake2, Matrix3f viewDirection, Vector3f cameraPosition, World world, boolean subDivide) {
		// Setzt den zu rendernden Bereich (bei Fenstergrößenänderungen wichtig)
		texture.resize(width,height);
		framebuffer.bind();
		GL11.glViewport(0,0,width,height);
		
		// Setzt den Inhalt des Fensters auf die Hintergrundfarbe zurück
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		// lädt alle Infos in den Shader
		shader.start();
		shader.loadViewMatrix(viewDirection);
		shader.loadPosition(cameraPosition);
		shader.loadSnake(snake1);
		shader.loadSecondSnake(snake2);
		shader.loadFoodPosition(world.food.position);
		shader.loadFoodRadius(world.food.radius);
		shader.loadFoodRotation(world.food.rotation);
		shader.loadGridWidth(world.gridWidth);
		shader.loadFOV(fov);
		shader.loadReflectivity(reflectivity);
		// lädt das aktuelle Seitenverhältnis des Fensters in den Shader
		float ratio = (float)width/height;
		shader.loadScreenRatio(ratio);

		// Rendert das Viereck in den Framebuffer
		if ((!subDivide)||(width*height<500*500)) {
			vao.bind();
			vao.render();
		}else {
			int columns = (int)Math.ceil(width/500f);
			int rows = (int)Math.ceil(height/500f);
			System.out.println("Rendering screenshot! Chunks: "+columns+"x"+rows+", chunk size: "+width/columns+"x"+height/columns);
			int chunk = 0;
			for (int x=0;x<columns;x++) {
				for (int y=0;y<rows;y++) {
					System.out.println("Rendered chunk "+(++chunk));
					float x1 = (((float)x)/columns)*2-1;
					float y1 = (((float)y)/rows)*2-1;
					float x2 = (((float)x+1)/columns)*2-1;
					float y2 = (((float)y+1)/rows)*2-1;
					Vao tempVao = new Vao(new float[] {x1,y1,x1,y2,x2,y1,x1,y2,x2,y2,x2,y1},new float[12]);
					tempVao.bind();
					tempVao.render();
					tempVao.destroy();
				}
			}
		}
		
		return framebuffer;
	}

	/**
	 * Rendert die gesamte Szene in eine Bilddatei
	 * 
	 * @param path Dateipfad relativ zum Projektordner bzw. relativ zu dem Ordner, in dem sich die Jar-Datei befindet
	 * @param width  Bildbreite in Pixeln
	 * @param height Bildhöhe in Pixeln
	 * @param snake1 Schlange, aus deren Perspektive gerendert werden soll
	 * @param snake2 zweite Schlange oder <code>null</code>
	 * @param viewDirection Blickrichtung der ersten Schlange
	 * @param cameraPosition Position des Kopfes der ersten Schlange
	 * @param world Welt für Umgebungsvariablen
	 * @return Framebuffer mit dem Rendering-Ergebnis
	 */
	public void renderToFile(String path, int width, int height, Snake snake1, Snake snake2, Matrix3f viewDirection, Vector3f cameraPosition, World world) {
		render(width*2,height*2,snake1,snake2,viewDirection,cameraPosition,world,true);
		texture.saveAsFile(path);
	}
	
	/**
	 * Setzt das FOV (Field of View)
	 * @param fov Rauszoom-Faktor
	 */
	public void setFOV(float fov) {
		this.fov = fov;
	}
	
	/**
	 * Setzt die Stärke der Reflektionen
	 * @param reflectivity Stärke der Reflektionen
	 */
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
	/**
	 * Lädt den Shader mit den entsprechenden Effekten
	 * 
	 * @param useCaveEffect ob der "Cave Effect" aktiv sein soll
	 * @param useAcidEffect ob der "Acid Effect" aktiv sein soll
	 */
	public void applyEffects(boolean useCaveEffect, boolean useAcidEffect) {
		if (this.useCaveEffect!=useCaveEffect||this.useAcidEffect!=useAcidEffect) {
			this.useCaveEffect = useCaveEffect;
			this.useAcidEffect = useAcidEffect;
			reloadShader();
		}
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
