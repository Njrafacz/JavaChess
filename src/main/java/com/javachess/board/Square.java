package com.javachess.board;

public class Square {
	private final int column;
	private final int row;
		
	public Square(int column, int row) {
		this.column = column;
		this.row = row;
	}
	
	public boolean isValid() {
		return column > 0 && column < 9 && row > 0 && row < 9;
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
		Square other = (Square) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

	public int getColumn() {
		return column;
	}
	
	public int getRow() {
		return row;
	}
}