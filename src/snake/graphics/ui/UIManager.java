package snake.graphics.ui;

import java.awt.Canvas;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class UIManager {
	
	public List<UIPanel> panels = new ArrayList<UIPanel>();
	
	private static Canvas canvas;
	
	public UIManager(Canvas canvasArg) {
		canvas = canvasArg;
	}
	
	public static Canvas getCanvas() {
		return canvas;
	}
	
	public void update() {
		for (UIPanel panel : panels) {
			panel.update();
		}
	}
	
	public void render(Graphics g) {
		for (UIPanel panel : panels) {
			panel.render(g);
		}
	}
	
	public void addPanel(UIPanel panel) {
		panels.add(panel);
	}
}
