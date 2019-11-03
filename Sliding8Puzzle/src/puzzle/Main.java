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
		Board start = generateBoard();
		System.out.println("Puzzle to solve is " + start);

		Solver solve = new Solver(start);
		long startTime = System.currentTimeMillis();
		solve.run();
		System.out.println("This board was solved in " + (System.currentTimeMillis() - startTime) + " ms");
	}

	//Takes a solved board and use a rng to shuffle it, generating a new one
	private static Board generateBoard() {
		int[][] puzzle = { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 } };
		Board start = new Board(puzzle);
		List<Board> a = null;

		for (int i = 0; i < 100000; i++) {
			a = start.neighbours();
		    start = a.get(ThreadLocalRandom.current().nextInt(a.size()));
		}
		return start;
	}
	
	
	@SuppressWarnings("unused")
	private static int[][] generatePuzzle() {
		int[][] puzzle = new int[3][3];
		List<Integer> l = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 0));

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				puzzle[i][j] = l.get(ThreadLocalRandom.current().nextInt(l.size()));
				l.remove(Integer.valueOf(puzzle[i][j]));
			}
		}
		return puzzle;
	}
	
	@SuppressWarnings("unused")
	private static boolean isSolvable(int[][] a) {
		int inversions = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				inversions += getPrecedes(i, j, a);
			}
		}
		return !(inversions % 2 == 0);
	}

	@SuppressWarnings("unused")
	private static int getPrecedes(int x, int y, int[][] a) {
		int precedes = 0;
		for (int i = x; i < 3; i++) {
			for (int j = y; j < 3; j++) {
				if (a[i][j] < a[x][y])
					precedes++;
			}
		}
		return precedes;
	}
}
