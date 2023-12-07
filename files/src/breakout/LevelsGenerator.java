package breakout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Random;

public class LevelsGenerator {
	//21 blocks
	public int level[][];
	public int width;
	public int height;
	
	
	public LevelsGenerator(int row, int col) {
		//set with 1 first
		level = new int[row][col];
		Random random = new Random();
		for(int i=0;i<level.length;i++) {
			for(int j=0;j<level[0].length;++j) {
				level[i][j] = random.nextInt(10)+1; 
				//level[i][j] =10;
			}
		}
		
		width = 540/col;
		height = 150/row;
	}
	
	public void draw(Graphics2D g) {
		for(int i=0;i<level.length;i++) {
			for(int j=0;j<level[0].length;++j) {
				if(level[i][j] !=  0) {
					if(level[i][j] == 10) {
						g.setColor(Color.RED);
					}else {
						g.setColor(Color.white);
					}
					g.fillRect(j * width + 80, i * height + 50, width, height);
				
					//draw string
					g.setColor(Color.black);
					if(level[i][j] != 10) {
						String text = Integer.toString(level[i][j]);
		                FontMetrics fontMetrics = g.getFontMetrics();
		                int x = j * width + 80 + (width - fontMetrics.stringWidth(text)) / 2;
		                int y = i * height + 50 + (height - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();
		                g.drawString(text, x, y);
					} else {
						String text = "Upgrade!";
		                FontMetrics fontMetrics = g.getFontMetrics();
		                int x = j * width + 80 + (width - fontMetrics.stringWidth(text)) / 2;
		                int y = i * height + 50 + (height - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();
		                g.drawString(text, x, y);
					}
					
					//create border for bricks
					g.setStroke(new BasicStroke(3));
					g.setColor(Color.black);
					g.drawRect(j * width + 80, i * height + 50, width, height);
				}
			}
		}
	}
	
	public void hitBricks(int row, int col) {
		//each hit would reduce the value by 1, until 0
		if(level[row][col] == 10) {
			level[row][col] = 0;
		} else if(level[row][col] > 0) {
			level[row][col]--;
		}
	}
	
	public int getStatus(int row, int col) {
		return level[row][col];
	}
}
