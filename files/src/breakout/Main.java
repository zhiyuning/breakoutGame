package breakout;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		JFrame obj = new JFrame();
		Gameplay gameplay = new Gameplay();
		ScoreBoard scoreboard = new ScoreBoard();
		
		 gameplay.setScoreBoard(scoreboard);
		
		obj.setLayout(new BorderLayout());
		obj.add(gameplay, BorderLayout.CENTER);
		obj.add(scoreboard, BorderLayout.EAST);
		
		obj.setBounds(10, 10, 1000, 600);
		obj.setTitle("Breakout Ball");
		obj.setResizable(false);
		obj.setVisible(true);
		obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		obj.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        // Check if the mouse click is within the region of the gameplay component
		        if (gameplay.getBounds().contains(e.getPoint())) {
		            gameplay.requestFocus();
		        } else if (scoreboard.getBounds().contains(e.getPoint())) {
		            scoreboard.requestFocus();
		        }
		    }
		});
	}
}
