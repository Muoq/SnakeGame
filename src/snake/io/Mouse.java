package snake.io;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener {
	
	boolean[] buttons;
	public boolean leftButton;
	
	public Mouse() {
		buttons = new boolean[20];
	}
	
	public void update() {
		leftButton = buttons[MouseEvent.BUTTON1];
	}
	
	public boolean getButton() {
		return leftButton;
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {		
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		buttons[e.getButton()] = true;
	}

	public void mouseReleased(MouseEvent e) {
		buttons[e.getButton()] = false;
	}
	
	public void resetButton() {
		leftButton = false;
	}
	
}
