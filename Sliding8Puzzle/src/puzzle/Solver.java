package puzzle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Solver {
	private final Board start;
	private Board currentBoard;
	private final Queue<Board> boards;
	private final Set<Board> visited;

	public Solver(Board s) {
		start = s;
		boards = new PriorityQueue<>(Arrays.asList(start));
		visited = new HashSet<>(Arrays.asList(start));
	}

	// Uses A* pathfinding to solve puzzle
	public void run() {
		while (!boards.isEmpty()) {
			// Returns the smallest board in the queue i.e the one with the lowest g value
			currentBoard = boards.poll();

			// Iterates through the boards adjacents
			for (Board b : currentBoard.neighbours()) {
				// Since each edge is equal to one we never have to visit the same board twice
				if (!visited.contains(b)) {
					boards.add(b);
					b.setPointer(currentBoard);
					visited.add(b);
				}
			}

			// We found the path!
			if (currentBoard.isGoal()) {
				System.out.println(stringPath());
				return;
			}
		}
		throw new AssertionError("Puzzle hasn't been solved");
	}

	// Follows the chain of pointers to get the steps that were taken to solve the puzzle
	private String stringPath() {
		Board b = currentBoard;
		StringBuilder build = new StringBuilder("");
		build.insert(0, currentBoard);

		while ((b = b.getPointer()) != null)
			build.insert(0, b + " -> ");

		return build.toString();
	}
}
