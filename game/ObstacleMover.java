package game;

import environment.LocalBoard;
import remote.RemoteBoard;
import environment.Board;

public class ObstacleMover extends Thread {
	private Obstacle obstacle;
	private Board board;

	
	public ObstacleMover(Obstacle obstacle, LocalBoard board) {
		super();
		this.obstacle = obstacle;
		this.board = board;
	}
	public ObstacleMover(Obstacle obstacle, RemoteBoard board) {
		super();
		this.obstacle = obstacle;
		this.board = board;
	}

	@Override
	public void run() {
			try {
				while(obstacle.getRemainingMoves()!=0) {
					obstacle.randomMove();
					Thread.sleep(obstacle.getMoveInterval());
				}
				interrupt();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
