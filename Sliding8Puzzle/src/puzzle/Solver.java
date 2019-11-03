package puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Solver {
	private Board start, currentBoard;
	private final List<Board> boards, closedBoards;

	public Solver(Board s) {
		start = s;
		boards = new ArrayList<>(Arrays.asList(start));
		// Keeps track of boards we have already dealt with
		closedBoards = new ArrayList<>();
		start.initDistance();
	}

	// Uses A* pathfinding to solve puzzle, where a node is represented by a board
	// and each edge is equal to one. A board is adjacent to another if it can be
	// moved to that position in one turn. This is called a 'neighbour'
	public void run() {
		while (!boards.isEmpty()) {
			// Returns the smallest board in the queue i.e the one with the lowest g value
			currentBoard = Collections.min(boards);

			// Iterates through the boards adjacents
			for (Board b : currentBoard.neighbours()) {

				// Since each edge is equal to one we never have to visit the same board twice
				if (boards.contains(b) || closedBoards.contains(b))
					continue;

				boards.add(b);
				b.incrementDistance();
				b.setPointer(currentBoard);

				// We found the path!
				if (currentBoard.isGoal()) {
					System.out.println(stringPath());
					return;
				}
			}
			boards.remove(currentBoard);
			closedBoards.add(currentBoard);
		}
		throw new AssertionError("Puzzle hasn't been solved");
	}

	// Follows the chain of pointers to get the steps that were taken to solve the
	// puzzle
	private String stringPath() {
		Board b = currentBoard;
		StringBuilder build = new StringBuilder("");
		build.insert(0, currentBoard);

		while ((b = b.getPointer()) != null) 
			build.insert(0, b + " -> ");		

		return build.toString();
	}
}
