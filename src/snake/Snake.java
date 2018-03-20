package snake;

import java.util.ArrayList;

import snake.graphics.Screen;
import snake.io.Serializer;

public class Snake {
	
	Serializer serializer = new Serializer();

	ArrayList<int[]> snakeBody;

	int[] dirCoefficient;
	int score;

	public Snake(int[] loc) {
		this.snakeBody = new ArrayList<int[]>();
		this.snakeBody.add(loc);
		
		load();

		dirCoefficient = new int[2];
	}
	
	public void move() {
		int[] newCell = new int[3];

		newCell[0] = this.snakeBody.get(0)[0] + dirCoefficient[0];
		newCell[1] = this.snakeBody.get(0)[1] + dirCoefficient[1];
		if (dirCoefficient[0] == 1) {
			newCell[2] = 4;
		} else if (dirCoefficient[0] == -1) {
			newCell[2] = 3;
		} else if (dirCoefficient[1] == 1) {
			newCell[2] = 2;
		} else if (dirCoefficient[1] == -1) {
			newCell[2] = 1;
		} else {
			newCell[2] = -1;
		}

		int snakeSize = snakeBody.size();
		for (int i = 0; i < snakeSize; i++) {
			int[] temp = snakeBody.get(i);
			snakeBody.set(i, newCell);
			newCell = temp;
		}
	}
	
	public ArrayList<int[]> getBody() {
		return snakeBody;
	}
	
	public void addCell(int[] newLoc) {
		this.snakeBody.add(newLoc);
	}
	
	public void setDir(String dirArg) {
//		dirArg = dirArg.toLowerCase();
		
		switch (dirArg) {
		case "up":
			dirCoefficient = new int[] {0, -1};
			break;
		case "down":
			dirCoefficient = new int[] {0, 1};
			break;
		case "left":
			dirCoefficient = new int[] {-1, 0};
			break;
		case "right":
			dirCoefficient = new int[] {1, 0};
			break;
		default:
			dirCoefficient = new int[] {0, 0};
			
		}
	}
	
	public void save() {
		if (snakeBody.size() > 1 || snakeBody.get(0)[0] != Game.width / Screen.squareSide / 2 - 2 ||
				snakeBody.get(0)[1] != Screen.gameFieldHeight / Screen.squareSide / 2) {			
			serializer.write(this.snakeBody, null);
		} else {
			serializer.resetSave();
		}
	}
	
	public void saveFruit(int[] fruit) {
		if (snakeBody.size() > 1 || snakeBody.get(0)[0] != Game.width / Screen.squareSide / 2 - 2 ||
				snakeBody.get(0)[1] != Screen.gameFieldHeight / Screen.squareSide / 2) {			
			serializer.write(null, fruit);
		}
	}
	
	public void resetSave() {
		serializer.resetSave();
	}
	
	private void load() {
		ArrayList<int[]> tempBody = serializer.getSnake();
//		System.out.println(tempBody);
		if (tempBody != null) {
			snakeBody = tempBody;
			score = snakeBody.size() - 1;
		}
	}
	
	public void closeWriter() {
		serializer.closeWriter();
	}
	
	public int[] getFruit() {
		return serializer.getFruit();
	}
	
	public int getScore() {
		return score;
	}

}
