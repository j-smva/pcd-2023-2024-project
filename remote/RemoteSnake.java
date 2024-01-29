package remote;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
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
import game.HumanSnake;

public class RemoteSnake extends HumanSnake {
	public LocalBoard board;
	public RemoteSnake(int id,Board board) {
		super(id,board);
		this.board=(LocalBoard) board;
	}
	public void HumanMove(int keyCode){
		switch(keyCode) {
		case(KeyEvent.VK_UP):
			try {
			MoveAux(board.getCell(this.getCells().getFirst().getPosition().getCellAbove()));
			}catch(ArrayIndexOutOfBoundsException e){
				
			}
			break;
		case(KeyEvent.VK_DOWN):
			try {
			MoveAux(board.getCell(this.getCells().getFirst().getPosition().getCellBelow()));
			}catch(ArrayIndexOutOfBoundsException e){
				
			}
			break;
		case(KeyEvent.VK_LEFT):
			try {
			MoveAux(board.getCell(this.getCells().getFirst().getPosition().getCellLeft()));
			}catch(ArrayIndexOutOfBoundsException e){
				
			}
			break;
		case(KeyEvent.VK_RIGHT):
			try {
			MoveAux(board.getCell(this.getCells().getFirst().getPosition().getCellRight()));
			}catch(ArrayIndexOutOfBoundsException e){
			}
			break;
		}
	}
	public void MoveAux(Cell cell) {
		if(cell.isOcupiedByGoal())
			addSize(cell.removeGoal().captureGoal());
		try {
			if(!cell.isOcupied())
				move(cell);
		} catch (InterruptedException e) {
		}
	}
}
