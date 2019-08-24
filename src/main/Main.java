package main;

import graphic.Display;

/**
 * Die Klasse mit der Main-Methode unseres Programms.<br>
 * 
 * Bisher nur als Beispiel für den Umgang mit Eclipse, Git  und Javadoc gedacht.
 * 
 * @author Ben
 * @author Alex
 */

public class Main {
	
	/**
	 * Die Main-Methode selbst. Sie öffnet ein Fenster.
	 * 
	 * @param args Kommandozeilenparameter
	 */
	
	public static void main(String[] args) {
		Display display = new Display(960,540,"TITEL");
		while(!display.isCloseRequested()) {
			display.update();
		}
		display.close();
	}
	
}
