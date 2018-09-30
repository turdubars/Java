import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PuzzleMain extends JFrame {
	public static int SIZE = Integer.parseInt(JOptionPane.showInputDialog("Enter the size of puzzle"));
	public static int dX;
	public static int dY;
	public static int x, y;
	public static int mouseI, mouseJ;
	
	CanvasPanel panel = new CanvasPanel();
	static Field field = new Field(SIZE);
	JButton button = new JButton("Shuffle");

	PuzzleMain() {
		super("15-Puzzle Game");
		setLayout(new BorderLayout());
		panel.setBackground(Color.BLACK);
		panel.setFocusable(true);
		add(panel, BorderLayout.CENTER);
		panel.addKeyListener(new KeyHandler());
		panel.addMouseListener(new MouseHandler());
		panel.addMouseMotionListener(new MotionHandler());
		add(button, BorderLayout.SOUTH);
		button.addMouseListener(new MouseHandler());

	}

	public static void main(String[] args) {
//		Scanner scan = new Scanner(System.in);
//		field.initializeField();
//		System.out.print("Number of moves for shuffle: ");
//		field.shuffle(scan.nextInt());
//		while (!field.isWin()) {
//			field.showField();
//			System.out.print("Your move (W, S, D, A): ");
//			field.makeKeyMove(scan.next());
//		}
		
		PuzzleMain frame = new PuzzleMain();
		frame.setSize(SIZE * 100, SIZE * 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		field.initializeField();
		field.shuffle(Integer.parseInt(JOptionPane.showInputDialog("Number of moves for shuffle")));
		field.showField();
	}

	class CanvasPanel extends JPanel {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			for (int i = 0; i < SIZE; i++) {
				for (int j = 0; j < SIZE; j++) {
					dX = getWidth() / SIZE;
					dY = getHeight() / SIZE;
					g.setColor(Color.DARK_GRAY);
					if (mouseI == i && mouseJ == j) {
						g.fill3DRect(i * dX, j * dY, dX, dY, false);
					} else {
						g.fill3DRect(i * dX, j * dY, dX, dY, true);
					}
					int number = field.getNumber(j, i);
					if (number != SIZE * SIZE) {
						g.setColor(Color.WHITE);
						g.drawString(Integer.toString(number), i * dX + dX / 2, j * dY + dY / 2);
					}
				}
			}
		}
	}

	class KeyHandler extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
				field.makeKeyMove("w");
				repaint();
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
				field.makeKeyMove("s");
				repaint();
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
				field.makeKeyMove("d");
				repaint();
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
				field.makeKeyMove("a");
				repaint();
			}
			field.showField();
			if (field.isWin()) {
				JOptionPane.showMessageDialog(null, "Congratulations! You win");
			}
		}
	}

	class MouseHandler extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == button) {
				field.shuffle(Integer.parseInt(JOptionPane.showInputDialog("Number of moves for shuffle")));
				repaint();
			} else {
				for (int i = 0; i < SIZE; i++) {
					for (int j = 0; j < SIZE; j++) {
						x = e.getX();
						y = e.getY();
						if (isBoxSelected(x, y, j, i)) {
							field.makeMouseMove(j, i);
							repaint();
							field.showField();
							if (field.isWin()) {
								JOptionPane.showMessageDialog(null, "Congratulations! You win");
							}
						}
					}
				}
			}
		}
	}
	
	class MotionHandler extends MouseMotionAdapter {
		@Override
		public void mouseMoved(MouseEvent e) {
			for (int i = 0; i < SIZE; i++) {
				for (int j = 0; j < SIZE; j++) {
					if (isBoxSelected(e.getX(), e.getY(), j, i)) {
						mouseI = i;
						mouseJ = j;
						repaint();
					}
				}
			}

		}
	}
	public static boolean isBoxSelected(int mouseX, int mouseY, int row, int col) {
		boolean b1 = (mouseX >= col * dX && mouseX <= col * dX + dX);
		boolean b2 = (mouseY >= row * dY && mouseY <= row * dY + dY);
		return b1 & b2;
	}
}
