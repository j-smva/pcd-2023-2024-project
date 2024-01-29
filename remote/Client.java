package remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Scanner;

import environment.Board;
import environment.Cell;
import game.Snake;
import gui.SnakeGui;

public class Client {
	private InetAddress ipAddress;
	private int port;
	private Socket socket;
	private PrintWriter out;
	private ObjectInputStream in;
	public RemoteBoard board;

	public Client(InetAddress byName, int i) {
		this.ipAddress = byName;
		this.port = i;
	}

	public void runClient() {
		try {
			socket = new Socket(ipAddress, port);
			getStreams();
			proccessConnection();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}

	private void closeConnection() {
		System.out.println("clossing it");
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

	private void proccessConnection() {
		board = new RemoteBoard();
		SnakeGui game = new SnakeGui(board,600,0);
		game.init();
		//board.setChanged();
		InputConnection();
		while(socket.isConnected()) {
			try {
				out.println(board.LastPressedKey);
				Thread.sleep(Board.PLAYER_PLAY_INTERVAL);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void InputConnection() {
		new Thread() {
			@Override
			public void run() {
				do{
					try {
						board.setSnakes((LinkedList<Snake>)in.readObject());	
						board.setCells((Cell [][])in.readObject());
						board.setChanged();
					} catch (ClassNotFoundException | IOException e) {
						try {
							in.reset();
						} catch (IOException e1) {

							e1.printStackTrace();
						}
						e.printStackTrace();
					}
					}
				while(socket.isConnected());
			}
		}.start();
	}

	private void getStreams() throws IOException {
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new ObjectInputStream (socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws UnknownHostException {
		Client client = new Client(InetAddress.getByName("localhost"), 12345);
		client.runClient();
	}

}
