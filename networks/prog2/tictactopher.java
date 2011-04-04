/* Networks Program 2 - Server
 * Written by Joshua Wood
 * March 2011 */

import java.io.*;
import java.net.*;

class tictactopher {
	public static void main(String argv[]) throws Exception {
		char [] gameBoard;
		String welcomeString = "GREETINGS PROFESSOR CAIN.\n";
		String welcomeString2 = "SHALL WE PLAY A GAME?";
		String chooseMove = "PLEASE ENTER YOUR DESIRED POSITION\n";
		String clientResponse;

		ServerSocket welcomeSocket = new ServerSocket(42134);
		while(true){
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			outToClient.writeBytes("BOG\n"); // The client outer loop "eats" this
			outToClient.writeBytes(welcomeString);
			outToClient.writeBytes(welcomeString2);

			gameBoard = makeBoard();
			while (isWon(gameBoard) == 'N'){
				outToClient.writeBytes("\n"); // The client inner loop "eats" this
				printBoard(gameBoard, outToClient);
				outToClient.writeBytes(chooseMove);
				outToClient.writeBytes("EOF\n"); // The client inner loop "eats" this
				clientResponse = inFromClient.readLine();
				// We don't want to allow a bad move
				if (!checkMove(gameBoard, Integer.parseInt(clientResponse))){
					outToClient.writeBytes("\n");
					outToClient.writeBytes("PLEASE CHOOSE ANOTHER MOVE.\n");
				} else {
					// Player move
					makeMove(gameBoard, Integer.parseInt(clientResponse));
					// Computer move
					chooseMove(gameBoard, 'X');
				}
			}
			printBoard(gameBoard, outToClient);
			// The player is always O
			if (isWon(gameBoard) == 'O'){
				outToClient.writeBytes("WELL DONE PROFESSOR.\n");
				outToClient.writeBytes("PERHAPS WE SHALL PLAY AGAIN SOMETIME.\n");
			// Therefore, the computer is always X
			} else if (isWon(gameBoard) == 'X'){
				outToClient.writeBytes("PEOPLE SOMETIMES MAKE MISTAKES.\n");
				outToClient.writeBytes("PERHAPS NEXT TIME YOU WILL DO BETTER.\n");
			// The computer plays to not lose, so we should see this all the time.
			} else if (isWon(gameBoard) == 'C'){
				outToClient.writeBytes("A STRANGE GAME. THE ONLY WINNING MOVE IS NOT TO PLAY.\n");
				outToClient.writeBytes("I BELIEVE THEY CALL THIS \"CAT\".\n");
			}
			outToClient.writeBytes("GOODBYE.\n");
			outToClient.writeBytes("PRESS ENTER TO EXIT.\n");
			outToClient.writeBytes("EOF\n"); // Eaten by the inner loop
			outToClient.writeBytes("EOG\n"); // Eaten by the outer loop
		}
	}

	public static char[] makeBoard(){
		// This method creates the gameboard itself.
		char [] gameBoard = new char [25];
		gameBoard[0] = '1';
		gameBoard[2] = '2';
		gameBoard[4] = '3';
		gameBoard[10] = '4';
		gameBoard[12] = '5';
		gameBoard[14] = '6';
		gameBoard[20] = '7';
		gameBoard[22] = '8';
		gameBoard[24] = '9';
		// Generate the vertical lines
		for (int i = 0; i < 3; i++){
			for (int j = 1; j < 4; j+=2){
				gameBoard[i*10 + j] = '|';
			}	
		}
		// Generate the horizontal lines
		for (int i = 1; i < 5; i+=2){
			for (int j = 0; j < 5; j++){
				gameBoard[i*5 + j] = '-';
			}
		}
		return gameBoard;
	}

	public static void printBoard(char[] gameBoard, DataOutputStream outToClient) throws Exception {
		// Prints the board in its current state
		for (int i = 0; i < 5; i++){
			for (int j = 0; j < 5; j++){
				outToClient.writeChar(gameBoard[i*5 + j]);
			}
		outToClient.writeBytes("\n");
		}
	}

	public static char [] makeMove(char[] gameBoard, int position){
		// This makes the move the player selects
		gameBoard[realPosition(position)] = 'O';
		return gameBoard;
	}

	public static boolean checkMove(char[] gameBoard, int position){
		// We don't want to make an invalid move
		boolean validMove = false;
		for (int i = 1; i < 10; i++){
			if (position == i){
				validMove = true;
			}
		}

		if (!validMove){
			return false;
		}
		if (gameBoard[realPosition(position)] != 'X' && gameBoard[realPosition(position)] != 'O'){
			return true;
		} else {
			return false;
		}
	}

	public static int realPosition(int position){
		// The move selected by the player is not
		// the actual array position - a mapping function.
		int realPosition = -1;
		switch (position){
			case 1: realPosition = 0; break;
			case 2: realPosition = 2; break;
			case 3: realPosition = 4; break;
			case 4: realPosition = 10; break;
			case 5: realPosition = 12; break;
			case 6: realPosition = 14; break;
			case 7: realPosition = 20; break;
			case 8: realPosition = 22; break;
			case 9: realPosition = 24; break;
		}
		return realPosition;
	}

	public static char [] chooseMove(char[] gameBoard, char side){
		// The move choosing algorithm for the computer
		char otherSide;
		// The computer is always X, but this allows for the other way
		if (side == 'X'){
			otherSide = 'O';
		} else {
			otherSide = 'X';
		}
		if (isWon(gameBoard) == 'N'){
			// Priority list of possible moves
			if (winOrBlock(gameBoard, side) != -1){
				gameBoard[winOrBlock(gameBoard, side)] = side;
			} else if (winOrBlock(gameBoard, otherSide) != -1){
				gameBoard[winOrBlock(gameBoard, otherSide)] = side;
			} else if (center(gameBoard)){
				gameBoard[realPosition(5)] = side;
			} else if (oppositeCorner(gameBoard, side) != -1){
				gameBoard[oppositeCorner(gameBoard, side)] = side;
			} else if (emptyCorner(gameBoard) != -1){
				gameBoard[emptyCorner(gameBoard)] = side;
			} else { // Choosing an empty side is our last possible move
				gameBoard[emptySide(gameBoard)] = side;
			}
		} else {
			return gameBoard;
		}
			
		return gameBoard;
	}

	public static int winOrBlock(char[] gameBoard, char side){
		// First priority - win the game, or stop the player from winning
		boolean[] gb = truthTable(gameBoard, side);

		if (((gb[1] && gb[2]) || (gb[3] && gb[6]) || (gb[4] && gb[8])) && checkMove(gameBoard, 1)){
			return 0; // Upper left corner
		} else if (((gb[0] && gb[2]) || (gb[4] && gb[7])) && checkMove(gameBoard, 2)){
			return 2; // Upper middle
		} else if (((gb[0] && gb[1]) || (gb[5] && gb[8]) || (gb[4] && gb[6])) && checkMove(gameBoard, 3)){
			return 4; // Upper right corner
		} else if (((gb[0] && gb[6]) || (gb[4] && gb[5])) && checkMove(gameBoard, 4)){
			return 10; // Left middle
		} else if (((gb[0] && gb[8]) || (gb[1] && gb[7]) || (gb[2] && gb[6]) || (gb[3] && gb[5])) && checkMove(gameBoard, 5)){
			return 12; // Center
		} else if (((gb[2] && gb[8]) || (gb[3] && gb[4])) && checkMove(gameBoard, 6)){
			return 14; // Right middle
		} else if (((gb[0] && gb[3]) || (gb[2] && gb[4]) || (gb[7] && gb[8])) && checkMove(gameBoard, 7)){
			return 20; // Bottom left corner
		} else if (((gb[6] && gb[8]) || (gb[1] && gb[4])) && checkMove(gameBoard, 8)){
			return 22; // Bottom middle
		} else if (((gb[0] && gb[4]) || (gb[2] && gb[5]) || (gb[6] && gb[7])) && checkMove(gameBoard, 9)){
			return 24; // Bottom right corner
		} else return -1; // Didn't find anything here
	}

	public static boolean fork(char[] gameBoard){
		return true;
		// The idea here is to create an opportunity to win in more
		// than one way - an unblockable combo. This is programmatically
		// difficult.
	}

	public static boolean blockFork1(char[] gameBoard){
		return true;
		// The idea here is to respond to a potential fork by making
		// two in a row in such a way that the opponent is forced
		// to block it, but not by creating a potential fork
		// for themself (or winning the game.)
	}

	public static boolean blockFork2(char[] gameBoard){
		return true;
		// This attempts to foresee potential forks from the opponent,
		// and places a piece in such a way that the fork cannot
		// happen.
	}

	public static boolean center(char[] gameBoard){
		// Second priority - play in the center of the board,
		// allowing the most possible moves.
		if (checkMove(gameBoard, 5)){
			return true;
		} else
			return false;
	}

	public static int oppositeCorner(char[] gameBoard, char side){
		// Third priority - play in the opposite corner from the player
		char otherSide;
		if (side == 'X'){
			otherSide = 'O';
		} else
			otherSide = 'X';
		if ((gameBoard[realPosition(1)] == otherSide) && (checkMove(gameBoard, 9))){
			return 24;
		} else if ((gameBoard[realPosition(3)] == otherSide) && (checkMove(gameBoard, 7))){
			return 20;
		} else if ((gameBoard[realPosition(7)] == otherSide) && (checkMove(gameBoard, 3))){
			return 4;
		} else if ((gameBoard[realPosition(9)] == otherSide) && (checkMove(gameBoard, 1))){
			return 0;
		} else
			return -1;
	}

	public static int emptyCorner(char[] gameBoard){
		// Fourth priority - just play in any empty corner.
		if (checkMove(gameBoard, 1)){
			return realPosition(1);
		} else if (checkMove(gameBoard, 3)){
			return realPosition(3);
		} else if (checkMove(gameBoard, 7)){
			return realPosition(7);
		} else if (checkMove(gameBoard, 9)){
			return realPosition(9);
		} else
			return -1;
	}

	public static int emptySide(char[] gameBoard){
		// All we have left is an empty side square.
		if (checkMove(gameBoard, 2)){
			return 2;
		} else if (checkMove(gameBoard, 4)){
			return 4;
		} else if (checkMove(gameBoard, 6)){
			return 14;
		} else {// if (checkMove(gameBoard, 8)) - it has to.
			return 22;
		}
	}

	public static char isWon(char[] gameBoard){
		// Checks if the game has been won, of course
		boolean [] sideX = truthTable(gameBoard, 'X');
		boolean [] sideO = truthTable(gameBoard, 'O');
		boolean [] totalPlacements = new boolean [9];
		for (int i = 0; i < 9; i++){
			totalPlacements[i] = sideX[i] || sideO[i];
		}
		boolean gameDone = true;
		for (int i = 0; i < 9; i++){
			gameDone = gameDone && totalPlacements[i];
		}
		if (gameDone){ // cat
			return 'C';
		}

		for (int i = 0; i < 9; i+=3){ // Across
			if (sideX[i+0] && sideX[i+1] && sideX[i+2]){
				return 'X';
			} else if (sideO[i+0] && sideO[i+1] && sideO[i+2]){
				return 'O';
			}
		}
		for (int i = 0; i < 3; i++){ // Down
		   	if (sideX[i+0] && sideX[i+3] && sideX[i+6]){
				return 'X';
			} else if (sideO[i+0] && sideO[i+3]&& sideO[i+6]){
				return 'O';
			}
		}
		if (sideX[0] && sideX[4] && sideX[8]){ // Diagonal left to right
			return 'X';
		} else if (sideO[0] && sideO[4] && sideO[8]){
			return 'O';
		} else if (sideX[2] && sideX[4] && sideX[6]){ // Diagonal right to left
			return 'X';
		} else if (sideO[2] && sideO[4] && sideO[6]){
			return 'O';
		} else // Nobody wins yet
			return 'N';
	}

	public static boolean[] truthTable(char[] gameBoard, char side){
		// Shows whether the given side controls
		// certain positions.
		boolean [] gb = new boolean [9];
		for (int i = 0; i < 9; i++){
			gb[i] = false;
		}
		if (gameBoard[0] == side){
			gb[0] = true;
		}
		if (gameBoard[2] == side){
			gb[1] = true;
		}
		if (gameBoard[4] == side){
			gb[2] = true;
		}
		if (gameBoard[10] == side){
			gb[3] = true;
		}
		if (gameBoard[12] == side){
			gb[4] = true;
		}
		if (gameBoard[14] == side){
			gb[5] = true;
		}
		if (gameBoard[20] == side){
			gb[6] = true;
		}
		if (gameBoard[22] == side){
			gb[7] = true;
		}
		if (gameBoard[24] == side){
			gb[8] = true;
		}
		return gb;
	}

}
