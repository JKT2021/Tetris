package tetrisgame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Implementing the game logic
 */

public class GamePanel extends JPanel implements ActionListener{
	
	final static int WINDOW_HEIGHT = 700;
	final static int WINDOW_WIDTH = 600;
	final static int SCREEN_HEIGHT = 700;
	final static int SCREEN_WIDTH = 400;
	final static int DELAY = 25;
	final static Color[] color = {new Color(0,255,255), new Color(255,255,0), new Color(255,20,147), new Color(255,140,0), new Color(124,252,0)};
	
	static int speed;
	static int[][] board;
	boolean running;
	int score;
	int level;
	int lines;
	static int highscore;
	
	Timer timer;
	Tetrominoes t;
	JButton restart;
	
	public GamePanel() {
		
		restart = new JButton("RESTART");
		restart.setBounds(WINDOW_WIDTH-160, 600, 120, 60);
		restart.addActionListener(this);
		this.add(restart);
		
		this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		this.setBackground(Color.LIGHT_GRAY);
		this.addKeyListener(new KeyHandler());
		this.setFocusable(true);
		this.setLayout(null);
		
		setup();
		
	}
	
	//is called only once when the game is started
	public void setup() {

		highscore = 0;
		speed = 2;
		level = 1;
		t = new Tetrominoes();
		timer = new Timer(DELAY, this);
		start();
		
	}
	
	//is called when the game is started/restarted or when a new level is reached
	public void start() {
		
		timer.start();
		running = true;
		restart.setVisible(false);
		t.piece = t.chooseShape();
		board = newBoard();
		score = 0;
		lines = 0;
		
	}
	
	//setting up a new empty Board
	public int[][] newBoard(){
		
		int[][] newBoard = new int[SCREEN_HEIGHT/20][SCREEN_WIDTH/20];
		
		for(int i=0; i<newBoard.length; i++) {
			for(int j=0; j<newBoard[0].length; j++) {
				newBoard[i][j] = -1;
			}
		}
		return newBoard;
	}
	
	public void newShape() {
		t.piece = t.chooseShape();
	}
	
	//placing the old piece on the board and assigning a number referring to the color of this piece
	public void place() {
		for(int i=0; i<t.piece.length; i++) {
			for(int j=0; j<t.piece[0].length; j++) {
			
				if(t.piece[i][j])board[i+(t.y/20)][j+(t.x/20)] = t.shape;
			}
		}
	}
	
	//moving down the current piece
	public void moveDown() {

		if(possibleMove(1)) {
			t.y+=speed;
		}else {
			place(); //if the current piece can't move down place it on the board
			fullRow(); //check if there exist any full rows
			checkLevelUp(); //check if the next level is reached
			newShape(); //generate a new piece
			
			//if the new piece can't move down show game over screen
			if(!possibleMove(1)) gameOver();
		}
		
	}
	
	//checking whether it is possible to move the piece in a certain direction
	public boolean possibleMove(int direction) {
		
		int xpos = t.x;
		int ypos = t.y;
		
		switch(direction) {
		
		case 1 : ypos+=20; break; //down
		case 2 : xpos-=20; break; //left
		case 3 : xpos+=20; break; //right
		
		}
		
		for(int i=0; i<t.piece.length; i++) {
			for(int j=0; j<t.piece[0].length; j++) {
				
				if(t.piece[i][j]) {
					
					int blockXPos = j*20;
					int blockYPos = i*20;
					int currx = xpos+blockXPos;
					int curry = ypos+blockYPos;
					
					//if the piece would be outside the game frame or overlapping with another piece return false
					if(currx<0||currx>=SCREEN_WIDTH||curry>=SCREEN_HEIGHT)return false;
					else if(board[curry/20][currx/20]!=-1)return false;
					
				}
				
			}
		}
		
		return true;
	}
	
	//checking whether a rotation of the current piece is possible
	public boolean possibleRotation() {
		
		boolean[][] rotatedPiece = t.rotate();

		for(int i=0; i<rotatedPiece.length; i++) {
			for(int j=0; j<rotatedPiece[0].length; j++) {
				
				if(rotatedPiece[i][j]) {
					
					int blockXPos = j*20;
					int blockYPos = i*20;
					int currx = t.x+blockXPos;
					int curry = t.y+blockYPos;
					
					if(currx<0||currx>=SCREEN_WIDTH||curry>=SCREEN_HEIGHT)return false;
					else if(board[curry/20][currx/20]!=-1)return false;
					
				}
				
			}
		}
		
		return true;
	}
	
	//moving the piece 
	public void move(int direction) {
		
		switch(direction) {
		
		case 1 : t.y+=20; break; 
		case 2 : t.x-=20; break;
		case 3 : t.x+=20; break;
		
		}
		
	}
	
	//checking whether there exist one or multiple full rows
    public void fullRow() {
		
		List<Integer> l = new ArrayList<>();
		
		for(int i=0; i<board.length; i++) {
			boolean full = true;
			for(int j=0; j<board[0].length; j++) {
				if(board[i][j]==-1) {
					full = false;
					break;
				}
			}
			if(full)l.add(i);
		}
		
		if(l.size()!=0) {
			
			//if there are one or more full rows update the score/highscore
			score+=20*l.size();
			highscore = Math.max(highscore, score);
			
			lines+=l.size();
			removeFull(l);
		}
	}
	
    //removing/shifting full lines
	public void removeFull(List<Integer> l) {
		
		int shift = l.size();
		
		for(int i=l.get(0)-1; i>=0; i--) {
			for(int j=0; j<board[0].length; j++) {
				board[i+shift][j]=board[i][j];
			}
		}
		
	}
	
	//check if the player has reached the next level
	public void checkLevelUp() {
		
		if(score>=200) {
		speed++;
		level++;
		start();
		}
		
	}
	
	public void gameOver() {
		
		running = false;
		restart.setVisible(true); 
		timer.stop();
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
	  super.paintComponent(g);
	  draw(g);
		
	}
	
	public void draw(Graphics g) {
		
		g.setColor(Color.BLACK);
		for(int i=0; i<SCREEN_HEIGHT; i+=20) {
			g.drawLine(0, i, SCREEN_WIDTH, i);
		}
		for(int j=0; j<=SCREEN_WIDTH; j+=20) {
			g.drawLine(j, 0, j, SCREEN_HEIGHT);
		}
		
		//drawing current piece
		g.setColor(color[t.shape]);
		for(int i=0; i<t.piece.length; i++) {
			for(int j=0; j<t.piece[0].length; j++) {
				if(t.piece[i][j])g.fillRect(t.x+20*j, t.y+20*i, 20, 20);
			}
		}
		
		//drawing current board
		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board[0].length; j++) {
				
				if(board[i][j]!=-1) {
					g.setColor(color[board[i][j]]);
					g.fillRect(j*20, i*20, 20, 20);
				}
			}
		}
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.drawString("Score: " + score, WINDOW_WIDTH-180, 40);
		g.drawString("Level: " + level, WINDOW_WIDTH-180, 120);
		g.drawString("Full Lines: " + lines, WINDOW_WIDTH-180, 200);
		g.drawString("High Score: " + highscore, WINDOW_WIDTH-180, 280);
		g.drawString("Next: ", WINDOW_WIDTH-130, 400);
		
		//drawing next piece
		g.setColor(color[t.next]);
		boolean[][] nextShape = t.createShape(t.next);
		for(int i=0; i<nextShape.length; i++) {
			for(int j=0; j<nextShape[0].length; j++) {
				if(nextShape[i][j])g.fillRect(WINDOW_WIDTH-130+20*j, 430+20*i, 20, 20);
			}
		}
		
		//game over text
		if(!running) {
			g.setColor(Color.RED);
			g.drawString("GAME OVER", WINDOW_WIDTH-160, 580);
		}
		
	}
	
	//game loop
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			moveDown();
		}else {
			if(e.getSource()==restart)start();
		}
		
		repaint();
		
	}
	
	public class KeyHandler extends KeyAdapter {
		
		@Override
		public void keyPressed(KeyEvent e) {
			
			int keycode = e.getKeyCode();
			
			//if a key is pressed check whether it is possible to move/rotate the piece in that direction
			switch(keycode) {
			
			case KeyEvent.VK_UP: if(possibleRotation())t.piece = t.rotate(); break;
			case KeyEvent.VK_DOWN : if(possibleMove(1))move(1); break; 
			case KeyEvent.VK_LEFT : if(possibleMove(2))move(2); break; 
			case KeyEvent.VK_RIGHT : if(possibleMove(3))move(3); break; 
			
			}
			
		}
		
	}

}

