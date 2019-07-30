package com.xhrick.tictactoeai;

public class Cell {
	private char token = ' ';
	private int x;
	private int y;
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean setToken(char token) {
		if(token == ' ')
			return false;
		if(this.token != ' ')
			return false;
		
		this.token = token;
		return true;
	}
	
	public char getToken() {
		return token;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
