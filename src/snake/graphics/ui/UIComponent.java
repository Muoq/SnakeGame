package snake.graphics.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.IllegalComponentStateException;
import java.awt.MouseInfo;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import snake.util.Vector2i;

public class UIComponent {
	
	protected int width;
	protected int height;
	public Vector2i position, offset;
	protected Vector2i mouseLocationOnCanvas, mouseLocationOnComponent;
	
	private Canvas canvas;
	public Graphics g;
	
	protected Font font;
	protected String text;
	
	protected boolean isSizeToString;
	protected boolean isBackgroundVisible;
	protected boolean drawIcon;
	
	protected Color backgroundColor;
	protected Color foregroundColor;
	
	protected BufferedImage icon;

	private ArrayList<UIActionListener> ActionListeners = new ArrayList<UIActionListener>();
	
	public UIComponent(Vector2i position) {
		this.position = position;
		text = "";
		this.canvas = UIManager.getCanvas();
	}

	public UIComponent(BufferedImage icon) {
		this.width = icon.getWidth();
		this.height = icon.getHeight();
		this.icon = icon;
		drawIcon = true;
	}
	
	
	public void update() {
		
	}
	
	public void render(Graphics g) {
		renderString(g);
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
		
		g.setFont(font);
		g.setColor(foregroundColor);
		g.drawString(text, x, y);
	}
	
	public void setOffset(Vector2i offset) {
		this.offset = offset;
	}
	
	public void addActionListener(UIActionListener listenerToAdd) {
		ActionListeners.add(listenerToAdd);
	}
	
	public void actionPerformed() {
		for (UIActionListener elem : ActionListeners) {
			elem.actionPerformed();
		}
	}
	
	public void setPreferredSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void setIcon(BufferedImage icon) {
		this.icon = icon;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setLocation(int x, int y) {
		position = new Vector2i(x, y);
	}
	
	public void setSizeToString(boolean isSizeToString) {
		this.isSizeToString = isSizeToString;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	public void setTextColor(int hexaColor) {
		this.foregroundColor = new Color(hexaColor);
	}
	
	public void setTextColor(Color color) {
		this.foregroundColor = color;
	}
	
	public void setBackgroundColor(int hexaColor) {
		this.backgroundColor = new Color(hexaColor);
	}
	
	public void setBackgroundColor(Color color) {
		this.backgroundColor = color;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	protected void calculateMouseLocation() {
		Vector2i mouseLocation = new Vector2i(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
		Vector2i canvasLocation = new Vector2i(0, 0);
		try {			
			canvasLocation = new Vector2i(canvas.getLocationOnScreen().x, canvas.getLocationOnScreen().y);
		} catch (IllegalComponentStateException e) {
		}
		
		Vector2i mouseLocationOnCanvas = new Vector2i(mouseLocation.subtract(canvasLocation));
		mouseLocationOnComponent = new Vector2i(mouseLocationOnCanvas.subtract(offset).subtract(position));
	}
	
}









