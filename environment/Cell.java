package environment;

import java.io.Serializable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.sound.midi.SysexMessage;

import game.GameElement;
import game.Goal;
import game.Obstacle;
import game.Snake;
import game.AutomaticSnake;
/** Main class for game representation. 
 * 
 * @author luismota
 *
 */
public class Cell implements Serializable{
	private BoardPosition position;
	private Snake ocuppyingSnake = null;
	private GameElement gameElement=null;
	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	
	public GameElement getGameElement() {
		return gameElement;
	}


	public Cell(BoardPosition position) {
		super();
		this.position = position;
	}

	public BoardPosition getPosition() {
		return position;
	}

	public void request(Snake snake) throws InterruptedException {
		lock.lock();
		try {
			while(isOcupied()) {
				condition.await();
			}
			ocuppyingSnake=snake;
		}finally {
			lock.unlock();
		}
	}

	public void release() {
		lock.lock();
		try {
		ocuppyingSnake=null;
		gameElement=null;
		condition.signalAll();
		}finally {
			lock.unlock();
		}
	}

	public boolean isOcupiedBySnake() {
		return ocuppyingSnake!=null;
	}
	
	public boolean isOcupiedByObstacle() {
		return (gameElement!=null && gameElement instanceof Obstacle);
	}


	public  void setGameElement(GameElement element) {
		// TODO coordination and mutual exclusion
		gameElement=element;

	}

	public boolean isOcupied() {
		return isOcupiedBySnake() || (gameElement!=null && gameElement instanceof Obstacle);
	}


	public Snake getOcuppyingSnake() {
		return ocuppyingSnake;
	}


	public  Goal removeGoal() {
		Goal goal = getGoal();
		release();
		return goal;
	}
	
	public void removeObstacle() {
	//TODO
	}


	public Goal getGoal() {
		return (Goal)gameElement;
	}


	public boolean isOcupiedByGoal() {
		return (gameElement!=null && gameElement instanceof Goal);
	}
	
	

}
