package com.sneha.jsudoku;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Cell in a Sudoku grid. knows the groups (row group, column group and sub-grid group)
 * that it belongs to. keeps track of the value assigned or candidate values if blank.
 *
 */
public class Cell {

	private int row;
	private int column;
	private int value = MISSING_VALUE;
	Set<Integer> candidates;
	private Collection<CellGroup> groups;

	public static final int MISSING_VALUE = 0;

	public Cell(int row, int column, Collection<CellGroup> groups) {
		this.row = row;
		this.column = column;
		candidates = new HashSet<Integer>();
		for (int i = 1; i < 10; i++) {
			candidates.add(i);
		}
		this.groups = groups;
		for (CellGroup g : groups) {
			g.addCell(this);
		}
	}

	public int getValue() {
		return value;
	}

	public void removeCandidate(int v) {
		candidates.remove(v);
	}

	/**
	 * sets the cell to value specified. notifies the cell groups that this
	 * cell belongs to.
	 * @param v
	 * @throws InvalidValueException
	 */
	public void setValue(int v) throws InvalidValueException {
		if (v != MISSING_VALUE && v > 0 && v < 10) {
			if (!candidates.contains(v)) {
				throw new InvalidValueException(
						"cannot set a value that is not part of the candidates list.");
			}
			value = v;
			candidates.clear();
			for (CellGroup g : groups) {
				g.onCellValue(this);
			}
		}
	}

	public Set<Integer> getCandidates() {
		return candidates;
	}
	
	public boolean isEmpty() {
		return value == MISSING_VALUE;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cell other = (Cell) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		if (value != other.value)
			return false;
		return true;
	}

	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}

}
