package snake.io;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Serializer {
	
	BufferedWriter writer;
	
	public Serializer() {
	}
	
	private void newWriter() {
		try {
			writer = new BufferedWriter(new FileWriter("snakefruit.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void resetSave() {
		try {			
			BufferedWriter snakeWriter = new BufferedWriter(new FileWriter("snakefruit.txt"));
			snakeWriter.write("non-retrievable\n");
			snakeWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<int[]> getSnake() {
		ArrayList<int[]> snakeBody = new ArrayList<>();
		try {
			BufferedReader snakeReader = new BufferedReader(new FileReader("snakefruit.txt"));
			String line = null;
			String[] cellString;
			int[] cell = new int[3];
			boolean noSave = false;
			
			if ((line = snakeReader.readLine()) == null || line.equals("non-retrievable")) {
				snakeReader.close();
				return null;
			}
			
			if (!line.equals("s")) {
				while (!(line = snakeReader.readLine()).equals("s")) {
					noSave = true;
				}
				if (line.equals("s")) {
					noSave = false;
				}
			}
			
			if (noSave) {
				snakeReader.close();
				return null;
			}
			
			
			while ((line = snakeReader.readLine()) != null && !line.equals("f")) {
				cellString = line.split("/");
				for (int i = 0; i < cellString.length; i++) {
					cell[i] = Integer.parseInt(cellString[i]);
				}
				snakeBody.add(cell);
				cell = new int[3];
			}
			
			snakeReader.close();
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			System.out.println("Could not read file.");
			e.printStackTrace();
			return null;
		}
		
		return snakeBody;
	}
	
	public int[] getFruit() {
		int[] fruit = new int[2];
		try {
			BufferedReader fruitReader = new BufferedReader(new FileReader("snakefruit.txt"));
			String[] fruitString;
			String line = null;
			boolean noSave = false;
			
			if ((line = fruitReader.readLine()) == null || line.equals("non-retrievable")) {
				fruitReader.close();
				return null;
			}
			if (!line.equals("f")) {
				while (!(line = fruitReader.readLine()).equals("f")) {
					noSave = true;
				}
				if (line.equals("f")) {
					noSave = false;
				}
			}
			
			if (noSave) {
				fruitReader.close();
				return null;
			}
			
			while ((line = fruitReader.readLine()) != null && !line.equals("s")) {
				fruitString = line.split("/");
				fruit[0] = Integer.parseInt(fruitString[0]);
				fruit[1] = Integer.parseInt(fruitString[1]);
			}
			
			fruitReader.close();
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not read file.");
			return null;
		}
		return fruit;
	}
	
	public void write(ArrayList<int[]> snakeBody, int[] fruit) {
		if (writer == null)
			newWriter();
		
		if (fruit == null) {
			try {
				writer.write("s\n");
				for (int[] cell : snakeBody) {
					writer.write(cell[0] + "/");
					writer.write(cell[1] + "/");
					writer.write(cell[2] + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (snakeBody == null) {
			try {				
				writer.write("f\n");
				writer.write(fruit[0] + "/");
				writer.write(fruit[1] + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void closeWriter() {
		try {
			if (writer != null) {				
				writer.close();
			}
		} catch (IOException e) {
			
		}
	}
	
}
