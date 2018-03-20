package snake.util;

public class Vector2i {
	
	public int x;
	public int y;
	
	public Vector2i() {
		
	}
	
	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2i(Vector2i vector) {
		this.x = vector.getX();
		this.y = vector.getY();
	}
	
	public void test() {
		Vector2i position = new Vector2i();
		position.setX(40);
	}
	
	public Vector2i set(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vector2i setX(int x) {
		this.x = x;
		return this;
	}
	
	public Vector2i setY(int y) {
		this.y = y;
		return this;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public Vector2i add(Vector2i vector) {
		this.x += vector.getX();
		this.y += vector.getY();
		return this;
	}
	
	public Vector2i subtract(Vector2i vector) {
		this.x -= vector.getX();
		this.y -= vector.getY();
		return this;
	}
	
	public Vector2i tempAdd(Vector2i vector) {
		int x = this.x + vector.x;
		int y = this.y + vector.y;
		return new Vector2i(x, y);
	}
	public Vector2i tempSubtract(Vector2i vector) {
		int x = this.x - vector.x;
		int y = this.y - vector.y;
		return new Vector2i(x, y);
	}
	
}
