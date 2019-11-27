package a7;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OthelloWidget extends JPanel implements ActionListener, SpotListener {

	/* Enum to identify player. */

	private enum Player {
		BLACK, WHITE
	};

	private JSpotBoard _board; /* SpotBoard playing area. */
	private JLabel _message; /* Label for messages. */
	private boolean _game_won; /* Indicates if games was been won already. */
	private boolean _game_draw; /* Indicates if games was been drawn. */
	public HashSet<Point> placeablePositions;

	private Player _next_to_play; /* Identifies who has next turn. */

	public OthelloWidget() {

		/* Create SpotBoard and message label. */


		_board = new JSpotBoard(8, 8, new Color(0.5f, 0.5f, 0.5f));
		_size = 8;

		Spot initSpot1 = _board.getSpotAt(4, 4);
		Spot initSpot2 = _board.getSpotAt(4, 3);
		Spot initSpot3 = _board.getSpotAt(3, 4);
		Spot initSpot4 = _board.getSpotAt(3, 3);
		
		placeablePositions = new HashSet<Point>();
		
		_message = new JLabel();

		/* Set layout and place SpotBoard at center. */

		setLayout(new BorderLayout());
		add(_board, BorderLayout.CENTER);

		/* Create subpanel for message area and reset button. */

		JPanel reset_message_panel = new JPanel();
		reset_message_panel.setLayout(new BorderLayout());

		/* Reset button. Add ourselves as the action listener. */

		JButton reset_button = new JButton("Restart");
		reset_button.addActionListener(this);
		reset_message_panel.add(reset_button, BorderLayout.EAST);
		reset_message_panel.add(_message, BorderLayout.CENTER);

		/* Add subpanel in south area of layout. */

		add(reset_message_panel, BorderLayout.SOUTH);

		/*
		 * Add ourselves as a spot listener for all of the spots on the spot board.
		 */
		_board.addSpotListener(this);

		/* Reset game. */
		resetGame();
		spotClicked(initSpot3);

		spotClicked(initSpot4);
		spotClicked(initSpot2);
		spotClicked(initSpot1);

	}

	/*
	 * resetGame
	 * 
	 * Resets the game by clearing all the spots on the board, picking a new secret
	 * spot, resetting game status fields, and displaying start message.
	 * 
	 */

	private void resetGame() {
		/*
		 * Clear all spots on board. Uses the fact that SpotBoard implements
		 * Iterable<Spot> to do this in a for-each loop.
		 */

		Spot initSpot1 = _board.getSpotAt(4, 4);
		Spot initSpot2 = _board.getSpotAt(4, 3);
		Spot initSpot3 = _board.getSpotAt(3, 4);
		Spot initSpot4 = _board.getSpotAt(3, 3);
		
		for (Spot s : _board) {
			s.clearSpot();
			s.setSpotColor(Color.BLACK);
		}

		/* Reset game won and next to play fields */
		_game_won = false;
		_game_draw = false;
		_next_to_play = Player.BLACK;

		/* Display game start message. */

		spotClicked(initSpot3);

		spotClicked(initSpot4);
		spotClicked(initSpot2);
		spotClicked(initSpot1);
		
		_message.setText("Welcome to Othello. Black to play");
	}

	public void setStarter() {

		_board.getSpotAt(4, 4).setSpotColor(Color.BLACK);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/* Handles reset game button. Simply reset the game. */
		resetGame();
	}

	/*
	 * Implementation of SpotListener below. Implements game logic as responses to
	 * enter/exit/click on spots.
	 */

	@Override
	public void spotClicked(Spot s) {

		/* If game already won, do nothing. */
		if (_game_won) {
			return;
		}

		/*
		 * Set up player and next player name strings, and player color as local
		 * variables to be used later.
		 */

		String player_name = null;
		String next_player_name = null;
		Color player_color = null;

		if (_next_to_play == Player.BLACK) {
			player_color = Color.BLACK;
			player_name = "Black";
			next_player_name = "White";
			_next_to_play = Player.WHITE;
		} else {
			player_color = Color.WHITE;
			player_name = "White";
			next_player_name = "Black";
			_next_to_play = Player.BLACK;
		}

		/* Set color of spot clicked and toggle. */
		if (s.isEmpty()) {
			s.setSpotColor(player_color);
			s.toggleSpot();

		} else {

			if (_next_to_play == Player.BLACK) {
				player_color = Color.WHITE;
				player_name = "White";
				next_player_name = "White";
				_next_to_play = Player.WHITE;

			} else {

				player_color = Color.BLACK;
				player_name = "Black";
				next_player_name = "Black";
				_next_to_play = Player.BLACK;

			}
		}
//		System.out.println("Checking Valid Moves");
//		Spot spot = _board.getSpotAt(3, 2);
//		System.out.println(isLegalMove(spot));
		for (Spot Spotter : _board) {
//			System.out.println("Testing");
			if (isLegalMove(Spotter)) {
				System.out.println("At Position: " + Spotter.getCoordString());
				System.out.println("Possible Move Checker Output : " + isLegalMove(Spotter));
			}
		}
		System.out.println("Win Checker Output: " + checkWin());

		System.out.println("Win Checker Output: " + checkWin());
		Color nextCol;
		if (player_color == Color.BLACK) {
			nextCol = Color.WHITE;
		} else {
			nextCol = Color.BLACK;
		}
		
		findPlaceableLocations(player_color, nextCol);
	
		/*
		 * Check if spot clicked is secret spot. If so, mark game as won and update
		 * background of spot to show that it was the secret spot.
		 */

		/*
		 * Update the message depending on what happened. If spot is empty, then we must
		 * have just cleared the spot. Update message accordingly. If spot is not empty
		 * and the game is won, we must have just won. Calculate score and display as
		 * part of game won message. If spot is not empty and the game is not won,
		 * update message to report spot coordinates and indicate whose turn is next.
		 */

		if (s.isEmpty()) {
			_message.setText(next_player_name + " to play.");
		} else {
			if (_game_won) {

				_message.setText(player_name + " wins! ");
			} else {

				if (checkDraw() && (_game_won == false)) {

					_message.setText(" Draw game.");

				} else {

					_message.setText(next_player_name + " to play.");
				}
			}
		}
	}

	@Override
	public void spotEntered(Spot s) {
		/* Highlight spot if game still going on. */

		if (_game_won) {
			return;
		}
		s.highlightSpot();
	}

	@Override
	public void spotExited(Spot s) {
		/* Unhighlight spot. */

		s.unhighlightSpot();
	}

//NEEDS TO BE EDITED
	public boolean checkWin() {

		if (_game_won) {
			return true;
		}

		int countBlacks = 0;
		int countWhites = 0;

		for (int x = 0; x < 3; x++) {

			countBlacks = 0;
			countWhites = 0;

			for (int y = 0; y < 3; y++) {

//				System.out.println("Spot Colour at Position: " + _board.getSpotAt(x, y).getSpotX()
//					+ " Y: " + _board.getSpotAt(x, y).getSpotY() + "  is: "
//				+ _board.getSpotAt(x, y).getSpotColor());

				if (_board.getSpotAt(x, y).getSpotColor() == Color.WHITE) {
					System.out.println("Grid Coordinates = X: " + _board.getSpotAt(x, y).getSpotX() + " Y: "
							+ _board.getSpotAt(x, y).getSpotY());
					countWhites++;
				}

				else if (_board.getSpotAt(x, y).getSpotColor() == Color.BLACK
						&& (_board.getSpotAt(x, y).isEmpty() == false)) {
					System.out.println("Grid Coordinates = X: " + _board.getSpotAt(x, y).getSpotX() + " Y: "
							+ _board.getSpotAt(x, y).getSpotY());
					countBlacks++;
				}

			}

			System.out.println("Blacks " + countBlacks);
			System.out.println("Whites " + countWhites);

			if (countWhites == 3 || countBlacks == 3) {
				System.out.println("Straight Line, Whites = " + countWhites + " Blacks: " + countBlacks);
				_game_won = true;

				return true;

				// break;

			}

		}

		countBlacks = 0;
		countWhites = 0;

		for (int y = 0; y < 3; y++) {

			countBlacks = 0;
			countWhites = 0;

			for (int x = 0; x < 3; x++) {

				if (_board.getSpotAt(x, y).getSpotColor() == Color.WHITE
						&& (_board.getSpotAt(x, y).isEmpty() == false)) {
					System.out.println("Grid Coordinates = X: " + _board.getSpotAt(x, y).getSpotX() + " Y: "
							+ _board.getSpotAt(x, y).getSpotY());
					countWhites++;
				}

				else if (_board.getSpotAt(x, y).getSpotColor() == Color.BLACK
						&& (_board.getSpotAt(x, y).isEmpty() == false)) {
					System.out.println("Grid Coordinates = X: " + _board.getSpotAt(x, y).getSpotX() + " Y: "
							+ _board.getSpotAt(x, y).getSpotY());
					countBlacks++;
				}

			}

			System.out.println("Blacks " + countBlacks);
			System.out.println("Whites " + countWhites);

			if (countWhites == 3 || countBlacks == 3) {
				System.out.println("Straight Line, Whites = " + countWhites + " Blacks: " + countBlacks);
				_game_won = true;

				return true;

				// break;

			}

		}

		countBlacks = 0;
		countWhites = 0;

		// check diagonal right

		for (int i = 0, j = 2; i < 3; i++, j--) {

			System.out.println("Spot Colour at Position: " + _board.getSpotAt(i, j).getSpotX() + " Y: "
					+ _board.getSpotAt(i, j).getSpotY() + "  is: " + _board.getSpotAt(i, j).getSpotColor());

			if (_board.getSpotAt(i, j).getSpotColor() == Color.WHITE && (_board.getSpotAt(i, j).isEmpty() == false)) {
				System.out.println("Grid Coordinates = X: " + _board.getSpotAt(i, j).getSpotX() + " Y: "
						+ _board.getSpotAt(i, j).getSpotY());
				countWhites++;
			}

			else if (_board.getSpotAt(i, j).getSpotColor() == Color.BLACK
					&& (_board.getSpotAt(i, j).isEmpty() == false)) {
				countBlacks++;
			}
		}

		if (countWhites == 3 || countBlacks == 3) {

			System.out.println("Diagonal Right, Whites = " + countWhites + " Blacks: " + countBlacks);
			_game_won = true;
			return true;
		}

		countBlacks = 0;
		countWhites = 0;

		// check diagonal left
		for (int i = 3 - 1, j = 3 - 1; i >= 0; i--, j--) {
			if (_board.getSpotAt(i, j).getSpotColor() == Color.WHITE && (_board.getSpotAt(i, j).isEmpty() == false)) {
				countWhites++;
			} else if (_board.getSpotAt(i, j).getSpotColor() == Color.BLACK
					&& (_board.getSpotAt(i, j).isEmpty() == false)) {
				countBlacks++;
			}
		}

		if (countBlacks == 3 || countWhites == 3) {
			System.out.println("Diagonal Left, Whites = " + countWhites + " Blacks: " + countBlacks);
			_game_won = true;
			return true;
		}

		return false;

	}

	public boolean checkDraw() {
		int countBlacks = 0;
		int countWhites = 0;

		for (int x = 0; x < 3; x++) {

			for (int y = 0; y < 3; y++) {

				if (_board.getSpotAt(x, y).getSpotColor() == Color.WHITE) {
					countWhites++;
				}

				else if (_board.getSpotAt(x, y).getSpotColor() == Color.BLACK
						&& (_board.getSpotAt(x, y).isEmpty() == false)) {

					countBlacks++;
				}

			}

			System.out.println("Blacks " + countBlacks);
			System.out.println("Whites " + countWhites);

			if (countWhites == 5 && countBlacks == 4) {

				_game_draw = true;
				_game_won = false;

				return true;

			}

		}
		return false;

	}
	
	//PLAYER = WHITE
	//OPPONENT = BLACK

	private void findPlaceableLocations(Color player, Color opponent) {
		
		placeablePositions.clear();
		
		for (int i = 0; i < 8; ++i) {
			for (int j = 0; j < 8; ++j) {
				if (_board.getSpotAt(i, j).getSpotColor() == opponent && _board.getSpotAt(i, j).isEmpty() == false) {
					int I = i, J = j;
					if (i - 1 >= 0 && j - 1 >= 0 && _board.getSpotAt(i - 1, j - 1).isEmpty() == true) {
						i = i + 1;
						j = j + 1;
						while (i < 7 && j < 7 && _board.getSpotAt(i, j).getSpotColor() == opponent) {
							i++;
							j++;
						}
						if (i <= 7 && j <= 7 && _board.getSpotAt(i, j).getSpotColor() == opponent) {
							System.out.println("I " + I);
							System.out.println("J " + J);
							placeablePositions.add(new Point(I - 1, J - 1));
						}
					}
					i = I;
					j = J;
					if (i - 1 >= 0 && _board.getSpotAt(i - 1, j).isEmpty() == true) {
						i = i + 1;
						while (i < 7 && _board.getSpotAt(i, j).getSpotColor() == opponent && _board.getSpotAt(i, j).isEmpty() == false)
							i++;
						if (i <= 7 && _board.getSpotAt(i, j).getSpotColor() == player)
							_board.getSpotAt(I-1, J).highlightSpot();
							placeablePositions.add(new Point(I - 1, J));
					}
					i = I;
					if (i - 1 >= 0 && j + 1 <= 7 && _board.getSpotAt(i - 1, j + 1).isEmpty() == true) {
						i = i + 1;
						j = j - 1;
						while (i < 7 && j > 0 && _board.getSpotAt(i, j).getSpotColor() == opponent && _board.getSpotAt(i, j).isEmpty() == false) {
							i++;
							j--;
						}
						if (i <= 7 && j >= 0 && _board.getSpotAt(i, j).getSpotColor() == player)
							_board.getSpotAt(I-1, J+1).highlightSpot();
							placeablePositions.add(new Point(I - 1, J + 1));
					}
					
					i = I;
					j = J;
					
					if (j - 1 >= 0 && _board.getSpotAt(i, j - 1).isEmpty() == true) {
						j = j + 1;
						while (j < 7 && _board.getSpotAt(i, j).getSpotColor() == opponent && _board.getSpotAt(i, j).isEmpty() == false)
							j++;
						if (j <= 7 && _board.getSpotAt(i, j).getSpotColor() == player)
							_board.getSpotAt(I, J-1).highlightSpot();
							placeablePositions.add(new Point(I, J - 1));
					}
					j = J;
					if (j + 1 <= 7 && _board.getSpotAt(i, j + 1).isEmpty() == true) {
						j = j - 1;
						while (j > 0 && _board.getSpotAt(i, j).getSpotColor() == opponent && _board.getSpotAt(i, j).isEmpty() == false)
							j--;
						if (j >= 0 && _board.getSpotAt(i, j).getSpotColor() == player)
							_board.getSpotAt(I, J+1).highlightSpot();
							placeablePositions.add(new Point(I, J + 1));
					}
					j = J;
					if (i + 1 <= 7 && j - 1 >= 0 && _board.getSpotAt(i + 1, j - 1).isEmpty() == true) {
						i = i - 1;
						j = j + 1;
						while (i > 0 && j < 7 && _board.getSpotAt(i, j).getSpotColor() == opponent && _board.getSpotAt(i, j).isEmpty() == false) {
							i--;
							j++;
						}
						if (i >= 0 && j <= 7 && _board.getSpotAt(i, j).getSpotColor() == player)
							_board.getSpotAt(I, J-1).highlightSpot();
							placeablePositions.add(new Point(I + 1, J - 1));
					}
					i = I;
					j = J;
					if (i + 1 <= 7 && _board.getSpotAt(i + 1, j).isEmpty() == true) {
						i = i - 1;
						while (i > 0 && _board.getSpotAt(i, j).getSpotColor() == opponent && _board.getSpotAt(i, j).isEmpty() == false)
							i--;
						if (i >= 0 && _board.getSpotAt(i, j).getSpotColor() == player)
							placeablePositions.add(new Point(I + 1, J));
					}
					i = I;
					if (i + 1 <= 7 && j + 1 <= 7 && _board.getSpotAt(i + 1, j + 1).isEmpty() == true) {
						i = i - 1;
						j = j - 1;
						while (i > 0 && j > 0 && _board.getSpotAt(i, j).getSpotColor() == opponent && _board.getSpotAt(i, j).isEmpty() == false) {
							i--;
							j--;
						}
						if (i >= 0 && j >= 0 && _board.getSpotAt(i, j).getSpotColor() == player)
							placeablePositions.add(new Point(I + 1, J + 1));
					}
					
					i = I;
					j = J;
					
				}
			}
		}

		
		
		
		System.out.println(placeablePositions);

	}
	
//    public void setState ()
//    {
//        Arrays.fill(_state, -1);
//        for (ReversiObject.Piece piece : pieces) {
//            _state[piece.y * _size + piece.x] = piece.owner;
//        }
//    }

    /**
     * Returns the index into the {@link ReversiObject#players} array of the
     * player to whom control should transition.
     */
//    public int getNextTurnHolderIndex (int curTurnIdx)
//    {
//        // if the next player can move, they're up
//        if (hasLegalMoves(1-curTurnIdx)) {
//            return 1-curTurnIdx;
//        }
//
//        // otherwise see if the current player can still move
//        if (hasLegalMoves(curTurnIdx)) {
//            return curTurnIdx;
//        }
//
//        // otherwise the game is over
//        return -1;
//    }
	
	protected static final int[] DX = { -1, 0, 1, -1, 1, -1, 0, 1 };
    protected static final int[] DY = { -1, -1, -1, 0, 0, 1, 1, 1 };

    /**
     * Returns true if the supplied piece represents a legal move for the
     * owning player.
     */
    public boolean isLegalMove (Spot piece)
    {
        // disallow moves on out of bounds and already occupied spots
        if (!piece.isEmpty()) {
            return false;
        }
        System.out.println();
        // determine whether this piece "captures" pieces of the opposite color
        for (int ii = 0; ii < DX.length; ii++) {
            // look in this direction for captured pieces
            boolean sawOther = false, sawSelf = false;
            int x = piece.getSpotX(), y = piece.getSpotY();
            
            System.out.println("x = " + x + ", y = " + y);
            for (int dd = 0; dd < _size; dd++) {
                x += DX[ii];
                y += DY[ii];
                System.out.println("x = " + x + ", y = " + y);

                // stop when we end up off the board
                if (!inBounds(x, y)) {
                    break;
                }

                
                int color = getColor(x, y);
                int playCol; //Will hold Player Color
                if (_next_to_play == Player.BLACK) {
                	playCol = 0;
                } else {
                	playCol = 1;
                }
                
                System.out.println("Color: " + color);
                System.out.println("My Color: " + playCol);
                if (color == -1) {
                    break;
                } else if (color == 1 - playCol) {
                    sawOther = true;
                } else if (color == playCol) {
                    sawSelf = true;
                    break;
                }
            }

            // if we saw at least one other piece and one of our own, we have a
            // legal move
            if (sawOther && sawSelf) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the player with the specified color has legal moves.
     */
    public boolean hasLegalMoves (int color, Spot piece)
    {
        // search every board position for a legal move

        Color owner = piece.getSpotColor();
        for (int yy = 0; yy < _size; yy++) {
            for (int xx = 0; xx < _size; xx++) {
                if (getColor(xx, yy) != -1) {
                    continue;
                }
//                piece.x = xx;
//                piece.y = yy;
                if (isLegalMove(piece)) {
                    return true;
                }
            }
        }
        return false;
    }

//    public boolean hasLegalMoves (int color)
//    {
//        // search every board position for a legal move
//
//    	for (int yy = 0; yy < _size; yy++) {
//            for (int xx = 0; xx < _size; xx++) {
//                if (getColor(xx, yy) != -1) {
//                    continue;
//                }
//                
////                piece.x = xx;
////                piece.y = yy;
//                
//                if (isLegalMove(piece)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
    /**
     * Determines which pieces should be flipped based on the placement of the
     * specified piece onto the board. The pieces in question are changed to
     * the appropriate color and updated in the game object.
     */
//    public void flipPieces (ReversiObject.Piece placed, ReversiObject gameobj)
//    {
//        ArrayList<ReversiObject.Piece> toflip = new ArrayList<ReversiObject.Piece>();
//
//        // determine where this piece "captures" pieces of the opposite color
//        for (int ii = 0; ii < DX.length; ii++) {
//            // look in this direction for captured pieces
//            int x = placed.x, y = placed.y;
//            for (int dd = 0; dd < _size; dd++) {
//                x += DX[ii];
//                y += DY[ii];
//
//                // stop when we end up off the board
//                if (!inBounds(x, y)) {
//                    break;
//                }
//
//                int color = getColor(x, y);
//                if (color == -1) {
//                    break;
//
//                } else if (color == 1-placed.owner) {
//                    // add the piece at this coordinates to our to flip list
//                    for (ReversiObject.Piece piece : gameobj.pieces) {
//                        if (piece.x == x && piece.y == y) {
//                            toflip.add(piece);
//                            break;
//                        }
//                    }
//
//                } else if (color == placed.owner) {
//                    // flip all the toflip pieces because we found our pair
//                    for (ReversiObject.Piece piece : toflip) {
//                        piece.owner = 1-piece.owner;
//                        gameobj.updatePieces(piece);
//                    }
//                    break;
//                }
//            }
//            toflip.clear();
//        }
//    }
    
    protected final Color getColorC (int x, int y)
    {
        return _board.getSpotAt(x, y).getSpotColor();
    }
    
    protected final int getColor (int x, int y)
    {
    	if (_board.getSpotAt(x, y).isEmpty() == true) {
    		return -1;
    	} else if (_board.getSpotAt(x, y).getSpotColor() == Color.BLACK) {
    		return 0;
    	} else {
    		return 1;
    	}
    	
    }
    
    protected final int getColor (Spot spot)
    {
    	
    	int x = spot.getSpotX();
    	int y = spot.getSpotY();
    			
    	
    	if (_board.getSpotAt(x, y).getSpotColor() == Color.BLACK && _board.getSpotAt(x, y).isEmpty() == true) {
    		return -1;
    	}
    	if (_board.getSpotAt(x, y).getSpotColor() == Color.BLACK && _board.getSpotAt(x, y).isEmpty() == false) {
    		return 0;
    	}
    	if (_board.getSpotAt(x, y).getSpotColor() == Color.WHITE) {
    		return 1;
    	}
    	
    	else {
    		return -2;
    	}
    }
    
    
    protected final boolean inBounds (Spot spot)
    {
    	
    	int x = spot.getSpotX();
    	int y = spot.getSpotY();
    	
        return x >= 0 && y >= 0 && x < this._size && y < this._size;
    }
    
    
    protected final boolean inBounds (int x, int y)
    {
    	
        return x >= 0 && y >= 0 && x < this._size && y < this._size;
    }
    
    
    protected int _size;
    protected int[] _state;

}