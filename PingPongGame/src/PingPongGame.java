import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;import java.applet.AudioClip;
import java.applet.*;
import java.awt.*;
import java.net.*;
import java.util.*;

@SuppressWarnings({ "serial", "unused" })
public class PingPongGame extends Applet implements Runnable, MouseListener, MouseMotionListener {

//Variables and setup for game
	final int WIDTH = 600, HEIGHT = 400;
	Thread thread;
	int Xball, Yball, playerPaddle,cpuPaddle;
	int paddleMin, paddleMax;
	int XballMin, XballMax, YballMin, YballMax, ballSpeedIncr;
	boolean up,left, startGame, gameOver;
	int topScore = 3;
	int startPlayer;
	int playerScore, cpuScore;
	String winner;
	Color black;
		
//Sound for Ball/Paddle collision and Game over and player wins
		AudioClip hitBallSound;
		AudioClip gameOverSound;
		AudioClip cheeringSound;
  
        public void init(){  
			Graphics graphics = getGraphics();
			this.resize(WIDTH,HEIGHT);
			addMouseMotionListener(this);
			addMouseListener(this);
			
	//ball speed
		ballSpeedIncr = 4;
	//minimum position for ball in x-axis
		XballMin = 10 + ballSpeedIncr;
	//maximum position for ball in x-axis
		XballMax = 580 - ballSpeedIncr;
	//minimum position for ball in y-axis
		YballMin = 10 + ballSpeedIncr;
	//maximum position for ball in x-axis
		YballMax = 310 - ballSpeedIncr;
		
	//minimum position paddle can reach in x-axis
		paddleMin = 10;
	//maximum position paddle can reach in x-axis
		paddleMax = 289;
		
		playerPaddle = cpuPaddle = 40;
		
		//Select CPU to serve the ball first when game starts
		startPlayer = 1;
			
		startGame = false;
		gameOver = false;

		//black = new Color(0,113,0);
		//setBackground(black);
		
//for sound implementation
		loadSounds();

		this.requestFocus();
    }
	
	protected void reStartGame() {				
		
		//set startGame and gameOver initial values
		startGame = true;
		gameOver = false;
		
		//Score for Player and CPU
		playerScore = 0; 
		cpuScore = 0;
		
		if (startPlayer==0) {
			Xball = 550;
			
		//select player position
			Yball = playerPaddle;
			
		//move the ball towards player
			left = true;
			startPlayer = 0;				
		}
		else {
			Xball = 20;
			
		//select CPU position
			Yball = (int)(Math.random()*200)+10;
			
		//move the ball towards computer
			left = false; 
			startPlayer = 1;
		}			
		
		playerPaddle = Yball;
		
		//If ball service is above the middle of the court, serve upwards
		if (Yball > 160){
			up = true;
		}
		else {
			up = false;
		}
		cpuPaddle = Yball;
		//CPU paddle position
		
		showStatus(new Integer(Yball).toString());			
	}
				protected void endGame() {	
					if (cpuScore>playerScore) {
						winner = "Computer Wins!";
					}
					else {
						winner = "Congratulations, You Win!";
					}
					
					startGame = false;
					gameOver = true;
				}
		
			    public void start(){
			        thread = new Thread(this);
			        thread.start();
			    }
			
			    @SuppressWarnings("deprecation")
				public void stop() {
			        thread.stop();
			    }
			
			    public void run() {
			
			        while(true) {
					
//Move CPU paddle include error margin
		if (Xball<160){
			if (up){
				if ( Yball > paddleMin) cpuPaddle = Yball + 5;
				else cpuPaddle = paddleMin;
			}
			else {
				if ( Yball < paddleMax) cpuPaddle = Yball - 5;
				else cpuPaddle = paddleMax;
			}
		}
		
	//Check if ball hits Paddle
		if(Xball > 569 && Xball < 574) {
	//if ball hits the player's paddle, change ball direction.
			if( ((Yball + 5) > playerPaddle) && ((playerPaddle + 31) > Yball) ) {
				left = true;
				showStatus("Player hits ball");
				hitBallSound.play(); 
			}
		}					
		if(Xball > 16 && Xball < 21) {
	//if ball hits the CPU's paddle, change ball direction.
			if( ((Yball + 5) > cpuPaddle) && ((cpuPaddle + 31) > Yball) ) {
				left = false;
				showStatus("Computer hits ball");							
				hitBallSound.play();
			}
		}
		
	//End the game when cpuScore or playerScore equals topScore and Play Game Over sound
					if (cpuScore==topScore || playerScore==topScore)endGame();
					
					if (startGame){
						Xball = getXPos(Xball);
						Yball = getYPos(Yball);
					}
					
			//rePaint the game details
	                repaint();
	
	                try {
	                        Thread.sleep(30);
	                }
	                catch (InterruptedException e)
	                {
	                }
	        }
	    }
		
	public void update(Graphics g) 
	{
//set play region for game
		Graphics graphics;
		Image Screen = null;
		Dimension dm = getSize();
		Screen = createImage(dm.width, dm.height);
		graphics = Screen.getGraphics();
		graphics.setColor(Color.black);
		graphics.fillRect(0, 0, dm.width, dm.height);
		graphics.setColor(getForeground());
		paint(graphics);
		g.drawImage(Screen, 0, 0, this);
		Screen.flush();
	}
	
//Design background
    public void paint(Graphics g) {	
		drawgraphics(g);
		if(startGame){
			
	//Draw Ball and make it yellow
			g.setColor(Color.yellow);
			g.fillOval(Xball,Yball,10,10);
	//make player paddle red
			g.setColor(Color.green);
			g.fillRect(580,playerPaddle,5,30);
	//make CPU paddle blue
			g.setColor(Color.blue);
			g.fillRect(15,cpuPaddle,5,30); 
		}
		
	//Setup messages and their display properties
		
		//Display messages
		g.setColor(Color.white);
		g.setFont(new Font("Calibri", Font.ITALIC, 30));
		if(!startGame) g.drawString("Ping Pong Game! ",250,80);
		
		g.setFont(new Font("Calibri", Font.ITALIC, 20));
		g.drawString("Points:",20,360);
		
		g.setColor(Color.red);
		g.setFont(new Font("Calibri", Font.ITALIC, 15));
		if(!startGame) g.drawString("Click HERE to Start Game!",250,180);
		
		g.setColor(Color.white);
		//Display CPU and Player labels
		g.drawString("CPU",20,380);
		g.drawString("Player",220,380);
		
		//Display cpuScore and playerScore
		g.setColor(Color.green);
		if(gameOver) g.drawString(winner,180,150);
		g.setColor(Color.yellow);
		g.drawString(new Integer(cpuScore).toString(),80,380);
		g.drawString(new Integer(playerScore).toString(),300,380);
		
		g.setColor(Color.red);
		g.setFont(new Font("Calibri", Font.ITALIC, 25));
		if(gameOver) g.drawString("Game Over!!!",200,110);
	}
			    //background properties
				protected void drawgraphics(Graphics graphics){			
					graphics.setColor(Color.magenta);
					
					//set boundary for game
					graphics.drawRect(9,9,581,310);
					graphics.fillRect(0,240,300,10);
					
					graphics.setColor(Color.black);
					//graphics.setColor(Color.black);
					graphics.fillRoundRect(0,220,320,60,20,20);
				}
			//implementation of sound for ball/paddle collide and gameOver
				public void loadSounds() {
			
				  try {
				    hitBallSound = getAudioClip(new URL(getDocumentBase(), "hit.au"));
				    gameOverSound = getAudioClip(new URL(getDocumentBase(), "gameOver.wav"));
				    cheeringSound = getAudioClip(new URL(getDocumentBase(), "cheering.wav"));
				  }
				  catch (MalformedURLException e) {}
					hitBallSound.play();    hitBallSound.stop();
					gameOverSound.play();	gameOverSound.stop();
				}
//Movement of ball in x-direction
	protected int getXPos(int x) {
		if(x > XballMax) {				
			if(startGame && !left) {
				//increase cpuScore by 1
				cpuScore += 1;
				//Display status of player
				showStatus("Player missed");
				hitBallSound.play();
				
			//if CPU wins play Game Over sound
				if(cpuScore==topScore) gameOverSound.play();
				
			}
			left = true;
			return XballMax;
		}
		
		if(x < XballMin ) {				
			if(startGame && left){
				//increase playerScore by 1
				playerScore += 1;
				//Display status of CPU
				showStatus("Computer missed");
				hitBallSound.play();
				
			//if player wins play Cheering sound
				if(playerScore==topScore) cheeringSound.play();
			}
			left = false;
			return XballMin;
		}
		
		if(left) return Xball - ballSpeedIncr;
		else return Xball + ballSpeedIncr;
	}
//Movement of ball in Y-direction
	protected int getYPos(int y) {
		if( y > YballMax) {
			up = true;
			hitBallSound.play(); 
			return YballMax;
		}
		if(y < YballMin) {
			up = false;
			hitBallSound.play();
			return YballMin;
		}
		if(up) return Yball - ballSpeedIncr;
		else return Yball + ballSpeedIncr;
	}
	 
public void mouseDragged(MouseEvent e) {
	
}
//public void paint(Graphics graphics){
	//graphics.setColor(Color.black);
	//graphics.fillRect(0, 0, WIDTH, HEIGHT);
	//GameOver = false;
//}			//movement of player paddle
			public void mouseMoved(MouseEvent e) {
				int y = e.getY();
				if ( (y - playerPaddle) > 0 ) MoveDown(y);
				else MoveUp(y);
			}
			
			public void mouseEntered(MouseEvent event) {
			}
			public void mouseExited(MouseEvent event) {
			}
			public void mousePressed(MouseEvent event) {
			}
			public void mouseReleased(MouseEvent event) {
			}
						//start the game
			public void mouseClicked(MouseEvent e) {
				if(!startGame && e.getX() < 350 && e.getX() > 150) {		
					reStartGame();
				}
				else {			
				}
			}
			//moving player paddle up
			protected void MoveUp(int y){		
				if ( y > paddleMin) playerPaddle = y;
				else playerPaddle = paddleMin;
			}
			//moving player paddle down
				protected void MoveDown(int y){			
					if ( y < paddleMax) playerPaddle = y;
					else playerPaddle = paddleMax;
				}
			}