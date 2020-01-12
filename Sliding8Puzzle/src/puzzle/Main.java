package puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
	private Main() {
		throw new AssertionError("Can't instantiate this class");
	}

	public static void main(String[] args) {
		// The argument to generateBoard() decides the size of the puzzle
		Board start = generateBoard(3);

		System.out.println("Puzzle to solve is " + start);

		Solver solve = new Solver(start);
		long startTime = System.currentTimeMillis();
		solve.run();
		System.out.println("This board was solved in " + (System.currentTimeMillis() - startTime) + " ms");
	}

	// Takes a solved board and use a rng to shuffle it, generating a new one
	private static Board generateBoard(int size) {
		if (size <= 1)
			throw new IllegalArgumentException("Board is of an invalid size");		
		
		int[][] puzzle = new int[size][size];

		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				puzzle[i][j] = i * size + j;

		Board start = new Board(puzzle, size);
		List<Board> a = null;

		for (int i = 0; i < 100000; i++) {
			a = start.neighbours();
			start = a.get(ThreadLocalRandom.current().nextInt(a.size()));
		}
		return start;
	}

	// Alternative method of generating the puzzle. Tends to generate harder boards to solve
	@SuppressWarnings("unused")
	private static Board generatePuzzle(int size) {
		if (size <= 1)
			throw new IllegalArgumentException("Board is of an invalid size");		
		
		int[][] puzzle = new int[size][size];
		List<Integer> l = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 0));
		
		if (size <= 1)
			throw new IllegalArgumentException("Board is of an invalid size");

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				puzzle[i][j] = l.get(ThreadLocalRandom.current().nextInt(l.size()));
				l.remove(Integer.valueOf(puzzle[i][j]));
			}
		}
		return new Board(puzzle, size);
	}
}
