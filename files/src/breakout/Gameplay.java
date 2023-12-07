package breakout;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.Timer;

import javax.swing.JPanel;

public class Gameplay extends JPanel implements KeyListener, ActionListener{
	//indicate whether the game has started
	private boolean play = false;
	private double speedFactor = 1.1;
	
	private ScoreBoard scoreboard;
	
	
	private int score = 0;
	
	private int totalBricks = 21;
	
	private Timer timer;
	private int delay = 8;
	
	//Starting position of slider
	private int playerX = 310;
	
	//Starting position of ball
	private int ballposX = 350;
	private int ballposY = 300;
	
	//direction of ball (which is the speed on the axis)
	private double ballXdir = -2.0;
	private double ballYdir = -2.0;
	
	
	private LevelsGenerator level;
	private boolean restart = false;
	
	//this control the overall movement speed
	private long numHit = 1;
	private int paddleLen = 100;

	
	public Gameplay() {
		//basic settings
		level = new LevelsGenerator(3, 7);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}
	
	private void onScoreUpload(boolean status, int score) {
        if (scoreboard != null) {
            scoreboard.uploadScore(status, score);
        }
    }
	
	public void setScoreBoard(ScoreBoard scoreboard) {
        this.scoreboard = scoreboard;
    }
	
	public void paint(Graphics g) {
		// background settings
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);
		
		//drawing map
		level.draw((Graphics2D)g);
		
		// define borders
		// no need for bottom
		//g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		//scores
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString(""+score, 590, 30);
		
		// the paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 550, paddleLen, 8);
		
		// the ball
        g.setColor(Color.yellow);
        g.fillOval(ballposX, ballposY, 20, 20);
		
		
		//condition: finish the game
		if(totalBricks <= 0) {
			play = false;
			restart = true;
			ballXdir = 0;
			ballYdir = 0;
			
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("You won! " + score, 260, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press ENTER to restart this level", 200, 350);
		}
		
		//set a welcome message
		if(!play && !restart) {
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 15));
			g.drawString("Use <- or -> to start your ball", 100, 300);
		}
		
		//lose the game
		if(ballposY > 570) {
			
			play = false;
			restart = true;
			ballXdir = 0;
			ballYdir = 0;
			
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Game Over, Scores: " + score, 190, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press ENTER to restart", 200, 350);
		}
		
		g.dispose();
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		
		if(play) {
			// hit the player paddle
			if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, paddleLen, 8))) {
				//change this so that paddle influence the ball movement
				updateXYspeed(new Rectangle(ballposX, ballposY, 20, 20), new Rectangle(playerX, 550, paddleLen, 8), 0.75, numHit);
			}
			
			//add collision functionality for bricks
			A: for(int i=0;i<level.level.length;i++) {
				for(int j=0;j<level.level[0].length;j++) {
					if(level.level[i][j] != 0) {
						//detect the intersection
						int brickX = j * level.width + 80;
						int brickY = i * level.height + 50;
						int brickWidth = level.width;
						int brickHeight = level.height;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
						Rectangle brickRect = rect;
						
						if(ballRect.intersects(brickRect)) {
							//System.out.println("hit once");
							if(level.getStatus(i, j) == 10) {
								upgradeBrick();
							}
							level.hitBricks(i, j);
							numHit++;
							//each hit get 1 point
							score += 1;
							if(level.getStatus(i,j) == 0) {
								totalBricks--;
							}
							
							// change its direction after collision
							// direction depends on the x pos
							if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
								ballXdir = -ballXdir;
							} else {
								ballYdir = -ballYdir;
							}
							//System.out.println("break loop");
							break A;
						}
					}
				}
				
			}
			
			//speed factor
			ballposX += speedFactor*ballXdir;
			ballposY += speedFactor*ballYdir;
			
			//left border
			if(ballposX < 0) {
				ballXdir = -ballXdir;
			}
			//Top border
			if(ballposY < 0) {
				ballYdir = -ballYdir;
			}
			//Right border
			if(ballposX > 670) {
				ballXdir = -ballXdir;
			}
		}
		
		repaint();
	}
	
	public void upgradeBrick() {
		//make bricks 20% longer
		paddleLen *= 1.2;
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			//keep player inside the rect
			if(playerX < 10) {
				playerX = 10;
			} else {
				moveLeft();
			}
			
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			//keep player inside the rect
			if(playerX >= 600) {
				playerX = 600;
			} else {
				moveRight();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(!play) {
				
				//send score
				if(totalBricks <= 0) {
					onScoreUpload(true, score);
				} else {
					onScoreUpload(false, score);
				}
				//reset everything to restart the game
				play = true;
				ballposX = 350;
				ballposY = 300;
				ballXdir = -1;
				ballYdir = -2;
				playerX = 310;
				score = 0;
				totalBricks = 21;
				paddleLen = 100; 
				level = new LevelsGenerator(3, 7);
				repaint();
			}
		}
		
	}
	
	public void moveLeft() {
		play = true;
		playerX -= 60;
	}
	
	public void moveRight() {
		play = true;
		playerX += 60;
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub	
	}
	
	public void updateXYspeed(Rectangle R1, Rectangle R2, double influenceX, long speedFactor) {
		//Reference:  https://gamedev.stackexchange.com/questions/20456/a-more-sophisticated-ball-paddle-collision-algorithm-for-breakouts
		//a helper function use to update the direction of two speed after collision
		//input 2 rect obj
		double ballWidth = R1.getWidth();
		double ballCenterX = R1.getX() + ballWidth/2;
		double paddleWidth = R2.getWidth();
		double paddleCenterX = R2.getX() + paddleWidth/2;
		double speedX = ballXdir;
		double speedY = ballYdir;
		
		// Applying the Pythagorean theorem, calculate the ball's overall
	    // speed from its X and Y components.  This will always be a
	    // positive value.
	    double speedXY = Math.sqrt(speedX*speedX + speedY*speedY);

	    // Calculate the position of the ball relative to the center of
	    // the paddle, and express this as a number between -1 and +1.
	    // (Note: collisions at the ends of the paddle may exceed this
	    // range, but that is fine.)
	    double posX = (ballCenterX - paddleCenterX) / (paddleWidth/2);
		
		// Define an empirical value (tweak as needed) for controlling
	    // the amount of influence the ball's position against the paddle
	    // has on the X speed.  This number must be between 0 and 1.
	    
	    // Let the new X speed be proportional to the ball position on
	    // the paddle.  Also make it relative to the original speed and
	    // limit it by the influence factor defined above.
	    speedX = speedXY * posX * influenceX;
	    //every 50 hit would increase the speed by 10%
	    ballXdir = speedX * (1 + (speedFactor*0.1)/50);
	    
	    // Finally, based on the new X speed, calculate the new Y speed
	    // such that the new overall speed is the same as the old.  This
	    // is another application of the Pythagorean theorem.  The new
	    // Y speed will always be nonzero as long as the X speed is less
	    // than the original overall speed.
	    speedY = Math.sqrt(speedXY*speedXY - speedX*speedX) *
	             (speedY > 0? -1 : 1);
	    ballYdir = speedY * (1 + (speedFactor*0.1)/20);
	}
	
	//return the current score after the game has ended
	public int getScore() {
		return this.score;
	}
}
