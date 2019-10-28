package graphics.gui;

import graphics.core.Display;
import graphics.gui.engine.MouseEvent;

/**
 * Klasse zur Verarbeitung der Inputs auf ein Fenster.
 * 
 * @author Ben
 */
public class InputHandler {
	
	private Display display;
	
	private float mouseX;
	private float mouseY;
	private boolean isLeftMouseDown;
	private boolean isRightMouseDown;
	private boolean wasLeftMouseClicked;
	private boolean wasRightMouseClicked;
	
	/**
	 * Erstellt einen neuen InputHandler.
	 * 
	 * @param display Fenster, dessen Inputs verarbeitet werden sollen
	 */
	public InputHandler(Display display) {
		this.display = display;
	}
	
	/**
	 * Updated alle Inputs
	 */
	public void update() {
		boolean wasLeftMouseDown = isLeftMouseDown;
		boolean wasRightMouseDown = isRightMouseDown;
		isLeftMouseDown = display.isLeftMouseDown();
		isRightMouseDown = display.isRightMouseDown();
		wasLeftMouseClicked = isLeftMouseDown&&!wasLeftMouseDown;
		wasRightMouseClicked = isRightMouseDown&&!wasRightMouseDown;
		mouseX = display.getMouseX();
		mouseY = display.getMouseY();
	}
	
	/**
	 * Gibt Informationen über den derzeitigen Zustand der Maus in Form eines MouseEvents zurück
	 * 
	 * @return MouseEvent mit Informationen zur Maus
	 */
	public MouseEvent getCurrentMouseEvent() {
		return new MouseEvent(mouseX,mouseY,isLeftMouseDown,isRightMouseDown,wasLeftMouseClicked,wasRightMouseClicked);
	}
	
}
