package representations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import utils.MatrixBank;

/*
 * Wrapper object for elementary matricies. They have limited functionality
 * as they are only intended for finding inverses (Should only really be multiplied)
 */
public class ElementaryMatrix implements Matrix {

	List<MyVector> rowVectors;
	int dimension;

	public ElementaryMatrix(int dimension) {
		this.dimension = dimension;
		this.rowVectors = MatrixBank.identityVectors(dimension);
	}

	/* ============================= Unique methods ============================= */
	public ElementaryMatrix swapRows(int firstRowIndex, int secondRowIndex) {
		if (firstRowIndex == secondRowIndex)
			throw new IllegalArgumentException("Cannot swap the same row");

		if (firstRowIndex < 0 || firstRowIndex >= dimension || secondRowIndex < 0 || secondRowIndex >= dimension)
			throw new IllegalArgumentException("Rows do not exist");

		Collections.swap(rowVectors, firstRowIndex, secondRowIndex);
		return this;
	}

	public ElementaryMatrix scaleRow(int rowIndex, double c) {
		if (rowIndex < 0 || rowIndex >= dimension)
			throw new IllegalArgumentException("Row does not exist");

		rowVectors.get(rowIndex).scale(c);
		return this;
	}

	public ElementaryMatrix subtractRow(int targetIndex, int subtractorIndex) {
		if (subtractorIndex == targetIndex)
			throw new IllegalArgumentException("Cannot subtract the same row");

		if (subtractorIndex < 0 || subtractorIndex >= dimension || targetIndex < 0 || targetIndex >= dimension)
			throw new IllegalArgumentException("Rows do not exist");

		rowVectors.get(targetIndex).subtract(rowVectors.get(subtractorIndex));
		return this;
	}

	public ElementaryMatrix subtractScaledRow(int targetIndex, int subtractorIndex, double scalar) {
		rowVectors.get(targetIndex).subtract(rowVectors.get(subtractorIndex).scaled(scalar));
		return this;
	}

	/* ============================ Standard methods ============================ */

	@Override
	public void scale(double c) {
		for (MyVector vector : rowVectors)
			vector.scale(c);
	}

	@Override
	public int rows() {
		return rowVectors.size();
	}

	@Override
	public int columns() {
		return rowVectors.get(0).getSize();
	}

	@Override
	public List<MyVector> getRowVectors() {
		return List.copyOf(rowVectors);
	}

	@Override
	public List<MyVector> getColumnVectors() {
		return new ArrayList<>(IntStream.range(0, dimension)
				.mapToObj(i -> new MyVector(rowVectors.stream().mapToDouble(row -> row.get(i)).toArray()))
				.toList());
	}

	@Override
	public String toString() {
		return new OriginalMatrix(rowVectors).toString();
	}

	/* ========================== Not supported methods ========================= */
	@Override
	public double get(int row, int column) {
		throw new UnsupportedOperationException("Can't get elements from elementary matricies");
	}

	@Override
	public void set(int row, int column, double value) {
		throw new UnsupportedOperationException("Can't set elements in elementary matricies");

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
