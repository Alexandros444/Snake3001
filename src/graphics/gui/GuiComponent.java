package graphics.gui;

import graphics.Matrix3f;
import graphics.guiRenderer.GuiShader;

/**
 * Die abstrakte Klasse für all unsere Gui-Komponenten.<br>
 * Die Klasse <code>GuiComponent</code> kann <b>nicht</b> selber instanziiert werden, da es sich um eine abstrakte Klasse handelt - stattdessen können andere Klassen diese Klasse via Vererbung erweitern und dann instanziiert werden.<br>
 * Zum Beispiel kann mit <code>GuiComponent c = new TextComponent("Abc")</code> ein neues Text-Element erstellt werden.
 * <br><br>
 * Der Vorteil der Vererbung ist eben, dass durch die hier festgelegten Methoden auf alle Gui-Komponenten zugegriffen werden kann, egal ob es sich um eine Zeile Text oder ein Bild handelt - so wird eben ein System geschaffen, durch das mit verschiedenen Komponenten einheitlich umgegangen werden kann.
 * <br><br>
 * Das heißt allerdings auch nicht, dass die hier festgelegten Methoden für alle Klassen gelten müssen - Klassen, die diese Klassen erweitern, können die Methoden immer noch überschreiben, um das Verhalten beim Aufruf der Methode zu verändern.
 * Durch die Deklaration der Methoden in dieser Klasse hier ist lediglich festgelegt, dass diese Methoden für alle diese Klasse erweiternden Klassen existieren müssen, nicht welchen Effekt sie jeweils haben.
 * 
 * @author Ben
 */
public abstract class GuiComponent {
	
	// Kontanten für die verschiedenen Positionierungsarten; so können Zahlen als symbolische Werte für Positionen übergeben werden
	public static final int POSITION_FLOW = 0;
	public static final int POSITION_CENTER = 1;
	public static final int POSITION_CORNER_TOPLEFT = 2;
	public static final int POSITION_CORNER_TOPRIGHT = 3;
	public static final int POSITION_CORNER_BOTTOMRIGHT = 4;
	public static final int POSITION_CORNER_BOTTOMLEFT = 5;
	
	private int width;
	private int height;
	
	private Matrix3f outerTransform;
	private Matrix3f innerTransform;
	private Matrix3f totalTransform;
	
	private int position = POSITION_FLOW;
	private int offsetX;
	private int offsetY;
	
	private boolean wasSizeChanged;
	
	/**
	 * Erstellt eine neuen Gui-Komponente.
	 * 
	 * @param width Anfängliche Breite in Pixeln
	 * @param height Anfängliche Höhe in Pixeln
	 */
	public GuiComponent(int width, int height) {
		this.width = width;
		this.height = height;
		innerTransform = new Matrix3f();
		outerTransform = new Matrix3f();
		updateTotalTransform();
	}
	
	/**
	 * Rendert die Komponente. Muss von allen abgeleiteten Klassen implementiert werden.
	 * 
	 * @param shader zum Rendern benutzter Shader. Nötig zum Laden der Transformationsmatrizen
	 */
	public abstract void render(GuiShader shader);
	
	/**
	 * Updated die Komponente. Kann von abgeleiteten Klassen überschrieben werden, um gegebenenfalls jeden Frame Änderungen an ihrem Inhalt durchführen zu können.
	 */
	public void update() {}
	
	/**
	 * Löscht alle von der Komponente abhängigen Objekte, um Speicher wieder freizugeben. Muss von allen abgeleiteten Klassen implementiert werden.
	 */
	public abstract void destroy();
	
	/**
	 * Ändert die Größe der Komponente.
	 * 
	 * @param width neue Breite in Pixeln
	 * @param height neue Höhe in Pixeln
	 */
	public void setSize(int width, int height) {
		if (this.width!=width||this.height!=height) {
			this.width = width;
			this.height = height;
			onSizeChange();
			wasSizeChanged = true;
		}
	}
	
	/**
	 * Wird bei jeglichen Größenänderungen des Elements aufgerufen. Kann von erweiternden Klassen überschrieben werden, um bei Größenänderungen ggf. eigene Werte anzupassen.
	 */
	protected void onSizeChange() {};
	
	/**
	 * Gibt zurück, ob die Größe oder eine damit in Zusammenhang stehende Eigenschaft (z.B. Position oder Offset) geändert wurde.<br>
	 * So kann das Elternelement ermitteln, ob eine Neuanordnung der Elemente nötig ist oder nicht.
	 * <br><br>
	 * Wurde eine solche Eigenschaft einmal geändert, gibt diese Methode solange <code>true</code> zurück, bis {@link #clearChangesBuffer()} aufgerufen wurde.
	 * 
	 * @return ob Größe, Position oder Offset des Elements sein dem letzten Aufruf von {@link #clearChangesBuffer()} geändert worden sind
	 */
	public boolean wasSizeChanged() {
		return wasSizeChanged;
	}
	
	/**
	 * Signalisiert dem Element, dass die erforderliche Neupositionierung stattgefunden hat und {@link #wasSizeChanged()} vorerst wieder <code>false</code> zurückgeben kann.
	 */
	public void clearChangesBuffer() {
		wasSizeChanged = false;
	}
	
	/**
	 * Gibt die Breite des Elements zurück
	 * 
	 * @return Breite in Pixeln
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Gibt die Höhe des Elements zurück
	 * 
	 * @return Höhe in Pixeln
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Setzt die äußere Transformation des Elements. Public, also von überall aus aufrufbar.<br>
	 * In der äußeren Transformation sollten in der Regel die Transformation des Elternelements und die Position in diesem kodiert sein.
	 * 
	 * @param transform Transformationmatrix
	 */
	public void setTransform(Matrix3f transform) {
		outerTransform = transform.copy();
		updateTotalTransform();
		onPositionChange();
	}
	
	/**
	 * Setzt die innere Transformation des Elements. Protected, also nur von vererbten Klassen aus aufrufbar.<br>
	 * Die innere Transformation kann genutzt werden, um z.B. Rotationen und Skalierungen auf das Element anzuwenden.
	 * 
	 * @param transform Transformationmatrix
	 */
	protected void setInnerTransform(Matrix3f transform) {
		innerTransform = transform.copy();
		updateTotalTransform();
		onPositionChange();
	}
	
	/**
	 * Errechnet die Gesamttransformation aus den einzelnen Teiltransformationen
	 */
	private void updateTotalTransform() {
		totalTransform = new Matrix3f();
		totalTransform.multiply(outerTransform);
		totalTransform.multiply(innerTransform);
	}
	
	/**
	 * Gibt die Gesamttransformation zurück.
	 * 
	 * @return Gesamttransformationsmatrix
	 */
	protected Matrix3f getTotalTransform() {
		return totalTransform;
	}
	
	/**
	 * Wird bei jeglichen Änderungen an der Transformation des Elements aufgerufen. Kann von erweiternden Klassen überschrieben werden, um bei solchen Änderungen ggf. eigene Werte anzupassen.
	 */
	protected void onPositionChange() {};
	
	/**
	 * Setzt die Positionierungsart des Elements, z.B. POSITION_CENTER für zentriert oder POSITION_FLOW für automatische Positionierung der Flow-Elemente untereinander.
	 * 
	 * @param position eine der Konstanten POSITION_FLOW, POSITION_CENTER, POSITION_CORNER_TOPLEFT usw.
	 */
	public void setPosition(int position) {
		this.position = position;
		wasSizeChanged = true;
	}
	
	/**
	 * Gibt die Positionierungsart des Elements aus.
	 * 
	 * @return eine der Symbolkonstanten POSITION_FLOW, POSITION_CENTER, POSITION_CORNER_TOPLEFT usw.
	 */
	public int getPosition() {
		return position;
	}
	
	/**
	 * Setzt den Rand bzw. Leerraum um das Element, der bei der Positionierung freigelassen werden soll.<br>
	 * Hat ein Element beispielsweise die Positionierung POSITION_TOPLEFT und ein Offset von 10, wird es mit Zehn Pixeln Abstand zur oberen linken Ecke des Elternelements positioniert.
	 * 
	 * @param offsetX horizontales Offset in Pixeln
	 * @param offsetY vertikales Offset in Pixeln
	 */
	public void setOffset(int offsetX, int offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		wasSizeChanged = true;
	}
	
	/**
	 * Gibt das horizontale Offset des Elements zurück.<br>
	 * Das Offset gibt an, wieviel Leerraum bei der Positionierung links und rechts vom Element gelassen werden soll.
	 * 
	 * @return horizontales Offset in Pixeln
	 */
	public int getOffsetX() {
		return offsetX;
	}
	
	/**
	 * Gibt das vertikale Offset des Elements zurück.<br>
	 * Das Offset gibt an, wieviel Leerraum bei der Positionierung über und unter dem Element gelassen werden soll.
	 * 
	 * @return vertikales Offset in Pixeln
	 */
	public int getOffsetY() {
		return offsetY;
	}
	
}
