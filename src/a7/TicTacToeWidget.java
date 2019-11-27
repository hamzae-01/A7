package a7;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TicTacToeWidget extends JPanel implements ActionListener, SpotListener {


	
	private enum Player {BLACK, WHITE};
	
	private JSpotBoard board;	//field creating board
	private JLabel message;		//field creating messages
	private boolean gameWon;	//tells if game is won
	private boolean drawGame;	//tells if there is a draw
	private Spot sSpot;			//winning spot
	private Color sSpotReset;  	//resets the sSpot
	private Player nextPlayer;	//lets next plaer make a move
	
	public TicTacToeWidget() {
		

		
		board = new JSpotBoard(3,3);
		message = new JLabel();

		
		setLayout(new BorderLayout());
		add(board, BorderLayout.CENTER);

	
		
		JPanel reset_message_panel = new JPanel();
		reset_message_panel.setLayout(new BorderLayout());

	
		
		JButton reset_button = new JButton("Restart");
		reset_button.addActionListener(this);
		reset_message_panel.add(reset_button, BorderLayout.EAST);
		reset_message_panel.add(message, BorderLayout.CENTER);

		
		add(reset_message_panel, BorderLayout.SOUTH);

		board.addSpotListener(this);

		resetGame();
	}



	private void resetGame() {


		for (Spot s : board) {
			s.clearSpot();
			s.setSpotColor(Color.BLACK);
		}
			
		

		gameWon = false;
		drawGame = false;
		nextPlayer = Player.WHITE;		
		

	
		
		message.setText("Welcome to TicTacToe. White to play");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
				resetGame();
	}

	
	
	@Override
	public void spotClicked(Spot s) {
		
		
		if (gameWon) {
			return;
		}


		
		String playerName = null;
		String nextPlayerName = null;
		Color playerColor = null;
		
		if (nextPlayer == Player.BLACK) {
			playerColor = Color.BLACK;
			playerName = "Black";
			nextPlayerName = "White";
			nextPlayer = Player.WHITE;
		} else {
			playerColor = Color.WHITE;
			playerName = "White";
			nextPlayerName = "Black";
			nextPlayer = Player.BLACK;			
		}
				
		
		
		if (s.isEmpty()) {
			s.setSpotColor(playerColor);
			s.toggleSpot();
			
		} else {
			
			if (nextPlayer == Player.BLACK) {
				playerColor = Color.WHITE;
				playerName = "White";
				nextPlayerName = "White";
				nextPlayer = Player.WHITE;
				
			} else {
				
				playerColor = Color.BLACK;
				playerName = "Black";
				nextPlayerName = "Black";
				nextPlayer = Player.BLACK;			
				
			}
		}
		
		System.out.println("Win Checker Output: " + checkWin());


		
		if (s.isEmpty()) {
			message.setText(nextPlayerName + " to play.");
		} else {
			if (gameWon)  {

				message.setText(playerName + " wins! ");
			} else {
				
				if (checkDraw() && (gameWon == false))  {

					message.setText(" Draw game.");
					
				} else {
				
				message.setText(nextPlayerName + " to play.");
				}
			}
		}
	}

	@Override
	public void spotEntered(Spot s) {
		
		if (gameWon) {
			return;
		}
		s.highlightSpot();
	}

	@Override
	public void spotExited(Spot s) {

		s.unhighlightSpot();
	}

	public boolean checkWin() {
		
		if (gameWon) {
			return true;
		}
		
		int blackAmount = 0;
		int whiteAmount = 0;
		
		
		for (int x = 0; x < 3; x++) {
			
			blackAmount = 0;
			whiteAmount = 0;
			
			for (int y = 0; y < 3; y++) {

				
				if (board.getSpotAt(x, y).getSpotColor() == Color.WHITE) {
					System.out.println("Grid Coordinates = X: " + board.getSpotAt(x, y).getSpotX()
							+ " Y: " + board.getSpotAt(x, y).getSpotY());
					whiteAmount++;
				}
				
				else if (board.getSpotAt(x, y).getSpotColor() == Color.BLACK && (board.getSpotAt(x, y).isEmpty() == false)) {
					System.out.println("Grid Coordinates = X: " + board.getSpotAt(x, y).getSpotX()
							+ " Y: " + board.getSpotAt(x, y).getSpotY());
					blackAmount++;
				}
				

				
				
			}
			
			
			
			
			
			if (whiteAmount == 3 || blackAmount == 3) {
				System.out.println("Straight Line, Whites = " + whiteAmount
						+ " Blacks: " + blackAmount);
				gameWon = true;

				return true;
				
				
			}
			
			
		
		}
		
		blackAmount = 0;
		whiteAmount = 0;
		
		
		for (int y = 0; y < 3; y++) {
			
			blackAmount = 0;
			whiteAmount = 0;

			for (int x = 0; x < 3; x++) {
				
				if (board.getSpotAt(x, y).getSpotColor() == Color.WHITE  && (board.getSpotAt(x, y).isEmpty() == false)) {
					System.out.println("Grid Coordinates = X: " + board.getSpotAt(x, y).getSpotX()
							+ " Y: " + board.getSpotAt(x, y).getSpotY());
					whiteAmount++;
				}
				
				else if (board.getSpotAt(x, y).getSpotColor() == Color.BLACK && (board.getSpotAt(x, y).isEmpty() == false)) {
					System.out.println("Grid Coordinates = X: " + board.getSpotAt(x, y).getSpotX()
							+ " Y: " + board.getSpotAt(x, y).getSpotY());
					blackAmount++;
				}
				

				
				
			}
			
			
			
			System.out.println("Blacks " + blackAmount);
			System.out.println("Whites " + whiteAmount);
			
			
			if (whiteAmount == 3 || blackAmount == 3) {
				System.out.println("Straight Line, Whites = " + whiteAmount
						+ " Blacks: " + blackAmount);
				gameWon = true;

				return true;

				
			}
			
			
		
		}
		
		blackAmount = 0;
		whiteAmount = 0;
		

		for (int n = 0, l = 2; n < 3; n++, l--) {
			
			System.out.println("Spot Colour at Position: " + board.getSpotAt(n, l).getSpotX()
					+ " Y: " + board.getSpotAt(n, l).getSpotY() + "  is: "
				+ board.getSpotAt(n, l).getSpotColor());

			
			if (board.getSpotAt(n, l).getSpotColor() == Color.WHITE  && (board.getSpotAt(n, l).isEmpty() == false)) {
				System.out.println("Grid Coordinates = X: " + board.getSpotAt(n, l).getSpotX()
						+ " Y: " + board.getSpotAt(n, l).getSpotY());
				whiteAmount++;
			}
			
			else if (board.getSpotAt(n, l).getSpotColor() == Color.BLACK && (board.getSpotAt(n, l).isEmpty() == false)) {
				blackAmount++;
			}
		}
		
		if (whiteAmount == 3 || blackAmount == 3) {
			
			System.out.println("Diagonal Right, Whites = " + whiteAmount
					+ " Blacks: " + blackAmount);
			gameWon = true;
			return true;
		} 
		
		blackAmount = 0;
		whiteAmount = 0;

		for (int i = 3 - 1, k = 3 - 1; i >= 0; i--, k--) {
			if (board.getSpotAt(i, k).getSpotColor() == Color.WHITE  && (board.getSpotAt(i, k).isEmpty() == false)) {
				whiteAmount++;
			}
			else if (board.getSpotAt(i, k).getSpotColor() == Color.BLACK && (board.getSpotAt(i, k).isEmpty() == false)) {
				blackAmount++;
		}
		}
		
		if (blackAmount == 3 || whiteAmount == 3) {
			System.out.println("Diagonal Left, Whites = " + whiteAmount
					+ " Blacks: " + blackAmount);
			gameWon = true;
			return true;
		}
		
		return false;

	}
	



public boolean checkDraw() {
	int countBlacks = 0;
	int countWhites = 0;
	
	for (int x = 0; x < 3; x++) {
		
		for (int y = 0; y < 3; y++) {
			
			if (board.getSpotAt(x, y).getSpotColor() == Color.WHITE) {
				countWhites++;
			}
			
			else if (board.getSpotAt(x, y).getSpotColor() == Color.BLACK && (board.getSpotAt(x, y).isEmpty() == false)) {
				
				countBlacks++;
			}
			
		}
		
		System.out.println("Blacks " + countBlacks);
		System.out.println("Whites " + countWhites);
		
		
		if (countWhites == 5 && countBlacks == 4) {

			drawGame = true;
			gameWon = false;

			return true;
			
			
		}
		
		
		
	
	}
	return false;
	

}

}