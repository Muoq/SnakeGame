package snake.io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener{
	public boolean[] keys = new boolean[120];
	public boolean up, down, left, right, pause, quit;
	
	public void update() {
		up = keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP];
		down = keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN];
		left = keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT];
		right = keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT];
		pause = keys[KeyEvent.VK_P];
		quit = keys[KeyEvent.VK_Q];
	}
	
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}
	
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}
	
	public void keyTyped(KeyEvent e) {
	}

	public boolean[] getDirections() {
		return new boolean[] {up, down, left, right};
	}
	
	public boolean getPause() {
		return pause;
	}
	
	public boolean getQuit() {
		return quit;
	}
}
