package representations;

import java.util.List;

import functionality.VectorSpace;

/**
 * This class provides a transposed view of an underlying matrix
 * This is not a deep copy, so there are side effects to modifying either object
 */
public class TransposedMatrix implements Matrix {

	Matrix originalMatrix;

	/* ============================== Constructors ============================== */
	TransposedMatrix(Matrix originalMatrix) {
		this.originalMatrix = originalMatrix;
	}

	/* ================================= Methods ================================ */

	@Override
	public void scale(double c) {
		originalMatrix.scale(c);
	}

	@Override
	public void add(Matrix m) {
		if (!(originalMatrix.rows() == m.columns() && originalMatrix.columns() == m.rows()))
			throw new IllegalArgumentException("Can not subtract matricies of different dimensions");

		originalMatrix.add(new TransposedMatrix(m));
	}

	@Override
	public void subtract(Matrix m) {
		if (!(originalMatrix.rows() == m.columns() && originalMatrix.columns() == m.rows()))
			throw new IllegalArgumentException("Can not subtractmatricies of different dimensions");
	}

	@Override
	public Matrix transposed() {
		return originalMatrix;
	}

	/* ================================= Getters ================================ */
	@Override
	public int rows() {
		return originalMatrix.columns();
	}

	@Override
	public void set(int row, int column, double value) {
		originalMatrix.set(column, row, value);
	}

	@Override
	public int columns() {
		return originalMatrix.rows();
	}

	@Override
	public double get(int row, int column) {
		return originalMatrix.get(column, row);
	}

	@Override
	public List<MyVector> getRowVectors() {
		return originalMatrix.getColumnVectors();
	}

	@Override
	public List<MyVector> getColumnVectors() {
		return originalMatrix.getRowVectors();
	}

	/* ============================ Subspace getters ============================ */
	@Override
	public VectorSpace col() {
		return originalMatrix.row();
	}

	@Override
	public VectorSpace row() {
		return originalMatrix.col();
	}

	@Override
	public VectorSpace ker() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'ker'");
	}

	@Override
	public VectorSpace coker() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'coker'");
	}

	/* ================================== Other ================================= */

	@Override
	public String toString() {
		return new OriginalMatrix(originalMatrix.getColumnVectors()).toString();
	}

	@Override
	public MyVector multiply(MyVector vector) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'multiply'");
	}

}
