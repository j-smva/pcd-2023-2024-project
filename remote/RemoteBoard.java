package remote;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import game.GameElement;
import environment.LocalBoard;
import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import game.AutomaticSnake;
import game.Goal;
import game.Obstacle;
import game.ObstacleMover;
import game.Snake;
import game.Server;

/** Remote representation of the game, no local threads involved.
 * Game state will be changed when updated info is received from Srver.
 * Only for part II of the project.
 * @author luismota
 *
 */
public class RemoteBoard extends Board{
	
	private static final int NUM_SNAKES = 1;
	private static final int NUM_OBSTACLES = 25;
	private static final int NUM_SIMULTANEOUS_MOVING_OBSTACLES = 3;
	public int LastPressedKey;
	@Override
	public void handleKeyPress(int keyCode) {
		LastPressedKey=keyCode;
	}

	@Override
	public void handleKeyRelease() {
		LastPressedKey=KeyEvent.VK_UNDEFINED;
	}

	@Override
	public void init() {
		// TODO 		
	}


	

}
