package Entity.Objects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.MapObject;
import TileMap.TileMap;

public class Crate extends MapObject{
	
	private BufferedImage[] sprites;
	
	private boolean pushed;
	
	public Crate(TileMap tm){
		super(tm);
		
		moveSpeed=1;
		maxSpeed=1;
		stopSpeed = 0.4;
		maxFallSpeed=10.0;
		fallSpeed=0.1;
		
		width=32;
		height=32;
		cwidth=30;
		cheight=30;
		
		//load sprites
		try{
			BufferedImage spritesheet=ImageIO.read(getClass().getResourceAsStream("/Sprites/Objects/crate.gif"));
			sprites=new BufferedImage[1];
			for(int i=0;i<sprites.length;i++){
				sprites[i]=spritesheet.getSubimage(i*width, 0, width, height);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		animation=new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
		
		right=true;
		facingRight=true;
	}
	
	private void getNextPosition(){
		//movement
		if(left&&pushed) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right&&pushed) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		else if(dx>0){
			dx-=stopSpeed;
			if(dx<0)
				dx=0;
		}
		else if(dx<0){
			dx+=stopSpeed;
			if(dx>0)
				dx=0;
		}
		
		//falling
		if(falling){
			dy+=fallSpeed;
		}
	}
	
	public void update(){
		
		//update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp,ytemp);
		
		//update animation
		animation.update();
		
	}
	
	public void draw(Graphics2D g){
		
		setMapPosition();
		
		super.draw(g);
	}
	
	public void setPushed(boolean b){
		pushed=b;
	}
	
	public void setRight(boolean b){
		right=b;
	}
	
	public void setLeft(boolean b){
		left=b;
	}

	public void checkButtonCollisions(ArrayList<SuperCollidingSuperButton> buttons) {
		for(int i=0;i<buttons.size();i++){
			if(buttons.get(i).isPressed()){
				//System.out.println("Already Pressed!");
			}
			else{
				if(intersects(buttons.get(i))){
					//System.out.println("Pressed!");
					buttons.get(i).setPressed(true);
				}
				else{
					//System.out.println("Not Pressed!");
					buttons.get(i).setPressed(false);
				}
			}
		}
	}
}
