package com.xhrick.tictactoeai;

import java.util.*;

/*
 * TicTacToe artificial intelligence
 * 3 difficulty levels: 
 * Random - AI do random move.
 * Medium - If either player or AI can win in next move, the AI will win or blocks player
 * 			opportunity to win.
 * Impossible - Player has no chance to win. Minimax algorithm used.
 * 
 * Author: Tomáš Hricko
 */
public class AI {

	private char difficulty;
	private char myToken;
	private char whoseTurn;
	private Random r = new Random();

	// AI constructor
	public AI() {
		difficulty = 'I';
		myToken = 'O';
	}

	// Change AI's difficulty level
	public void setDifficulty(char difficulty) {
		this.difficulty = difficulty;
	}

	public char getDifficulty() {
		return difficulty;
	}

	// Change AI's token
	public void setMyToken(char c) {
		myToken = c;
	}

	// Method to do next move
	public int[] doMove(char whoseTurn, Cell[][] cells) {
		int moveAI[] = new int[2];

		switch (difficulty) {
			case 'R':
				this.whoseTurn = whoseTurn;
				moveAI = randomMove(cells);
				break;
			case 'M':
				this.whoseTurn = whoseTurn;
				moveAI = mediumMove(cells);
				break;
			case 'I':
				this.whoseTurn = whoseTurn;
				moveAI = impossibleMove(cells);
				break;
		}
		return moveAI;
	}

	// Random difficulty algorithm
	private int[] randomMove(Cell[][] cellsController) {

		ExtCell cells = new ExtCell(cellsController);
		int x = r.nextInt(3);
		int y = r.nextInt(3);
		int moveAI[] = new int[2];

		while (cells.getCellsChar(y, x) != ' ') {
			x = r.nextInt(3);
			y = r.nextInt(3);
		}

		moveAI[0] = x;
		moveAI[1] = y;
		return moveAI;

	}

	// Medium difficulty algorithm
	private int[] mediumMove(Cell[][] cellsController) {

		ExtCell cells = new ExtCell(cellsController);
		int moveAI[] = new int[2];

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (cells.getCellsChar(i, j) != ' ')
					continue;
				cells.setCellsChar(myToken, i, j);
				if (cells.isWon() == myToken) {
					moveAI[0] = j;
					moveAI[1] = i;
					return moveAI;
				}

				cells.setCellsChar(' ', i, j);
			}
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (cells.getCellsChar(i, j) != ' ')
					continue;
				cells.setCellsChar((myToken == 'X') ? 'O' : 'X', i, j);
				if (cells.isWon() == ((myToken == 'X') ? 'O' : 'X')) {
					moveAI[0] = j;
					moveAI[1] = i;
					return moveAI;
				}
				cells.setCellsChar(' ', i, j);
			}
		}
		return randomMove(cellsController);
	}

	// Impossible difficulty algorithm
	private int[] impossibleMove(Cell[][] cells) {
		int position = 0;

		List<ExtCell> cellArray = new ArrayList<>();
		cellArray.add(new ExtCell(cells));

		ExtCell parentCell = cellArray.get(0);
		ExtCell thisCell = null;

		int moveAI[] = new int[2];


		//For performance reasons. If AI has first move, the move is random.
		//Because AI always calculate all the fields with the same priority (0)
		if (parentCell.getEmptySpaces() == 9) {
			moveAI[0] = r.nextInt(3);
			moveAI[1] = r.nextInt(3);
			return moveAI;
		}

		while (position < cellArray.size()) {
			parentCell = cellArray.get(position);
			if (parentCell.isWon() != 'N') {
				position++;
				continue;
			}

			if (position != 0)
				/*
				 * change whoseTurn
				 */
				if (thisCell.getEmptySpaces() + 1 != parentCell.getEmptySpaces()) {
					whoseTurn = (whoseTurn == 'X') ? 'O' : 'X';
				}

			List<Integer> iL = randomGenerator(0, 3);
			List<Integer> jL = randomGenerator(0, 3);

			for (int i : iL) {
				for (int j : jL) {
					if (parentCell.getCellsChar(i, j) == ' ') {
						thisCell = new ExtCell(parentCell);
						parentCell.addChild(thisCell);
						thisCell.setCellsChar(whoseTurn, i, j);
						thisCell.dicreaseEmptySpaces();

						if (thisCell.isWon() == myToken) {
							thisCell.setScore((thisCell.getEmptySpaces()) + 1);
						} else if (thisCell.isWon() == ((myToken == 'X') ? 'O' : 'X')) {
							thisCell.setScore(-(thisCell.getEmptySpaces()) - 1);
						} else {
							thisCell.setScore(0);
						}

						cellArray.add(thisCell);
					}

				}
			}
			position++;
		}

		// Set scored from the bottom of the tree to the top.
		for (int i = cellArray.size() - 1; i >= 0; i--) {
			choosenValue(cellArray.get(i));
			// System.out.println(cellArray.get(i).getScore());
		}

		System.out.println("Child: ");
		ExtCell bestChild = cellArray.get(0).getChildren().get(0);
		for (ExtCell child : cellArray.get(0).getChildren()) {
			System.out.println(child.getScore());
			if (bestChild.getScore() < child.getScore())
				bestChild = child;
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (bestChild.getCellsChar(i, j) != cellArray.get(0).getCellsChar(i, j)) {
					moveAI[0] = j;
					moveAI[1] = i;
				}
			}
		}
		return moveAI;
	}

	// Method to set score on current ExtCell from their children
	private void choosenValue(ExtCell parent) {
		if (parent.getChildren().size() <= 0)
			return;
		int lowestValue = parent.getChildren().get(0).getScore();
		int highestValue = parent.getChildren().get(0).getScore();

		for (ExtCell child : parent.getChildren()) {
			if (child.getScore() < lowestValue)
				lowestValue = child.getScore();
			if (child.getScore() > highestValue)
				highestValue = child.getScore();
		}

		if (myToken == 'O') {
			if (parent.getEmptySpaces() % 2 == 0) {
				parent.setScore(highestValue);
			} else {
				parent.setScore(lowestValue);
			}
		} else {
			if (parent.getEmptySpaces() % 2 == 0) {
				parent.setScore(lowestValue);
			} else {
				parent.setScore(highestValue);
			}
		}
	}

	/*
	 * Generate int numbers from min (inclusive) to max (exclusive) in random order.
	 * Example: min = 0; max = 3; Possible outputs: 0, 1, 2; 0, 2, 1; 1, 0, 2; 1, 2,
	 * 0; 2, 1, 0; 2, 0, 1
	 */
	private List<Integer> randomGenerator(int min, int max) {
		List<Integer> numbers = new ArrayList<>();
		int num = 0;
		for (int i = 0; i < (max - min); i++) {
			num = r.nextInt(max - min) + min;
			while (numbers.contains(num))
				num = r.nextInt(max - min) + min;
			numbers.add(num);
		}
		return numbers;
	}
}
