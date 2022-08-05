import java.util.Random;

public class BasicGame {
	
	static int gameLoopNumber = 100;
	static int height = 15;
	static int width = 15;
	static Random random = new Random();

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
		addRandomWall(level, 5, 0);
		
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
			addSomeDelay(100L, iterationNumber);
			
			if (playerRow == enemyRow && playerColumn == enemyColumn) {
				break;
			}
		}
		System.out.println("Game Over!");
	}
	
	static void addRandomWall(String[][] level, int numOfHorWall, int numOfVertWall) {
		// TODO fal ne kerüljön a játékosra vagy az ellenfélre
		for (int i = 0; i < numOfHorWall; i++) {
			addHorizontalWall(level);
		}
		for (int i = 0; i < numOfVertWall; i++) {
			addVerticalWall(level);
		}
	}
	
	static void addHorizontalWall(String[][] level) {
		int wallWidth = random.nextInt(width-3); // 0 - 11
		int wallRow = random.nextInt(height-3) + 1;
		int wallColumn = random.nextInt(width-2 - wallWidth);
		for (int i = 0; i < wallWidth; i++) {
			level[wallRow][wallColumn + 1] = "X";
		}
	}
	
	static void addVerticalWall(String[][] level) {
		int wallHeight = random.nextInt(height-3);
		int wallRow = random.nextInt(height-2 - wallHeight);
		int wallColumn = random.nextInt(width - 2) +1;
		for (int i = 0; i < wallHeight; i++) {
			level[wallRow + 1][wallColumn] = "X";
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







