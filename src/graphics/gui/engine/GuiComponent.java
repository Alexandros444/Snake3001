package graphics.gui.engine;

import graphics.gui.renderer.GuiShader;
import util.math.Matrix3f;
import util.math.Vector3f;

/**
 * Die abstrakte Klasse f�r all unsere Gui-Komponenten.<br>
 * Die Klasse <code>GuiComponent</code> kann <b>nicht</b> selber instanziiert werden, da es sich um eine abstrakte Klasse handelt - stattdessen k�nnen andere Klassen diese Klasse via Vererbung erweitern und dann instanziiert werden.<br>
 * Zum Beispiel kann mit <code>GuiComponent c = new TextComponent("Abc")</code> ein neues Text-Element erstellt werden.
 * <br><br>
 * Der Vorteil der Vererbung ist eben, dass durch die hier festgelegten Methoden auf alle Gui-Komponenten zugegriffen werden kann, egal ob es sich um eine Zeile Text oder ein Bild handelt - so wird eben ein System geschaffen, durch das mit verschiedenen Komponenten einheitlich umgegangen werden kann.
 * <br><br>
 * Das hei�t allerdings auch nicht, dass die hier festgelegten Methoden f�r alle Klassen gelten m�ssen - Klassen, die diese Klassen erweitern, k�nnen die Methoden immer noch �berschreiben, um das Verhalten beim Aufruf der Methode zu ver�ndern.
 * Durch die Deklaration der Methoden in dieser Klasse hier ist lediglich festgelegt, dass diese Methoden f�r alle diese Klasse erweiternden Klassen existieren m�ssen, nicht welchen Effekt sie jeweils haben.
 * 
 * @author Ben
 */
public abstract class GuiComponent {
	
	// Konstanten f�r die verschiedenen Positionierungsarten; so k�nnen Zahlen als symbolische Werte f�r Positionen �bergeben werden
	public static final int POSITION_FLOW = 0;
	public static final int POSITION_CENTER = 1;
	public static final int POSITION_CORNER_TOPLEFT = 2;
	public static final int POSITION_CORNER_TOPRIGHT = 3;
	public static final int POSITION_CORNER_BOTTOMRIGHT = 4;
	public static final int POSITION_CORNER_BOTTOMLEFT = 5;
	public static final int POSITION_FULL = 6;
	public static final int POSITION_CENTER_TOP = 7;
	public static final int POSITION_CENTER_FLOW = 8;
	
	public static final int WIDTH_STATIC = 0;
	public static final int WIDTH_AUTO = 1;
	public static final int HEIGHT_STATIC = 2;
	public static final int HEIGHT_AUTO = 3;
	
	public static final int FLOW_TOP_TO_BOTTOM = 0;
	public static final int FLOW_LEFT_TO_RIGHT = 1;
	
	public static final int VISIBILITY_VISIBLE = 0;
	public static final int VISIBILITY_HIDDEN = 1;
	
	private int width;
	private int height;
	
	private Matrix3f outerTransform;
	private Matrix3f innerTransform;
	private Matrix3f totalTransform;
	private Matrix3f inverseTransform;
	
	private int position = POSITION_FLOW;
	private int offsetX;
	private int offsetY;
	
	private boolean wasSizeChanged;
	
	private boolean touchesMouse;
	
	private float parentTransparency = 1;
	private float ownTransparency = 1;
	
	private int visibility = VISIBILITY_VISIBLE;
	
	/**
	 * Erstellt eine neuen Gui-Komponente.
	 * 
	 * @param width Anf�ngliche Breite in Pixeln
	 * @param height Anf�ngliche H�he in Pixeln
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
	 * @param shader zum Rendern benutzter Shader. N�tig zum Laden der Transformationsmatrizen
	 */
	public abstract void render(GuiShader shader);
	
	/**
	 * Updated die Komponente. Kann von abgeleiteten Klassen �berschrieben werden, um gegebenenfalls jeden Frame �nderungen an ihrem Inhalt durchf�hren zu k�nnen.
	 */
	public void update() {}
	
	/**
	 * L�scht alle von der Komponente abh�ngigen Objekte, um Speicher wieder freizugeben. Muss von allen abgeleiteten Klassen implementiert werden.
	 */
	public abstract void destroy();
	
	/**
	 * �ndert die Gr��e der Komponente.
	 * 
	 * @param width neue Breite in Pixeln
	 * @param height neue H�he in Pixeln
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
	 * Wird bei jeglichen Gr��en�nderungen des Elements aufgerufen. Kann von erweiternden Klassen �berschrieben werden, um bei Gr��en�nderungen ggf. eigene Werte anzupassen.
	 */
	protected void onSizeChange() {};
	
	/**
	 * Gibt zur�ck, ob die Gr��e oder eine damit in Zusammenhang stehende Eigenschaft (z.B. Position oder Offset) ge�ndert wurde.<br>
	 * So kann das Elternelement ermitteln, ob eine Neuanordnung der Elemente n�tig ist oder nicht.
	 * <br><br>
	 * Wurde eine solche Eigenschaft einmal ge�ndert, gibt diese Methode solange <code>true</code> zur�ck, bis {@link #clearChangesBuffer()} aufgerufen wurde.
	 * 
	 * @return ob Gr��e, Position oder Offset des Elements sein dem letzten Aufruf von {@link #clearChangesBuffer()} ge�ndert worden sind
	 */
	public boolean wasSizeChanged() {
		return wasSizeChanged;
	}
	
	/**
	 * Signalisiert dem Element, dass die erforderliche Neupositionierung stattgefunden hat und {@link #wasSizeChanged()} vorerst wieder <code>false</code> zur�ckgeben kann.
	 */
	public void clearChangesBuffer() {
		wasSizeChanged = false;
	}
	
	/**
	 * Gibt die Breite des Elements zur�ck
	 * 
	 * @return Breite in Pixeln
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Gibt die H�he des Elements zur�ck
	 * 
	 * @return H�he in Pixeln
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Gibt zur�ck, ob das Element den gegebenen Punkt ber�hrt.
	 * 
	 * @param x x-Position des Punktes
	 * @param y y-Position des Punktes
	 * @return ob das Element den Punkt ber�hrt
	 */
	public boolean touchesPoint(float x, float y) {
		Vector3f mouseCoords = new Vector3f(x,y,1);
		mouseCoords.apply(inverseTransform);
		return mouseCoords.x>0&&mouseCoords.y>0&&mouseCoords.x<width&&mouseCoords.y<height;
	}
	
	/**
	 * Gibt Informationen �ber den aktuellen Zustand der Maus an das Element weiter.
	 * 
	 * @param canTouchMouse ob das MouseEvent nicht bereits von anderen Elementen im Vordergrund abgefangen wurde.
	 * @param event Event mit Informationen zur Maus
	 */
	public void receiveMouseEvent(boolean canTouchMouse, MouseEvent event) {
		boolean touchesMouse = canTouchMouse&&touchesPoint(event.mouseX,event.mouseY);
		if (touchesMouse&&!this.touchesMouse) {
			onMouseOn(event);
		}else if (this.touchesMouse&&!touchesMouse) {
			onMouseOff(event);
		}
		this.touchesMouse = touchesMouse;
		if(touchesMouse) {
			onMouseMove(event);
			if (event.wasLeftClicked) {
				onLeftClick(event);
			}
			if (event.wasRightClicked) {
				onRightClick(event);
			}
		}
	}
	
	/**
	 * Wird aufgerufen, wenn die Maus sich auf das Element bewegt. Kann von erweiternden Klassen �berschrieben werden, um dann z.B. Farb�nderungen vorzunehmen.
	 * @param event Event mit Informationen zur Maus
	 */
	protected void onMouseOn(MouseEvent event) {}
	
	/**
	 * Wird aufgerufen, wenn die Maus das Element wieder verl�sst. Kann von erweiternden Klassen �berschrieben werden, um dann z.B. Farb�nderungen vorzunehmen.
	 * @param event Event mit Informationen zur Maus
	 */
	protected void onMouseOff(MouseEvent event) {}
	
	/**
	 * Wird aufgerufen, wenn sich die Maus auf dem Element bewegt. Kann von erweiternden Klassen �berschrieben werden.
	 * @param event Event mit Informationen zur Maus
	 */
	protected void onMouseMove(MouseEvent event) {}
	
	/**
	 * Wird aufgerufen, wenn mit der linken Maustaste auf das Element geklickt wird. Kann von erweiternden Klassen �berschrieben werden, um auf den Klick zu reagieren.
	 * @param event Event mit Informationen zur Maus
	 */
	protected void onLeftClick(MouseEvent event) {}
	
	/**
	 * Wird aufgerufen, wenn mit der rechten Maustaste auf das Element geklickt wird. Kann von erweiternden Klassen �berschrieben werden, um auf den Klick zu reagieren.
	 * @param event Event mit Informationen zur Maus
	 */
	protected void onRightClick(MouseEvent event) {}
	
	/**
	 * Setzt die �u�ere Transformation des Elements. Public, also von �berall aus aufrufbar.<br>
	 * In der �u�eren Transformation sollten in der Regel die Transformation des Elternelements und die Position in diesem kodiert sein.
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
		inverseTransform = totalTransform.getInverse();
	}
	
	/**
	 * Gibt die Gesamttransformation zur�ck.
	 * 
	 * @return Gesamttransformationsmatrix
	 */
	protected Matrix3f getTotalTransform() {
		return totalTransform;
	}
	
	/**
	 * Wird bei jeglichen �nderungen an der Transformation des Elements aufgerufen. Kann von erweiternden Klassen �berschrieben werden, um bei solchen �nderungen ggf. eigene Werte anzupassen.
	 */
	protected void onPositionChange() {};
	
	/**
	 * Setzt die Positionierungsart des Elements, z.B. POSITION_CENTER f�r zentriert oder POSITION_FLOW f�r automatische Positionierung der Flow-Elemente untereinander.
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
	 * Gibt das horizontale Offset des Elements zur�ck.<br>
	 * Das Offset gibt an, wieviel Leerraum bei der Positionierung links und rechts vom Element gelassen werden soll.
	 * 
	 * @return horizontales Offset in Pixeln
	 */
	public int getOffsetX() {
		return offsetX;
	}
	
	/**
	 * Gibt das vertikale Offset des Elements zur�ck.<br>
	 * Das Offset gibt an, wieviel Leerraum bei der Positionierung �ber und unter dem Element gelassen werden soll.
	 * 
	 * @return vertikales Offset in Pixeln
	 */
	public int getOffsetY() {
		return offsetY;
	}
	
	/**
	 * Setzt die gespeicherte Transparenz des Elternelements. Diese wird zus�tzlich zur eigenen Transparenz auf das Element angewendet.
	 * @param transparency Transparenz des Elternelements
	 */
	public void setParentTransparency(float transparency) {
		parentTransparency = transparency;
		onTransparencyChange();
	}
	
	/**
	 * Setzt die Sichtbarkeit des Elements.
	 * @param visibility <code>VISIBILITY_VISIBLE</code> oder <code>VISIBILITY_HIDDEN</code>
	 */
	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}
	
	/**
	 * Gibt die Sichtbarkeit des Elements zur�ck.
	 * @return <code>VISIBILITY_VISIBLE</code> oder <code>VISIBILITY_HIDDEN</code>
	 */
	public int getVisibility() {
		return visibility;
	}
	
	/**
	 * Setzt die Transparenz des Elements.
	 * @param transparency Transparenz-Wert zwischen 0 und 1.
	 */
	public void setTransparency(float transparency) {
		ownTransparency = transparency;
		onTransparencyChange();
	}
	
	/**
	 * Wird ausgef�hrt, wenn die Transparenz des Elements ge�ndert wird.
	 * <br>
	 * Andere Klassen k�nnen diese Methode �berschreiben, um bei �nderungen der Transparenz eigenen COde auszuf�hren.
	 */
	protected void onTransparencyChange() {}
	
	/**
	 * Gibt die Gesamttransparenz, also das Produkt aus der eigenen Transparenz und der Transparenz des Elternelements, zur�ck.
	 * @return Gesamttransparenz, zwischen 0 und 1
	 */
	public float getTotalTransparency() {
		return ownTransparency*parentTransparency;
	}
	
}
