package representations;

import java.util.List;

public class TransposedMatrix implements Matrix {

	Matrix originalMatrix;

	/* ============================== Constructors ============================== */
	TransposedMatrix(Matrix originalMatrix) {
		this.originalMatrix = originalMatrix;
	}

	/* ================================= Methods ================================ */
	@Override
	public Matrix transpose() {
		return originalMatrix;
	}

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

	/* ================================== Other ================================= */

	@Override
	public String toString() {
		return originalMatrix.toString() + "T";
	}

}
