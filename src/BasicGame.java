
public class BasicGame {
	
	static int height = 10;
	static int width = 10;

	public static void main(String[] args) throws InterruptedException {
		String[][] level = new String[height][width];
		String playerMark = "O";
		int row = 2;
		int column = 2;
		Direction direction = Direction.RIGHT;
		initLevel(level);
		
		for (int k=1; k <= 100; k++) {
			
			if (k % 10 == 0) {
				direction = changeDirection(direction);
			}
			int[] coordinates = makeMove(direction, level, row, column);
			row = coordinates[0];
			column = coordinates[1];
			draw(level, playerMark, row, column);
			addSomeDelay(200L);
		}
	}

	private static void addSomeDelay(long timeout) throws InterruptedException {
		System.out.println("----------");
		Thread.sleep(timeout);
	}
	
	static int[] makeMove(Direction direction, String[][] level, int row, int column) {
		switch (direction) {
		case UP:
			if (level[row-1][column].equals(" ")) {
				row--;
			}	
			break;
		case DOWN:
			if (level[row+1][column].equals(" ")) {
				row++;
			}
			break;
		case LEFT:
			if (level[row][column-1].equals(" ")) {
				column--;
			}
			break;
		case RIGHT:
			if (level[row][column+1].equals(" ")) {
				column++;
			}
			break;
		}
		return new int[] {row, column};
	}

	private static void initLevel(String[][] level) {
		for (int i = 0; i < level.length; i++) {
			for (int j = 0; j < level[i].length; j++) {
				if (i == 0 || i == 9 || j == 0 || j == 9) {
					level[i][j] = "X";
				} else {
					level[i][j] = " ";
				}
			}
		}
	}

	static Direction changeDirection(Direction direction) {
		switch (direction) {
		case RIGHT:
			return direction = Direction.DOWN;
		case DOWN:
			return direction = Direction.LEFT;
		case LEFT:
			return direction = Direction.UP;
		case UP:
			return direction = Direction.RIGHT;
		}
		return direction;
	}
	
	static void draw(String[][] board, String mark, int x, int y) {
		// pálya és játékos kirajzolása
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (i == x && j == y) {
					System.out.print(mark);
				} else {
					System.out.print(board[i][j]);
				}
			}
			System.out.println();
		}
	}

}







