import java.util.Random;

public class BasicGame {
	
	static final int GAME_LOOP_NUMBER = 1_0;
	static final int HEIGHT = 15;
	static final int WIDTH = 60;
	static final Random random = new Random(103L);
	static String enemyMark = "-";

	public static void main(String[] args) throws InterruptedException {
		String[][] level = new String[HEIGHT][WIDTH];
		initLevel(level);
		addRandomWalls(level);
		
		String playerMark = "O";
		int[] playerStartCoordinates = getRandomStartingCoordinates(level);
		int playerRow = playerStartCoordinates[0];
		int playerColumn = playerStartCoordinates[1];
		Direction playerDirection = Direction.RIGHT;
		
		String enemyMark = "-";
		int[] enemyStartCoordinates = getRandomStartingCoordinatesAtLeastCertainDistance(level, playerStartCoordinates, 10);
		int enemyRow = enemyStartCoordinates[0];
		int enemyColumn = enemyStartCoordinates[1];
		Direction enemyDirection = Direction.LEFT;
		
		String powerUpMark = "*";
		int[] powerUpStartCoordinates = getRandomStartingCoordinates(level);
		int powerUpRow = powerUpStartCoordinates[0];
		int powerUpColumn = powerUpStartCoordinates[1];
		boolean powerUpPresentOnLevel = false;
		boolean powerUpActive = false;
		int powerUpPresenceCounter = 0;
		int powerUpActiveCounter = 0;
		
		GameResult gameResult = GameResult.TIE;
		
		for (int iterationNumber=1; iterationNumber <= GAME_LOOP_NUMBER; iterationNumber++) {
			// játékos léptetése
			if (powerUpActive) {
				playerDirection = changeDirectionTowards(level, playerDirection, playerRow, playerColumn, enemyRow, enemyColumn);
			} else {
				if (powerUpPresentOnLevel) {
					playerDirection = changeDirectionTowards(level, playerDirection, playerRow, playerColumn, powerUpRow, powerUpColumn);
				} else {
					if (iterationNumber % 15 == 0) {
						playerDirection = changeDirection(playerDirection);
					}
				}
			}
			int[] playerCoordinates = makeMove(playerDirection, level, playerRow, playerColumn);
			playerRow = playerCoordinates[0];
			playerColumn = playerCoordinates[1];
			
			// ellenfél irány változtatása és léptetése
			if (powerUpActive) {
				Direction directionTowardsPlayer = changeDirectionTowards(level, enemyDirection, enemyRow, enemyColumn, playerRow, playerColumn);
				enemyDirection = getEscapeDirection(level, enemyRow, enemyColumn, directionTowardsPlayer);
			} else {
				enemyDirection = changeDirectionTowards(level, enemyDirection, enemyRow, enemyColumn, playerRow, playerColumn);
			}
			if (iterationNumber % 2 == 0) {
				enemyMark = changeEnemyMarkOnDirection(enemyDirection);
				int[] enemyCoordinates = makeMove(enemyDirection, level, enemyRow, enemyColumn);
				enemyRow = enemyCoordinates[0];
				enemyColumn = enemyCoordinates[1];
			}
			
			// Powerup frissítése
			if (powerUpActive) {
				powerUpActiveCounter++;
			} else {
				powerUpPresenceCounter++;
			}
			if (powerUpPresenceCounter >= 20) {
				if (powerUpPresentOnLevel) {
					powerUpStartCoordinates = getRandomStartingCoordinates(level);
					powerUpRow = powerUpStartCoordinates[0];
					powerUpColumn = powerUpStartCoordinates[1];
				}
				powerUpPresentOnLevel = !powerUpPresentOnLevel;
 				powerUpPresenceCounter = 0;
			}
			if (powerUpActiveCounter >= 20) {
				powerUpActive = false;
				powerUpActiveCounter = 0;
				powerUpStartCoordinates = getRandomStartingCoordinates(level);
				powerUpRow = powerUpStartCoordinates[0];
				powerUpColumn = powerUpStartCoordinates[1];
			}
			
			// játékos powerup interakció
			if (powerUpPresentOnLevel && playerRow == powerUpRow && playerColumn == powerUpColumn) {
				powerUpActive = true;
				powerUpPresentOnLevel = false;
				powerUpPresenceCounter = 0;
			}
			
			draw(level, playerMark, playerRow, playerColumn, enemyMark, enemyRow, enemyColumn, powerUpMark, powerUpRow, powerUpColumn, powerUpPresentOnLevel, powerUpActive);
			addSomeDelay(100L, iterationNumber);
			
			// Elkapja az ellenség a játékost
			if (playerRow == enemyRow && playerColumn == enemyColumn) {
				if (powerUpActive) {
					gameResult = GameResult.WIN;
				} else {
					gameResult = GameResult.LOSE;
				}
				break;
			}
		}
		switch (gameResult) {
		case WIN:
			System.out.println("Congratulation, YOU WON!");
			break;
		case LOSE:
			System.out.println("Sorry, You Lose!");
			break;
		case TIE:
			System.out.println("The game is draw");
			break;
		}
		
	}
	
	static Direction getEscapeDirection(String[][] level, int enemyRow, int enemyColumn, Direction directionTowardsPlayer) {
		Direction escapeDirection = getOppositeDirection(directionTowardsPlayer);
		switch(escapeDirection) {
		case UP: 
			if (level[enemyRow - 1][enemyColumn].equals(" ")) {
				return Direction.UP;
			} else if (level[enemyRow][enemyColumn - 1].equals(" ")) {
				return Direction.LEFT;
			} else if (level[enemyRow][enemyColumn + 1].equals(" ")) {
				return Direction.RIGHT;
			} else {
				return Direction.UP;
			}
		case DOWN: 
			if (level[enemyRow + 1][enemyColumn].equals(" ")) {
				return Direction.DOWN;
			} else if (level[enemyRow][enemyColumn - 1].equals(" ")) {
				return Direction.LEFT;
			} else if (level[enemyRow][enemyColumn + 1].equals(" ")) {
				return Direction.RIGHT;
			} else {
				return Direction.DOWN;
			}
		case RIGHT: 
			if (level[enemyRow][enemyColumn+1].equals(" ")) {
				return Direction.RIGHT;
			} else if (level[enemyRow - 1][enemyColumn].equals(" ")) {
				return Direction.UP;
			} else if (level[enemyRow + 1][enemyColumn].equals(" ")) {
				return Direction.DOWN;
			} else {
				return Direction.RIGHT;
			}
		case LEFT: 
			if (level[enemyRow][enemyColumn - 1].equals(" ")) {
				return Direction.LEFT;
			} else if (level[enemyRow - 1][enemyColumn].equals(" ")) {
				return Direction.UP;
			} else if (level[enemyRow + 1][enemyColumn].equals(" ")) {
				return Direction.DOWN;
			} else {
				return Direction.LEFT;
			}
		default: return escapeDirection;
		}
	}

	static Direction getOppositeDirection(Direction direction) {
		switch (direction) {
		case UP:
			return Direction.DOWN;
		case DOWN:
			return Direction.UP;
		case RIGHT:
			return Direction.LEFT;
		case LEFT:
			return Direction.RIGHT;
		default:
			return direction;
		}
	}

	static int[] getRandomStartingCoordinatesAtLeastCertainDistance(String[][] level, int[] playerStartCoordinates, int dist) {
		int playerStartingRow = playerStartCoordinates[0];
		int playerStartingColumn = playerStartCoordinates[1];
		int randomRow;
		int randomColumn;
		int counter = 0;
		do {	
			randomRow = random.nextInt(HEIGHT);
			randomColumn = random.nextInt(WIDTH);
		} while (counter++ < 1_000 && !level[randomRow][randomColumn].equals(" ") || calculateDistance(randomRow, randomColumn, playerStartingRow, playerStartingColumn) < dist);
		return new int[] {randomRow, randomColumn};
	}

	static int calculateDistance(int row1, int column1, int row2, int column2) {
		int rowDifference = Math.abs(row1-row2);
		int columnDifference = Math.abs(column1 - column2);
		return rowDifference + columnDifference;
	}

	static int[] getRandomStartingCoordinates(String[][] level) {
		int randomRow;
		int randomColumn;
		do {	
			randomRow = random.nextInt(HEIGHT);
			randomColumn = random.nextInt(WIDTH);
		} while (!level[randomRow][randomColumn].equals(" "));
		return new int[] {randomRow, randomColumn};
	}

	private static String changeEnemyMarkOnDirection(Direction dir) {
		switch (dir) {
		case RIGHT:
			return enemyMark = ">";
		case DOWN:
			return enemyMark = "V";
		case LEFT:
			return enemyMark = "<";
		case UP:
			return enemyMark = "A";
		}
		return enemyMark = "-";
	}

	static Direction changeDirectionTowards(String[][] level, Direction originalEnemyDirection, int enemyRow, int enemyColumn, int playerRow, int playerColumn) {
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
	
	static void addRandomWalls(String[][] level) {
		addRandomWalls(level, 5, 5);
	}

	static void addRandomWalls(String[][] level, int numOfHorWall, int numOfVertWall) {
		// TODO fal ne kerüljön a játékosra vagy az ellenfélre
		for (int i = 0; i < numOfHorWall; i++) {
			addHorizontalWall(level);
		}
		for (int i = 0; i < numOfVertWall; i++) {
			addVerticalWall(level);
		}
	}
	static void addVerticalWall(String[][] level) {
		int wallHeight = random.nextInt(HEIGHT - 3);
		int wallRow = random.nextInt(HEIGHT - 2 - wallHeight);
		int wallColumn = random.nextInt(WIDTH - 2) + 1;
		for (int i = 0; i < wallHeight; i++) { 
			level[wallRow + i][wallColumn] = "X";
		}
	}
	
	static void addHorizontalWall(String[][] level) {
		int wallWidth = random.nextInt((WIDTH - 3)); // 0 - 11
		int wallRow = random.nextInt((HEIGHT - 2)) + 1;
		int wallColumn = random.nextInt(WIDTH - 2 - wallWidth);
		for (int i = 0; i < wallWidth; i++) {
			level[wallRow][wallColumn + i] = "X";
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
					if ((row == 0 && column == 0) || (row == 0 && column == WIDTH - 1) || (row == HEIGHT -1 && column == 0) || (row == HEIGHT -1 && column == WIDTH -1)) {
						level[row][column] = "|";
					} else {
						if (row == 0 || row == HEIGHT-1) {
							level[row][column] = "X";
						} else {
							level[row][column] = "|";
						}
					}
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
	
	static void draw(String[][] board, String playerMark, int playerRow, int playerColumn, String enemyMark, int enemyRow, int enemyColumn, String poUpMark, int poUpRow, int poUpCol, boolean powerUpPresentOnLevel, boolean powerUpActive) {
		// pálya és játékos kirajzolása
		for (int row = 0; row < HEIGHT; row++) {
			for (int column = 0; column < WIDTH; column++) {
				if (row == playerRow && column == playerColumn) {
					System.out.print(playerMark);
				} else if (row == enemyRow && column == enemyColumn) {
					System.out.print(enemyMark);
				} else if (powerUpPresentOnLevel && row == poUpRow && column == poUpCol) {
					System.out.print(poUpMark);
				} else {
					System.out.print(board[row][column]);
				}
			}
			System.out.println();
		}
		if (powerUpActive) {
			System.out.println("Power UP is Active!");
		}
	}

}







