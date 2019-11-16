package graphics.gui.engine.components;

import graphics.gui.engine.fonts.Font;

/**
 * Klasse für einfache Text-Buttons
 * 
 * @author Ben
 */
public class TextButtonComponent extends ButtonComponent {
	
	private TextComponent text;
	
	/**
	 * Erstellt einen neuen Button
	 * 
	 * @param width Breite in Pixeln
	 * @param height Höhe in Pixeln
	 * @param text Beschriftung
	 * @param font Schriftart
	 */
	public TextButtonComponent(int width, int height, String text, Font font) {
		super(width,height,0x80000000,0x4040bfff,0xa0808080,4);
		this.text = new TextComponent(text,font);
		this.text.setPosition(POSITION_CENTER);
		this.text.setScale(2);
		super.addComponent(this.text);
	}
	
	/**
	 * Setzt den Text des Buttons
	 * 
	 * @param text Text
	 */
	public void setText(String text) {
		this.text.setText(text);
	}
	
}
