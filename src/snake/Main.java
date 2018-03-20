package snake;

public class Main {

	public static void main(String[] args) {
		
		if (System.getProperty("os.name").equals("Mac OS X")) {
			System.out.println("title thing ran");
//			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Snake");
			
//			Game.launch();
		}
	}
	
}
