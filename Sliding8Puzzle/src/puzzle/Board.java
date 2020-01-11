package puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board implements Comparable<Board> {
	private final int[][] tiles;
	private int hamming, manhattan, distance = Integer.MAX_VALUE;
	private Board pointer;
	private int size;

	public Board(int[][] t, int s) {
		tiles = t;
		size = s;
		computeHamming();
	}

	@Override
	public boolean equals(Object obj) {
		Board board = (Board) obj;
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				if (this.tiles[i][j] != board.tiles[i][j])
					return false;

		return true;
	}

	@Override
	public int hashCode() {
		int ret = 0;
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				ret += Math.pow(tiles[i][j], i * size + j);

		return ret;
	}

	@SuppressWarnings("unused")
	private void printBoard() {
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
		return hamming == 0;
	}

	// Returns a list of neighbours of this board
	public List<Board> neighbours() {
		List<Board> neighbours = new ArrayList<>();
		// Integer representation of the co-ords of hole
		int zeroPos = findHole();
		// Gets x co-ords from zeroPos
		int zeroPosI = zeroPos / size;
		// Gets y co-ords from zeroPos
		int zeroPosJ = zeroPos % size;

		// Attempts to swap the hole with all of its adjacent tiles
		swap(zeroPosI + 1, zeroPosJ, zeroPosI, zeroPosJ, neighbours);
		swap(zeroPosI - 1, zeroPosJ, zeroPosI, zeroPosJ, neighbours);
		swap(zeroPosI, zeroPosJ + 1, zeroPosI, zeroPosJ, neighbours);
		swap(zeroPosI, zeroPosJ - 1, zeroPosI, zeroPosJ, neighbours);

		return neighbours;
	}

	// Verifies whether a holes adjacent is on board and swaps, adding to list
	private void swap(int i, int j, int o, int p, List<Board> l) {
		if (i >= 0 && i < size && j >= 0 && j < size) {
			int[][] placehold = copy(tiles);
			int temp = placehold[i][j];
			placehold[i][j] = placehold[o][p];
			placehold[o][p] = temp;
			l.add(new Board(placehold, size));
		}
	}

	// Returns a deep copy of a 2d array
	private int[][] copy(int[][] a) {
		int[][] copy = new int[size][size];

		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				copy[i][j] = a[i][j];

		return copy;
	}

	// Iterates through tiles and returns the position of the hole
	private int findHole() {
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				if (tiles[i][j] == 0)
					return i * size + j;

		throw new AssertionError("Board doesn't seem to have a hole in it");
	}

	// Calculates heuristic hamming distance. The hamming distance is the total
	// number of pieces that are in the wrong position
	private void computeHamming() {
		int h = 0;
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				if (tiles[i][j] != i * size + j)
					h++;

		hamming = h;
	}

	// Calculates heuristic manhattan distance. The manhattan distance is the total
	// distance that each piece put together is out of position
	@SuppressWarnings("unused")
	private void computeManhattan() {
		int m = 0;
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				m += Math.abs((i - tiles[i][j] / size) + (j - tiles[i][j] % size));

		manhattan = m;
	}

	@Override
	public int compareTo(Board b) {
		return (hamming + distance) - (b.hamming + b.distance);
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
