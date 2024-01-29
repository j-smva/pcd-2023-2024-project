package game;

import java.util.LinkedList;
import java.util.List;

import javax.swing.text.Position;

import remote.RemoteBoard;
import environment.LocalBoard;
import gui.SnakeGui;
import environment.Cell;
import environment.Board;
import environment.BoardPosition;

public class AutomaticSnake extends Snake {
	private Cell previousMove=null;
	
	public AutomaticSnake(int id, LocalBoard board) {
		super(id,board);

	}
	public AutomaticSnake(int id, RemoteBoard board) {
		super(id,board);

	}
	@Override
	public void run() {
		doInitialPositioning();
		System.err.println("initial size:"+cells.size());
			do{
				try {
				autoMove();
				Thread.sleep(getBoard().PLAYER_PLAY_INTERVAL);
				} catch (InterruptedException e) {
					//e.printStackTrace();
					interrupt=true;
				}
			}while(running);
		}
	
	
	
	//public void pathPick()
	public void autoMove() {
		BoardPosition goalPosition = getBoard().getGoalPosition();
		BoardPosition snakePosition = getCells().get(0).getPosition();
		List<BoardPosition> list = getBoard().getNeighboringPositions(getBoard().getCell(snakePosition));
		double bestDistance = Double.MAX_VALUE;
		double worstDistance = 0;
		Cell Move = null;
		for(BoardPosition pos: list) {
			pos.distanceTo(goalPosition);
			if(interrupt) {
				Move=randomMove(list);
			}else if(!interrupt && pos.distanceTo(goalPosition) < bestDistance && getBoard().getCell(pos).getOcuppyingSnake()!=this) {
			   bestDistance = pos.distanceTo(goalPosition);
			   Move=getBoard().getCell(pos);
			}
		}
		if(Move!= null) {
			previousMove=Move;
			interrupt=false;
			moveaux(Move);
		}
	}
	
	private Cell randomMove(List<BoardPosition> list){
		if(list.isEmpty())
			return null;
		list.remove(previousMove);
		int random=(int)(Math.random()*list.size());
		Cell cell = getBoard().getCell(list.get(random));
		if(cell.getOcuppyingSnake()!=this) {
			return cell;
		}else {
			list.remove(cell);
			return randomMove(list);
		}
	}

	
	
	public void moveaux(Cell cell){
				if(cell.isOcupiedByGoal())
					addSize(cell.removeGoal().captureGoal());
				try {
					if(!cell.isOcupied())
					move(cell);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	}
}