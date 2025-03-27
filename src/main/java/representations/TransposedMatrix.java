package representations;

public class TransposedMatrix implements Matrix {

	Matrix originalMatrix;

	TransposedMatrix(Matrix originalMatrix) {
		this.originalMatrix = originalMatrix;
	}

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
			throw new IllegalArgumentException("Can not subtractmatricies of different dimensions");

		originalMatrix.add(new TransposedMatrix(m));
	}

	@Override
	public void subtract(Matrix m) {
		if (!(originalMatrix.rows() == m.columns() && originalMatrix.columns() == m.rows()))
			throw new IllegalArgumentException("Can not subtractmatricies of different dimensions");
	}

	@Override
	public int rows() {
		return originalMatrix.columns();
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
	public String toString() {
		return originalMatrix.toString() + "T";
	}
}
