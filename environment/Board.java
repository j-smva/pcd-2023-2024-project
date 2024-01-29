package environment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import game.GameElement;
import game.Goal;
import game.Obstacle;
import game.Snake;

public abstract class Board extends Observable {
	protected Cell[][] cells;
	private BoardPosition goalPosition;
	public static final long PLAYER_PLAY_INTERVAL = 100;
	public static final long REMOTE_REFRESH_INTERVAL = 200;
	public static final int NUM_COLUMNS = 30;
	public static final int NUM_ROWS = 30;
	protected LinkedList<Snake> snakes = new LinkedList<Snake>();
	private LinkedList<Obstacle> obstacles= new LinkedList<Obstacle>();
	protected boolean isFinished;

	public Board() {
		cells = new Cell[NUM_COLUMNS][NUM_ROWS];
		for (int x = 0; x < NUM_COLUMNS; x++) {
			for (int y = 0; y < NUM_ROWS; y++) {
				cells[x][y] = new Cell(new BoardPosition(x, y));
			}
		}

	}

	public Cell getCell(BoardPosition cellCoord) {
		return cells[cellCoord.x][cellCoord.y];
	}
	public Cell[][] getCells() {
		return cells;
	}
	public void setCells(Cell[][] celss) {
		cells=celss;
	}

	public BoardPosition getRandomPosition() {
		return new BoardPosition((int) (Math.random() *NUM_ROWS),(int) (Math.random() * NUM_ROWS));
	}

	public BoardPosition getGoalPosition() {
		return goalPosition;
	}

	public void setGoalPosition(BoardPosition goalPosition) {
		this.goalPosition = goalPosition;
	}
	
	public void addGameElement(GameElement gameElement) {
		boolean placed=false;
		while(!placed) {
			BoardPosition pos=getRandomPosition();
			if(!getCell(pos).isOcupied() && !getCell(pos).isOcupiedByGoal()) {
				if(gameElement instanceof Obstacle) {
					Obstacle obs = (Obstacle)gameElement;
					obs.setObstaclePosition(pos);
					getCell(pos).setGameElement(obs);
//					System.out.println("Goal placed at:"+pos);
				}
				if(gameElement instanceof Goal) {
					setGoalPosition(pos);
					getCell(pos).setGameElement(gameElement);
//					System.out.println("Goal placed at:"+pos);
				}
				placed=true;
			}
		}
	}

	public List<BoardPosition> getNeighboringPositions(Cell cell) {
		ArrayList<BoardPosition> possibleCells=new ArrayList<BoardPosition>();
		BoardPosition pos=cell.getPosition();
		if(pos.x>0)
			possibleCells.add(pos.getCellLeft());
		if(pos.x<NUM_COLUMNS-1)
			possibleCells.add(pos.getCellRight());
		if(pos.y>0)
			possibleCells.add(pos.getCellAbove());
		if(pos.y<NUM_ROWS-1)
			possibleCells.add(pos.getCellBelow());
		return possibleCells;

	}
	public Goal newGoal(Goal goal) {
		addGameElement( goal);
		return goal;
	}
	

	protected Goal addGoal() {
		Goal goal=new Goal(this);
		addGameElement( goal);
		return goal;
	}

	public void addObstacles(int numberObstacles) {
		getObstacles().clear();
		while(numberObstacles>0) {
			Obstacle obs=new Obstacle(this);
			addGameElement(obs);
			getObstacles().add(obs);
			numberObstacles--;
		}
	}
	
	public LinkedList<Snake> getSnakes() {
		return snakes;
	}
	public void setSnakes(LinkedList<Snake> snakes) {
		this.snakes=snakes;
	}


	@Override
	public void setChanged() {
		super.setChanged();
		notifyObservers();
	}

	public LinkedList<Obstacle> getObstacles() {
		return obstacles;
	}

	
	public abstract void init(); 
	
	public abstract void handleKeyPress(int keyCode);

	public abstract void handleKeyRelease();
	
	
	

	public void addSnake(Snake snake) {
		snakes.add(snake);
	}


}