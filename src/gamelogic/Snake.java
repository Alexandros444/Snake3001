package gamelogic;

import org.lwjgl.glfw.GLFW;

import graphics.Display;
import graphics.Matrix3f;
import graphics.Vector3f;

public class Snake {
	public Vector3f cameraPosition, movement;
	public Matrix3f viewDirection;

	public Vector3f[] arrayVecsSnake, arrayVecsMove;
	
	private float rotationSpeed = 1f;
	private float movementSpeed = 0.01f;	
	
	public Snake(){
	    cameraPosition = new Vector3f();
	    viewDirection = new Matrix3f();
	    movement = new Vector3f();
	    arrayVecsSnake = new Vector3f[10];
	    arrayVecsMove = new Vector3f[10];
	    arrayVecsMove[0] = movement.copy();
	}
	public void update(Display display) {
		// dreht die Sichtmatrix je nach Tasteninput und lädt sie in den Shader
					
		if (display.isKeyPressed(GLFW.GLFW_KEY_W) || display.isKeyPressed(GLFW.GLFW_KEY_UP) ) {
			viewDirection.rotate(-rotationSpeed, 0, 0);
		}
		if (display.isKeyPressed(GLFW.GLFW_KEY_S) || display.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
			viewDirection.rotate(rotationSpeed, 0, 0);
		}
		if (display.isKeyPressed(GLFW.GLFW_KEY_A) || display.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
			viewDirection.rotate(0, -rotationSpeed, 0);
		}
		if (display.isKeyPressed(GLFW.GLFW_KEY_D) || display.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
			viewDirection.rotate(0, rotationSpeed, 0);
		}
		if (display.isKeyPressed(GLFW.GLFW_KEY_Q)) {
			viewDirection.rotate(0, 0, rotationSpeed);
		}
		if (display.isKeyPressed(GLFW.GLFW_KEY_E)) {
			viewDirection.rotate(0, 0, -rotationSpeed);
		}
		if (!display.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
			movement.x = 0;
			movement.y = 0;
			movement.z = movementSpeed;
			movement.apply(viewDirection);
			cameraPosition.add(movement);
		}
		
		updateVecsMovement();
		updateSnakePositions();
	}
	
	private void updateVecsMovement() {
		for (int i = 0; i < arrayVecsMove.length-1; i++) {
			arrayVecsMove[i+1] = arrayVecsMove[i];
		}
		arrayVecsMove[0] = movement.copy();
		arrayVecsMove[0].scale(-5.6f);
	}
	
	private void updateSnakePositions() {
		arrayVecsSnake[0] = cameraPosition.copy();
		arrayVecsSnake[0].add(arrayVecsMove[0]);
		for (int i = 0; i < arrayVecsSnake.length-1; i++) {
			arrayVecsSnake[i+1] = arrayVecsSnake[i].copy();
			arrayVecsSnake[i+1].add(arrayVecsMove[i+1]);
		}
	}
	
	
}
