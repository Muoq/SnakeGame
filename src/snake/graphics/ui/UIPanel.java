package snake.graphics.ui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import snake.util.Vector2i;

public class UIPanel {
	
	public Vector2i position;
	private List<UIComponent> components = new ArrayList<UIComponent>();
	
	public UIPanel(Vector2i position) {
		this.position = position;
	}
	
	public UIPanel(int x, int y) {
		this(new Vector2i(x, y));
	}
	
	public void update() {
		for (UIComponent component : components) {
			component.setOffset(position);
			component.update();
		}
	}
	
	public void render(Graphics g) {
		for (UIComponent component : components) {
			component.render(g);
		}
	}
	
	public void addComponent(UIComponent component) {
		components.add(component);
	}
	
}
