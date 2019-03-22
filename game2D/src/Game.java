import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import game2D.*;

// Game demonstrates how we can override the GameCore class
// to create our own 'game'. We usually need to implement at
// least 'draw' and 'update' (not including any local event handling)
// to begin the process. You should also add code to the 'init'
// method that will initialise event handlers etc. By default GameCore
// will handle the 'Escape' key to quit the game but you should
// override this with your own event handler.

/**
 * @author David Cairns
 *
 */
@SuppressWarnings("serial")

public class Game extends GameCore 
{
	// Useful game constants
	static int screenWidth = 1024;
	static int screenHeight = 768;
	int xoffset;
	int yoffset;

    float 	lift = 0.005f;
    float	gravity = 0.0001f;
    Window win;
    // Game state flags
    boolean flap = false;
    boolean l1 = false;
    boolean l2 = false;
    boolean l3 = false;
    boolean running = false;
    boolean spriteHit = false;
    boolean menuOn = false;
    //protected ScreenManager screen;
    // Game resources
    Animation landing;
    Animation background;
    Animation men;
    Sprite backgrounds;
    Sprite	player = null;
    Sprite menu = null;
    ArrayList<Sprite> ss = new ArrayList<Sprite>();
    ArrayList<Sprite> heart = new ArrayList<Sprite>();
    ArrayList<Sprite> laugh = new ArrayList<Sprite>();
    TileMap tmap = new TileMap();	// Our tile map, note that we load it in init()
    
    long total;         			// The score will be the total time elapsed since a crash


    /**
	 * The obligatory main method that creates
     * an instance of our class and starts it running
     * 
     * @param args	The list of parameters this program might use (ignored)
     */
    public static void main(String[] args) {

        Game gct = new Game();
        gct.init();
        // Start in windowed mode with the given screen height and width
        //does not work in full screen
        gct.run(false,screenWidth,screenHeight);
    }

    /**
     * Initialise the class, e.g. set up variables, load images,
     * create animations, register event handlers
     */
    public void init()
    {         
        Sprite s;	// Temporary reference to a sprite

        // Load the tile map and print it out so we can check it is valid
        tmap.loadMap("maps", "snapmap.txt");
        men = new Animation();
        men.addFrame(loadImage("images/menu_1.png"),100);
        menu = new Sprite(men);
        menu.setX(90);
        menu.setY(100);
        menu.setVelocityX(0);
        menu.setVelocityY(0);
        //ScreenManager screen = new ScreenManager();
        //DisplayMode displayMode = new DisplayMode(1024, 768, 32, 0);
        //screen.setFullScreen(displayMode);
        //Window window = screen.getFullScreenWindow();
        //window.setVisible(false);
        //window.setVisible(true);
        //window.addKeyListener(this);
       // window.addMouseListener(this);
        // Create a set of background sprites that we can 
        // rearrange to give the illusion of motion
        background = new Animation();
        background.addFrame(loadImage("images/snap_background.png"),100);
        backgrounds = new Sprite(background);
        landing = new Animation();
        landing.addFrame(loadImage("images/ghost.png"), 200);
        landing.addFrame(loadImage("images/ghost2.png"), 500);
        landing.addFrame(loadImage("images/ghost.png"), 200);
        landing.addFrame(loadImage("images/ghost3.png"), 500);
        // Initialise the player with an animation
        player = new Sprite(landing);
        addMouseListener(this);
        // Load a single cloud animation
        Animation st = new Animation();
        st.addFrame(loadImage("images/screenshot.png"), 2500);
        st.addFrame(loadImage("images/screenshot2.png"), 2500);
        
        // Create 3 enemy sprites at random positions off the screen
        // to the right
        for (int c=0; c<3; c++)
        {
        	s = new Sprite(st);
        	s.setX(((float)(screenWidth*0.75)) + (int)(Math.random()*500.0f));
        	s.setY(tmap.getPixelHeight() - (int)(Math.random()*900.0f));
        	s.setVelocityX((float)(Math.random()*0.4)-0.8f);
        	s.setVelocityY((float)(Math.random()*0.4)-0.4f);
        	s.show();
        	ss.add(s);
        }
        xoffset = 128;
        yoffset = tmap.getPixelHeight()/2;
        
        initialiseGame();
      		
    }

    /**
     * You will probably want to put code to restart a game in
     * a separate method so that you can call it to restart
     * the game.
     */
    public void initialiseGame()
    {
    	running = true;
    	total = 0;
    	l1 = true;
        player.setX(64);
        player.setY(tmap.getPixelHeight()/2);
        player.setVelocityX(0);
        player.setVelocityY(0);
        player.show();
        backgrounds.setX(player.getX()-150);
        backgrounds.setY(player.getY()-750);
        backgrounds.show();
    }
    
    //initialise level 2
    public void initialiseL2()
    {
    	l2 = true;
    	Sprite s;
    	Animation st = new Animation();
        st.addFrame(loadImage("images/heart.png"), 500);
        st.addFrame(loadImage("images/heart2.png"), 750);
        Animation plr = new Animation();
        plr.addFrame(loadImage("images/bird.png"), 2000);
        plr.addFrame(loadImage("images/bird2.png"), 2000);
        player = new Sprite(plr);
        tmap.loadMap("maps", "twittermap.txt");
        men = new Animation();
        men.addFrame(loadImage("images/menu2.png"),100);
        menu = new Sprite(men);
        background = new Animation();
        background.addFrame(loadImage("images/twit_bg.png"), 200);
        backgrounds = new Sprite(background);
        menu.setX(90);
        menu.setY(100);
        menu.setVelocityX(0);
        menu.setVelocityY(0);
        
    	total = 0;
    	player.setX(64);
        player.setY(tmap.getPixelHeight()/2);
        player.setVelocityX(0);
        player.setVelocityY(0);
        player.show();
        for (int c=0; c<3; c++)
        {
        	s = new Sprite(st);
        	s.setX(screenWidth + (int)(Math.random()*500.0f));
        	s.setY(tmap.getPixelHeight() - (int)(Math.random()*800.0f));
        	s.setVelocityX((float)(Math.random()*0.4)-0.8f);
        	s.setVelocityY((float)(Math.random()*0.4)-0.4f);
        	s.show();
        	heart.add(s);
        }
    }
    
    //initialise level3
    public void initialiseL3()
    {
    	l3 = true;
        tmap.loadMap("maps", "fbmap.txt");
    	Sprite s;
    	Animation st = new Animation();
        st.addFrame(loadImage("images/haha4.png"), 4000);
        st.addFrame(loadImage("images/haha2.png"), 4000);
        st.addFrame(loadImage("images/haha4.png"), 4000);
        st.addFrame(loadImage("images/haha3.png"), 4000);
        Animation plr = new Animation();
        plr.addFrame(loadImage("images/friends.png"), 100);
        player = new Sprite(plr);
        men = new Animation();
        men.addFrame(loadImage("images/menu3.png"),100);
        menu = new Sprite(men);
        menu.setX(90);
        menu.setY(100);
        menu.setVelocityX(0);
        menu.setVelocityY(0);
        background = new Animation();
        background.addFrame(loadImage("images/facebook_bg.png"),200);
        backgrounds = new Sprite(background);
    	total = 0;
    	player.setX(0);
        player.setY(tmap.getPixelHeight()/2);
        player.setVelocityX(0);
        player.setVelocityY(0);
        player.show();
        for (int c=0; c<4; c++)
        {
        	s = new Sprite(st);
        	s.setX(screenWidth + (int)(Math.random()*500.0f));
        	s.setY(tmap.getPixelHeight() - (int)(Math.random()*800.0f));
        	s.setVelocityX((float)(Math.random()*0.4)-0.8f);
        	s.setVelocityY((float)(Math.random()*0.4)-0.4f);
        	s.show();
        	laugh.add(s);
        }

    	
    }
    //when the enter key is pressed, restart to level1
    public void restart()
    {
    	l1 = true;
    	l2 = false;
    	l3 = false;
    	menuOn = false;
    	for (int i = 0; i < ss.size(); i++)
    		ss.remove(i);
    	for (int j = 0; j < heart.size(); j++)
    		heart.remove(j);
    	for (int k = 0; k < laugh.size(); k++)
    		laugh.remove(k);
    	init();
    }
    /**
     * Draw the current state of the game
     */
    public void draw(Graphics2D g)
    {    	
    	// Be careful about the order in which you draw objects - you
    	// should draw the background first, then work your way 'forward'

    	// First work out how much we need to shift the view 
    	// in order to see where the player is.
    	
        int xo = (int)-player.getX() + xoffset;
        int yo = (int)-player.getY() + yoffset;
        
        // If relative, adjust the offset so that
        // it is relative to the player

        // ...?
        
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        backgrounds.setOffsets(xo, yo);
    	backgrounds.draw(g);				//parallax background
        if (l1){
        	//g.drawImage(loadImage("images/snap_bg.png"), 0, 0, null);
        	
        	// Apply offsets to sprites then draw them
        	for (Sprite s: ss)
        	{
        		s.setOffsets(xo,yo);
        		s.draw(g);
        	}
        }
        if (l2){
        	g.drawImage(loadImage("images/twit_bg.png"), 0, 0, null);
        	for (Sprite s: heart)
        	{
        		s.setOffsets(xo, yo);
        		s.draw(g);
        	}
        }
        if (l3){
        	g.drawImage(loadImage("images/facebook_bg.png"), 0, 0, null);
        	for (Sprite s: laugh)
        	{
        		s.setOffsets(xo, yo);
        		s.draw(g);
        	}
        }
        // Apply offsets to player and draw 
        
        
        player.setOffsets(xo, yo);
        player.drawTransformed(g);
                
        // Apply offsets to tile map and draw  it
        tmap.draw(g,xo,yo);    
        
        // Show score and status information
        String msg = String.format("Score: %d", total);
        g.setColor(Color.darkGray);
        g.drawString(msg, getWidth() - 80, 50);
        if (menuOn)				//display menu
        {
            
       	 	menu.show();
       	 	menu.draw(g);
       	 }
        
        
    }

    /**
     * Update any sprites and check for collisions
     * 
     * @param elapsed The elapsed time between this call and the previous call of elapsed
     */    
    public void update(long elapsed)
    {
    	if (running)
    	{
        // Make adjustments to the speed of the sprite due to gravity
    		player.setVelocityY(player.getVelocityY()+(gravity*elapsed));
    	    	
    		player.setAnimationSpeed(1.0f);

                
	       	Sound hit;
	       	
	        // Now update the sprites animation and position
	        // Then check for any collisions that may have occurred
	        handleTileMapCollisions(player,elapsed,false);
	        if (l1)
	        {
	        	hit = new Sound("sounds/error.wav");
		        for (Sprite s: ss)
		        {
		        	if (s.boundingBoxCollision(player)) //first level of collision detection
		        	{
		        		if (s.boundingCircleCollision(player)); //second level
		        		{
		        			s.hide(); //remove sprite
		        			s.setX(0);
		        			total -= 5; 
		        			hit.start(); //play sound
		        			spriteHit = true; //so new sprite will be added
		        		}
		        	}
		        	handleTileMapCollisions(s,elapsed,true);
	        	
		        	s.update(elapsed);
		        }
		        player.update(elapsed);
		        
		        if (spriteHit)			//add new moving sprite
		        {
		        	Animation st = new Animation();
	    	        st.addFrame(loadImage("images/screenshot.png"), 5000);
	    	        st.addFrame(loadImage("images/screenshot2.png"), 5000);
	    			Sprite s = new Sprite(st);
	            	s.setX(((float)(player.getX()*0.75)) + (int)(Math.random()*1000.0f));
	            	s.setY(tmap.getPixelHeight() - (int)(Math.random()*800.0f));
	            	s.setVelocityX((float)(Math.random()*0.4)-0.8f);
	            	s.setVelocityY((float)(Math.random()*0.4)-0.4f);
	            	s.show();
	            	ss.add(s);
	            	spriteHit = false;
		        }
	        	
	        }
	        if (l2)
	        {
	        	hit = new Sound("sounds/coin.wav");
	        	for (Sprite s: heart)
	        	{
		        	if (s.boundingBoxCollision(player))
		        	{
		        		if (s.boundingCircleCollision(player));
		        		{
		        			s.hide();
		        			s.setX(0);
		        			total += 15;
		        			hit.start();
		        			spriteHit = true;
		        		}
		        	}
		        	handleTileMapCollisions(s,elapsed,true);
		        	s.update(elapsed);
	        	}
	        	player.update(elapsed);
	        	if (spriteHit)
	        	{
	        		Animation st = new Animation();
	                st.addFrame(loadImage("images/heart.png"), 3000);
	                st.addFrame(loadImage("images/heart2.png"), 5000);
	                Sprite t = new Sprite(st);
	                t.setX(((float)(screenWidth*0.75)) + (int)(Math.random()*1000.0f));
	            	t.setY(tmap.getPixelHeight() - (int)(Math.random()*800.0f));
	            	t.setVelocityX((float)(Math.random()*0.4)-0.8f);
	            	t.setVelocityY((float)(Math.random()*0.4)-0.4f);
	            	t.show();
	            	heart.add(t);
	            	spriteHit = false;
	        	}
	        }
	        if (l3)
	        {
	        	hit = new Sound("sounds/blah.wav");
	        	for (Sprite s: laugh)
	        	{
		        	if (s.boundingBoxCollision(player))
		        	{
		        		if (s.boundingCircleCollision(player));
		        		{
		        			s.hide();
		        			s.setX(0);
		        			total += 15;
		        			hit.start();
		        			spriteHit = true;
		        		}
		        	}
		        handleTileMapCollisions(s,elapsed,true);
	        	s.update(elapsed);
	        	}
	        	player.update(elapsed);
	        	if (spriteHit)
	        	{
	        		Animation st = new Animation();
	        		st.addFrame(loadImage("images/haha.png"), 4000);
	        	    st.addFrame(loadImage("images/haha2.png"), 4000);
	        	    st.addFrame(loadImage("images/haha.png"), 4000);
	        	    st.addFrame(loadImage("images/haha3.png"), 4000);
	                Sprite w = new Sprite(st);
	                w.setX(((float)(screenWidth*0.75)) + (int)(Math.random()*1000.0f));
	            	w.setY(tmap.getPixelHeight() - (int)(Math.random()*800.0f));
	            	w.setVelocityX((float)(Math.random()*0.4)-0.8f);
	            	w.setVelocityY((float)(Math.random()*0.4)-0.4f);
	            	w.show();
	            	laugh.add(w);
	            	spriteHit = false;
	        	}
        }
        
        if (l1 && total > 100){
        	l1 = false;
        	initialiseL2();}
        
        if (l2 && total == 75)
        {
        	l2 = false;
        	initialiseL3();
        }
        if (l3 && total > 75)
        {
        	men = new Animation();
        	men.addFrame(loadImage("images/end.png"), 100);
        	menu = new Sprite(men);
        	menu.setX(90);
            menu.setY(100);
            menu.setVelocityX(0);
            menu.setVelocityY(0);
        	menuOn = true;
        	running = false;
        }
        backgrounds.setVelocityX(player.getVelocityX());
        backgrounds.setVelocityY(player.getVelocityY());
        backgrounds.update(elapsed);
    	}   
    }
    
    
    /**
     * Checks and handles collisions with the tile map for the
     * given sprite 's'. Initial functionality is limited...
     * 
     * @param s			The Sprite to check collisions for
     * @param elapsed	How time has gone by
     */
    public void handleTileMapCollisions(Sprite s, long elapsed, boolean spr)
    {
    	// This method should check actual tile map collisions. For
    	// now it just checks if the player has gone off the bottom
    	// of the tile map.
    	
    	int xp = (int)(s.getX()/tmap.getTileWidth());
    	int ypr = (int)((s.getY()+s.getHeight())/tmap.getTileHeight());
    	int xpr = (int)((s.getX()+s.getWidth())/tmap.getTileWidth());
    	int yp = (int)(s.getY()/tmap.getTileHeight());
    	
        if (s.getX() < 0 || s.getX() > tmap.getPixelWidth())
        {
        	
        	s.setVelocityX(-s.getVelocityX() );
        }
        
        if (spr) //if a sprite runs into anything
        {
        	if ((tmap.getTileChar(xp, yp)!='.')||(tmap.getTileChar(xpr, yp)!='.')||(tmap.getTileChar(xp,ypr)!='.')||(tmap.getTileChar(xpr, ypr)!='.'))
			{
				s.setVelocityX(-s.getVelocityX());
				if (s.getY() <= 0)
				{
					s.setVelocityY(-.08f);
				}
				else if (s.getY() + s.getHeight() >= tmap.getPixelHeight()) 
				{
					s.setVelocityY(0.08f);
				}
				else s.setVelocityY(-s.getVelocityY());
			}
        }
        else
        {
	        if (l1)
	        {
	        	Sound b = new Sound("sounds/ding.wav");
		        if (tmap.getTileChar(xp, yp)=='e'||tmap.getTileChar(xpr, yp)=='e'||tmap.getTileChar(xp,ypr)=='e'||tmap.getTileChar(xpr, ypr)=='e')
	        	{
		        	if (s.getVelocityY() > 0 || s.getVelocityY() < 0) s.setVelocityY(-s.getVelocityY());
		        	else s.setVelocityY(-1.0f);
				}
		        else if (tmap.getTileChar(xp, yp)=='t'||tmap.getTileChar(xpr, yp)=='t'||tmap.getTileChar(xp,ypr)=='t'||tmap.getTileChar(xpr, ypr)=='t')
	        	{
		        	if (s.getVelocityY() > 0 || s.getVelocityY() < 0) s.setVelocityY(-s.getVelocityY());
		        	else s.setVelocityY(-0.8f);
				}
		        else if (tmap.getTileChar(xp, yp)=='s')
				{
					total += 10;
					b.start();
					tmap.setTileChar('.', xp, yp);
				}
		        else if (tmap.getTileChar(xpr, yp)=='s')
		        {
					total += 10;
					b.start();
					tmap.setTileChar('.', xpr, yp);
				}
		        else if (tmap.getTileChar(xp,ypr)=='s')
		        {
					total += 10;
					b.start();
					tmap.setTileChar('.', xp, ypr);
				}
		        else if (tmap.getTileChar(xpr, ypr)=='s')
		        {
					total += 10;
					b.start();
					tmap.setTileChar('.', xpr, ypr);
				}
	        }
	        else if (l2)
	        {
	        	Sound d = new Sound("sounds/down.wav");
	        	Sound p = new Sound("sounds/up.wav");
	        	if (tmap.getTileChar(xp, yp)=='e'||tmap.getTileChar(xpr, yp)=='e'||tmap.getTileChar(xp,ypr)=='e'||tmap.getTileChar(xpr, ypr)=='e')
	        	{
		        	if (s.getVelocityY() > 0 || s.getVelocityY() < 0) s.setVelocityY(-s.getVelocityY());
		        	else s.setVelocityY(-0.3f);
				}
		        else if (tmap.getTileChar(xp, yp)=='t'||tmap.getTileChar(xpr, yp)=='t'||tmap.getTileChar(xp,ypr)=='t'||tmap.getTileChar(xpr, ypr)=='t')
	        	{
		        	if (s.getVelocityY() > 0 || s.getVelocityY() < 0) s.setVelocityY(-s.getVelocityY());
		        	else s.setVelocityY(0.3f);
				}
	        	// retweet without circle: +10
		        else if (tmap.getTileChar(xp, yp)=='r')
				{
					total += 10;
					p.start();
					tmap.setTileChar('.', xp, yp);
				}
		        else if (tmap.getTileChar(xpr, yp)=='r')
		        {
					total += 10;
					p.start();
					tmap.setTileChar('.', xpr, yp);
				}
		        else if (tmap.getTileChar(xp,ypr)=='r')
		        {
					total += 10;
					p.start();
					tmap.setTileChar('.', xp, ypr);
				}
		        else if (tmap.getTileChar(xpr, ypr)=='r')
		        {
					total += 10;
					p.start();
					tmap.setTileChar('.', xpr, ypr);
				}
	        	//with circle: -5, remove circle, move back
		        else if (tmap.getTileChar(xp, yp)=='c')
				{
					tmap.setTileChar('r', xp, yp);
					total -= 5;
					d.start();
					player.setVelocityX(-0.3f);
					player.setVelocityY(-0.3f);
				}
		        else if (tmap.getTileChar(xpr, yp)=='c')
		        {
					tmap.setTileChar('r', xpr, yp);
					total -=5;
					d.start();
					player.setVelocityX(-.3f);
					player.setVelocityY(-0.2f);
				}
		        else if (tmap.getTileChar(xp,ypr)=='c')
		        {
					tmap.setTileChar('r', xp, ypr);
					total -=5;
					d.start();
					player.setVelocityX(-0.3f);
					player.setVelocityY(-0.2f);
				}
		        else if (tmap.getTileChar(xpr, ypr)=='c')
		        {
					tmap.setTileChar('r', xpr, ypr);
					total -=5;
					d.start();
					player.setVelocityX(-0.3f);
					player.setVelocityY(-0.2f);
				}
	        }
	        else if (l3)
	        {
	        	Sound like = new Sound("sounds/pop2.wav");
	        	Sound heart = new Sound("sounds/heart.wav");
	        	Sound sad = new Sound("sounds/error.wav");
	        	Sound mad = new Sound("sounds/bad.wav");
	        	if (tmap.getTileChar(xp, yp)=='e'||tmap.getTileChar(xpr, yp)=='e'||tmap.getTileChar(xp,ypr)=='e'||tmap.getTileChar(xpr, ypr)=='e')
	        	{
		        	if (s.getVelocityY() > 0 || s.getVelocityY() < 0) s.setVelocityY(-s.getVelocityY());
		        	else s.setVelocityY(-0.3f);
				}
		        else if (tmap.getTileChar(xp, yp)=='t'||tmap.getTileChar(xpr, yp)=='t'||tmap.getTileChar(xp,ypr)=='t'||tmap.getTileChar(xpr, ypr)=='t')
	        	{
		        	if (s.getVelocityY() > 0 || s.getVelocityY() < 0) s.setVelocityY(-s.getVelocityY());
		        	else s.setVelocityY(0.3f);
				}
	        	// like: +2
		        else if (tmap.getTileChar(xp, yp)=='l')
				{
					total += 2;
					like.start();
					tmap.setTileChar('.', xp, yp);
				}
		        else if (tmap.getTileChar(xpr, yp)=='l')
		        {
					total += 2;
					like.start();
					tmap.setTileChar('.', xpr, yp);
				}
		        else if (tmap.getTileChar(xp,ypr)=='l')
		        {
					total += 2;
					like.start();
					tmap.setTileChar('.', xp, ypr);
				}
		        else if (tmap.getTileChar(xpr, ypr)=='l')
		        {
					total += 2;
					like.start();
					tmap.setTileChar('.', xpr, ypr);
				}
	        	// love: +5
		        else if (tmap.getTileChar(xp, yp)=='h')
				{
					total += 5;
					heart.start();
					tmap.setTileChar('.', xp, yp);
				}
		        else if (tmap.getTileChar(xpr, yp)=='h')
		        {
					total += 5;
					heart.start();
					tmap.setTileChar('.', xpr, yp);
				}
		        else if (tmap.getTileChar(xp,ypr)=='h')
		        {
					total += 5;
					heart.start();
					tmap.setTileChar('.', xp, ypr);
				}
		        else if (tmap.getTileChar(xpr, ypr)=='h')
		        {
					total += 5;
					heart.start();
					tmap.setTileChar('.', xpr, ypr);
				}
	        	// sad: -7
		        else if (tmap.getTileChar(xp, yp)=='s')
				{
					total -= 7;
					sad.start();
					tmap.setTileChar('.', xp, yp);
				}
		        else if (tmap.getTileChar(xpr, yp)=='s')
		        {
					total -= 7;
					sad.start();
					tmap.setTileChar('.', xpr, yp);
				}
		        else if (tmap.getTileChar(xp,ypr)=='s')
		        {
					total -= 7;
					sad.start();
					tmap.setTileChar('.', xp, ypr);
				}
		        else if (tmap.getTileChar(xpr, ypr)=='s')
		        {
					total -= 7;
					sad.start();
					tmap.setTileChar('.', xpr, ypr);
				}
	        	// mad: -20, slow down
		        else if (tmap.getTileChar(xp, yp)=='m')
				{
					total -= 20;
					tmap.setTileChar('.', xp, yp);
					mad.start();
					player.setVelocityX(-0.07f);
					player.setVelocityY(-0.07f);
				}
		        else if (tmap.getTileChar(xpr, yp)=='m')
		        {
					total -= 20;
					mad.start();
					tmap.setTileChar('.', xpr, yp);
					player.setVelocityX(-0.07f);
					player.setVelocityY(-0.07f);
				}
		        else if (tmap.getTileChar(xp,ypr)=='m')
		        {
					total -= 20;
					mad.start();
					tmap.setTileChar('.', xp, ypr);
					player.setVelocityX(-0.07f);
					player.setVelocityY(-0.07f);
				}
		        else if (tmap.getTileChar(xpr, ypr)=='m')
		        {
					total -= 20;
					mad.start();
					tmap.setTileChar('.', xpr, ypr);
					player.setVelocityX(-0.07f);
					player.setVelocityY(-0.07f);
				}
	        }
        }
        
    }
    
    
     
    /**
     * Override of the keyPressed event defined in GameCore to catch our
     * own events
     * 
     *  @param e The event that has been generated
     */
    public void keyPressed(KeyEvent e) 
    { 
    	int key = e.getKeyCode();
    	
    	if (key == KeyEvent.VK_ESCAPE) stop();
    	
    	if (key == KeyEvent.VK_UP) player.setVelocityY(-0.25f);
    	
    	if (key == KeyEvent.VK_DOWN) player.setVelocityY(0.25f);
    		
    	if (key == KeyEvent.VK_RIGHT) player.setVelocityX(.3f);
    	
    	if (key == KeyEvent.VK_LEFT) player.setVelocityX(-.25f);
    	
    	if (key == KeyEvent.VK_BACK_SPACE) restart();
    	if (key == KeyEvent.VK_P) running = false;
    	   	
    	if (key == KeyEvent.VK_G) running = true;
    	
    	if (key == KeyEvent.VK_M)
    	{
    		//running = !running;
    		menuOn = !menuOn;
    		if (running) running = false;
    		
    		//displayMenu();
    	}
    	if (key == KeyEvent.VK_S)
    	{
    		// Example of playing a sound as a thread
    		Sound s = new Sound("sounds/ding.wav");
    		s.start();
    	}
    	
    	if (key == KeyEvent.VK_ENTER)
    	{
    		if (l1)
    		{
    			l1 = false;
    			initialiseL2();
    		}
    		else if (l2)
    		{
    			l2 = false;
    			initialiseL3();
    		}
    		else if (l3)
    		{
    			l3 = false;
    			restart();
    		}
    	}
    	if (key == KeyEvent.VK_SPACE)
    	{
    		player.setVelocityX(0);
    		player.setVelocityY(0);
    	}
    }

    
  

	public void keyReleased(KeyEvent e) { 

		int key = e.getKeyCode();

		// Switch statement instead of lots of ifs...
		// Need to use break to prevent fall through.
		switch (key)
		{
			case KeyEvent.VK_ESCAPE : stop(); break;
			//case KeyEvent.VK_RIGHT	: player.setVelocityX(player.getVelocityX()*1.5f);
			//case KeyEvent.VK_M		:  menuOn = false; break;
			case KeyEvent.VK_R		: running = true;
			default :  break;
		}
	}


	public void mouseClicked(MouseEvent e){
		if (player.getScale()==1f)
			player.setScale(1.5f);
	}
}
