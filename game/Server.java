package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.LinkedList;
import java.util.Scanner;

import environment.Board;
import environment.Cell;
import environment.LocalBoard;
import gui.SnakeGui;
import remote.RemoteSnake;

public class Server {
	private ServerSocket server;
	public LocalBoard board;

	private void runServer() {
		try {
			server = new ServerSocket(12345);
			board = new LocalBoard();
			board.init();
			while(true) {
				waitForConnections();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void waitForConnections() throws IOException {
		System.out.println("Waiting for connections...");
		Socket connection = server.accept();

		ConnectionHandler handler = new ConnectionHandler(connection);
		handler.start();

		System.out.println("New client connection: " + connection.getInetAddress().getHostName());
	}

	private class ConnectionHandler extends Thread{
		private Socket socket;
		private ObjectOutputStream out;
		private Scanner in;
		public  Snake snaem;

		public ConnectionHandler(Socket connection) {
			this.socket = connection;
		}

		@Override
		public void run() {
			try {
				getStream();
				proccessConnection();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				closeConnection();
			}
		}

		private void proccessConnection() {
			RemoteSnake snake= new RemoteSnake(board.getSnakes().size(),board);
			snaem=snake;
			board.addSnake(snake);
			snake.doInitialPositioning();
			InputConnection(snake);
			//System.out.println(snaem.getId());
			do {
				try {
					LinkedList<Snake> snakes=board.getSnakes();
					out.writeObject(snakes);
					out.writeObject(board.getCells());
					out.reset();
					//System.out.println(snake.getId());
					Thread.sleep(Board.REMOTE_REFRESH_INTERVAL);
				} catch (InterruptedException | IOException e) {
					try {
						out.reset();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			} while (snake.running);
		}

		public void InputConnection(RemoteSnake snake) {
			new Thread() {
				@Override
				public void run() {
					do{
						snake.HumanMove(Integer.parseInt(in.next()));
					}
					while(socket.isConnected());
				}
			}.start();
		}
		private void getStream() throws IOException {
			in = new Scanner(socket.getInputStream());
			out =new ObjectOutputStream(socket.getOutputStream());
		}

		private void closeConnection() {	
			//System.out.println(snaem.getId());
			//System.out.println("clossing it");
			try {
				if (in != null)
					in.close();

				if (out != null)
					out.close();

				if (socket != null)
					socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.runServer();	
	}

}