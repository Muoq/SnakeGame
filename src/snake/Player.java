package snake;

import java.awt.Font;

import snake.graphics.ui.UILabel;
import snake.graphics.ui.UIManager;
import snake.graphics.ui.UIPanel;

import snake.io.HighScore;
import snake.util.Vector2i;

public class Player {
	
	private UIManager ui;
	private UIPanel scorePanel;
	private UILabel scoreLabel;
	private UILabel highScoreLabel;
	public Font pressStart;
	public String highScoreText;
	
	private int score;
	private int highScore;
	private int highScoreWidth;
	
	public Player() {
		ui = Game.getUIManager();
		
		highScore = HighScore.getHighScore();
		
		highScoreText = "High Score:" + highScore;
		pressStart = new Font("Press Start 2P", Font.PLAIN, 14);
	}
	
	public void createScoreLabels() {
		scorePanel = new UIPanel(new Vector2i(0, 0));
		scoreLabel = new UILabel(new Vector2i(5, 8));
		
		highScoreLabel = new UILabel(new Vector2i(Game.width - highScoreWidth - 5, 8));
		
		scoreLabel.setFont(pressStart);
		scoreLabel.setText("Score:" + score);
		scoreLabel.setSizeToString(true);
		
		highScoreLabel.setFont(pressStart);
		highScoreLabel.setText("High Score:" + highScore);
		highScoreLabel.setSizeToString(true);
		
		scorePanel.addComponent(scoreLabel);
		scorePanel.addComponent(highScoreLabel);
		ui.addPanel(scorePanel);
		
		ui.update();
	}
	
	public void updateScoreLabels() {
		scoreLabel.setText("Score:" + score);
		highScoreLabel.position = new Vector2i(Game.width - highScoreWidth - 5, 8);
		highScoreText = "High Score:" + highScore;
		highScoreLabel.setText(highScoreText);
	}
	
	public void updateScore() {
		if (score > HighScore.getHighScore()) {
			highScore = score;
			HighScore.writeHighScore(highScore);
		}
	}
	
	public void addScore() {
		score++;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void resetScore() {
		score = 0;
	}
	
	public void setHighScoreWidth(int highScoreWidth) {
		this.highScoreWidth = highScoreWidth;
	}
	
}
