package snake.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Screen {
	
	public static int squareSide = 20;
	public static int snakeWidth = 14;
	public static int snakeLength = (squareSide - snakeWidth) + 2 * snakeWidth;
	public static int borderThickness = 18;
	public static int gameFieldYPosition = 30;
	public static int gameFieldHeight = 260;
	
	private int width, height;
	public int[] pixels;
	public Graphics g;
	
	private int color;
	private Color colorObj;
	private int bgColor;
	
	private Font font;
	
	public ArrayList<BufferedImage> arrowImages = new ArrayList<BufferedImage>();
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		
		pixels = new int[this.width * this.height];
		
		this.color = 0xffffff;
		
		Fonts.loadFont("/fonts/PressStart2P.ttf");
		
		drawBorders();
	}
	
	public void clear() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixels[x + y * width] = this.bgColor;
			}
		}
	}
	
	public void render() {
		for (int y = 0; y < height; y++) {
			if (y < 0 || y >= height) break;
			for (int x = 0; x < width; x++) {
				if (x < 0 || x >= width) break;
				pixels[x + y * width] = this.color;
			}
		}
	}
	
		
	
	public void fillRect(int x, int y, int widthArg, int heightArg) {	
		boolean isOOBX = false; //out of bounds
		boolean isOOBY = false;
		
		for (int xx = 0; xx < widthArg; xx++) {
			
			if (x + xx >= width) 
				break;
			if (x + xx < 0)
				isOOBX = true;
			else
				isOOBX = false;
			for (int yy = 0; yy < heightArg; yy++) {
				if (y + yy >= height) 
					break;
				if (y + yy < 0)
					isOOBY = true;
				else
					isOOBY = false;
				
				if (!isOOBX & !isOOBY) {
					pixels[x + y * this.width + xx + yy * width] = this.color;
				}
			}
		}
	}
	
	public void drawBorders() {
		//Horizontal Upper
		fillRect(squareSide / 2 + (squareSide - borderThickness) / 2, gameFieldYPosition + (squareSide - borderThickness) / 2,
				width - (squareSide / 2 + (squareSide - borderThickness) / 2) * 2, borderThickness);
		
		//Horizontal Lower
		fillRect(squareSide / 2 + (squareSide - borderThickness) / 2,
				squareSide + gameFieldHeight + gameFieldYPosition + (squareSide - borderThickness) / 2,
				width - (squareSide / 2 + (squareSide - borderThickness) / 2) * 2,
				borderThickness);
		
		//Vertical Left
		fillRect(squareSide / 2 + (squareSide - borderThickness) / 2, gameFieldYPosition + (squareSide - borderThickness) / 2,
				borderThickness, squareSide + gameFieldHeight);
		
		// Vertical Right
		fillRect(width - (int) (squareSide * 1.5) + (squareSide - borderThickness) / 2, gameFieldYPosition + (squareSide - borderThickness) / 2,
				borderThickness, squareSide + gameFieldHeight);
	}
	
	public void drawSnake(ArrayList<int[]> snakeBody) {
		
		int snakePlacer = (squareSide - snakeWidth) / 2;
		if (snakeBody.size() == 1) {
			fillRect(squareSide * 3 / 2 + snakePlacer + snakeBody.get(0)[0] * squareSide,
					gameFieldYPosition + snakePlacer + snakeBody.get(0)[1] * squareSide + squareSide,
					snakeWidth, snakeWidth);
		}
		for (int i = 0; i < snakeBody.size() - 1; i++) {
			int[] tempCell = snakeBody.get(i);
//			Direction: Up
			if (tempCell[2] == 1) {
				fillRect(squareSide * 3 / 2 + snakePlacer + tempCell[0] * squareSide, gameFieldYPosition + squareSide + snakePlacer + tempCell[1] * squareSide,
						snakeWidth, snakeLength);
//			Direction: Down
			} else if (tempCell[2] == 2) {
				fillRect(squareSide * 3 / 2 + snakePlacer + tempCell[0] * squareSide, gameFieldYPosition + squareSide + snakePlacer + tempCell[1] * squareSide - (snakeLength - snakeWidth),
						snakeWidth, snakeLength);
//			Direction: Left
			} else if (tempCell[2] == 3) {
				fillRect(squareSide * 3 / 2 + snakePlacer + tempCell[0] * squareSide, gameFieldYPosition + squareSide + snakePlacer + tempCell[1] * squareSide,
						snakeLength, snakeWidth);
//			Direction: Right
			} else if (tempCell[2] == 4) {
				fillRect(squareSide * 3 / 2 + snakePlacer + tempCell[0] * squareSide - (snakeLength - snakeWidth), gameFieldYPosition + squareSide + snakePlacer + tempCell[1] * squareSide,
						snakeLength, snakeWidth);
			}
		}
	}
	
	public void drawFruit(int[] fruitLoc) {
		int fruitPlacer = (squareSide - snakeWidth) / 2;
		
		fillRect(squareSide * 3 / 2 + fruitPlacer + fruitLoc[0] * squareSide, gameFieldYPosition + squareSide + fruitPlacer + fruitLoc[1] * squareSide,
				snakeWidth, snakeWidth);
	}
	
	public void renderString(String text, int x, int y) {
//		System.out.println("x: " + x);
//		System.out.println("y: " + y);
		g.setFont(this.font);
		g.setColor(this.colorObj);
		g.drawString(text, x, y);
	}
	
	public void printGameOverText(Graphics g) {
		int fontHeight = 60;
		Font font = new Font("Press Start 2P", Font.PLAIN, fontHeight);
		g.setFont(font);
		g.setColor(Color.black);
		
		int yGame;
		int yOver;
		int xGame;
		int xOver;
		int gameStringWidth = g.getFontMetrics().stringWidth("Game");
		int overStringWidth = g.getFontMetrics().stringWidth("Over");
		
		xGame = (width - gameStringWidth) / 2;
		yGame = gameFieldYPosition + squareSide + ((gameFieldHeight - squareSide) / 2 + fontHeight) / 2;
		xOver = (width - overStringWidth) / 2;
		yOver = gameFieldYPosition + squareSide + gameFieldHeight - ((gameFieldHeight - squareSide) / 2 - fontHeight) / 2;
		
		g.drawString("Game", xGame, yGame);
		g.drawString("Over", xOver, yOver);
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	public void setColor(int c) {
		this.color = c;
		this.colorObj = new Color(c);
	}
	
	public void setColor(Color c) {
		this.colorObj = c;
		this.color = c.getRGB();
	}
	
	public void setBgColor(int c) {
		this.bgColor = c;
	}
}
