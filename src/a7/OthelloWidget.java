package a7;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OthelloWidget extends JPanel implements ActionListener, SpotListener {

	private enum Player {
		BLACK, WHITE
	};

	
	private JSpotBoard board;
	private JLabel message;
	private boolean gameWon;
	private boolean drawGame;
	private int gameInt;
	public int noBlack;
	public int noWhite;
	private String winnerName;
	public HashSet<Point> placePosition;
	private Player nextPlayer;
	protected int size;
	protected int[] state;

	public OthelloWidget() {

		board = new JSpotBoard(8, 8, new Color(0.5f, 0.5f, 0.5f));
		size = 8;

		Spot initSpot1 = board.getSpotAt(4, 4);
		Spot initSpot2 = board.getSpotAt(4, 3);
		Spot initSpot3 = board.getSpotAt(3, 4);
		Spot initSpot4 = board.getSpotAt(3, 3);

		placePosition = new HashSet<Point>();

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
		spotClicked(initSpot3);

		spotClicked(initSpot4);
		spotClicked(initSpot2);
		spotClicked(initSpot1);

	}

	private void resetGame() {

		Spot spot1 = board.getSpotAt(4, 4);
		Spot spot2 = board.getSpotAt(4, 3);
		Spot spot3 = board.getSpotAt(3, 4);
		Spot spot4 = board.getSpotAt(3, 3);
		gameInt = 0;

		for (Spot s : board) {
			s.clearSpot();
			s.setSpotColor(Color.BLACK);
		}

		gameWon = false;
		drawGame = false;
		nextPlayer = Player.BLACK;

		spotClicked(spot3);

		spotClicked(spot4);
		spotClicked(spot2);
		spotClicked(spot1);

		message.setText("Welcome to Othello. Black to play");
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		resetGame();
	}

	@Override
	public void spotClicked(Spot s) {

		if (isLegalMove(s) == false && gameInt >= 4) {
			return;
		}

		if (gameWon) {
			return;
		}

		flipPieces(s);

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

		int validCount = 0;
		gameInt++;
		for (Spot finder : board) {

			finder.unhighlightSpot();
			if (isLegalMove(finder)) {
				validCount++;
				finder.highlightSpot();

			}
		}

		if (validCount == 0 && gameInt >= 4) {

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

		Color nextColor;
		if (playerColor == Color.BLACK) {
			nextColor = Color.WHITE;
		} else {
			nextColor = Color.BLACK;
		}

		if (gameInt >= 4) {
			if (s.isEmpty()) {
				message.setText(nextPlayerName + " to play.");
			} else {
				if ((checkWin() == true) && (winnerName != (null))) {

					message.setText(winnerName + " wins! " + " Score: " + noBlack + " to " + noWhite);

				} else {

					if ((winnerName == null) && (gameWon == true)) {

						message.setText(" Draw game.");

					} else {

						message.setText(nextPlayerName + " to play.");
					}
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

		if (isLegalMove(s) == true) {
			return;
		} else {

			if (gameWon == true) {
				return;
			}

			s.unhighlightSpot();
		}
	}

	public boolean checkWin() {

		if (gameWon) {
			return true;
		}

		int blackAmount = 0;
		int whiteAmount = 0;

		for (Spot s : board) {
			if (s.getSpotColor() == Color.WHITE) {

				whiteAmount++;
			}

			else if (s.getSpotColor() == Color.BLACK && (s.isEmpty() == false)) {

				blackAmount++;
			}
		}

		System.out.println("Blacks " + blackAmount);
		System.out.println("Whites " + whiteAmount);
		noWhite = whiteAmount;
		noBlack = blackAmount;

		int playColor;
		if (nextPlayer == Player.BLACK) {
			playColor = 0;
		} else {
			playColor = 1;
		}

		System.out.println("Legal? " + hasLegalMoves());
		if ((hasLegalMoves() == false)) {
			if (blackAmount > whiteAmount) {
				winnerName = "Black";
			}

			if (whiteAmount > blackAmount) {
				winnerName = "White";
			}

			if (whiteAmount == blackAmount) {
				winnerName = null;
			}

			System.out.println("GAME WON");
			gameWon = true;

			return true;

		}

		return false;

	}

	public boolean checkDraw() {
		int blackAmount = 0;
		int whiteAmount = 0;

		for (int x = 0; x < 8; x++) {

			for (int y = 0; y < 8; y++) {

				if (board.getSpotAt(x, y).getSpotColor() == Color.WHITE) {
					whiteAmount++;
				}

				else if (board.getSpotAt(x, y).getSpotColor() == Color.BLACK
						&& (board.getSpotAt(x, y).isEmpty() == false)) {

					blackAmount++;
				}

			}

		}
		return false;

	}


	protected static final int[] DX = { -1, 0, 1, -1, 1, -1, 0, 1 };
	protected static final int[] DY = { -1, -1, -1, 0, 0, 1, 1, 1 };

	public boolean isLegalMove(Spot marker) {

		if (!marker.isEmpty()) {
			return false;
		}
		for (int ii = 0; ii < DX.length; ii++) {
			boolean opposite = false, same = false;
			int x = marker.getSpotX(), y = marker.getSpotY();
			for (int dd = 0; dd < size; dd++) {
				x += DX[ii];
				y += DY[ii];

				if (!inBounds(x, y)) {
					break;
				}

				int color = getColor(x, y);
				int playColor;
				if (nextPlayer == Player.BLACK) {
					playColor = 0;
				} else {
					playColor = 1;
				}

				if (color == -1) {
					break;
				} else if (color == 1 - playColor) {
					opposite = true;
				} else if (color == playColor) {
					same = true;
					break;
				}
			}

			if (opposite && same) {
				return true;
			}
		}
		return false;
	}

//
	public boolean hasLegalMoves(int color, Spot marker) {

		Color possesor = marker.getSpotColor();
		Point Marker = null;
		for (int yy = 0; yy < size; yy++) {
			for (int xx = 0; xx < size; xx++) {
				if (getColor(xx, yy) != -1) {
					continue;
				}
				Marker.x = xx;
				Marker.y = yy;
				if (isLegalMove(board.getSpotAt(Marker.x, Marker.y))) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean hasLegalMoves() {

		for (Spot s : board) {

			if (isLegalMove(s)) {
				return true;
			}
		}

		return false;
	}

	public void flipPieces(Spot chosen) {
		ArrayList<Spot> flipped = new ArrayList<Spot>();

		for (int ii = 0; ii < DX.length; ii++) {

			int x = chosen.getSpotX(), y = chosen.getSpotY();
			int next;
			int playCol;
			if (nextPlayer == Player.BLACK) {
				playCol = 0;
				next = 1;
			} else {
				playCol = 1;
				next = 0;
			}

			for (int dd = 0; dd < size; dd++) {
				x += DX[ii];
				y += DY[ii];

				if (!inBounds(x, y)) {
					break;
				}

				int color = getColor(x, y);
				if (color == -1) {
					break;

				} else if (color == next) {

					for (Spot marker : board) {
						if (marker.getSpotX() == x && marker.getSpotY() == y) {
							flipped.add(marker);
							break;
						}
					}

				} else if (color == playCol) {

					for (Spot marker : flipped) {
						Color temp;
						if (marker.getSpotColor() == Color.BLACK) {
							temp = Color.WHITE;
						} else {
							temp = Color.BLACK;
						}

						marker.setSpotColor(temp);
					}
					break;
				}
			}
			flipped.clear();
		}
	}

	protected final Color getColorC(int x, int y) {
		return board.getSpotAt(x, y).getSpotColor();
	}

	protected final int getColor(int x, int y) {
		if (board.getSpotAt(x, y).isEmpty() == true) {
			return -1;
		} else if (board.getSpotAt(x, y).getSpotColor() == Color.BLACK) {
			return 0;
		} else {
			return 1;
		}

	}

	protected final int getColor(Spot spot) {

		int x = spot.getSpotX();
		int y = spot.getSpotY();

		if (board.getSpotAt(x, y).getSpotColor() == Color.BLACK && board.getSpotAt(x, y).isEmpty() == true) {
			return -1;
		}
		if (board.getSpotAt(x, y).getSpotColor() == Color.BLACK && board.getSpotAt(x, y).isEmpty() == false) {
			return 0;
		}
		if (board.getSpotAt(x, y).getSpotColor() == Color.WHITE) {
			return 1;
		}

		else {
			return -2;
		}
	}

	protected final boolean inBounds(Spot spot) {

		int x = spot.getSpotX();
		int y = spot.getSpotY();

		return x >= 0 && y >= 0 && x < this.size && y < this.size;
	}

	protected final boolean inBounds(int x, int y) {

		return x >= 0 && y >= 0 && x < this.size && y < this.size;
	}


}