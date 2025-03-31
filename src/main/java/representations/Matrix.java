package representations;

import java.util.List;

public interface Matrix {

	/* ============================== Util methods ============================== */
	public static boolean isIdentityMatrix(Matrix m) {
		if (m.rows() != m.columns())
			return false;

		for (int i = 0; i < m.rows(); i++) {
			for (int j = 0; j < m.columns(); j++) {

				if (i == j && Math.abs(m.get(i, j) - 1) > 1e-5)
					// This element should be 1. We allow for some floating point error
					return false;

				if (i != j && Math.abs(m.get(i, j)) > 1e-5)
					// This element should be 0. We allow for some floating point error
					return false;
			}
		}
		return true;
	}

	public MyVector multiply(MyVector vector);

	public Matrix transposed();

	public void set(int row, int column, double value);

	public void scale(double c);

	public void add(Matrix m);

	public void subtract(Matrix m);

	public int rows();

	public int columns();

	public double get(int row, int column);

	public List<MyVector> getRowVectors();

	public List<MyVector> getColumnVectors();

}
