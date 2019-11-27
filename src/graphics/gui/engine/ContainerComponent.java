package graphics.gui.engine;

import java.util.ArrayList;
import java.util.List;

import graphics.gui.renderer.GuiShader;
import util.math.Matrix3f;
import util.math.Vector3f;

/**
 * Die Basisklasse für Komponenten, die andere Komponenten enthalten können.<br>
 * Die enthaltenen Komponenten werden von dieser Klasse automatisch positioniert, gerendert und am Ende gelöscht, sodass sie als eine Art Einheit betrachtet werden können - ein Aufruf der entsprechenden Methode dieser Klasse reicht bereits aus.
 * <br><br>
 * Andere Klassen können diese Klasse erweitern, um auch als Container für andere Komponenten dienen zu können. Dazu können sie die Methoden dieser Klasse überschrieben, sollten sie dabei aber auch immer mit <code>super.xyz()</code> wieder aufrufen, damit keine Funktionalität verloren geht.
 * 
 * @author Ben
 */
public class ContainerComponent extends GuiComponent {
	
	// Liste mit allen Kind-Komponenten
	private List<GuiComponent> childComponents;
	
	private int innerOffsetX;
	private int innerOffsetY;
	
	private int widthMode = WIDTH_STATIC;
	private int heightMode = HEIGHT_STATIC;
	private int flowDirection = FLOW_TOP_TO_BOTTOM;
	
	/**
	 * Erstellt einen neuen, leeren ContainerComponent
	 * 
	 * @param width Breite in Pixeln
	 * @param height Höhe in Pixeln
	 */
	public ContainerComponent(int width, int height) {
		super(width,height);
		childComponents = new ArrayList<GuiComponent>();
	}
	
	/**
	 * Fügt eine Komponente als Kindelement hinzu
	 * @param component die Komponente
	 */
	public void addComponent(GuiComponent component) {
		childComponents.add(component);
		component.setParentTransparency(super.getTotalTransparency());
		refreshChildPositions();
	}
	
	/**
	 * Fügt eine Komponente vor einer anderen als Kindelement ein
	 * 
	 * @param component1 Element, das eingefügt werden soll
	 * @param component2 Element, vor dem es eingefügt werden soll
	 */
	public void insertBefore(GuiComponent component1, GuiComponent component2) {
		childComponents.add(childComponents.indexOf(component2),component1);
		component1.setParentTransparency(super.getTotalTransparency());
		refreshChildPositions();
	}
	
	/**
	 * Entfernt eine Komponente als Kindelement
	 * @param component die Komponente
	 */
	public void removeComponent(GuiComponent component) {
		childComponents.remove(component);
		refreshChildPositions();
	}
	
	/**
	 * Updated die Positionen aller Kindelemente. Sollte immer dann aufgerufen werden, wenn die Größe oder Position dieses Elements oder Größe, Position oder Offset einer Kindkomponente geändert wurden.
	 */
	protected void refreshChildPositions() {
		Matrix3f baseTransformation = super.getTotalTransform();
		int innerWidth = super.getWidth();
		int innerHeight = super.getHeight();
		int flowX = innerOffsetX;
		int flowY = innerOffsetY;
		int contentWidth = 0;
		int contentHeight = 0;
		for (GuiComponent childComponent:childComponents) {
			// bestimmt die Position des Kindelements innerhalb des Elternelements
			Vector3f positionOffset = new Vector3f(0,0,1);
			int childPosition = childComponent.getPosition();
			if (childPosition==POSITION_FULL) {
				positionOffset.x = childComponent.getOffsetX();
				positionOffset.y = childComponent.getOffsetY();;
				childComponent.setSize(innerWidth-2*childComponent.getOffsetX(),innerHeight-2*childComponent.getOffsetY());
			}else if (childPosition==POSITION_CENTER) {
				positionOffset.x = (innerWidth-childComponent.getWidth())/2;
				positionOffset.y = (innerHeight-childComponent.getHeight())/2;
			}else if (childPosition==POSITION_CORNER_TOPLEFT){
				positionOffset.x = childComponent.getOffsetX();
				positionOffset.y = childComponent.getOffsetY();
			}else if (childPosition==POSITION_CORNER_TOPRIGHT) {
				positionOffset.x = innerWidth-childComponent.getWidth()-childComponent.getOffsetX();
				positionOffset.y = childComponent.getOffsetY();
			}else if (childPosition==POSITION_CORNER_BOTTOMRIGHT) {
				positionOffset.x = innerWidth-childComponent.getWidth()-childComponent.getOffsetX();
				positionOffset.y = innerHeight-childComponent.getHeight()-childComponent.getOffsetY();
			}else if (childPosition==POSITION_CORNER_BOTTOMLEFT){
				positionOffset.x = childComponent.getOffsetX();
				positionOffset.y = innerHeight-childComponent.getHeight()-childComponent.getOffsetY();
			}else if (childPosition==POSITION_CENTER_TOP){
				positionOffset.x = (innerWidth-childComponent.getWidth())/2;
				positionOffset.y = childComponent.getOffsetY();
			}else if (childPosition==POSITION_FLOW){
				positionOffset.x = flowX;
				positionOffset.y = flowY;
				contentWidth = Math.max(contentWidth,flowX+childComponent.getWidth()+innerOffsetX);
				contentHeight = Math.max(contentHeight,flowY+childComponent.getHeight()+innerOffsetY);
				if (flowDirection==FLOW_TOP_TO_BOTTOM) {
					flowY += childComponent.getHeight()+childComponent.getOffsetY();
				}else if(flowDirection==FLOW_LEFT_TO_RIGHT) {
					flowX += childComponent.getWidth()+childComponent.getOffsetX();
				}
			}else if (childPosition==POSITION_CENTER_FLOW) {
				contentWidth = Math.max(contentWidth,flowX+childComponent.getWidth()+innerOffsetX);
				contentHeight = Math.max(contentHeight,flowY+childComponent.getHeight()+innerOffsetY);
				if (flowDirection==FLOW_TOP_TO_BOTTOM) {
					positionOffset.x = (innerWidth-childComponent.getWidth())/2;
					positionOffset.y = flowY;
					flowY += childComponent.getHeight()+childComponent.getOffsetY();
				}else if(flowDirection==FLOW_LEFT_TO_RIGHT) {
					positionOffset.x = flowX;
					positionOffset.y = (innerHeight-childComponent.getHeight())/2;
					flowX += childComponent.getWidth()+childComponent.getOffsetX();
				}
			}
			// wendet die Transformation des Elternelements darauf an, um die absolute Position zu ermitteln
			positionOffset.apply(baseTransformation);
			Matrix3f transform = baseTransformation.copy();
			transform.m20 = positionOffset.x;
			transform.m21 = positionOffset.y;
			childComponent.setTransform(transform);
		}
		if (widthMode==WIDTH_AUTO&&heightMode==HEIGHT_AUTO) {
			super.setSize(contentWidth,contentHeight);
		}else if (widthMode==WIDTH_AUTO) {
			super.setSize(contentWidth,super.getHeight());
		}else if (heightMode==HEIGHT_AUTO) {
			super.setSize(super.getWidth(),contentHeight);
		}
	}
	
	/**
	 * Passt bei Größenänderungen die Positionen der Kindelemente an. Wird von der Klasse GuiComponent aufgerufen, wenn sich die Größe des Elements ändert.<br>
	 * Diese Methode kann von erweiternden Klassen überschrieben werden, um eigene Anpassungen an die neue Größe vorzunehmen, sollte aber stehts über <code>super.onSizeChange()</code> diese Methode auch ausführen.
	 */
	protected void onSizeChange() {
		refreshChildPositions();
	}
	
	/**
	 * Passt bei Positionsänderungen die Positionen der Kindelemente an. Wird von der Klasse GuiComponent aufgerufen, wenn sich die Größe des Elements ändert.<br>
	 * Diese Methode kann von erweiternden Klassen überschrieben werden, um eigene Anpassungen an die neue Position vorzunehmen, sollte aber stehts über <code>super.onPositionChange()</code> diese Methode auch ausführen.
	 */
	protected void onPositionChange() {
		refreshChildPositions();
	}
	
	/**
	 * Updated alle Kindelemente und nimmt gegebenenfalls eine Neupositionierung der Elemente vor.
	 * <br><br>
	 * Erweiternde Klassen können diese Methode überschreiben um eigene Funktionalität hinzuzufügen, sollten sie dabei aber immer nochmal über <code>super.update()</code> aufrufen.
	 */
	public void update() {
		boolean wasChanged = false;
		for (GuiComponent childComponent:childComponents) {
			childComponent.update();
			if (childComponent.wasSizeChanged()) {
				wasChanged = true;
				childComponent.clearChangesBuffer();
			}
		}
		if (wasChanged) {
			refreshChildPositions();
		}
	}
	
	/**
	 * Gibt das MouseEvent an die Kindelemente weiter, wenn sich die Maus über das Element bewegt
	 * 
	 * @param event Event mit Informationen zur Maus
	 */
	protected void onMouseMove(MouseEvent event) {
		boolean mouseCovered = false;
		for (int i=childComponents.size()-1;i>=0;i--) {
			boolean touchesMouse = childComponents.get(i).touchesPoint(event.mouseX,event.mouseY);
			childComponents.get(i).receiveMouseEvent(!mouseCovered,event);
			if (touchesMouse) {
				mouseCovered = true;
			}
		}
	}
	
	/**
	 * Gibt das MouseEvent ein letztes Mal an die Kindelemente weiter, wenn die Maus das Element verlässt
	 * 
	 * @param event Event mit Informationen zur Maus
	 */
	protected void onMouseOff(MouseEvent event) {
		for (GuiComponent childComponent:childComponents) {
			childComponent.receiveMouseEvent(false,event);
		}
	}
	
	/**
	 * Setzt das "innere Offset" des Elements, also den Abstand, den Flow-Kindelemente zum Rand des Containers haben sollen.
	 * 
	 * @param x Abstand links und rechts in Pixeln
	 * @param y Abstand oben und unten in Pixeln
	 */
	public void setInnerOffset(int x, int y) {
		innerOffsetX = x;
		innerOffsetY = y;
		refreshChildPositions();
	}
	
	/**
	 * Setzt den "Width Mode" des Elements - der bestimmt, ob sich das Element der Breite seines Inhalts anpassen soll oder nicht.<br>
	 * 
	 * @param mode eine der Konstanten <code>WIDTH_STATIC</code> oder <code>WIDTH_AUTO</code>
	 */
	public void setWidthMode(int mode) {
		widthMode = mode;
		refreshChildPositions();
	}
	
	/**
	 * Setzt den "Height Mode" des Elements - der bestimmt, ob sich das Element der Höhe seines Inhalts anpassen soll oder nicht.<br>
	 * 
	 * @param mode eine der Konstanten <code>HEIGHT_STATIC</code> oder <code>HEIGHT_AUTO</code>
	 */
	public void setHeightMode(int mode) {
		heightMode = mode;
		refreshChildPositions();
	}
	
	/**
	 * Setzt die Richtung, in die sich Flow-Elemente anordnen sollen
	 * 
	 * @param direction eine der Konstanten <code>FLOW_TOP_TO_BOTTOM</code> oder <code>FLOW_LEFT_TO_RIGHT</code>
	 */
	public void setFlowDirection(int direction) {
		flowDirection = direction;
		refreshChildPositions();
	}
	
	/**
	 * Updated die Transparenz der Kindelemente, wenn die Transparenz des Containers geändert wird
	 */
	protected void onTransparencyChange() {
		for (GuiComponent childComponent:childComponents) {
			childComponent.setParentTransparency(super.getTotalTransparency());
		}
	}
	
	/**
	 * Rendert alle Kindelemente.
	 * <br><br>
	 * Erweiternde Klassen, die zusätzlich noch etwas anderes wie z.B. einen Hintergrund rendern wollen, können dazu diese Methode überschreiben, sollten sie aber dabei nochmal über <code>super.render(shader)</code> aufrufen, damit die Kindelemente auch gerendert werden.
	 * 
	 * @param shader zum Rendern genutzter Shader.
	 */
	public void render(GuiShader shader) {
		if (super.getVisibility()==VISIBILITY_VISIBLE) {
			for (GuiComponent childComponent:childComponents) {
				childComponent.render(shader);
			}
		}
	}
	
	/**
	 * Löscht alle Kindelemente, um Ressourcen freizugeben.
	 * <br><br>
	 * Erweiternde Klassen, die ebenfalls Ressourcen freizugeben haben, können dazu diese Methode überschreiben, sollten sie aber dabei nochmal über <code>super.destroy()</code> aufrufen.
	 */
	public void destroy() {
		for (GuiComponent childComponent:childComponents) {
			childComponent.destroy();
		}
	}
	
}
