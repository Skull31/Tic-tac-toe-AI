package com.xhrick.tictactoeai;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class GameUi extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JLabel status;
	private JLabel difficultyLabel;
	private JMenuBar menuBar;
	
	private Controller controller;
	
	private CellPanel[][] cellsPanel = new CellPanel[3][3];
	
	private Cell[][] cells;

	public GameUi(String title) {
		super(title);
		panel = new JPanel(new GridLayout(3, 3));
		status = new JLabel("X's turn");
		difficultyLabel = new JLabel("Difficulty: Impossible");
		menuBar = new MenuBar();
		controller = new Controller(this);
		cells = controller.getCells();
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				cellsPanel[i][j] = new CellPanel(cells[i][j]);
				panel.add(cellsPanel[i][j]);
			}
		}

		add(panel, BorderLayout.CENTER);
		add(difficultyLabel, BorderLayout.NORTH);
		add(status, BorderLayout.SOUTH);
		setJMenuBar(menuBar);
	}
	
	public void newGame() {
		remove(panel);
		controller.newGame();
		panel = new JPanel(new GridLayout(3, 3));
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				cellsPanel[i][j] = new CellPanel(cells[i][j]);
				panel.add(cellsPanel[i][j]);
			}
		}
		status.setText("X's turn.");
		add(panel, BorderLayout.CENTER);
		revalidate();
		repaint();
	}
	
	private GameUi getOuter() {
		return this;
	}
	
	public void changeStatusLabel(String s) {
		this.status.setText(s);
	}
	
	public void changeDifficultyLabel(String s) {
		this.difficultyLabel.setText(s);
	}
	
	public void cellRepaint(int x, int y) {
		cellsPanel[y][x].repaint();
	}
	
	private class CellPanel extends JPanel {
		
		private static final long serialVersionUID = 1L;
		private Cell cell;
		
		public CellPanel(Cell cell) {
			this.cell = cell;
			setBackground(Color.white);
			setBorder(new LineBorder(Color.black, 2));		
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					controller.getCellFromPanel(cell);
					super.mouseClicked(e);
				}
			});
		}
		
		@Override
		protected void paintComponent(Graphics g2) {
				super.paintComponent(g2);
				Graphics2D g = (Graphics2D) g2;
				g.setStroke(new BasicStroke(3));
				if (cell.getToken() == 'X') {
					g.setColor(Color.red);
					g.drawLine(10, 10, getWidth() - 10, getHeight() - 10);
					g.drawLine(getWidth() - 10, 10, 10, getWidth() - 10);
				} else if (cell.getToken() == 'O') {
					g.setColor(Color.blue);
					g.drawOval(10, 10, getWidth() - 20, getHeight() - 20);
				}
			}
		
	}

	private class MenuBar extends JMenuBar {

		private static final long serialVersionUID = 1L;
		private JMenu menu = new JMenu("Menu");
		private JMenu difficulty = new JMenu("Difficulty");
		private JMenu aboutM = new JMenu("About");
		
		private JMenuItem newGame = new JMenuItem("New game");

		private JMenuItem exit = new JMenuItem("Exit");
		private JMenuItem pcStart = new JMenuItem("PC first");

		private JMenuItem human = new JMenuItem("Player vs player");
		private JMenuItem random = new JMenuItem("Easy");
		private JMenuItem medium = new JMenuItem("Medium");
		private JMenuItem impossible = new JMenuItem("Impossible");

		private JMenuItem about = new JMenuItem("About");

		public MenuBar() {
			setAction();

			menu.add(newGame);
			menu.add(pcStart);
			menu.add(exit);

			difficulty.add(human);
			difficulty.add(random);
			difficulty.add(medium);
			difficulty.add(impossible);

			aboutM.add(about);

			add(menu);
			add(difficulty);
			add(aboutM);
		}

		private void setAction() {
			exit.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					System.exit(0);
				}
			});
			
			pcStart.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					controller.pcStart();
				}
			});
			
			newGame.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					newGame();
				}
			});
			
			human.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					controller.changeDifficulty('H');
					newGame();
					changeDifficultyLabel("Difficulty: Player vs player");
				}
			});
			
			random.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					controller.changeDifficulty('R');
					newGame();
					changeDifficultyLabel("Difficulty: Easy");

				}
			});
			
			medium.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					controller.changeDifficulty('M');
					newGame();
					changeDifficultyLabel("Difficulty: Medium");

				}
			});
			
			impossible.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					controller.changeDifficulty('I');
					newGame();
					changeDifficultyLabel("Difficulty: Impossible");
				}
			});
			
			about.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(getOuter(),
						    "Author: Tomáš Hricko\nVersion: v1.2",
						    "About Tic-tac-toe",
						    JOptionPane.INFORMATION_MESSAGE);
					
				}
			});
		}
	}// end of MenuBar class
}// end of GameUi class
