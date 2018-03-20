package snake.graphics.ui;

import java.awt.Font;
import java.awt.Graphics;

import snake.util.Vector2i;

public class UILabel extends UIComponent {
	
	public UILabel(Vector2i position) {
		super(position);
		font = new Font("Press Start 2P", Font.PLAIN, 12);
	}
	
	public void render(Graphics g) {
		renderString(g);
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	
	
}
