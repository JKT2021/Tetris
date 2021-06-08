package tetrisgame;

import javax.swing.JFrame;

/**
 * Java Tetris clone
 * @author jkt
 */

public class Tetris extends JFrame{
	
	GamePanel panel;
	
	//setting up the game frame
	public Tetris() {
		
		panel = new GamePanel();
		this.add(panel);
		
		this.setTitle("Tetris");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.pack();
		this.setVisible(true);
	}
	
	public static void main (String[] args) {
		
		//starting the game
		new Tetris();
	}

}

