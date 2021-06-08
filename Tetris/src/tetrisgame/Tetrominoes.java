package tetrisgame;

import java.util.Random;

/**
 * Used to establish the basic properties of the different Tetrominoe pieces
 */

public class Tetrominoes {
	
	final static int TETROMINOE_SIZE = 4;
	
	Random random = new Random();
	boolean[][] piece;
	int shape;
	int next;
	int y;
	int x;	
	
	public Tetrominoes() {
		next = randomShape();
	}
	
	public int randomShape() {
		
		return random.nextInt(TETROMINOE_SIZE+1);
	}
	
	//returns a random Tetrominoe shape
	public boolean[][] chooseShape(){
		
        shape = next;
        x = GamePanel.SCREEN_WIDTH/2;
		y = 0;
		next = random.nextInt(TETROMINOE_SIZE+1);
		
		return createShape(shape);
	}
	
	public boolean[][] createShape(int shapeNum){
		
		boolean[][] newShape = new boolean[TETROMINOE_SIZE][TETROMINOE_SIZE];
		
		switch(shapeNum) {
		case 0 : return straight(newShape);
		case 1 : return square(newShape);
		case 2 : return T(newShape);
		case 3 : return L(newShape);
		case 4 : return skew(newShape);
		default : return null;
		}
		
	}
	
	public boolean[][] straight(boolean[][] newShape) {
		newShape[1][0] = true;
		newShape[1][1] = true;
		newShape[1][2] = true;
		newShape[1][3] = true;
		
		return newShape;
	}
	
	public boolean[][] square(boolean[][] newShape){
		newShape[0][0] = true;
		newShape[0][1] = true;
		newShape[1][0] = true;
		newShape[1][1] = true;
		
		return newShape;
	}
	
	public boolean[][] T(boolean[][] newShape){
		newShape[1][0] = true;
		newShape[1][1] = true;
		newShape[1][2] = true;
		newShape[2][1] = true;
		
		return newShape;
	}
	
	public boolean[][] L(boolean[][] newShape){
		newShape[0][1] = true;
		newShape[1][1] = true;
		newShape[2][1] = true;
		newShape[2][2] = true;
		
		return newShape;
	}
	
	public boolean[][] skew(boolean[][] newShape){
		newShape[1][0] = true;
		newShape[1][1] = true;
		newShape[0][1] = true;
		newShape[2][0] = true;
		
		return newShape;
	}
	
	//method that rotates the current piece
	public boolean[][] rotate() {
		
		int len;
		boolean[][] rotation = new boolean[TETROMINOE_SIZE][TETROMINOE_SIZE];
		
		switch(shape) {
		
		case 0: len = 4; break;
		case 1: len = 2; break;
		default : len = 3;
		
		}
		
		//calculating the transpose of the Matrix that represents the current piece
		for(int i=0; i<len; i++) {
			for(int j=0; j<len; j++) {
				rotation[i][j] = piece[j][i];
				rotation[j][i] = piece[i][j];
			}
		}
		
		//reversing each row
		for(int i=0; i<len; i++) {
			for(int j=0; j<len/2; j++) {
				boolean store = rotation[i][j];
				rotation[i][j] = rotation[i][len-1-j];
				rotation[i][len-1-j] = store;
		    }
		}
		
		return rotation;
			
		}

}
