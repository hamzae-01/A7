package a7;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConnectFourWidget extends JPanel implements ActionListener, SpotListener {

	
	private enum Player {BLACK, RED};
	
	private JSpotBoard board;		
	private JLabel message;		
	private boolean gameWon;		
	private boolean drawGame;		
	
	
	private String playerName;

	private Player nextPlayer;	
	private Color winner;	

	
	public ConnectFourWidget() {
		
		
		
		board = new JSpotBoard(7,6, new Color(0.8f, 0.8f, 0.8f), new Color(0.5f, 0.5f, 0.5f));
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

		for (Spot x : board) {
			x.unhighlightSpot();
		}
	
		
		for (Spot s : board) {
			s.clearSpot();
			s.setSpotColor(Color.BLACK);
			
		}
			
		
		gameWon = false;
		drawGame = false;
		nextPlayer = Player.RED;		
		
	
		
		message.setText("Welcome to ConnectFour. Red to play");
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
			nextPlayerName = "Red";
			nextPlayer = Player.RED;
		} else {
			playerColor = Color.RED;
			playerName = "Red";
			nextPlayerName = "Black";
			nextPlayer = Player.BLACK;			
		}
				
		
		if (s.isEmpty() || s.isEmpty()==false) {
			int greatestValue = -1;
			int originalGV = greatestValue;
			for (Spot x : board) {
				if (x.getSpotX() == s.getSpotX()) {
					if (x.getSpotY() > greatestValue) {
						originalGV = greatestValue;
						greatestValue = x.getSpotY();
						if (board.getSpotAt(x.getSpotX(), greatestValue).isEmpty() == true) {
							greatestValue = x.getSpotY();
						} else {
							greatestValue = originalGV;
						}
					}
					
				}
			}
			
			if (greatestValue == -1) {
				return;
			}
			
			System.out.println("Highest Value is: " + greatestValue);
			Spot temp = board.getSpotAt(s.getSpotX(), greatestValue);
			
			temp.setSpotColor(playerColor);
			temp.toggleSpot();
			
		} else {
			
			if (nextPlayer == Player.BLACK) {
				playerColor = Color.RED;
				playerName = "Red";
				nextPlayerName = "Red";
				nextPlayer = Player.RED;
				
			} else {
				
				playerColor = Color.BLACK;
				playerName = "Black";
				nextPlayerName = "Black";
				nextPlayer = Player.BLACK;			
				
			}
		}
		
		System.out.println("Win Checker Output: " + checkWin());
		System.out.println("Winner is : " + winner);


		
		if (s.isEmpty()) {
			
			message.setText(nextPlayerName + " to play.");
		} else {
			if (gameWon)  {
				if (winner == Color.BLACK) {
					playerName = "Black";
				}else {
						playerName = "Red";
					}
				
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

		int cValue = s.getSpotX();
		
		for (Spot x : board) {
			if (x.getSpotX() == cValue) {
				x.highlightSpot();
			}
		}

	}
	
	public void highlightFinal(Spot s1, Spot s2, Spot s3, Spot s4) {
		
		if (gameWon) {
			return;
		}

		for (Spot x : board) {
				x.unhighlightSpot();
			}
		
		
		s1.highlightSpot();
		s2.highlightSpot();
		s3.highlightSpot();
		s4.highlightSpot();

		
	}

	@Override
	public void spotExited(Spot s) {
		
		if (gameWon) {
			return;
		}

		
		
		int cValue = s.getSpotX();

		
		for (Spot x : board) {
			if (x.getSpotX() == cValue) {
				x.unhighlightSpot();
			}
		}
		
		

	}

		
		public boolean checkWin() {
			
			if (gameWon) {
				return true;
			}
			

		    final int HEIGHT = 7;
		    final int WIDTH = 6;


		    for (int x = 0; x < 7; x++) { 
		        for (int y = 0; y < 6; y++) { 
		            System.out.println("Empty Spots: r = " + x + "c = " + y);
		            Spot player = board.getSpotAt(x, y);
		            

		    
		            if (player.isEmpty()) {
		                continue; 
		            }

		            System.out.println("r = " + x + "c = " + y);
		            
		            if (x + 3 < WIDTH) {
				            if (
		                player.getSpotColor() == board.getSpotAt(x+1, y).getSpotColor() && // look RIGHT
		                player.getSpotColor() ==  board.getSpotAt(x+2, y).getSpotColor() &&
		                player.getSpotColor() ==  board.getSpotAt(x+3, y).getSpotColor()) {
		            	if (board.getSpotAt(x+3, y).isEmpty() == false && board.getSpotAt(x+2, y).isEmpty() == false) {
		            	winner = player.getSpotColor();
		            	player.highlightSpot();
		            	board.getSpotAt(x+1, y).highlightSpot();
		            	board.getSpotAt(x+2, y).highlightSpot();
		            	board.getSpotAt(x+3, y).highlightSpot();
		            	
		            	highlightFinal(player, board.getSpotAt(x+1, y), 
		            			board.getSpotAt(x+2, y), board.getSpotAt(x+3, y));
		            	
		            	gameWon = true;
		                return true;
		            	}
		            	}
		            	
		                
		            }
		            
		            if (y - 3 >= 0) {
		            	System.out.println("CHECKING UP");
		            	System.out.println(player.getSpotColor());
		            	System.out.println(board.getSpotAt(x, y-1).getSpotColor());
		            	System.out.println(board.getSpotAt(x, y-2).getSpotColor());
		            	System.out.println(board.getSpotAt(x, y-3).getSpotColor());



		                if (player.getSpotColor() == board.getSpotAt(x, y-1).getSpotColor() && // look UP
		                    player.getSpotColor() == board.getSpotAt(x, y-2).getSpotColor() &&
		                    player.getSpotColor() == board.getSpotAt(x, y-3).getSpotColor()) {
		                	if (board.getSpotAt(x, y-3).isEmpty() == false) {
				            	System.out.println("CHECK UP PASSED");
				            	winner = player.getSpotColor();
				            	highlightFinal(player, board.getSpotAt(x, y-1), 
				            			board.getSpotAt(x, y-2), board.getSpotAt(x, y-3));
				            	gameWon = true;
				            	
				            	

				                return true;
		                	}
		                	}
		                
		                if (x + 3 < WIDTH &&
		                    player.getSpotColor() == board.getSpotAt(x+1, y-1).getSpotColor() && // look UP & RIGHT
		                    player.getSpotColor() == board.getSpotAt(x+2, y-2).getSpotColor() &&
		                    player.getSpotColor() ==  board.getSpotAt(x+3, y-3).getSpotColor()) {
		                	if (board.getSpotAt(x+3, y-3).isEmpty() == false) {
				            	winner = player.getSpotColor();
				            	highlightFinal(player, board.getSpotAt(x+1, y-1), 
				            			board.getSpotAt(x+2, y-2), board.getSpotAt(x+3, y-3));
				            	gameWon = true;
				                return true;
		                	}
		                	}
		                }
		                
		                if (x - 3 >= 0 && y - 3 >= 0) {
		                	if (
			                    player.getSpotColor() == board.getSpotAt(x-1, y-1).getSpotColor() && // look UP & LEFT
			                    player.getSpotColor() == board.getSpotAt(x-2, y-2).getSpotColor() &&
			                    player.getSpotColor() ==  board.getSpotAt(x-3, y-3).getSpotColor()) {
			                	if (board.getSpotAt(x-3, y-3).isEmpty() == false) {
					            	winner = player.getSpotColor();
					            	highlightFinal(player, board.getSpotAt(x-1, y-1), 
					            			board.getSpotAt(x-2, y-2), board.getSpotAt(x-3, y-3));
					            	gameWon = true;
					                return true;
			                	}
		                	}
		                }
		            
		        }
		    }
		

		    winner = null;
		    return false; 
		}





public boolean checkDraw() {
	
	int count = 0;
	
	for (int x = 0; x < 7; x++) {
		
		for (int y = 0; y < 6; y++) {
			
			if ((board.getSpotAt(x, y).getSpotColor() == Color.BLACK && board.getSpotAt(x, y).isEmpty() == false || board.getSpotAt(x, y).getSpotColor() == Color.RED )) {
				count++;
			}
			

			
		}
		
		System.out.println("Total " + count);

		if (count == 42) {

			drawGame = true;
			gameWon = false;

			return true;
			
			
		}
		
		
		
	
	}
	return false;
	

}

}