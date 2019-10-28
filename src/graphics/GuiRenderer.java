package graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import graphics.gui.FpsCounter;
import graphics.gui.engine.ContainerComponent;
import graphics.gui.engine.GuiComponent;
import graphics.gui.engine.MouseEvent;
import graphics.gui.engine.components.BoxComponent;
import graphics.gui.engine.components.ButtonComponent;
import graphics.gui.engine.components.ImageComponent;
import graphics.gui.engine.components.TextComponent;
import graphics.gui.engine.fonts.Font;
import graphics.gui.engine.fonts.MonospaceFont;
import graphics.gui.renderer.GuiShader;
import util.Settings;

/**
 * Der Renderer für das Gui unseres Programms.<br>
 * Enthält bisher nur ein paar einfache Anzeigen als Test, um zu schauen, ob das Gui-Komponentensystem funktioniert
 * 
 * @author Ben
 */
public class GuiRenderer {
	
	private GuiShader shader;
	private GuiComponent crosshairs;
	private TextComponent scoreText, fpsText;
	private ContainerComponent container;
	private Font font;
	
	private ButtonComponent testButton;
	private int testButtonClicks = 0;
	private TextComponent testButtonText;
	
	/**
	 * Erstellt einen neuen Gui-Renderer.
	 */
	public GuiRenderer(Settings settings) {
		shader = new GuiShader();
		font = new MonospaceFont("res/font/ascii.png");
		// erstellt eine neue Gui-Komponente aus dem Bild des Fadenkreuzes
		crosshairs = new ImageComponent("res/"+settings.guiRendererCrosshair+".png");
		// erstellt zwei leere Textkomponenten für Punktzahl und FPS
		scoreText = new TextComponent("", font);
		fpsText = new FpsCounter(font);
		
		// legt die Positionen der Elemente fest
		crosshairs.setPosition(GuiComponent.POSITION_CENTER);
		
		scoreText.setPosition(GuiComponent.POSITION_CORNER_TOPLEFT);
		scoreText.setOffset(24,24);
		scoreText.setScale(3);
		
		fpsText.setPosition(GuiComponent.POSITION_CORNER_TOPRIGHT);
		fpsText.setOffset(6,6);
		fpsText.setScale(2);
		
		// Box zum Testen von Boxkomponenten, unten links in der Ecke
		BoxComponent testBox = new BoxComponent(250,100,0x80000000,0xa0808080,4);
		testBox.setWidthMode(GuiComponent.WIDTH_AUTO);
		testBox.setHeightMode(GuiComponent.HEIGHT_AUTO);
		testBox.setPosition(GuiComponent.POSITION_CORNER_BOTTOMLEFT);
		testBox.setOffset(8,8);
		testBox.setInnerOffset(8,8);
		TextComponent testText1 = new TextComponent("TextBox-Test",font);
		TextComponent testText2 = new TextComponent("Element-Flow-Test",font);
		TextComponent testText3 = new TextComponent("Und kuck mal, automatisch angepasste Breite ^^",font);
		TextComponent testText4 = new TextComponent("(ach ja, und natürlich Höhe)",font);
		TextComponent testText5 = new TextComponent("Und Umlaute gehen jetzt auch =D",font);
		TextComponent testText6 = new TextComponent("Jetzt fehlt bloß noch mehrzeiliger Text...",font);
		TextComponent testText7 = new TextComponent("TestButton:",font);
		testButton = new ButtonComponent(150,36,"Test",font);
		testButtonText = new TextComponent("Klicks: 0",font);
		testText1.setScale(2);
		testText2.setScale(2);
		testText3.setScale(2);
		testText4.setScale(2);
		testText5.setScale(2);
		testText6.setScale(2);
		testText7.setScale(2);
		testButtonText.setScale(2);
		testText1.setOffset(4,4);
		testText2.setOffset(4,4);
		testText3.setOffset(4,4);
		testText4.setOffset(4,4);
		testText5.setOffset(4,4);
		testText6.setOffset(4,4);
		testText7.setOffset(4,4);
		testButton.setOffset(4,4);
		testButtonText.setOffset(4,4);
		testBox.addComponent(testText1);
		testBox.addComponent(testText2);
		testBox.addComponent(testText3);
		testBox.addComponent(testText4);
		testBox.addComponent(testText5);
		testBox.addComponent(testText6);
		testBox.addComponent(testText7);
		testBox.addComponent(testButton);
		testBox.addComponent(testButtonText);
		
		// erstellt den Container und fügt alle Elemente zu ihm zu
		container = new ContainerComponent(640,480);
		container.addComponent(crosshairs);
		container.addComponent(scoreText);
		container.addComponent(fpsText);
		container.addComponent(testBox);
		
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	/**
	 * Rendert bisher nur ein einfaches Viereck als Test, soll aber später die ganze Gui rendern.
	 * 
	 * @param width Breite in Pixeln
	 * @param height Höhe in Pixeln
	 */
	public void render(int width, int height, MouseEvent mouseEvent) {
		// bindet den Haupt-Framebuffer und bereitet den Gui-Shader vor
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,0);
		GL11.glViewport(0,0,width,height);
		shader.start();
		shader.loadScreenSize(width,height);

		// passt die Größe des Containers an
		container.setSize(width,height);

		// verarbeitet Maus-Events
		container.receiveMouseEvent(true,mouseEvent);
		
		// testet den Test-Button auf Klicks
		if (testButton.wasClicked()) {
			testButtonClicks++;
			testButtonText.setText("Klicks: "+testButtonClicks);
		}
		
		// updated den Container
		container.update();
		
		// rendert den Container mit allen Elementen
		container.render(shader);
	}

	/**
	 * Zeigt die gegebene Punktzahl als Text an
	 * 
	 * @param score Punktzahl
	 */
	public void displayScore(int score) {
		scoreText.setText("Score: "+score);
	}
	
	/**
	 * Löscht den Renderer und gibt genutzte Ressourcen frei.
	 */
	public void destroy() {
		shader.destroy();
		crosshairs.destroy();
		scoreText.destroy();
		font.destroy();
	}
	
}
