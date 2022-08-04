
public class BasicGame {

	public static void main(String[] args) throws InterruptedException {
		String[][] level = new String[10][10];
		String playerMark = "O";
		int row = 2;
		int column = 2;
		
		Direction direction = Direction.RIGHT;
		
		// pálya inicializálása
		for (int i = 0; i < level.length; i++) {
			for (int j = 0; j < level[i].length; j++) {
				if (i == 0 || i == 9 || j == 0 || j == 9) {
					level[i][j] = "X";
				} else {
					level[i][j] = " ";
				}
			}
		}
		
		for (int k=1; k <= 100; k++) {
			
			if (k % 10 == 0) {
				direction = changeDirection(direction);
			}
			
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
			
			
			draw(level, playerMark, row, column);
			
			System.out.println("----------");
			Thread.sleep(500L);
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







