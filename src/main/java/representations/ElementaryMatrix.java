package representations;

import java.util.List;
import java.util.stream.IntStream;

/*
 * Wrapper object for elementary matricies. They have limited functionality
 * as they are only intended for finding inverses (Should only really be multiplied)
 * 
 * This class is quite inefficient as it creates new objects quite often
 * Have not bothered fixing yet
 */
public class ElementaryMatrix implements Matrix {

	Matrix matrix;
	int dimension;

	public ElementaryMatrix(int dimension) {
		this.dimension = dimension;
		this.matrix = new OriginalMatrix(dimension, dimension);
		IntStream.range(0, dimension).forEach(i -> matrix.set(i, i, 1));
	}

	public ElementaryMatrix(Matrix matrix) {
		if (matrix.rows() != matrix.columns())
			throw new IllegalArgumentException("Elementary matricies have to be square");

		this.matrix = matrix;
		this.dimension = matrix.rows();
	}

	/* ============================= Unique methods ============================= */
	public ElementaryMatrix swapRows(int firstRowIndex, int secondRowIndex) {
		if (firstRowIndex == secondRowIndex)
			throw new IllegalArgumentException("Cannot swap the same row");

		if (firstRowIndex < 0 || firstRowIndex >= dimension || secondRowIndex < 0 || secondRowIndex >= dimension)
			throw new IllegalArgumentException("Rows do not exist");

		List<MyVector> rows = matrix.getRowVectors();
		MyVector memoryRow = rows.get(firstRowIndex);
		rows.set(firstRowIndex, rows.get(secondRowIndex));
		rows.set(secondRowIndex, memoryRow);
		this.matrix = new OriginalMatrix(rows);

		return this;
	}

	public ElementaryMatrix scaleRow(int rowIndex, double c) {
		if (rowIndex < 0 || rowIndex >= dimension)
			throw new IllegalArgumentException("Row does not exist");

		List<MyVector> rows = matrix.getRowVectors();
		rows.get(rowIndex).scale(c);
		this.matrix = new OriginalMatrix(rows);

		return this;
	}

	public ElementaryMatrix subtractRow(int targetIndex, int subtractorIndex) {
		if (subtractorIndex == targetIndex)
			throw new IllegalArgumentException("Cannot subtract the same row");

		if (subtractorIndex < 0 || subtractorIndex >= dimension || targetIndex < 0 || targetIndex >= dimension)
			throw new IllegalArgumentException("Rows do not exist");

		List<MyVector> rows = matrix.getRowVectors();
		rows.get(targetIndex).subtract(rows.get(subtractorIndex));
		this.matrix = new OriginalMatrix(rows);
		return this;
	}

	/* ============================ Standard methods ============================ */

	@Override
	public void set(int row, int column, double value) {
		matrix.set(row, column, value);
	}

	@Override
	public void scale(double c) {
		matrix.scale(c);
	}

	@Override
	public int rows() {
		return matrix.rows();
	}

	@Override
	public int columns() {
		return matrix.columns();
	}

	@Override
	public List<MyVector> getRowVectors() {
		return matrix.getRowVectors();
	}

	@Override
	public List<MyVector> getColumnVectors() {
		return matrix.getColumnVectors();
	}

	@Override
	public String toString() {
		return matrix.toString();
	}

	/* ========================== Not supported methods ========================= */
	@Override
	public double get(int row, int column) {
		throw new UnsupportedOperationException("Can't get elements from elementary matricies");
	}

	@Override
	public void add(Matrix m) {
		throw new UnsupportedOperationException("Elementary matricies can't add");

	}

	@Override
	public void subtract(Matrix m) {
		throw new UnsupportedOperationException("Elementary matricies can't subtract");

	}

	@Override
	public Matrix transpose() {
		throw new UnsupportedOperationException("Elementary matricies can't transpose");
	}

}
