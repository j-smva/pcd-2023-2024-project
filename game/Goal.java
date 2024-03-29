package game;

import environment.Board;
import environment.Cell;
import environment.LocalBoard;

public class Goal extends GameElement {
	private int value=1;
	private Board board;
	public static final int MAX_VALUE=10;
	public Goal( Board board2) {
		this.board = board2;
	}
	
	public int getValue() {
		return value;
	}
	public void incrementValue(){
		if(value!=MAX_VALUE)
		value++;
	}

	public int captureGoal() {
//		TODO
		incrementValue();
		if(value==MAX_VALUE) {
			for (Snake snake : board.getSnakes()) {
				snake.running=false;
			}
		}
		else {
			board.newGoal(this);
		}
		return value-1;
	}
}
