package snake.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class HighScore {
	
	public static void writeHighScore(int highScore) {
		try {
			FileWriter writer = new FileWriter(new File("highscore.txt"));
			writer.write(highScore);
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int getHighScore() {
		int highScore = 0;
		FileReader reader;
		
		for (int i = 0; i < 1; i++) {
			try {
				reader = new FileReader("highscore.txt");
				highScore = reader.read();
				reader.close();
			} catch(FileNotFoundException e) {
				System.out.println("not found");
				writeHighScore(0);
				continue;
			} catch(IOException e) {
				continue;
			}
		}
		
		return highScore;
	}
	
}
