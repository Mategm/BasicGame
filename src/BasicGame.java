import java.util.Random;

public class BasicGame {
	
	static final int GAME_LOOP_NUMBER = 100;
	static final int HEIGHT = 15;
	static final int WIDTH = 15;
	static final Random random = new Random();

	public static void main(String[] args) throws InterruptedException {
		String playerMark = "O";
		int playerRow = 2;
		int playerColumn = 2;
		Direction playerDirection = Direction.RIGHT;
		
		String enemyMark = "-";
		int enemyRow = 7;
		int enemyColumn = 4;
		Direction enemyDirection = Direction.LEFT;
		
		String[][] level = new String[HEIGHT][WIDTH];
		initLevel(level);
		addRandomWall(level, 5, 0);
		
		for (int iterationNumber=1; iterationNumber <= GAME_LOOP_NUMBER; iterationNumber++) {
			// játékos léptetése
			if (iterationNumber % 15 == 0) {
				playerDirection = changeDirection(playerDirection);
			}
			int[] playerCoordinates = makeMove(playerDirection, level, playerRow, playerColumn);
			playerRow = playerCoordinates[0];
			playerColumn = playerCoordinates[1];
			
			// ellenfél irány változtatása és léptetése
			enemyDirection = changeEnemyDirection(level, enemyDirection, playerRow, playerColumn, enemyRow, enemyColumn);
			if (iterationNumber % 2 == 0) {
				int[] enemyCoordinates = makeMove(enemyDirection, level, enemyRow, enemyColumn);
				enemyRow = enemyCoordinates[0];
				enemyColumn = enemyCoordinates[1];
			}
			
			draw(level, playerMark, playerRow, playerColumn, enemyMark, enemyRow, enemyColumn);
			addSomeDelay(100L, iterationNumber);
			
			if (playerRow == enemyRow && playerColumn == enemyColumn) {
				break;
			}
		}
		System.out.println("Game Over!");
	}
	
	static Direction changeEnemyDirection(String[][] level, Direction originalEnemyDirection, int playerRow, int playerColumn, int enemyRow, int enemyColumn) {
		if (playerRow < enemyRow && level[enemyRow-1][enemyColumn].equals(" ")) {
			return Direction.UP;
		}
		if (playerRow > enemyRow && level[enemyRow+1][enemyColumn].equals(" ")) {
			return Direction.DOWN;
		}
		if (playerColumn < enemyColumn && level[enemyRow][enemyColumn-1].equals(" ")) {
			return Direction.LEFT;
		}
		if (playerColumn > enemyColumn && level[enemyRow][enemyColumn+1].equals(" ")) {
			return Direction.RIGHT;
		}
		return originalEnemyDirection;
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
		int wallWidth = random.nextInt(WIDTH-3); // 0 - 11
		int wallRow = random.nextInt(HEIGHT-3) + 1;
		int wallColumn = random.nextInt(WIDTH-2 - wallWidth);
		for (int i = 0; i < wallWidth; i++) {
			level[wallRow][wallColumn + 1] = "X";
		}
	}
	
	static void addVerticalWall(String[][] level) {
		int wallHeight = random.nextInt(HEIGHT-3);
		int wallRow = random.nextInt(HEIGHT-2 - wallHeight);
		int wallColumn = random.nextInt(WIDTH - 2) +1;
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
				if (row == 0 || row == HEIGHT-1 || column == 0 || column == WIDTH-1 ) {
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
		for (int row = 0; row < HEIGHT; row++) {
			for (int column = 0; column < WIDTH; column++) {
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







