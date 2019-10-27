package graphics.gui;

/**
 * Enthält Informationen über den Zustand der Maus.<br>
 * Wird von anderen Klassen genutzt, um Informationen über die Maus kompakt zu speichern und auszutauschen.
 * 
 * @author Ben
 */
public class MouseEvent {
	
	public final float mouseX;
	public final float mouseY;
	public final boolean isLeftMouseDown;
	public final boolean isRightMouseDown;
	public final boolean wasLeftClicked;
	public final boolean wasRightClicked;
	
	/**
	 * Erstellt eine neue Instanz mit den gegebenen Werten.
	 * 
	 * @param mouseX Maus-X-Position relativ zur oberen linken Ecke des Fensters
	 * @param mouseY Maus-Y-Position relativ zur oberen linken Ecke des Fensters
	 * @param isLeftMouseDown ob die linke Maustaste gedrückt ist
	 * @param isRightMouseDown ob die rechte Maustaste gedrückt ist
	 * @param wasLeftClicked ob mit der linken Maustaste kürzlich geklickt wurde
	 * @param wasRightClicked ob mit der rechten Maustaste kürzlich geklickt wurde
	 */
	public MouseEvent(float mouseX, float mouseY, boolean isLeftMouseDown, boolean isRightMouseDown, boolean wasLeftClicked, boolean wasRightClicked) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.isLeftMouseDown = isLeftMouseDown;
		this.isRightMouseDown = isRightMouseDown;
		this.wasLeftClicked = wasLeftClicked;
		this.wasRightClicked = wasRightClicked;
	}
	
}
