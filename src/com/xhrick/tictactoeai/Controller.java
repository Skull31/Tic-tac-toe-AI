package com.xhrick.tictactoeai;

import javax.swing.JOptionPane;

public class Controller {

	private GameUi gameUi;
	private Cell[][] cells = new Cell[3][3];
	private AI ai;

	private boolean firstPc = false;
	private boolean isEnd = false;

	private char whoseTurn = 'X';

	public Controller(GameUi gameUi) {
		this.gameUi = gameUi;
		ai = new AI();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				cells[i][j] = new Cell(j, i);
			}
		}
	}

	public Cell[][] getCells() {
		return cells;
	}

	public char getWhoseTurn() {
		return whoseTurn;
	}

	public void changeDifficulty(char c) {
		ai.setDifficulty(c);
	}

	public void pcStart() {
		if (isEnd || firstPc)
			return;

		int[] fromAI;

		if (ai.getDifficulty() == 'H')
			return;

		ai.setMyToken(whoseTurn);
		fromAI = ai.doMove(whoseTurn, cells);
		getMoveFromAI(fromAI[0], fromAI[1]);

		if (afterMove())
			return;

	}

	public void getCellFromPanel(Cell cell) {
		if (cell.getToken() != ' ' || isEnd)
			return;

		int x = cell.getX();
		int y = cell.getY();
		int[] fromAI;

		cells[y][x].setToken(whoseTurn);
		gameUi.cellRepaint(x, y);

		if (afterMove())
			return;

		if (ai.getDifficulty() == 'H')
			return;

		fromAI = ai.doMove(whoseTurn, cells);
		getMoveFromAI(fromAI[0], fromAI[1]);

		if (afterMove())
			return;

	}

	private boolean afterMove() {
		firstPc = true;
		if (isWon() != 'N') {
			isEnd = true;
			switch (isWon()) {
				case 'T':
					int e = JOptionPane.showConfirmDialog(gameUi,
							"Tie game!\n\nNew game?",
							"Game over!",
							JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

					gameUi.changeStatusLabel("Tie game!");
					if (e == JOptionPane.YES_OPTION)
						gameUi.newGame();
					break;
				default:
					int ee = JOptionPane.showConfirmDialog(gameUi,
							whoseTurn + " is winner!\n\nNew game?",
							"Game over!",
							JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

					gameUi.changeStatusLabel(whoseTurn + " is winner!");
					if (ee == JOptionPane.YES_OPTION)
						gameUi.newGame();
			}
			return true;
		}
		whoseTurn = (whoseTurn == 'X') ? 'O' : 'X';
		gameUi.changeStatusLabel(whoseTurn + "'s turn.");
		return false;
	}

	private void getMoveFromAI(int x, int y) {
		cells[y][x].setToken(whoseTurn);
		gameUi.cellRepaint(x, y);
	}

	public void newGame() {
		firstPc = false;
		ai.setMyToken('O');
		whoseTurn = 'X';
		isEnd = false;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				cells[i][j] = new Cell(j, i);
			}
		}
	}

	public char isWon() {
		if (isWinner('X'))
			return 'X';
		else if (isWinner('O'))
			return 'O';
		else if (isFull())
			return 'T';
		else
			return 'N';
	}

	private boolean isWinner(char token) {
		// check rows
		for (int i = 0; i < 3; i++)
			if ((cells[i][0].getToken() == token) && (cells[i][1].getToken() == token)
					&& (cells[i][2].getToken() == token)) {
				return true;
			}

		// check columns
		for (int j = 0; j < 3; j++)
			if ((cells[0][j].getToken() == token) && (cells[1][j].getToken() == token)
					&& (cells[2][j].getToken() == token)) {
				return true;
			}
		// check diagonal
		if ((cells[0][0].getToken() == token) && (cells[1][1].getToken() == token)
				&& (cells[2][2].getToken() == token)) {
			return true;
		}

		if ((cells[0][2].getToken() == token) && (cells[1][1].getToken() == token)
				&& (cells[2][0].getToken() == token)) {
			return true;
		}

		return false;
	}

	private boolean isFull() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (cells[i][j].getToken() == ' ')
					return false;

		return true;
	}

}
