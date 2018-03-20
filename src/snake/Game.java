package snake;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import snake.graphics.Images;
import snake.graphics.Screen;
import snake.graphics.ui.UIActionListener;
import snake.graphics.ui.UIButton;
import snake.graphics.ui.UIManager;
import snake.graphics.ui.UIPanel;
import snake.io.Keyboard;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	public static int width = 360;
	public static int height = 640;
	
	private Thread gameThread;
	boolean running = false;
	boolean started;
	boolean pause, pauseReset;
	boolean gameOver, gameOverComplete;
	boolean printGameOverText;
	int gameOverCounter;
	
	private String title = "Snake";
	private JFrame frame;
	private Screen screen;
	private Graphics g;
	private Snake snake;
	private Player player;
	private int[] fruit = new int[2];
	private boolean isDrawFruit;
	private Keyboard keyboard;
	
	private static UIManager uiManager;
	private static UIButton pauseButton;
	private static UIButton restartButton;
	
	private String snakeDir;
	private String tempDir;
	boolean[] keyDirections;
	boolean dirSet;
	boolean listenForKeys;
	boolean isButtonPress;
	boolean deacUp, deacDown, deacLeft, deacRight;
	
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	private List<BufferedImage> arrowImages = new ArrayList<BufferedImage>();
	private List<UIButton> arrowButtons = new ArrayList<UIButton>();
	
	public Game() {
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		
		frame = new JFrame(title);
		frame.setIconImage(new Images().loadIcon().getImage());
		
		screen = new Screen(width, height);
		screen.setBgColor(0xffffff);

		uiManager = new UIManager(this);

		player = new Player();
		player.createScoreLabels();
		
		snake = new Snake(new int[] {width / Screen.squareSide / 2 - 2, Screen.gameFieldHeight / Screen.squareSide / 2, -1});
		snakeDir = "";
		tempDir = "";
		
		player.setScore(snake.getScore());
		
		keyboard = new Keyboard();
		this.addKeyListener(keyboard);
		frame.addKeyListener(keyboard);
		listenForKeys = true;
		
		Images arrowImageLoader = new Images();
		arrowImageLoader.loadArrowImages();
		arrowImages = arrowImageLoader.arrowImages;
		
		if (snake.getFruit() == null) {
			newFruit();
		} else {
			fruit = snake.getFruit();
			isDrawFruit = true;
		}
	}
	
	public static UIManager getUIManager() {
		return uiManager;
	}
	
	public synchronized void start() {
		gameThread = new Thread(this, "Snake Game");
		gameThread.start();
		running = true;
	}
	
	public synchronized void stop() {
		try {
			gameThread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void close() {
		snake.saveFruit(fruit);
		snake.save();
		snake.closeWriter();
		frame.dispose();
		System.exit(0);
	}
	
	private void restart() {
		snake.resetSave();
		snake = new Snake(new int[] {width / Screen.squareSide / 2 - 2, Screen.gameFieldHeight / Screen.squareSide / 2, -1});
		isDrawFruit = false;
		newFruit();
		snakeDir = "default";
		tempDir = "default";
		player.resetScore();
		pause = false;
	}
	
	public void run() {
		int fps = 0;
		int ups = 0;
		
		long pastTime = System.nanoTime();
		long pastTimeMS = System.currentTimeMillis();
		long pastMoveTime = System.nanoTime();
		
		double delta = 0;
		double deltaMove = 0;
		
		double nsFrequency = 1000000000.0 / 60;
		double moveSpeed = 180000000.0;
		while(running) {
			
			long nowTime = System.nanoTime();
			delta += (nowTime - pastTime) / nsFrequency;
			pastTime = nowTime;
			if (delta >= 1) {
				
				update();
				ups++;
				
				if (gameOver) {					
					updateGameOverText();
				}
				
				delta = 0;
			}
			
			long nowMoveTime = System.nanoTime();
			deltaMove += (nowMoveTime - pastMoveTime) / moveSpeed;
			pastMoveTime = nowMoveTime;
			if (deltaMove >= 1) {
				
				if (!pause & started) {					
					updateSnake();
				}
				
				deltaMove = 0;
			}
			
			render();
			fps++;
			
			if (System.currentTimeMillis() - pastTimeMS >= 1000) {
				frame.setTitle(title + " | ups: " + ups + ", fps: " + fps); 
				pastTimeMS += 1000;
				ups = 0;
				fps = 0;
			}
		}
		stop();
	}
	
	public void update() {
		if (listenForKeys) {
			updateKeys();
		}
		
		if (printGameOverText & g != null) {
			screen.printGameOverText(g);
		}
		
		updatePause();
		updateQuit();
		
		uiManager.update();
		
		player.updateScore();
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		screen.g = g;
		
		screen.clear();
		
		player.updateScoreLabels();

		screen.setColor(Color.BLACK);
		screen.drawBorders();
		
		screen.drawSnake(snake.getBody());
		
		
		if (isDrawFruit) {
			screen.drawFruit(fruit);
		}
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}		
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

		if (printGameOverText) {
			screen.printGameOverText(g);
		}
		
		int stringWidth = g.getFontMetrics(player.pressStart).stringWidth(player.highScoreText);
		player.setHighScoreWidth(stringWidth);
		
		if (pause) {
			pauseButton.setText("Resume");
		} else {
			pauseButton.setText("Pause");
		}
		
		int restartStringWidth = g.getFontMetrics(new Font("Press Start 2P", Font.PLAIN, 20)).stringWidth("Restart");
		restartButton.setLocation(width - restartStringWidth - 5, 0);

		pauseButton.setTextColor(Color.BLACK);
		restartButton.setTextColor(Color.BLACK);
		
		uiManager.render(g);
		
//		g.drawImage(arrowImages.get(0), 320, 200, null);
		g.dispose();
		
		bs.show();
	}
	
	private void updateSnake() {
		
		snakeDir = tempDir;
		dirSet = false;
		listenForKeys = true;
		
		snake.setDir(snakeDir);
		snake.move();
		checkWalls();
		checkBody();
		checkFruit();
	}
	
	private void updateGameOverText() {
		gameOverCounter++;
		if (gameOverCounter >= 80) {
			printGameOverText = false;
			gameOverCounter = 0;
		}
	}
	
	private void gameOver() {
		isDrawFruit = false;
		printGameOverText = true;
		snake.resetSave();
		snake = new Snake(new int[] {width / Screen.squareSide / 2 - 2, Screen.gameFieldHeight / Screen.squareSide / 2, -1});
		snakeDir = "default";
		tempDir = "default";
		newFruit();
		player.resetScore();
	}
	
	private void updateKeys() {
		keyboard.update();
		
		if (!isButtonPress) {
			keyDirections = keyboard.getDirections();
		}
		isButtonPress = false;
		
		if (!pause) {
		
			if (!keyDirections[0]) {
				deacUp = false;
			}
			if (!keyDirections[1]) {
				deacDown = false;
			}
			if (!keyDirections[2]) {
				deacLeft = false;
			}
			if (!keyDirections[3]) {
				deacRight = false;
			}
			
			if (keyDirections[0] & snakeDir != "down" & !deacUp) {			
				tempDir = "up";
				deacUp = true;
				dirSet = true;
			} else if (keyDirections[1] & snakeDir != "up" & !deacDown) {
				tempDir = "down";
				deacDown = true;
				dirSet = true;
			} else if (keyDirections[2] & snakeDir != "right" & !deacLeft) {
				tempDir = "left";
				deacLeft = true;
				dirSet = true;
			} else if (keyDirections[3] & snakeDir != "left" & !deacRight) {
				tempDir = "right";
				deacRight = true;
				dirSet = true;
			}
			
			if (dirSet) {
				started = true;
				listenForKeys = false;
				printGameOverText = false;
				gameOverCounter = 0;
				gameOver = false;
				gameOverComplete = true;
				isDrawFruit = true;
			}
			
		}
	}
	
	private void updatePause() {
		keyboard.update();
		
		if (pauseReset & keyboard.getPause()) {
			pause = !pause;
			pauseReset = false;
		}
		if (!keyboard.getPause()) {
			pauseReset = true;
		}
	}
	
	private void updateQuit() {
		keyboard.update();
		
		if (keyboard.getQuit()) {
			close();
		}
	}
	
	private void checkWalls() {
		if (snake.getBody().get(0)[0] < 0 || snake.getBody().get(0)[0] > width / Screen.squareSide - 4) {
			gameOver = true;
			gameOver();
		}
		if (snake.getBody().get(0)[1] < 0 || snake.getBody().get(0)[1] > Screen.gameFieldHeight / Screen.squareSide - 1) {
			gameOver = true;
			gameOver();
		}
	}
	
	private void checkBody() {
		ArrayList<int[]> tempBody = snake.getBody();
		
		for (int i = 0; i < tempBody.size() - 4; i++) {
			if (tempBody.get(i + 4)[0] == tempBody.get(0)[0] & tempBody.get(i+4)[1] == tempBody.get(0)[1]) {
				gameOver = true;
				gameOver();
			}
		}
	}
	
	private void checkFruit() {
		if (snake.getBody().get(0)[0] == fruit[0] & snake.getBody().get(0)[1] == fruit[1]) {
			
			snake.addCell(snake.getBody().get(snake.getBody().size() - 1));
			newFruit();
			player.addScore();
			
		}
	}
	
	private void newFruit() {
		boolean isIn = true;
		int[] tempFruit = new int[2];
		Random random = new Random();
		
		for (int j = 0; j < 15 * 13; j++) {			
			tempFruit[0] = random.nextInt(width / Screen.squareSide - 3);
			tempFruit [1] = random.nextInt(Screen.gameFieldHeight / Screen.squareSide);
			
			isIn = false;
			for (int i = 0; i < snake.getBody().size(); i++) {
				if (snake.getBody().get(i)[0] == tempFruit[0] & snake.getBody().get(i)[1] == tempFruit[1]) {
					isIn = true;
				}
			}
			if (!isIn)
				break;
		}
		
		fruit = tempFruit;
	}
	
	private void createGameFlowButtons() {
		UIPanel testPanel = new UIPanel(0, Screen.gameFieldYPosition + Screen.gameFieldHeight + 2 * Screen.squareSide + 5);
		Font pressStart20p = new Font("Press Start 2P", Font.PLAIN, 20);

		pauseButton = new UIButton(5, 0);
		restartButton = new UIButton(0, 0);
		
		pauseButton.setTextColor(0);
		pauseButton.setFont(pressStart20p);
		pauseButton.setText("Pause");
		pauseButton.setSizeToString(true);
		pauseButton.setBackgroundVisible(false);
		pauseButton.addActionListener(new PauseButtonListener());
		
		restartButton.setTextColor(0);
		restartButton.setPressedTextColor(0x636363);
		restartButton.setFont(pressStart20p);
		restartButton.setText("Restart");
		restartButton.setSizeToString(true);
		restartButton.setBackgroundVisible(false);
		restartButton.addActionListener(new RestartListener());
		
		testPanel.addComponent(restartButton);
		testPanel.addComponent(pauseButton);
		uiManager.addPanel(testPanel);
		uiManager.update();
	}
	
	private void createDirectionalButtons() {
		UIPanel arrowButtonPanel = new UIPanel(0,
				Screen.gameFieldHeight + Screen.gameFieldYPosition + Screen.squareSide * 2 + 40);

		List<UIActionListener> arrowButtonListeners = new ArrayList<UIActionListener>();
		arrowButtonListeners.add(new UpButtonListener());
		arrowButtonListeners.add(new DownButtonListener());
		arrowButtonListeners.add(new LeftButtonListener());
		arrowButtonListeners.add(new RightButtonListener());
		
		for (int i = 0; i < 4; i++) {
			arrowButtons.add(new UIButton(arrowImages.get(i)));
			arrowButtons.get(i).addActionListener(arrowButtonListeners.get(i));
			arrowButtons.get(i).setPressedIcon(arrowImages.get(i + 4));
		}
		
		arrowButtons.get(0).setLocation((width - arrowButtons.get(0).getWidth()) / 2, 0);
		arrowButtons.get(1).setLocation((width - arrowButtons.get(0).getWidth()) / 2,
				arrowButtons.get(0).getHeight() + arrowButtons.get(2).getHeight());
		arrowButtons.get(2).setLocation((width - (arrowButtons.get(2).getWidth() * 2) - arrowButtons.get(0).getWidth()) / 2,
				arrowButtons.get(0).getHeight());
		arrowButtons.get(3).setLocation((width + arrowButtons.get(0).getWidth()) / 2, arrowButtons.get(0).getHeight());
		
		
		for (int i = 0; i < 4; i++) {
			arrowButtonPanel.addComponent(arrowButtons.get(i));
		}
		
		uiManager.addPanel(arrowButtonPanel);
		uiManager.update();
		
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.frame.setResizable(false);
		game.frame.setAlwaysOnTop(false);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setLocationRelativeTo(null);
		game.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		game.frame.setVisible(true);
		
		game.frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				game.close();
			}
		});
		
		game.requestFocus();	
		
		game.createGameFlowButtons();
		game.createDirectionalButtons();
		
		game.start();
	}
	
	public class PauseButtonListener implements UIActionListener {
		public void actionPerformed() {
			pause = !pause;
		}
	}
	
	public class RestartListener implements UIActionListener {
		public void actionPerformed() {
			restart();
		}
	}
	
	public class UpButtonListener implements UIActionListener {

		public void actionPerformed() {
			isButtonPress = true;
			keyDirections[0] = true;
		}
		
	}
	public class DownButtonListener implements UIActionListener {
		
		public void actionPerformed() {
			isButtonPress = true;
			keyDirections[1] = true;
		}
		
	}
	public class LeftButtonListener implements UIActionListener {
		
		public void actionPerformed() {	
			isButtonPress = true;
			keyDirections[2] = true;
		}
		
	}
	public class RightButtonListener implements UIActionListener {
		
		public void actionPerformed() {			
			isButtonPress = true;
			keyDirections[3] = true;
		}
		
	}
	
}
