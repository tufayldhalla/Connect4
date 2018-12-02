import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

/**
 * The "ConnectFourBoard" class. Handles the board play for a simple game of
 * Connect 4
 * 
 * @author ICS3U
 * @version November 2011
 */
public class ConnectFourBoard extends JPanel implements MouseListener,
		KeyListener
{
	// Program constants (declared at the top, these can be used by any method)
	private final int MARIO = 1;
	private final int LUIGI = -1;
	private final int EMPTY = 0;

	private final int SQUARE_SIZE = 64;
	private final int DROPPING_SPEED = 2;
	private final int NO_OF_ROWS = 6;
	private final int NO_OF_COLUMNS = 7;
	private final boolean ANIMATION_ON = true;

	// Artwork by iconshock.com www.iconshock.com
	private final String IMAGE_FILENAME_PLAYER2 = "mario.png";
	private final String IMAGE_FILENAME_PLAYER1 = "luigi.png";
	public final Dimension BOARD_SIZE = new Dimension(NO_OF_COLUMNS
			* SQUARE_SIZE + 1, (NO_OF_ROWS + 1) * SQUARE_SIZE + 1);

	// Program variables (declared at the top, these can be
	// used or changed by any method)
	private int[][] board;
	private boolean droppingPiece;
	private int xFallingPiece, yFallingPiece;
	private int currentPlayer;
	private int currentColumn;
	private Image firstImage, secondImage;
	private boolean gameOver;
	private int noOfMoves;

	/**
	 * Constructs a new ConnectFourBoard object
	 */
	{
		// Sets up the board area, loads in piece images and starts a new game
		setPreferredSize(BOARD_SIZE);

		setBackground(new Color(200, 200, 200));
		// Add mouse listeners and Key Listeners to the game board
		addMouseListener(this);
		setFocusable(true);
		addKeyListener(this);
		requestFocusInWindow();

		// Load up the images for the pieces
		firstImage = new ImageIcon(IMAGE_FILENAME_PLAYER1).getImage();
		secondImage = new ImageIcon(IMAGE_FILENAME_PLAYER2).getImage();

		// Sets up the board array and starts a new game
		board = new int[NO_OF_ROWS + 2][NO_OF_COLUMNS + 2];
		newGame();
	}

	/**
	 * Starts a new game
	 */
	public void newGame()
	{
		currentPlayer = LUIGI;
		clearBoard();
		gameOver = false;
		currentColumn = NO_OF_COLUMNS / 2 + 1;
		droppingPiece = false;
		noOfMoves = 0;
		repaint();
	}

	/**
	 * Clears the board of all pieces
	 */
	// Clears the board when the user wants to
	private void clearBoard()
	{
		// For loop keep tracks of the rows
		for (int rows = 0; rows < (NO_OF_ROWS + 1); rows++)
		{
			// For loop keeps track of the columns
			for (int column = 0; column < NO_OF_COLUMNS + 1; column++)
			{
				// Changes all the squares on the board to empty
				board[rows][column] = EMPTY;
			}
		}

	}

	/**
	 * Stacks player pieces on top of each other
	 * 
	 * @param column of the board
	 * @return if there is something on the board
	 */
	private int findRow(int column)
	{
		// Rows
		int value = 6;
		// Keeps track of the rows on the board
		for (int row = 0; row < NO_OF_ROWS + 1; row++)
		{
			// If there is something on the spot
			if (board[row][column] != 0)
			{
				// Decrease the rows by one so you place the next piece on top
				// of it
				value = value - 1;
			}
		}
		return value;
	}

	/**
	 * Checks to see if there is a winner
	 * 
	 * @param lastRow the last row the player played in
	 * @param lastColumn the last column the player played in
	 * @return
	 */
	private int checkForWinner(int lastRow, int lastColumn)
	{
		// Sets variables for the player's position
		int player = board[lastRow][lastColumn];
		// Sets variable to keep track of four matching pieces
		int inRow = 1;
		int row = lastRow + 1;
		int column = lastColumn - 1;

		// Checks to see if there is a match from Diagonal bottom left to top right /
		//Keeps track of everything under the players piece
		while (board[row][column] == player)
		{
			inRow++;
			row++;
			column--;
		}
		//Keeps track of everything above the players piece
		row = lastRow - 1;
		column = lastColumn + 1;
		while (board[row][column] == player)
		{
			inRow++;
			row--;
			column++;
		}

		if (inRow >= 4)
			return currentPlayer;

		// Checks to see if there is a match from Diagonal bottom right to top left \
		inRow = 1;
		row = lastRow - 1;
		column = lastColumn - 1;
		while (board[row][column] == player)
		{
			inRow++;
			row--;
			column--;
		}
		row = lastRow + 1;
		column = lastColumn + 1;
		while (board[row][column] == player)
		{
			inRow++;
			row++;
			column++;
		}

		if (inRow >= 4)
			return currentPlayer;

		// Checks to see if there is a match Vertically |
		inRow = 1;
		row = lastRow + 1;
		while (board[row][lastColumn] == player)
		{
			inRow++;
			row++;
		}
		if (inRow >= 4)
			return currentPlayer;

		// Checks to see if there is a match Horizontally -
		inRow = 1;
		column = lastColumn - 1;
		while (board[lastRow][column] == player)
		{
			inRow++;
			column--;
		}

		column = lastColumn + 1;
		while (board[lastRow][column] == player)
		{
			inRow++;
			column++;
		}

		if (inRow >= 4)
			return currentPlayer;

		return EMPTY; // Dummy value to avoid errors
	}

	/**
	 * Makes a move on the board (if possible)
	 * 
	 * @param selectedColumn the selected column to move in
	 */
	private void makeMove(int selectedColumn)
	{
		if (gameOver)
		{
			JOptionPane.showMessageDialog(this,
					"Please Select Game...New to start a new game",
					"Game Over", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int row = findRow(selectedColumn);
		if (row <= 0)
		{
			JOptionPane.showMessageDialog(this, "Please Select another Column",
					"Column is Full", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (ANIMATION_ON)
			animatePiece(currentPlayer, selectedColumn, row);
		board[row][selectedColumn] = currentPlayer;
		noOfMoves++;

		int winner = checkForWinner(row, selectedColumn);

		if (winner == MARIO)
		{
			gameOver = true;
			repaint(0);
			JOptionPane.showMessageDialog(this, "Mario Wins!!!",
					"GAME OVER", JOptionPane.INFORMATION_MESSAGE);
		}
		else if (winner == LUIGI)
		{
			gameOver = true;
			repaint(0);
			JOptionPane.showMessageDialog(this, "Luigi Wins!!!",
					"GAME OVER", JOptionPane.INFORMATION_MESSAGE);
		}
		else if (noOfMoves == 42)
		{
			gameOver = true;
			repaint(0);
			JOptionPane.showMessageDialog(this, "Tie game!!!", "GAME OVER",
					JOptionPane.INFORMATION_MESSAGE);
		}
		else
			// Switch to the other player
			currentPlayer *= -1;

		// Start piece in centre
		currentColumn = NO_OF_COLUMNS / 2 + 1;

		repaint();
	}

	/**
	 * Animates a falling piece
	 * 
	 * @param player the player whoose piece is falling
	 * @param column the column the piece is falling in
	 * @param finalRow the final row the piece will fall to
	 */
	private void animatePiece(int player, int column, int finalRow)
	{
		droppingPiece = true;
		for (double row = 0; row < finalRow; row += 0.20)
		{
			// Find the x and y positions for the falling piece
			xFallingPiece = (column - 1) * SQUARE_SIZE;
			yFallingPiece = (int) (row * SQUARE_SIZE);

			// Update the drawing area
			paintImmediately(0, 0, getWidth(), getHeight());

			delay(DROPPING_SPEED);

		}
		droppingPiece = false;
	}

	/**
	 * Delays the given number of milliseconds
	 * 
	 * @param milliSec The number of milliseconds to delay
	 */
	private void delay(int milliSec)
	{
		try
		{
			Thread.sleep(milliSec);
		}
		catch (InterruptedException e)
		{
		}
	}

	/**
	 * Repaint the board's drawing panel
	 * 
	 * @param g The Graphics context
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		// Redraw the board with current pieces
		for (int row = 1; row <= NO_OF_ROWS; row++)
			for (int column = 1; column <= NO_OF_COLUMNS; column++)
			{
				// Find the x and y positions for each row and column
				int xPos = (column - 1) * SQUARE_SIZE;
				int yPos = row * SQUARE_SIZE;

				// Draw the squares
				g.setColor(Color.BLUE);
				g.drawRect(xPos, yPos, SQUARE_SIZE, SQUARE_SIZE);

				// Draw each piece, depending on the value in board
				if (board[row][column] == MARIO)
					g.drawImage(firstImage, xPos + 4, yPos + 4, this);
				else if (board[row][column] == LUIGI)
					g.drawImage(secondImage, xPos + 4, yPos + 4, this);
			}

		// Draw moving piece if animating
		if (droppingPiece)
		{
			if (currentPlayer == MARIO)
				g.drawImage(firstImage, xFallingPiece + 4, yFallingPiece + 4,
						this);
			else
				g.drawImage(secondImage, xFallingPiece + 4, yFallingPiece + 4,
						this);
		}
		else
		// Draw next player
		{
			if (!gameOver)
				if (currentPlayer == MARIO)
					g.drawImage(firstImage, (currentColumn - 1) * SQUARE_SIZE,
							0, this);
				else
					g.drawImage(secondImage, (currentColumn - 1) * SQUARE_SIZE,
							0, this);
		}
	} // paint component method

	// Keyboard events you can listen for since this JPanel is a KeyListener

	/**
	 * Responds to a keyPressed event
	 * 
	 * @param event information about the key pressed event
	 */
	public void keyPressed(KeyEvent event)
	{
		// Change the currentRow and currentColumn of the player
		// based on the key pressed
		if (event.getKeyCode() == KeyEvent.VK_LEFT || event.getKeyCode() == KeyEvent.VK_A)
		{
			if (currentColumn > 1)
				currentColumn--;
		}
		else if (event.getKeyCode() == KeyEvent.VK_RIGHT || event.getKeyCode() == KeyEvent.VK_D)
		{
			if (currentColumn < NO_OF_COLUMNS)
				currentColumn++;
		}
		// These keys indicate player's move
		else if (event.getKeyCode() == KeyEvent.VK_DOWN
				|| event.getKeyCode() == KeyEvent.VK_ENTER
				|| event.getKeyCode() == KeyEvent.VK_S)
		{
			makeMove(currentColumn);
		}

		// Repaint the screen after the change
		repaint();
	}

	// Extra methods needed since this game board is a KeyListener
	public void keyReleased(KeyEvent event)
	{
	}

	public void keyTyped(KeyEvent event)
	{
	}

	// Mouse events you can listen for since this JPanel is a MouseListener

	/**
	 * Responds to a mousePressed event
	 * 
	 * @parameventinformation about the mouse pressed event
	 */
	public void mousePressed(MouseEvent event)
	{
		// Calculate which column was clicked, then make
		// the player's move for that column
		int selectedColumn = event.getX() / SQUARE_SIZE + 1;
		makeMove(selectedColumn);
	}

	// Extra methods needed since this game board is a MouseListener

	public void mouseReleased(MouseEvent event)
	{
	}

	public void mouseClicked(MouseEvent event)
	{
	}

	public void mouseEntered(MouseEvent event)
	{
	}

	public void mouseExited(MouseEvent event)
	{
	}
}
