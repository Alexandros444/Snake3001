package graphics.gui.engine;

public class KeyInput {
	
	private int key;
	
	private boolean isKeyDown;
	private boolean wasKeyPressed;
	
	public KeyInput(int key) {
		this.key = key;
	}
	
	public int getKey() {
		return key;
	}
	
	/**
	 * Updated das KeyInput 
	 */
	public void update(boolean isKeyDown) {
		this.wasKeyPressed = isKeyDown && !this.isKeyDown;
		this.isKeyDown = isKeyDown;
	}
	
	public boolean isKeyDown() {
		return isKeyDown;
	}
	
	public boolean wasKeyPressed() {
		return wasKeyPressed;
	}
}
