package calculation;

import java.util.List;

import representations.Matrix;
import representations.MyVector;
import representations.OriginalMatrix;

public class MatrixCalculator {

	public Matrix multiply(Matrix a, Matrix b) {
		// Check for compatible dimensions
		if (a.columns() != b.rows())
			throw new IllegalArgumentException("Incompatible dimensions");

		Matrix result = new OriginalMatrix(a.rows(), b.columns());
		List<MyVector> aRows = a.getRowVectors();
		List<MyVector> bColumns = b.getColumnVectors();

		for (int rowIndex = 0; rowIndex < a.rows(); rowIndex++) {
			MyVector row = aRows.get(rowIndex);
			for (int columnIndex = 0; columnIndex < b.columns(); columnIndex++) {
				MyVector column = bColumns.get(columnIndex);
				double value = 0;

				assert row.getSize() == column.getSize() : "Rows and columns were of different length";
				for (int elementIndex = 0; elementIndex < row.getSize(); elementIndex++) {
					value += row.get(elementIndex) * column.get(elementIndex);
				}

				result.set(rowIndex, columnIndex, value);
			}
		}
		return result;
	}

	public double determinant(Matrix m) {
		if (m.columns() != m.rows())
			throw new IllegalArgumentException("Can only compute determinants of square matricies");
		int dimension = m.rows();

		if (dimension == 2)
			return m.get(0, 0) * m.get(1, 1) - m.get(0, 1) * m.get(1, 0);

		int multiplier = 1;
		double determinant = 0;
		for (int i = 0; i < dimension; i++) {
			determinant += multiplier * m.get(0, i) * determinant(getSubmatrix(0, i, m));
			multiplier *= -1;
		}
		return determinant;
	}

	/* ================================= Helpers ================================ */
	public Matrix getSubmatrix(int currentRow, int currentColumn, Matrix matrix) {
		List<MyVector> rowVectors = matrix.getRowVectors();
		rowVectors.remove(currentRow);
		Matrix rectangularIntermediary = new OriginalMatrix(rowVectors);
		List<MyVector> columnVectors = rectangularIntermediary.getColumnVectors();
		columnVectors.remove(currentColumn);

		return new OriginalMatrix(columnVectors, true);
	}
}
