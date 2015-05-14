package GameState;

import java.awt.*;
import java.util.ArrayList;
import java.awt.event.KeyEvent;

import Audio.AudioPlayer;
import Entity.Enemy;
import Entity.Explosion;
import Entity.HUD;
import Entity.MapObject;
import Entity.Player;
import Entity.Enemies.*;
import Entity.Objects.Crate;
import Entity.Objects.SuperCollidingSuperButton;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class Level1State extends GameState
{
	private TileMap tileMap;
	private Background bg;
	
	private HUD hud;
	
	private Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	private ArrayList<Crate> crates;
	private ArrayList<SuperCollidingSuperButton> buttons;
	
	private ArrayList<Rectangle> tb;
	
	private AudioPlayer bgMusic;

	
	public Level1State(GameStateManager gsm)
	{
		this.gsm=gsm;
		init();
	}
	
	public void init()
	{
		tileMap=new TileMap(30);
		tileMap.loadTiles("/Tilesets/testtileset.gif");
		tileMap.loadMap("/Maps/testmap.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg=new Background("/Backgrounds/Background 1.gif", 0.2);
		//bg.setVector(1, 0);
		
		player = new Player(tileMap);
		
		bgMusic = new AudioPlayer("/Music/NGGYU.mp3");
		
		deathInit();
		
		hud = new HUD(player);
		
		explosions = new ArrayList<Explosion>();
		
		tb= new ArrayList<Rectangle>();
	}
	
	private void deathInit(){
		player.setPosition(100,100);
		player.reset();
		populateEnemies();
		
		//test chicken
		Chicken c1 = new Chicken(tileMap);
		c1.setPosition(150,100);
		enemies.add(c1);
		
		bgMusic.stop();
		bgMusic.play();
		bgMusic.setLooping(true);
	}
	
	private void populateEnemies(){
		
		enemies = new ArrayList<Enemy>();
		Point[] points = new Point[] {new Point(860,200), new Point (1525,200), new Point(1680, 200), new Point(1800, 200)};
		
		for(int i=0;i<points.length;i++){
			Chicken s = new Chicken(tileMap);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
		}
		
		crates = new ArrayList<Crate>();
		Crate c = new Crate(tileMap);
		Crate c2 = new Crate(tileMap);
		Crate c3 = new Crate(tileMap);
		c.setPosition(200,100);
		c2.setPosition(1220,70);
		c3.setPosition(2720,190);
		crates.add(c);
		crates.add(c2);
		crates.add(c3);
		
		buttons = new ArrayList<SuperCollidingSuperButton>();
		SuperCollidingSuperButton b = new SuperCollidingSuperButton(tileMap);
		SuperCollidingSuperButton b2 = new SuperCollidingSuperButton(tileMap);
		b.setPosition(300,100);
		b2.setPosition(2800,190);
		buttons.add(b);
		buttons.add(b2);
	}
	public void update()
	{
		//update the player
		player.update();
		
		//PRINT OUT COORDINATES
		//System.out.println("X= "+player.getx()+", Y= "+player.gety());
		
		//check if player is dead
		if(player.isDead()){
			deathInit();
			player.setDead(false);
			if(player.getLives()==0){
				gsm.setState(GameStateManager.DEADSTATE);
				bgMusic.close();
			}
		}
		
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
		bg.setPosition(tileMap.getx(),tileMap.gety());
	
		//attack enemies
		player.checkAttack(enemies);
		
		//check crate and button collisions for player
		player.checkObjectCollisions(crates, buttons);
		
		//check button collisions (crates)
		for(int i=0;i<crates.size();i++){
			for(int j=0;j<buttons.size();j++){
				crates.get(i).checkButtonCollisions(buttons);
			}
		}
		
		//update enemies
		for(int i=0;i<enemies.size();i++){
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead()){
				explosions.add(new Explosion(tileMap,e.getx(),e.gety()));
				enemies.remove(i);
				i--;
			}
		}
		
		//update explosions
		for(int i=0;i<explosions.size();i++){
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()){
				explosions.remove(i);
				i--;
			}
		}
		
		//update crates
		for(int i=0;i<crates.size();i++){
			crates.get(i).update();
		}
		
		//update buttons
		for(int i=0;i<buttons.size();i++){
			buttons.get(i).update();
		}
		
	}
	public void draw(Graphics2D g)
	{
		/*clean screen of the menu
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);*/
		
		//draw bg
		bg.draw(g);
		
		//draw tile map
		tileMap.draw(g);
		
		//draw crates
		for(int i=0;i<crates.size();i++){
			crates.get(i).draw(g);
		}
		
		//draw enemies
		for(int i=0;i<enemies.size();i++){
			enemies.get(i).draw(g);
		}
		
		//draw buttons
		for(int i=0;i<buttons.size();i++){
			buttons.get(i).draw(g);
		}
		
		//draw player
		player.draw(g);
		
		//draw HUD
		hud.draw(g);
		
		//draw explosions
		for(int i=0;i<explosions.size();i++){
			explosions.get(i).draw(g);
		}
		
		// draw transition boxes
		g.setColor(java.awt.Color.BLACK);

		if(tb.size()>0)
			for(int i = 0; i < tb.size(); i++)
				g.fill(tb.get(i));
	}
	public void keyPressed(int k)
	{
		if(k==KeyEvent.VK_LEFT){
			player.setLeft(true);
		}
		if(k==KeyEvent.VK_RIGHT){
			player.setRight(true);
		}
		if(k==KeyEvent.VK_UP){
			player.setUp(true);
		}
		if(k==KeyEvent.VK_DOWN){
			player.setDown(true);
		}
		if(k==KeyEvent.VK_A){
			player.setJumping(true);
		}
		if(k==KeyEvent.VK_S){
			player.setGliding(true);
		}
		if(k==KeyEvent.VK_D){
			player.setMelee();
		}
		if(k==KeyEvent.VK_F){
			player.setFiring();
		}
		if(k==KeyEvent.VK_C){
			player.setSprinting(true);
		}
		if(k==KeyEvent.VK_ESCAPE){
			gsm.setState(GameStateManager.MENUSTATE);
			bgMusic.close();
		}
	}
	public void keyReleased(int k)
	{
		if(k==KeyEvent.VK_LEFT){
			player.setLeft(false);
		}
		if(k==KeyEvent.VK_RIGHT){
			player.setRight(false);
		}
		if(k==KeyEvent.VK_UP){
			player.setUp(false);
		}
		if(k==KeyEvent.VK_DOWN){
			player.setDown(false);
		}
		if(k==KeyEvent.VK_A){
			player.setJumping(false);
		}
		if(k==KeyEvent.VK_S){
			player.setGliding(false);
		}
		if(k==KeyEvent.VK_C){
			player.setSprinting(false);
		}
	}
}