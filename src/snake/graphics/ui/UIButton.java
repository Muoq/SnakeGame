package snake.graphics.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import snake.io.Mouse;
import snake.util.Vector2i;

public class UIButton extends UIComponent {
	
	protected static Canvas canvas = UIManager.getCanvas();
	protected static Mouse mouse = new Mouse();
	
	protected boolean mousePrePressed, listenForPrePress;
	boolean pressed, stopCallingActionListener;
	
	public boolean drawIcon;
	
	BufferedImage icon;
	
	BufferedImage pressedIcon;
	protected int pressedForegroundColorHexa;
	protected Color pressedForegroundColor;
	
	protected boolean altPressedColor;
	
	public UIButton(int x, int y) {
		super(new Vector2i(x, y));
		isBackgroundVisible = true;
		canvas.addMouseListener(mouse);
	}
	
	public UIButton(BufferedImage icon) {
		this(0, 0);
		this.icon = icon;
		this.width = icon.getWidth();
		this.height = icon.getHeight();
		drawIcon = true;
	}
	
	public void setPressedIcon(BufferedImage pressedIcon) {
		this.pressedIcon = pressedIcon;
	}
	
	public void setPressedTextColor(Color color) {
		if (color == null) {
			altPressedColor = false;
		} else {
			altPressedColor = true;
			this.pressedForegroundColor = color;
		}
	}
	
	public void setPressedTextColor(int color) {
		if (color < 0) {
			altPressedColor = false;
		} else {
			altPressedColor = true;
			this.pressedForegroundColor = new Color(color);
		}
	}
	
	public void update() {
		calculateMouseLocation();
		checkForAction();
	}
	
	public void render(Graphics g) {
		if (isBackgroundVisible) {
			renderBackground(g);
		}
		
		if (drawIcon) {
			renderIcon(g);
		}
		
		renderString(g);
	}
	
	protected void renderIcon(Graphics g) {
		if (pressed) {
			if (pressedIcon != null) {				
				g.drawImage(pressedIcon, position.tempAdd(offset).x, position.tempAdd(offset).y, null);
			} else {				
				g.drawImage(icon, position.tempAdd(offset).x, position.tempAdd(offset).y, null);		
			}
		} else {			
			g.drawImage(icon, position.tempAdd(offset).x, position.tempAdd(offset).y, null);		
		}
	}
	
	protected void renderBackground(Graphics g) {
		g.setColor(backgroundColor);
		Vector2i fillLocation = new Vector2i(position.tempAdd(offset));
		g.fillRect(fillLocation.x, fillLocation.y, this.width, this.height);
	}
	
	protected void renderString(Graphics g) {
		g.setFont(font);
		FontMetrics fontMetrics = g.getFontMetrics();
		
		if (isSizeToString) {
			this.width = fontMetrics.stringWidth(text);
			this.height = fontMetrics.getHeight();
		}
		
		int x = (this.width - fontMetrics.stringWidth(text)) / 2 + position.x + offset.x;
		int y = (this.height + fontMetrics.getHeight()) / 2 + position.y + offset.y;
		
		if (altPressedColor & pressed) {
			g.setColor(pressedForegroundColor);
		} else {
			g.setColor(foregroundColor);
		}
		
		g.setFont(font);
		g.drawString(text, x, y);
	}
	
	public void setBackgroundVisible(boolean isVisible) {
		isBackgroundVisible = isVisible;
	}
	
	protected void checkForAction() {
		mouse.update();
		if (mouse.getButton() & listenForPrePress) {
			mousePrePressed = true;
		} else {
			mousePrePressed = false;
		}
		
		if (mouseLocationOnComponent.x >= 0 & mouseLocationOnComponent.x < this.getWidth() & mouseLocationOnComponent.y >= 0 & mouseLocationOnComponent.y < this.getHeight() & 
				!mousePrePressed) {

			mouse.update();
			pressed = mouse.getButton();
			
			if (pressed & !stopCallingActionListener) {				
				actionPerformed();
				stopCallingActionListener = true;
			}
			
			if (!pressed) {
				stopCallingActionListener = false;
			}
			
			listenForPrePress = false;
		} else {
			listenForPrePress = true;
			stopCallingActionListener = false;
		}
		
	}
	
}