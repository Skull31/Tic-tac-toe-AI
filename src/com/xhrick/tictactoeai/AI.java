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
	private Controller controller;
	private Random r = new Random();

	private List<ExtCell> cellArray = null;

	// AI constructor
	public AI(Controller controller) {
		difficulty = 'I';
		myToken = 'O';
		this.controller = controller;
	}

	// Change AI's difficulty level
	public void setDifficulty(char difficulty) {
		this.difficulty = difficulty;
	}

	// Change AI's token
	public void setMyToken(char c) {
		myToken = c;
	}

	// Method to do next move
	public boolean doMove() {
		switch (difficulty) {
		case 'H':
			return false;
		case 'R':
			whoseTurn = controller.getWhoseTurn();
			randomMove();
			break;
		case 'M':
			whoseTurn = controller.getWhoseTurn();
			mediumMove();
			break;
		case 'I':
			whoseTurn = controller.getWhoseTurn();
			cellArray = new ArrayList<>();
			cellArray.add(new ExtCell(controller.getCells()));
			impossibleMove();
			cellArray = null;
			break;
		}
		return true;
	}

	// Random difficulty algorithm
	private void randomMove() {

		ExtCell cells = new ExtCell(controller.getCells());
		int x = r.nextInt(3);
		int y = r.nextInt(3);

		while (cells.getCellsChar(y, x) != ' ') {
			x = r.nextInt(3);
			y = r.nextInt(3);
		}

		controller.getMoveFromAI(x, y);

	}

	// Medium difficulty algorithm
	private void mediumMove() {

		ExtCell cells = new ExtCell(controller.getCells());

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (cells.getCellsChar(i, j) != ' ')
					continue;
				cells.setCellsChar(myToken, i, j);
				if (cells.isWon() == myToken) {
					controller.getMoveFromAI(j, i);
					return;
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
					controller.getMoveFromAI(j, i);
					return;
				}
				cells.setCellsChar(' ', i, j);
			}
		}
		randomMove();
		return;

	}

	// Impossible difficulty algorithm
	private void impossibleMove() {
		int position = 0;

		ExtCell parentCell = cellArray.get(0);
		ExtCell thisCell = null;

		if (parentCell.getEmptySpaces() == 9) {
			controller.getMoveFromAI(r.nextInt(3), r.nextInt(3));
			return;
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
						parentCell.addKid(thisCell);
						thisCell.setCellsChar(whoseTurn, i, j);
						thisCell.dicreaseEmptySpaces();

						if (thisCell.isWon() == myToken) {
							thisCell.setScore(0 + thisCell.getEmptySpaces() + 1);
						} else if (thisCell.isWon() == ((myToken == 'X') ? 'O' : 'X')) {
							thisCell.setScore((0 - thisCell.getEmptySpaces() - 1));
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
		}

		//System.out.println("kido: ");
		ExtCell bestKid = cellArray.get(0).getKids().get(0);
		for (ExtCell kido : cellArray.get(0).getKids()) {
			//System.out.println(kido.getScore());
			if (bestKid.getScore() < kido.getScore())
				bestKid = kido;
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (bestKid.getCellsChar(i, j) != cellArray.get(0).getCellsChar(i, j))
					controller.getMoveFromAI(j, i);
			}
		}
	}

	// Method to set score on current ExtCell from their kids
	private void choosenValue(ExtCell parent) {
		int bestValue;
		if (myToken == 'O') {
			if (parent.getEmptySpaces() % 2 == 0)
				bestValue = -10;
			else
				bestValue = 10;
		} else {
			if (parent.getEmptySpaces() % 2 == 0)
				bestValue = 10;
			else
				bestValue = -10;
		}

		if (parent.getKids().size() > 0) {
			for (ExtCell kido : parent.getKids()) {
				if (myToken == 'O') {
					if (parent.getEmptySpaces() % 2 == 0)
						bestValue = (kido.getScore() > bestValue) ? kido.getScore() : bestValue;
					else
						bestValue = (kido.getScore() < bestValue) ? kido.getScore() : bestValue;
				} else {
					if (parent.getEmptySpaces() % 2 == 0)
						bestValue = (kido.getScore() < bestValue) ? kido.getScore() : bestValue;
					else
						bestValue = (kido.getScore() > bestValue) ? kido.getScore() : bestValue;
				}
			}
			parent.setScore(bestValue);
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
