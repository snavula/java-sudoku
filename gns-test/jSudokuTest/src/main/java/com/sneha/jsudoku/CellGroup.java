package com.sneha.jsudoku;

import java.util.HashSet;
import java.util.Set;

/**
 * encapsulates the row, column and sub-grid groups.
 * a Sudoku grid has 9 row groups, 9 column groups and 9 sub-grid groups.
 * each group has 9 cells. each cell belongs to 3 groups.
 * @author sneha
 *
 */
public class CellGroup {

	private String id;
	private Set<Cell> cells = new HashSet<Cell>();

	public CellGroup(String id) {
		this.id = id;
	}

	public void addCell(Cell c) {
		cells.add(c);
	}

	/**
	 * notified when a cell gets a value. Unassigned cells will then remove 
	 * that value as a candidate.
	 * @param source
	 */
	public void onCellValue(Cell source) {
		if (!cells.contains(source)) {
			throw new IllegalArgumentException("Got a cell that doesn't belong");
		}
		for (Cell c : cells) {
			c.removeCandidate(source.getValue());
		}
	}

	/**
	 * if only one cell in the group can get a value, assign the cell that value.
	 * @return
	 * @throws InvalidValueException
	 */
	public boolean solveGroup() throws InvalidValueException {
		boolean changed;
		boolean atleastOnce = false;
		do {
			changed = false;
			for (int i = 1; i < 10; i++) {
				Set<Cell> potentials = new HashSet<Cell>();
				for (Cell c : cells) {
					if (c.getCandidates().contains(i)) {
						potentials.add(c);
					}
				}
				if (potentials.size() == 1) {
					Cell candidate = potentials.iterator().next();
					System.out.println(id + ": [" + candidate.getRow() + "," + candidate.getColumn() + "] = " + i);
					candidate.setValue(i);
					changed = true;
					atleastOnce = true;
				}
			}
		} while (changed);
		return atleastOnce;

	}
}
