import java.util.*;
import java.io.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DenaHuangISU extends JPanel implements KeyListener, MouseListener, Runnable {
	
	static JFrame frame;
	final int SCREEN_W = 400;
	final int SCREEN_H = 600;
	int ground = (int) (SCREEN_H * 0.875);
	int centerX = SCREEN_W/2;
	int centerY = ground/2;
	int FPS = 60;
	Thread thread;
	Graphics2D g2;
	
	boolean start;
	boolean first;
	boolean pause = false;
	
	static AudioClip coin;
	static AudioClip drop;
	Image pauseSign = Toolkit.getDefaultToolkit().getImage("pauseSign.gif");
	Image resumeSign = Toolkit.getDefaultToolkit().getImage("resumeSign.gif");
	Image quitSign = Toolkit.getDefaultToolkit().getImage("quitSign.gif");
	Image platinum = Toolkit.getDefaultToolkit().getImage("platinum.png");
	Image gold = Toolkit.getDefaultToolkit().getImage("gold.png");
	Image silver = Toolkit.getDefaultToolkit().getImage("silver.png");
	Image bronze = Toolkit.getDefaultToolkit().getImage("bronze.png");
	Image terrible = Toolkit.getDefaultToolkit().getImage("terrible.png");
	
	// main menu
	boolean showMenu = true;
	Image title = Toolkit.getDefaultToolkit().getImage("title.png");
	final int titleW = 300;
	final int titleH = 100;
	Image startSign = Toolkit.getDefaultToolkit().getImage("startSign.gif");
	int startSignW = 200;
	int startSignH = 30;
	
	// scoreboard menu
	boolean showScoreboard = false;
	Image scoreboard = Toolkit.getDefaultToolkit().getImage("scoreboard.png");
	Image gameover = Toolkit.getDefaultToolkit().getImage("gameover.png");
	Image newSign = Toolkit.getDefaultToolkit().getImage("newSign.gif");
	final int scoreboardW = 300;
	final int scoreboardH = 150;
	int score;
	static int highScore;
	int pauseTime = 500;
	
	// player
	final int PLAYER_SIZE = 25;
	Rectangle rect = new Rectangle(centerX - PLAYER_SIZE/2, centerY - PLAYER_SIZE/2, PLAYER_SIZE, PLAYER_SIZE);
	int birdX = centerX - PLAYER_SIZE/2;
	int birdY = centerY - PLAYER_SIZE/2;
	Image bird = Toolkit.getDefaultToolkit().getImage("bird.png");
	Image pipeHeadTop = Toolkit.getDefaultToolkit ().getImage ("pipeHeadTop.png");
	Image pipeHeadBottom = Toolkit.getDefaultToolkit ().getImage ("pipeHeadBottom.png");
	Image pipe = Toolkit.getDefaultToolkit ().getImage ("pipe.png");
	boolean airborne = true;
	boolean jump;
	double speed = 3.5;	
	double jumpSpeed = 4;
	double xVel;
	double yVel;
	double gravity = 0.6;
	
	// obstacle
	final int NUM_OBSTACLE = 4;
	Rectangle [] obstacle1 = new Rectangle [NUM_OBSTACLE];
	Rectangle [] obstacle2 = new Rectangle [NUM_OBSTACLE];
	int [] x1 = new int [NUM_OBSTACLE];
	int [] height1 = new int [NUM_OBSTACLE];
	int [] height2 = new int [NUM_OBSTACLE];
	int [] y2 = new int [NUM_OBSTACLE];
	final int SPACE = 150;
	int obstacleSpeed;
	int h = 0;
	
	public DenaHuangISU() {
		setPreferredSize(new Dimension(SCREEN_W, SCREEN_H));
		
		if (start)
			showScoreboard = true;
		
		start = false;
		jump = false;
		first = true;
		
		for (h = 0; h < NUM_OBSTACLE; h++) {
			height1[h] = -1;
			x1[h] = SCREEN_W;
		}
		h = 0;
		rect.y = centerY - PLAYER_SIZE/2;
		birdY = centerY - PLAYER_SIZE/2;
		obstacleSpeed = 2;
		
		thread = new Thread(this);
		thread.start();
		
		setFocusable (true); 
		addKeyListener (this);

		addMouseListener (this);
	}
	
	// continuously runs and updates the screen
	// no parameters
	// no return
	public void run() {
		while (true) {
			if (start) {
				if (pause) {
					this.repaint();
					pause(1);
				} 
				else {
					System.out.println ("else");
					move(); 
					keepInBound();
					
					this.repaint();
					
					try {
						Thread.sleep(1000/FPS);
					}   
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	// continuously updates the graphics on the screen
	// parameters: graphics
	// no return
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2 = (Graphics2D) g;

		if (pause) {
			drawBackground();
			drawPlayer();
			drawObstacles();
			showScore();
			g2.drawImage(quitSign, 5, 5, 125, 25, this);
			g2.drawImage(resumeSign, SCREEN_W - 150 - 5, 5, 150, 25, this);
		}
		else {
			drawBackground();
			drawPlayer();
			
			if (showMenu)
				menu();
			
			if (showScoreboard && !start) {
				try {
					scoreboard();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
	
			if (!showScoreboard && first)
				score = 0;
			
			drawObstacles();
			if (start) {
				for (int i = 0; i <= h; i++) {
					x1[i] -= obstacleSpeed;
				}
				
				try {
					checkCollision();
				}
				catch(Exception e) {
					return;
				}
				
				showScore();
			}
			g2.drawImage(quitSign, 5, 5, 125, 25, this);
			
			if (start && !pause)
				g2.drawImage(pauseSign, SCREEN_W - 125 - 5, 5, 125, 25, this);
		}
	}
	
	public void actionPerformed (ActionEvent event) {

	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_SPACE) {
			if (pause) {
				pause = false;
			}
			else {
				jump = true;
				start = true;
				showScoreboard = false;
				showMenu = false;
			}
		}
		else if (key == KeyEvent.VK_P) {
			if (start)
				pause = true;
		} 
		else if (key == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_SPACE) {
			jump = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	private static URL getCompleteURL(String fileName) {
    	try
		{
			return new URL ("file:" + System.getProperty ("user.dir") + "/" + fileName);
		}
		catch (MalformedURLException e)
		{
			System.err.println (e.getMessage ());
		}
		return null;
	}
	
	// draws the background of the game, an image
	// no parameters
	// no return
	public void drawBackground() {
		Image img = Toolkit.getDefaultToolkit ().getImage ("background.png");
		g2.drawImage(img, 0, 0, SCREEN_W, SCREEN_H, this);
	}
	
	// draws the player
	// no parameters
	// no return
	public void drawPlayer() {
		g2.setColor(new Color (0,0,0,0));
		g2.fill(rect);
		g2.drawImage(bird, birdX, birdY, PLAYER_SIZE, PLAYER_SIZE, this);
	}
	
	// moves the player
	// no parameters
	// no return
	public void move() {
		if (airborne) {
			yVel -= gravity;
			if (jump) {
				airborne = true;
				yVel = jumpSpeed;
			}
		} 
		else {
			if (jump) {
				airborne = true;
				yVel = jumpSpeed;
			}
		}
		
		rect.y -= yVel;
		birdY -= yVel;
	}
	
	// makes sure that the player does not go beyond the screen window and that the player stays in the same column
	// no parameters
	// no return
	public void keepInBound() {		
		if (rect.x != centerX - PLAYER_SIZE/2 || birdX != centerX - PLAYER_SIZE/2) {
			rect.x = centerX - PLAYER_SIZE/2;
			birdX = centerX - PLAYER_SIZE/2;
		}
		
		if (rect.y < 0 || birdY < 0) {
			rect.y = 0;
			birdY = 0;
			yVel = 0;
		}
		else if (rect.y > ground - rect.height || birdY > ground - rect.height) {
			rect.y = ground - rect.height;
			birdY = ground - rect.height;
			airborne = false;
			yVel = 0;
		}
	}
	
	// draws the obstacles, which are constantly moving
	// no parameters
	// no return
	public void drawObstacles() {
		int obstacleWidth = 50;
		int headHeight = 30;
		int y1 = 0;
		
		for (int i = 0; i <= h; i++) {
			if (height1[i] == -1)
				height1[i]= (int) (Math.random() * (ground/2 - ground/4) + ground/4);
			
			y2[i] = height1[i] + SPACE;
			height2[i] = ground - y2[i];
			
			obstacle1[i] = new Rectangle(x1[i], y1, obstacleWidth, height1[i]);
			obstacle2[i] = new Rectangle(x1[i], y2[i], obstacleWidth, height2[i]);
			
			g2.setColor(new Color (0,0,0,0));
			g2.fill(obstacle1[i]);
			g2.fill(obstacle2[i]);
			g2.drawImage(pipeHeadTop, x1[i], height1[i] - headHeight, obstacleWidth, headHeight, this);
			g2.drawImage(pipe, x1[i], y1, obstacleWidth, height1[i] - headHeight, this);
			g2.drawImage(pipeHeadBottom, x1[i], y2[i], obstacleWidth, headHeight, this);
			g2.drawImage(pipe, x1[i], y2[i] + headHeight, obstacleWidth, height2[i] - headHeight, this);
		}
		
		if (x1[0] == centerX && first) {
			coin.play();
			h++;
			height1[h] = -1;
			first = false;
		}
		else if (x1[0] == 0) {
			coin.play();
			if (h < NUM_OBSTACLE - 1)
				h++;
			height1[h] = -1;
		}

		if (x1[0] + obstacleWidth == 0) {			
			for (int i = 0; i < NUM_OBSTACLE - 1; i++) {
				x1[i] = x1[i+1];
				height1[i] = height1[i+1];
				height2[i] = height2[i+1];
				y2[i] = y2[i+1];
			}
			height1[NUM_OBSTACLE-1] = -1;
 			height2[NUM_OBSTACLE-1] = -1;
			x1[NUM_OBSTACLE-1] = SCREEN_W;
			h--;
		}
		
		if (!pause)
			if (x1[0] == centerX || x1[1] == centerX)
				score++;
 	}
	
	// checks if the player has collided with any of the obstacles (pipes) or if it has hit the ground
	// no parameters
	// no return
	public void checkCollision() {
		for (int i = 0; i <= h; i++) {
				if (rect.intersects(obstacle1[i])) {
					double leftPlayer = rect.getX();
					double rightPlayer = rect.getX() + rect.getWidth();
					double topPlayer = rect.getY();
					double bottomPlayer = rect.getY() + rect.getHeight();
					double left = obstacle1[i].getX();
					double top = obstacle1[i].getY();
					double bottom = obstacle1[i].getY() + obstacle1[i].getHeight();
	
					if (rightPlayer > left && 
							leftPlayer < left && 
							rightPlayer - left < bottomPlayer - top && 
							rightPlayer - left < bottom - topPlayer) {
					    // left side of the top obstacles
						drop.play();
						rect.x = obstacle1[i].x - rect.width;
						birdX = obstacle1[i].x - rect.width;
						g2.clearRect(0, 0, SCREEN_W, SCREEN_H);
						newGame();
						pause(pauseTime);
			        }
			        else if (topPlayer < bottom && bottomPlayer > bottom) {
			            // bottom side of the top obstacle
			        	drop.play();
			        	rect.y = obstacle1[i].y + obstacle1[i].height;
			        	birdX = obstacle1[i].y + obstacle1[i].height;
						g2.clearRect(0, 0, SCREEN_W, SCREEN_H);
						newGame();
						pause(pauseTime);
			        }
					break;
					
				}
				
				if (rect.intersects(obstacle2[i])) {
					double leftPlayer = rect.getX();
					double rightPlayer = rect.getX() + rect.getWidth();
					double topPlayer = rect.getY();
					double bottomPlayer = rect.getY() + rect.getHeight();
					double left2 = obstacle2[i].getX();
					double top2 = obstacle2[i].getY();
					double bottom2 = obstacle2[i].getY() + obstacle2[i].getHeight();
					
					if (rightPlayer > left2 && 
							leftPlayer < left2 && 
							rightPlayer - left2 < bottomPlayer - top2 && 
							rightPlayer - left2 < bottom2 - topPlayer) {
					    // left side of the bottom obstacle
						drop.play();
						rect.x = obstacle2[i].x - rect.width;
						birdX = obstacle2[i].x - rect.width;
						g2.clearRect(0, 0, SCREEN_W, SCREEN_H);
						newGame();
						pause(pauseTime);
			        }
			        else if (bottomPlayer > top2 && topPlayer < top2) {
			            // top side of the bottom obstacle
			        	drop.play();
			        	rect.y = obstacle2[i].y - rect.height;
			        	birdY = obstacle2[i].y - rect.height;
						g2.clearRect(0, 0, SCREEN_W, SCREEN_H);
						newGame();
						pause(pauseTime);
			        }
					break;
				}
		}
		
		if (rect.y + rect.height == ground && start) {
			drop.play();
			g2.clearRect(0, 0, SCREEN_W, SCREEN_H);
			newGame();
			pause(pauseTime);
		}
	}
	
	// shows the live score count
	// no parameters
	// no return
	public void showScore() {
		String scoreStr = "" + score;
		
		for (int i = 0; i < scoreStr.length(); i++) {
			g2.drawImage(Toolkit.getDefaultToolkit().getImage(scoreStr.charAt(i) + ".png"), 
					(((SCREEN_W - scoreStr.length() * 30) / 2) + (i * 30)), 10, 30, 60, this);
		}
	}
	
	// resets the game
	// no parameters
	// no return
	public void newGame() {
		g2.clearRect(0, 0, SCREEN_W, SCREEN_H);
		
		if (start)
			showScoreboard = true;
		
		start = false;
		jump = false;
		first = true;
		
		for (h = 0; h < NUM_OBSTACLE; h++) {
			x1[h] = SCREEN_W;
			height1[h] = -1;
			height2[h] = -1;
			y2[h] = -1;
		}
		h = 0;
		rect.y = centerY - PLAYER_SIZE/2;
		birdY = centerY - PLAYER_SIZE/2;
		obstacleSpeed = 2;
		repaint();
		
	}
	
	// displays the main menu when game is first started
	// no parameters
	// no return
	public void menu() {
		g2.drawImage(title, (SCREEN_W - titleW) / 2, (SCREEN_H - titleH) / 4, titleW, titleH, this);
		g2.drawImage(startSign, (SCREEN_W - startSignW) / 2, (SCREEN_H - startSignH) / 2 + SCREEN_H / 25, startSignW, startSignH, this);
	}
	
	// displays the scoreboard with information once player has died 
	// no parameters
	// no return
	public void scoreboard() throws IOException {
		boolean newScore = false;
		PrintWriter out = new PrintWriter (new FileWriter ("bestScore.txt"));
		
		if (score > highScore) {
			highScore = score;
			newScore = true;
		}
		
		out.print(highScore);
		
		String scoreStr = "" + score;
		String highScoreStr = "" + highScore;
		
		g2.drawImage(gameover, (SCREEN_W - scoreboardW) / 2, (SCREEN_H - scoreboardH) / 7, scoreboardW, scoreboardW / 4, this);
		g2.drawImage(scoreboard, (SCREEN_W - scoreboardW) / 2, (SCREEN_H - scoreboardH) / 3, scoreboardW, scoreboardH, this);
		g2.drawImage(startSign, (SCREEN_W - startSignW) / 2, (SCREEN_H - startSignH) / 2 + SCREEN_H / 25, startSignW, startSignH, this);
		
		if (score >= 100) {
			g2.drawImage(platinum, ((SCREEN_W - scoreboardW) / 2) + scoreboardW / 8, 
					((SCREEN_H - scoreboardH) / 3) + scoreboardH / 3, 
					scoreboardW / 4, scoreboardH / 2, this);
		}
		else if (score >= 50) {
			g2.drawImage(gold, ((SCREEN_W - scoreboardW) / 2) + scoreboardW / 8, 
					((SCREEN_H - scoreboardH) / 3) + scoreboardH / 3, 
					scoreboardW / 4, scoreboardH / 2, this);
		}
		else if (score >= 25) {
			g2.drawImage(silver, ((SCREEN_W - scoreboardW) / 2) + scoreboardW / 8, 
					((SCREEN_H - scoreboardH) / 3) + scoreboardH / 3, 
					scoreboardW / 4, scoreboardH / 2, this);
		}
		else if (score >= 1) {
			g2.drawImage(bronze, ((SCREEN_W - scoreboardW) / 2) + scoreboardW / 8, 
					((SCREEN_H - scoreboardH) / 3) + scoreboardH / 3, 
					scoreboardW / 4, scoreboardH / 2, this);
		}
		else {
			g2.drawImage(terrible, ((SCREEN_W - scoreboardW) / 2) + scoreboardW / 8, 
					((SCREEN_H - scoreboardH) / 3) + scoreboardH / 3, 
					scoreboardW / 4, scoreboardH / 2, this);
		}
		
		
		for (int i = 0; i < scoreStr.length(); i++) {
			g2.drawImage(Toolkit.getDefaultToolkit().getImage(scoreStr.charAt(i) + ".png"), 
					((SCREEN_W - scoreboardW) / 2) + scoreboardW / 10 * (7 + i), 
					((SCREEN_H - scoreboardH) / 3) + scoreboardH / 3, 
					scoreboardW / 18, scoreboardH / 6, this);
		}
		
		for (int i = 0; i < highScoreStr.length(); i++) {
			g2.drawImage(Toolkit.getDefaultToolkit().getImage(highScoreStr.charAt(i) + ".png"), 
					((SCREEN_W - scoreboardW) / 2) + scoreboardW / 10 * (7 + i), 
					((SCREEN_H - scoreboardH) / 3) + scoreboardH / 3 * 2, 
					scoreboardW / 18, scoreboardH / 6, this);
		}
		
		if (newScore) {
			g2.drawImage(newSign, 
					((SCREEN_W - scoreboardW) / 2) + scoreboardW / 5 * 3, 
					((SCREEN_H - scoreboardH) / 3) + scoreboardH / 2, 
					40, 20, this);
		}

		out.close();
	}
	
	// pauses the game
	// parameters: number of milliseconds to pause
	// no return
	public void pause(int p) {
		try {
			Thread.sleep(p);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner (new File ("bestScore.txt"));
		highScore = Integer.parseInt(in.nextLine());
		coin = Applet.newAudioClip (getCompleteURL ("coin.wav"));
		drop = Applet.newAudioClip (getCompleteURL ("drop.wav"));
		
		frame = new JFrame ("Flappy Bird");
		DenaHuangISU panel = new DenaHuangISU();
		
		frame.add(panel);
		frame.addKeyListener(panel);
		frame.addMouseListener(panel);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		in.close();
	}
}
