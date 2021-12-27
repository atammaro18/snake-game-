import java.awt.event.*;
import java.awt.*; 
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{

	static final int SCREEN_WIDTH = 600;  // setting up screen width
	static final int SCREEN_HEIGHT = 600; //setting up the screen Height 
	static final int UNIT_SIZE = 25; // how big the objects are
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75; //Higher delay - slower the game 
	final int x[] = new int [GAME_UNITS]; // holds all x coordinates 
	final int y[] = new int [GAME_UNITS]; //Holds all y coordinates
	int bodyParts = 6; // begin body parts of the snake 
	int applesEaten; 
	int appleX; // where is the apple in x coordinate
	int appleY;  // where is the apple in y coordinate 
	char direction = 'R'; // snake begins by going right 
	boolean running = false; 
	Timer timer; 
	Random random; 
	
	
	GamePanel(){
		random = new Random(); 
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); // SET THE SIZE 
		this.setBackground(Color.black); //SET BACKGROUND TO BLACK 
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
		
		
	}
	
	public void startGame() {
		newApple(); // calls the a new apple to appear at the beginning of the game 
		running = true; 
		timer = new Timer(DELAY, this); //sets the pace of the game
		timer.start(); 
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g); 
		draw(g); 
	}
	public void draw(Graphics g) {
		if(running){
			for (int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT); //create grid of the game 
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE); //this created the grid of the game
			}
			g.setColor(Color.red); //color of the apple 
			g.fillOval(appleX, appleY, UNIT_SIZE,UNIT_SIZE); //fills the circle of the apple
		
			for(int i =0; i<bodyParts;i++) {
				if(i==0) {
					g.setColor(Color.green);
					g.fillRect(x[i],y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(45,180,0));
					//g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i],y[i], UNIT_SIZE, UNIT_SIZE);
				}
				
			}
			
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free",Font.BOLD, 20)); 
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
	}
	public void newApple() {
		//This generates the coordinates of a new apple 
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE; 
		appleX = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	public void move() {
		//moving the snake 
		for (int i = bodyParts; i>0; i--) {
			x[i] = x[i-1]; //shifts the array by one spot 
			y[i] = y[i-1]; //shifts the array by one spot 
		}
		
		switch(direction) {
		// defining the direction of the snake 
		case'U':
			y[0] = y[0] - UNIT_SIZE;
			break; 
		case'D':
			y[0] = y[0] + UNIT_SIZE;
			break; 
		case'L':
			x[0] = x[0] - UNIT_SIZE;
			break; 
		case'R':
			x[0] = x[0] + UNIT_SIZE;
			break; 
		}
	}
	public void checkApple() {
		//grabbing the apple 
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	public void checkCollisions() {
		// This checks if head collides with body
		for(int i = bodyParts; i>0;i--) {
			if((x[0] == x[i]) && (y[0]==y[i])) {
				running = false; 
			}
		}
		//check if head touches left border 
		if(x[0] < 0) {
			running = false;
		}
		//check if head touches right border 
		if(x[0] > SCREEN_WIDTH) {
			running = false; 
		}
		//check if head touches top border 
		if(y[0] < 0) {
			running =false;
		}
		//check if head touches bottom border 
		if (y[0] > SCREEN_HEIGHT) {
			running = false; 
		}
		if(!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {
		//score 
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.BOLD, 20)); 
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		//set up game over text 
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.BOLD, 75)); 
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move(); 
			checkApple(); 
			checkCollisions();
		}
		repaint(); 
		
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override 
		public void keyPressed(KeyEvent e) {
			// this allows us to control the snake with the arrows 
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
			
		}
	}
	

}
