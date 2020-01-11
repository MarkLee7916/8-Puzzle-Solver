package puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Board implements Comparable<Board> {
	private final int[][] tiles;
	private int hamming, manhattan, distance = Integer.MAX_VALUE;
	private Board pointer;

	public Board(int[][] t) {
		tiles = t;
		computeManhattan();
	}

	@Override
	public boolean equals(Object obj) {
		Board board = (Board) obj;
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (this.tiles[i][j] != board.tiles[i][j])
					return false;

		return true;
	}

	@Override
	public int hashCode() {
		int ret = 0;
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				ret += Math.pow(tiles[i][j], i * 3 + j);

		return ret;
	}

	public void printBoard() {
		for (int[] a : tiles)
			System.out.println(Arrays.toString(a));
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("");
		for (int[] a : tiles)
			str.append(Arrays.toString(a));

		return str.toString();
	}

	// Returns true is this is a solved board
	public boolean isGoal() {
		return manhattan == 0;
	}

	// Returns a list of neighbours of this board
	public List<Board> neighbours() {
		List<Board> neighbours = new ArrayList<>();
		// Integer representation of the co-ords of hole
		int zeroPos = findHole();
		// Gets x co-ords from zeroPos
		int zeroPosI = zeroPos / 3;
		// Gets y co-ords from zeroPos
		int zeroPosJ = zeroPos % 3;

		// Attempts to swap the hole with all of its adjacent tiles
		swap(zeroPosI + 1, zeroPosJ, zeroPosI, zeroPosJ, neighbours);
		swap(zeroPosI - 1, zeroPosJ, zeroPosI, zeroPosJ, neighbours);
		swap(zeroPosI, zeroPosJ + 1, zeroPosI, zeroPosJ, neighbours);
		swap(zeroPosI, zeroPosJ - 1, zeroPosI, zeroPosJ, neighbours);

		return neighbours;
	}

	// Verifies whether a holes adjacent is on board and swaps, adding to list
	private void swap(int i, int j, int o, int p, List<Board> l) {
		if (i >= 0 && i < 3 && j >= 0 && j < 3) {
			int[][] placehold = copy(tiles);
			int temp = placehold[i][j];
			placehold[i][j] = placehold[o][p];
			placehold[o][p] = temp;
			l.add(new Board(placehold));
		}
	}

	// Returns a deep copy of a 2d array
	private static int[][] copy(int[][] a) {
		return java.util.Arrays.stream(a).map(el -> el.clone()).toArray($ -> a.clone());
	}

	// Iterates through tiles and returns the position of the hole
	private int findHole() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (tiles[i][j] == 0)
					return i * 3 + j;

		throw new AssertionError("Board doesn't seem to have a hole in it");
	}

	// Calculates heuristic hamming distance. The hamming distance is the total
	// number of pieces that are in the wrong position
	@SuppressWarnings("unused")
	private void computeHamming() {
		int h = 0;
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (tiles[i][j] != i * 3 + j)
					h++;

		hamming = h;
	}

	// Calculates heuristic manhattan distance. The manhattan distance is the total
	// distance that each piece put together is out of position
	private void computeManhattan() {
		int m = 0;
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				m += Math.abs((i - tiles[i][j] / 3) + (j - tiles[i][j] % 3));

		manhattan = m;
	}

	@Override
	public int compareTo(Board b) {
		return (manhattan + distance) - (b.manhattan + b.distance);
	}

	public void incrementDistance() {
		distance++;
	}

	public int getDistance() {
		return distance;
	}

	public void setPointer(Board p) {
		pointer = p;
	}

	// Used for the starting board to initialise distance to 0
	public void initDistance() {
		distance = 0;
	}

	public Board getPointer() {
		return pointer;
	}
}
