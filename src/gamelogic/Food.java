package gamelogic;
import graphics.Vector3f;

public class Food {
	public Vector3f foodPosition;
	
		public Food() {
			//setzt das Essen auf eine zufällige Position
			float a = (float) (Math.random()-0.5);
			float b = (float) (Math.random()-0.5);
			float c = (float) (Math.random()-0.5);
			
			foodPosition = new Vector3f(a,b,c);				
		}
		//errechnet Distanz zum Essen
		public float foodDistance(Vector3f x , Vector3f y) {
			Vector3f temp = x.copy();
			temp.scale(-1);
			temp.add(y);
			//sorgt für Kollision mit Essen aus anderen Kästen
			temp.x = (temp.x+10.5f)%1-0.5f;
			temp.y = (temp.y+10.5f)%1-0.5f;
			temp.z = (temp.z+10.5f)%1-0.5f;
			//gibt Distanz zwischen Kopf und essen zurück
			return temp.getLength();
		}
}
