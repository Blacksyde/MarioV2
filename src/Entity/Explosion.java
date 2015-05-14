package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class Explosion extends MapObject{
	
	private boolean remove;
	private BufferedImage[] sprites;
	
	public Explosion(TileMap tm, int x, int y) {
		
		super(tm);
		this.x = x;
		this.y = y;
		
		width = 30;
		height = 30;
		
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Enemies/explosion.gif"
				)
			);
			
			sprites = new BufferedImage[6];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
					i * width,
					0,
					width,
					height
				);
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(70);
		
	}
	
	public void update() {
		checkTileMapCollision();
		setPosition(xtemp,ytemp);
		animation.update();
		if(animation.hasPlayedOnce()) {
			remove = true;
		}
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void draw(Graphics2D g) {
		setMapPosition();
		super.draw(g);
	}
	
}

















