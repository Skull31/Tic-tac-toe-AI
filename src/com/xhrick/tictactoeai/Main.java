package com.xhrick.tictactoeai;

import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		JFrame frame = new GameUi("Tic-tac-toe");
		frame.setSize(382,470);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
	}
}
