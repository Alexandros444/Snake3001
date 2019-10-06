package graphics.gui;

import graphics.Texture;
import graphics.Vao;

/*
 * Klasse für Text-Komponenten
 * 
 * @author Alex
 */
public class TextComponent extends GuiComponent{

	private Vao vao;
	private final Texture fontTexture;
	
	public TextComponent(String text) {
		// ruft den GuiComponent-Konstruktor auf
		super(0,0);
		
		// lädt das Bild und passt eigene Größe an
		fontTexture = new Texture("res/font/ascii.png");
		int width = 2*fontTexture.getWidth();
		int height = 2*fontTexture.getHeight();
		super.setSize(width,height);
		
		setText(text);
		
	}

	/*
	 * Setzt den Text
	 * 
	 * @param 
	 */
	private static Vao createTextVao(String text) {
		float[] positions = new float[text.length()*12];
		float[] textures = new float[text.length()*12];
		
		// Position Textur zuweisen
		int index = 0;
		int xPxPos = 0;
		int yPxPos = 0;
			for (int textChar = 0; textChar < text.length(); textChar++) {
						// TEXTUREN
					// 0,0 der Textur
					float xPos = (text.charAt(textChar)%16)/16f;
					float yPos = (float) Math.floor(text.charAt(textChar)/16f)/16f-(1/16);
					// Links-Oben
					textures[index]= xPos;
					textures[index+1]= yPos;
					// Links-Unten
					textures[index+2]= xPos;
					textures[index+3]= yPos+(1/16f);
					// Rechts-Oben
					textures[index+4]= xPos+(1/16f);
					textures[index+5]= yPos;
					// Rechts-Oben
					textures[index+6]= xPos+(1/16f);
					textures[index+7]= yPos;
					// Rechts-Unten
					textures[index+8]= xPos+(1/16f);
					textures[index+9]= yPos+(1/16f);
					// Links-Unten
					textures[index+10]= xPos;
					textures[index+11]= yPos+(1/16f);
					
						// POSITIONEN
					// Links-Oben
					positions[index]=xPxPos;
					positions[index+1]=yPxPos;
					// Links-Unten
					positions[index+2]=xPxPos;
					positions[index+3]=yPxPos+8;
					// Rechts-Oben
					positions[index+4]=xPxPos+8;
					positions[index+5]=yPxPos;
					// Rechts-Oben
					positions[index+6]=xPxPos+8;
					positions[index+7]=yPxPos;
					// Rechts-Unten
					positions[index+8]=xPxPos+8;
					positions[index+9]=yPxPos+8;
					// Links-Unten
					positions[index+10]=xPxPos;
					positions[index+11]=yPxPos+8;
					// Verschieben, abstand zwischen Buchstaben
					xPxPos+=6;
					// Inkrementiert Pointer
					index+=12;
			}
			return new Vao(positions,textures);
	}
	
	
	public void setText(String text) {
		if(vao!=null)vao.destroy();
		vao = createTextVao(text);
	}
	
	/**
	 * Rendert das Bild
	 */
	public void render() {
		fontTexture.bind();
		vao.bind();
		vao.render();
	}
	
	/**
	 * Löscht das Bild, um Speicher freizugeben
	 */
	public void destroy() {
		vao.destroy();
		fontTexture.destroy();
	}


}
