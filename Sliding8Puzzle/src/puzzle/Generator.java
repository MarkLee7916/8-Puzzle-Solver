package puzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {
	private final int size;

	public Generator(int s) {
		size = s;
	}

	public Board generateBoard() {
		if (size % 2 != 0)
			return generateUsingInversionCount();
		else
			return generateByShuffling();
	}

	private Board generateByShuffling() {
		if (size <= 1)
			throw new IllegalArgumentException("Board is of an invalid size");

		Board start = new Board(getGoalBoard());
		List<Board> neighbours = null;

		for (int i = 0; i < Math.pow(size, size); i++) {
			neighbours = start.neighbours();
			start = neighbours.get(rng(neighbours.size()));
		}

		return start;
	}

	private static int rng(int limit) {
		return ThreadLocalRandom.current().nextInt(limit);
	}

	private int[][] getGoalBoard() {
		int[][] puzzle = new int[size][size];

		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				puzzle[i][j] = getPositionFromCoords(i, j);

		return puzzle;
	}

	private Board generateUsingInversionCount() {
		if (size <= 1)
			throw new IllegalArgumentException("Board is of an invalid size");

		int[][] board = new int[size][size];
		List<Integer> shuffledList = new ArrayList<>();
		fillUpUntil(shuffledList, size * size);

		do {
			Collections.shuffle(shuffledList);

			for (int i = 0; i < size; i++)
				for (int j = 0; j < size; j++)
					board[i][j] = shuffledList.get(getPositionFromCoords(i, j));

		} while (getInversionCountForBoard(board) % 2 != 0);

		return new Board(board);
	}

	// Returns number of instances where a pair is in the wrong order
	private int getInversionCountForBoard(int[][] board) {
		int count = 0;

		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				if (board[i][j] != 0)
					count += getInversionCountForTile(i, j, board);

		return count;
	}

	private int getInversionCountForTile(int row, int column, int[][] board) {
		int count = 0;
		int limit = getPositionFromCoords(row, column);

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (getPositionFromCoords(i, j) >= limit)
					return count;

				if (board[i][j] > board[row][column])
					count++;
			}
		}

		throw new IllegalArgumentException("Arguments for row and column are off the board");
	}

	// Returns position in board (i.e 0,0 is the first tile)
	private int getPositionFromCoords(int i, int j) {
		return i * size + j;
	}

	// Fills arraylist with numbers 0.. limit - 1
	private void fillUpUntil(List<Integer> shuffledList, int limit) {
		for (int i = 0; i < limit; i++)
			shuffledList.add(i);
	}
}
