package com.sneha.jsudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Sudoku grid
 *
 */
public class Grid {

	private Cell[][] cells = new Cell[9][9];
	List<CellGroup> allGroups = new ArrayList<CellGroup>();

	/**
	 * seeds a grid with a start state. A start state is a '/' separated string of row values.
	 * each row value specifies the entries in the rows left to right. '-' denotes blank. 
	 * unspecified values are also blank.
	 * @param s
	 * @throws InvalidIndexException
	 * @throws InvalidValueException
	 */
	public Grid(String s) throws InvalidIndexException, InvalidValueException {
		_initialize();
		seedValues(s);
	}

	private void seedValues(String s) throws InvalidValueException,
			InvalidIndexException {
		String[] rows = s.split("/");
		for (int i = 0; i < rows.length; i++) {
			for (int j = 0; j < rows[i].length(); j++) {
				String item = rows[i].substring(j, j + 1);
				if (!item.equals("-")) {
					int k = Integer.parseInt(item);
					if (k < 1 || k > 9) {
						throw new InvalidValueException("Invalid value " + k);
					}
					checkIndexes(i, j);
					cells[i][j].setValue(k);
				}
			}
		}
	}

	// set up the cells and cell groups of the grid
	private void _initialize() {
		List<CellGroup> rowGroup = new ArrayList<CellGroup>();
		List<CellGroup> columnGroup = new ArrayList<CellGroup>();
		List<CellGroup> subgridGroup = new ArrayList<CellGroup>();

		for (int i = 0; i < 9; i++) {
			rowGroup.add(new CellGroup("row_" + i));
			columnGroup.add(new CellGroup("column_" + i));
			subgridGroup.add(new CellGroup("subgrid_" + i));
		}
		allGroups.addAll(rowGroup);
		allGroups.addAll(columnGroup);
		allGroups.addAll(subgridGroup);

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Collection<CellGroup> groups = new ArrayList<CellGroup>();
				groups.add(rowGroup.get(i));
				groups.add(columnGroup.get(j));
				int subGridIndex = (i / 3 * 3) + (j / 3);
				groups.add(subgridGroup.get(subGridIndex));
				cells[i][j] = new Cell(i, j, groups);
			}
		}
	}

	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("\n"); // space it out 
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int v = cells[i][j].getValue();
				b.append(v == Cell.MISSING_VALUE ? "_" : v);
				b.append(" ");
				if (j == 2 || j == 5) {
					b.append("|  ");
				}
			}
			for (int j = 0; j < 9; j++) {
				Cell c = cells[i][j];
				b.append("{");
				for (Integer p : c.getCandidates()) {
					b.append(p);
					b.append(",");
				}
				b.append("}");
			}
			b.append("\n");
			if (i == 2 || i == 5) {
				b.append("\n");
			}
		}
		return b.toString();
	}

	private void checkIndexes(int row, int column) throws InvalidIndexException {
		if (row < 0 || row > 8) {
			throw new InvalidIndexException("Row index is invalid " + row);
		}
		if (column < 0 || column > 8) {
			throw new InvalidIndexException("Column index is invalid " + column);
		}
	}

	/** indicates if a Grid is solved */
	public boolean solved() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (cells[i][j].isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * solves the grid if possible
	 * @throws InvalidValueException
	 */
	public void solve() throws InvalidValueException {
		boolean atleastOneCellSolved;
		do {
			boolean result1 = simpleGroupSolve();
			boolean result2 = simpleCellSolve();
			atleastOneCellSolved = result1 || result2;
			if (atleastOneCellSolved) {
				System.out.println(this);
			} else {
				System.out.println("Simple strategies made no further progress.");
			}
		} while (!this.solved() && atleastOneCellSolved);
		if (!this.solved()) {
			
			
			// candidate do something here to solve these grids.
			System.out.println("using backtracking strategy to solve it further");
		
			/* an array with all the values in the cell*/
			int[][] arr = new int[9][9];
			for(int i=0; i < 9; i++) {
				for(int j=0; j <9; j++) {
						arr[i][j] = cells[i][j].getValue();
				}
			}
			/* calling a bactracking solution */
			boolean result3 = backtrackSolve(0, arr);
			if(result3) {
				System.out.println(this);
			}
			else {
				System.out.println("Backtracking strategy did not give a solution");
			}
			
			}
			
		}
	/*Backtacking solution to solve the remaining cells in sudoku*/
	
	private boolean backtrackSolve(int pos, int[][] bcells) throws InvalidValueException {
		if(pos == -1) return true;
		if(pos == -2) return false;
		
		int j = pos & 15;
		int i = pos >> 4;
		
				if(bcells[i][j] != 0)
					return backtrackSolve(nextPosition(pos), bcells);
				
	    Set<Integer> hs_candidates = new HashSet<Integer>();
	    hs_candidates  = cells[i][j].getCandidates();
	    Iterator<Integer>  itr_candidates = hs_candidates.iterator();
	    //iterate through possible cell values using the candidates computed previously
		while(itr_candidates.hasNext()) {
			int val = itr_candidates.next();
			if(isValid(i,j,val,bcells)) {
				bcells[i][j] = val;
				if (backtrackSolve(nextPosition(pos), bcells)) {
					/*Final step to save the values computed to the Cells and returns true*/
					for(int x=0; x < 9; x++) {
						for(int y=0; y < 9; y++) {
					     if(cells[x][y].getValue() == 0) {
					    	 Cell c = cells[x][y];
					    	 c.setValue(bcells[x][y]);
					    	 c.candidates.clear();
				    	     }
						}
					}
			    	return true;
				}
			}
			
		}
		bcells[i][j] = 0;
		return false;
	}
	
	static boolean isValid(int i, int j, int val, int[][] cells) {
		/* checking if val is valid at cells[i][j]*/
	        for (int k = 0; k < 9; ++k)  // check row
	            if (val == cells[k][j])
	                return false;

	        for (int k = 0; k < 9; ++k) // check col
	            if (val == cells[i][k])
	                return false;

	        int boxRowOffset = (i / 3)*3;
	        int boxColOffset = (j / 3)*3;
	        for (int k = 0; k < 3; ++k) //check  grid
	            for (int m = 0; m < 3; ++m)
	                if (val == cells[boxRowOffset+k][boxColOffset+m])
	                    return false;

	        return true; // no violations, so it's valid
	    }

	
	static int nextPosition(int p) {
		// position p is an integer: -1 means no more positions, otherwise, it stores 
		//   both the row and column of a position:
		//   the lowest 4 bits are the column number and the next 4 bits are row number
		int j = p & 15;          // j is the column number stored at the lowest 4 bits of p
		if (j < 8) return p+1;   // increase j by one
		int i = p >> 4;          // i is the row number stored at the next 4 bits of p
		if (i == 8) return -1;   // no more position
		return (i+1)<< 4;        // increase i by 1 and set j = 0
	    }

	/* if a cell has only one candidate value, set that value. */
	private boolean simpleCellSolve() throws InvalidValueException {
		boolean atleastOneCellSolved = false;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Cell c = cells[i][j];
				if (c.isEmpty() && (c.getCandidates().size() == 1)) {
					int v = c.getCandidates().iterator().next();
					c.setValue(v);
					atleastOneCellSolved = true;
				}
			}
		}
		return atleastOneCellSolved;
	}

	/* if a group has only one cell that could take a value, give the cell that value. */
	private boolean simpleGroupSolve() throws InvalidValueException {
		boolean atleastOneCellSolved = false;
		for (CellGroup cg : allGroups) {
			if (cg.solveGroup()) {
				atleastOneCellSolved = true;
			}
		}
		return atleastOneCellSolved;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(cells);
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
		Grid other = (Grid) obj;
		if (!Arrays.deepEquals(cells, other.cells))
			return false;
		return true;
	}

}
