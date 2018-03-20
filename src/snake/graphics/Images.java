package snake.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Images {
	
	public ArrayList<BufferedImage> arrowImages = new ArrayList<BufferedImage>(8);
	public ImageIcon icon;
	
	public void loadArrowImages() {
		int index = 0;
		try {
			for (int i = 0; i < 4; i++) {
				arrowImages.add(ImageIO.read(getClass().getResource("/imgs/button" + i + ".png")));
			}
			for (int i = 0; i < 4; i++) {
				index = i + 4;
				arrowImages.add(ImageIO.read(getClass().getResource("/imgs/button" + index + ".png")));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ImageIcon loadIcon() {
		icon = new ImageIcon(getClass().getResource("/imgs/snakeicon.png"));
		return icon;
	}
}
