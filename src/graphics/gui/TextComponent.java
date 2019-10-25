package graphics.gui;

import graphics.Matrix3f;
import graphics.Vao;
import graphics.guiRenderer.GuiShader;

/**
 * Klasse für Text-Komponenten
 * 
 * @author Alex
 */
public class TextComponent extends GuiComponent {

	private Vao vao;

	private String currentText;
	private int scale = 1;

	private Font font;
	
	/**
	 * Konstruktor eines Textkomponeneten
	 * 
	 * @param text Der anzuzeigende Text
	 * @param font Die Schriftart des Textes
	 */
	public TextComponent(String text, Font font) {
		// ruft den GuiComponent-Konstruktor auf
		super(0,0);
		super.setPosition(POSITION_FLOW);

		// Setzt die private Variable Font = des Parameters Font
		this.font = font;

		// lädt den Text
		this.setText(text);

	}

	/**
	 * Setzt den Text der Komponente
	 * 
	 * @param text Text
	 */
	public void setText(String text) {
		// prüft, ob der Text überhaupt geändert werden muss
		if (!text.equals(this.currentText)) {
			this.currentText = text;
			// löscht altes Vao
			if (vao!=null) {
				vao.destroy();
			}
			// erstellt neues Vao
			vao = createTextVao(text, font);
			// passt die eigene Größe an
			refreshSize();
		}
	}

	/**
	 * Setzt die Schriftgröße bzw. Skalierung
	 * 
	 * @param scale Faktor
	 */
	public void setScale(int scale) {
		this.scale = scale;
		Matrix3f transform = new Matrix3f();
		transform.scale(scale);
		transform.m22 = 1;
		super.setInnerTransform(transform);
		refreshSize();
	}

	/**
	 * Passt die Größe des Elements an die Größe des Inhalts an.
	 */
	private void refreshSize() {
		super.setSize(6*currentText.length()*scale,8*scale);
	}

	/**
	 * Rendert den Text
	 */
	public void render(GuiShader shader) {
		shader.loadTransformationMatrix(super.getTotalTransform());
		font.getTexture().bind();
		vao.bind();
		vao.render();
	}

	/**
	 * Löscht das Vao, um Speicher freizugeben
	 */
	public void destroy() {
		vao.destroy();
	}

	/**
	 * Erstellt aus dem gegebenen Text ein Vao
	 * 
	 * @param text anzuzeigender Text
	 * @param font zu nutzende Schriftart
	 * @return Vao Daten für den Renderer
	 */
	private static Vao createTextVao(String text, Font font) {
		float[] positions = new float[text.length()*12];
		float[] textures = new float[text.length()*12];

		// Position Textur zuweisen
		int index = 0;
		int x = 0;
		int y = 0;
		for (int i=0;i<text.length();i++) {
			// Ascii-Code vom Zeichen
			char asciiCode = text.charAt(i);
			// obere linke Ecke des Buchstabens in der Textur
			float texX = font.getCharX(asciiCode);
			float texY = font.getCharY(asciiCode);
			// TEXTUREN
			// Links-Oben
			textures[index] = texX;
			textures[index+1] = texY;
			// Links-Unten
			textures[index+2] = texX;
			textures[index+3] = texY+font.getCharOffY(asciiCode);
			// Rechts-Oben
			textures[index+4] = texX+font.getCharOffX(asciiCode);
			textures[index+5] = texY;
			// Rechts-Oben
			textures[index+6] = texX+font.getCharOffX(asciiCode);
			textures[index+7] = texY;
			// Rechts-Unten
			textures[index+8] = texX+font.getCharOffX(asciiCode);
			textures[index+9] = texY+font.getCharOffY(asciiCode);
			// Links-Unten
			textures[index+10] = texX;
			textures[index+11] = texY+font.getCharOffY(asciiCode);

			// POSITIONEN
			// Links-Oben
			positions[index] = x;
			positions[index+1] = y;
			// Links-Unten
			positions[index+2] = x;
			positions[index+3] = y+font.getCharHeight(asciiCode);
			// Rechts-Oben
			positions[index+4] = x+font.getCharWidth(asciiCode);
			positions[index+5] = y;
			// Rechts-Oben
			positions[index+6] = x+font.getCharWidth(asciiCode);
			positions[index+7] = y;
			// Rechts-Unten
			positions[index+8] = x+font.getCharWidth(asciiCode);
			positions[index+9] = y+font.getCharHeight(asciiCode);
			// Links-Unten
			positions[index+10] = x;
			positions[index+11] = y+font.getCharHeight(asciiCode);
			// Abstand zwischen Zeichen
			x += font.getCharWidth(asciiCode)+1; 
			// Inkrementiert Pointer
			index += 12;
		}
		// gibt das Voa, erstellt aus Daten von Positions/Textures, zurück
		return new Vao(positions,textures);
	}

}
