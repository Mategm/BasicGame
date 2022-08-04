
public class BasicGame {
	
	static int gameLoopNumber = 100;
	static int height = 15;
	static int width = 15;

	public static void main(String[] args) throws InterruptedException {
		String playerMark = "O";
		int playerRow = 2;
		int playerColumn = 2;
		Direction playerDirection = Direction.RIGHT;
		
		String enemyMark = "-";
		int enemyRow = 7;
		int enemyColumn = 4;
		Direction enemyDirection = Direction.LEFT;
		
		String[][] level = new String[height][width];
		initLevel(level);
		
		for (int iterationNumber=1; iterationNumber <= gameLoopNumber; iterationNumber++) {
			// játékos léptetése
			if (iterationNumber % 15 == 0) {
				playerDirection = changeDirection(playerDirection);
			}
			int[] playerCoordinates = makeMove(playerDirection, level, playerRow, playerColumn);
			playerRow = playerCoordinates[0];
			playerColumn = playerCoordinates[1];
			
			// ellenfél léptetése
			if (iterationNumber % 10 == 0) {
				enemyDirection = changeDirection(enemyDirection);
			}
			int[] enemyCoordinates = makeMove(enemyDirection, level, enemyRow, enemyColumn);
			enemyRow = enemyCoordinates[0];
			enemyColumn = enemyCoordinates[1];
			
			draw(level, playerMark, playerRow, playerColumn, enemyMark, enemyRow, enemyColumn);
			addSomeDelay(200L, iterationNumber);
		}
	}

	private static void addSomeDelay(long timeout, int itNum) throws InterruptedException {
		System.out.println("---------- " + itNum + "-----------");
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
		for (int row = 0; row < level.length; row++) {
			for (int column = 0; column < level[row].length; column++) {
				if (row == 0 || row == height-1 || column == 0 || column == width-1 ) {
					level[row][column] = "X";
				} else {
					level[row][column] = " ";
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
	
	static void draw(String[][] board, String playerMark, int playerRow, int playerColumn, String enemyMark, int enemyRow, int enemyColumn) {
		// pálya és játékos kirajzolása
		for (int row = 0; row < height; row++) {
			for (int column = 0; column < width; column++) {
				if (row == playerRow && column == playerColumn) {
					System.out.print(playerMark);
				} else if (row == enemyRow && column == enemyColumn) {
					System.out.print(enemyMark);
				} else {
					System.out.print(board[row][column]);
				}
			}
			System.out.println();
		}
	}

}







