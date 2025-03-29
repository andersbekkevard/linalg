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

}
