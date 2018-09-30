import java.util.*;

public class Field {
	Random rnd = new Random();
	private int size;
	int[][] field;
	int indexI;
	int indexJ;
	boolean hasMoved = false;

	public Field(int size) {
		this.size = size;
	}

	public void initializeField() {
		field = new int[size][size];
		int number = 1;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				field[i][j] += number;
				number++;
			}
		}
	}

	public int getNumber(int i, int j) {
		return field[i][j];
	}

	public void showField() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (field[i][j] < 10) {
					System.out.print(" ");
				}
				if (field[i][j] == size * size) {
					System.out.print((size * size < 10)? "  " : "   ");
				} else {
					System.out.print(field[i][j] + " ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	public void makeKeyMove(String key) {
		getEmptyBoxIndex();
		if (key.equalsIgnoreCase("s")) {
			moveDown();
		}
		if (key.equalsIgnoreCase("w")) {
			moveUp();
		}
		if (key.equalsIgnoreCase("d")) {
			moveRight();
		}
		if (key.equalsIgnoreCase("a")) {
			moveLeft();
		}
	}

	public void getEmptyBoxIndex() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (field[i][j] == size * size) {
					indexI = i;
					indexJ = j;
				}
			}
		}
	}

	public void moveDown() {
		if (indexI > 0) {
			field[indexI][indexJ] = field[indexI - 1][indexJ];
			field[indexI - 1][indexJ] = size * size;
			hasMoved = true;
		}
	}

	public void moveUp() {
		if (indexI + 1 < size) {
			field[indexI][indexJ] = field[indexI + 1][indexJ];
			field[indexI + 1][indexJ] = size * size;
			hasMoved = true;
		}
	}

	public void moveRight() {
		if (indexJ > 0) {
			field[indexI][indexJ] = field[indexI][indexJ - 1];
			field[indexI][indexJ - 1] = size * size;
			hasMoved = true;
		}
	}

	public void moveLeft() {
		if (indexJ + 1 < size) {
			field[indexI][indexJ] = field[indexI][indexJ + 1];
			field[indexI][indexJ + 1] = size * size;
			hasMoved = true;
		}
	}

	public boolean isWin() {
		int count = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (field[i][j] == count + 1) {
					count++;
				}
			}
		}

		return (count == size * size) ? true : false;
	}

	public void shuffle(int number) {
		for (int i = 0; i < number; i++) {
			hasMoved = false;
			while (!hasMoved) {
				int t = rnd.nextInt(4);
				String randomKey = null;
				switch (t) {
				case 0:
					randomKey = "w";
					break;
				case 1:
					randomKey = "d";
					break;
				case 2:
					randomKey = "s";
					break;
				case 3:
					randomKey = "a";
					break;
				}
				makeKeyMove(randomKey);
			}
		}
	}

	public void makeMouseMove(int row, int col) {
		getEmptyBoxIndex();
		if (indexI == row + 1 && indexJ == col) {
			moveDown();
		}
		if (indexI == row - 1 && indexJ == col) {
			moveUp();
		}
		if (indexI == row && indexJ == col + 1) {
			moveRight();
		}
		if (indexI == row && indexJ == col - 1) {
			moveLeft();
		}
	}
}
