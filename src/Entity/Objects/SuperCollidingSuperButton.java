package Entity.Objects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import TileMap.TileMap;
import Entity.Animation;
import Entity.MapObject;

public class SuperCollidingSuperButton extends MapObject{
	
	private ArrayList<BufferedImage[]> sprites;
	
	private boolean pressed;
	
	private final int[] numFrames = {1, 1};
	
	private static final int IDLE = 0;
	private static final int PRESSED = 1;
	
	public SuperCollidingSuperButton(TileMap tm){
		super(tm);
		
		fallSpeed=0.1;
		
		width=32;
		height=10;
		cwidth=32;
		cheight=11;
		
		//load sprites
		try{
			BufferedImage spritesheet=ImageIO.read(getClass().getResourceAsStream("/Sprites/Objects/Button.gif"));
			sprites=new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 2; i++) {
				
				BufferedImage[] bi =
					new BufferedImage[numFrames[i]];
				
				for(int j = 0; j < numFrames[i]; j++) {
						bi[j] = spritesheet.getSubimage(
								j * width,
								i * height,
								width,
								height
						);
				}
				sprites.add(bi);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		animation=new Animation();
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(1000);
	}
	
	public void getNextPosition(){
		if(falling)
			dy+=fallSpeed;
	}
	
	
	public void update(){
		
		//update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp,ytemp);
		
		if(pressed){
			animation=new Animation();
			animation.setFrames(sprites.get(PRESSED));
			animation.setDelay(1000);
			animation.update();
		}
		else{
			animation=new Animation();
			animation.setFrames(sprites.get(IDLE));
			animation.setDelay(1000);
			animation.update();
		}
		
		//System.out.println(pressed);
	}
	
	public void draw(Graphics2D g){
		
		setMapPosition();
		
		super.draw(g);
	}

	public void setPressed(boolean b) {
		pressed=b;
	}
	
	public boolean isPressed(){
		return pressed;
	}
}
