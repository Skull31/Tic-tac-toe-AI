package com.xhrick.tictactoeai;

import java.util.ArrayList;

public class ExtCell {
	
	private char[][] cells = new char[3][3];
	
	private int emptySpaces = 0;
	private int score = 0;
	
	private ExtCell parentGame = null;
	private ArrayList<ExtCell> kids = new ArrayList<>();

	public ExtCell(Cell[][] cell) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (cell[i][j].getToken() == ' ')
					emptySpaces++;
				cells[i][j] = cell[i][j].getToken();
			}
		}

	}

	public ExtCell(ExtCell parentGame) {
		char[][] cells = parentGame.getCells();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (cells[i][j] == ' ')
					emptySpaces++;
				this.cells[i][j] = cells[i][j];
			}
		}
		this.parentGame = parentGame;
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
		for (int i = 0; i < 3; i++) {
			if (cells[i][0] == token && cells[i][1] == token && cells[i][2] == token)
				return true;
		}

		// check columns
		for (int i = 0; i < 3; i++) {
			if (cells[0][i] == token && cells[1][i] == token && cells[2][i] == token)
				return true;
		}

		// check diagonals
		if(cells[0][0] == token && cells[1][1] == token && cells[2][2] == token)
			return true;
		
		if(cells[2][0] == token && cells[1][1] == token && cells[0][2] == token)
			return true;

		return false;
	}

	private boolean isFull() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (cells[i][j] == ' ')
					return false;

		return true;
	}

	public char[][] getCells() {
		return cells;
	}

	public void setCells(char[][] cells) {
		this.cells = cells;
	}

	public char getCellsChar(int i, int j) {
		return cells[i][j];
	}

	public void setCellsChar(char cell, int i, int j) {
		this.cells[i][j] = cell;
	}

	public ExtCell getParentGame() {
		return parentGame;
	}

	public int getEmptySpaces() {
		return emptySpaces;
	}

	public void dicreaseEmptySpaces() {
		emptySpaces--;
	}

	public void addKid(ExtCell kid) {
		kids.add(kid);
	}

	public ArrayList<ExtCell> getKids() {
		return kids;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void changeScore(int score) {
		this.score = this.score + score;
	}
	
	public int getScore() {
		return score;
	}


}
