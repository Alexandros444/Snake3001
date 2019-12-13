package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class ScoresLoader {
	
	public static int[] loadScores(String path, int count) {
		int[] scores = new int[count];
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			for (int i=0;i<count;i++) {
				try {
					String line = reader.readLine();
					scores[i] = Integer.parseInt(line);
				} catch (NumberFormatException | IOException e) {
					System.out.println("Error reading from file, in line "+i+"!");
					e.printStackTrace();
					break;
				}
			}
			reader.close();
			
		} catch (IOException e) {
			System.out.println("Error reading Highscores from "+path+"!");
			e.printStackTrace();
		}
		System.out.println("Loaded scores: "+Arrays.toString(scores));
		return scores;
	}
	
	public static void saveScores(String path, int[] scores) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path));
			writer.write("");
			for (int i=0;i<scores.length;i++) {
				writer.append(scores[i]+"\n");
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Couldn't save highscores to "+path+"!");
			e.printStackTrace();
		}
	}
	
}
