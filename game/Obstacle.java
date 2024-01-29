package game;

import java.io.Serializable;

import environment.Board;import environment.BoardPosition;
import environment.LocalBoard;

public class Obstacle extends GameElement implements Serializable{
	
	
	private static final int NUM_MOVES=3;
	private static final int OBSTACLE_MOVE_INTERVAL = 400;
	private int remainingMoves=NUM_MOVES;
	private int moveInterval=OBSTACLE_MOVE_INTERVAL;
	private Board board;
	private BoardPosition pos;
	public Obstacle(Board board) {
		super();
		this.board = board;
	}

	public void setObstaclePosition(BoardPosition pos) {
		this.pos=pos;
	}
	public BoardPosition getObstaclePosition() {
		return pos;
	}
	public int getRemainingMoves() {
		return remainingMoves;
	}
	public void downRemainingMoves() {
		remainingMoves--;
	}
	public int getMoveInterval() {
		return moveInterval;
	}
	public synchronized void randomMove() {
		BoardPosition pos = board.getRandomPosition();
		if(!board.getCell(pos).isOcupied()) {
			board.getCell(getObstaclePosition()).release();
			setObstaclePosition(pos);
			board.getCell(pos).setGameElement(this);
			downRemainingMoves();
			board.setChanged();
		}else {
			randomMove();
		}
	}

}
