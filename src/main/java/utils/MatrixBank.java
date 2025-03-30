package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import representations.Matrix;
import representations.MyVector;
import representations.OriginalMatrix;

/**
 * MatrixBank provides predefined matrices with various properties for testing
 * and debugging.
 * Matrices can be generated with specific dimensions, structures, and
 * mathematical properties.
 */
public class MatrixBank {

	/**
	 * Returns an identity matrix of the specified size.
	 */
	public static Matrix identity(int size) {
		double[][] contents = new double[size][size];
		for (int i = 0; i < size; i++) {
			contents[i][i] = 1.0;
		}
		return new OriginalMatrix(contents);
	}

	/**
	 * Returns an list of row vectors that represent an identity matrix of the
	 * specified size.
	 */
	public static List<MyVector> identityVectors(int size) {
		double[][] contents = new double[size][size];
		for (int i = 0; i < size; i++) {
			contents[i][i] = 1.0;
		}
		return Arrays.stream(contents).map(MyVector::new).collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * Returns a zero matrix of the specified dimensions.
	 */
	public static Matrix zeros(int rows, int columns) {
		return new OriginalMatrix(rows, columns);
	}

	/**
	 * Returns a matrix filled with ones.
	 */
	public static Matrix ones(int rows, int columns) {
		double[][] contents = new double[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				contents[i][j] = 1.0;
			}
		}
		return new OriginalMatrix(contents);
	}

	/**
	 * Returns a diagonal matrix with the specified values on the diagonal.
	 */
	public static Matrix diagonal(double... diagonalValues) {
		int size = diagonalValues.length;
		double[][] contents = new double[size][size];
		for (int i = 0; i < size; i++) {
			contents[i][i] = diagonalValues[i];
		}
		return new OriginalMatrix(contents);
	}

	/**
	 * Returns an invertible square matrix of the specified size.
	 */
	public static Matrix invertible(int size) {
		double[][] contents = new double[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				contents[i][j] = i + j + 1.0;
			}
			// Make diagonal dominant to ensure invertibility
			contents[i][i] += size;
		}
		return new OriginalMatrix(contents);
	}

	/**
	 * Returns a non-invertible square matrix of the specified size.
	 */
	public static Matrix nonInvertible(int size) {
		if (size < 2) {
			throw new IllegalArgumentException("Size must be at least 2 for a non-invertible matrix");
		}

		double[][] contents = new double[size][size];
		// Fill the matrix with some values
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				contents[i][j] = i * 10 + j;
			}
		}

		// Make the last row a linear combination of other rows
		for (int j = 0; j < size; j++) {
			contents[size - 1][j] = contents[0][j] * 2 + contents[1][j];
		}

		return new OriginalMatrix(contents);
	}

	/**
	 * Returns a symmetric matrix of the specified size.
	 */
	public static Matrix symmetric(int size) {
		double[][] contents = new double[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = i; j < size; j++) {
				contents[i][j] = i * 10 + j + 1;
				contents[j][i] = contents[i][j]; // Make it symmetric
			}
		}
		return new OriginalMatrix(contents);
	}

	/**
	 * Returns a rectangular matrix with the specified dimensions.
	 */
	public static Matrix rectangular(int rows, int columns) {
		double[][] contents = new double[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				contents[i][j] = i * 10 + j + 1;
			}
		}
		return new OriginalMatrix(contents);
	}

	/**
	 * Returns a matrix with a specific rank.
	 */
	public static Matrix withRank(int rows, int columns, int rank) {
		int minDim = Math.min(rows, columns);
		if (rank > minDim) {
			throw new IllegalArgumentException("Rank cannot exceed min(rows, columns)");
		}

		double[][] contents = new double[rows][columns];

		// Create rank independent columns/rows
		for (int i = 0; i < rank; i++) {
			contents[i][i] = i + 1.0;
		}

		// Make later rows linear combinations of the first rank rows
		for (int i = rank; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				for (int k = 0; k < rank; k++) {
					contents[i][j] += (k + 1) * contents[k][j];
				}
			}
		}

		return new OriginalMatrix(contents);
	}

	/**
	 * Returns a sparse matrix with the specified dimensions.
	 */
	public static Matrix sparse(int rows, int columns, double density) {
		if (density < 0.0 || density > 1.0) {
			throw new IllegalArgumentException("Density must be between 0.0 and 1.0");
		}

		double[][] contents = new double[rows][columns];
		int nonZeroCount = (int) (rows * columns * density);
		nonZeroCount = Math.max(1, nonZeroCount);

		for (int k = 0; k < nonZeroCount; k++) {
			int i = (int) (Math.random() * rows);
			int j = (int) (Math.random() * columns);
			contents[i][j] = Math.random() * 10 + 1;
		}

		return new OriginalMatrix(contents);
	}

	/**
	 * Returns a matrix in row echelon form.
	 */
	public static Matrix rowEchelonForm(int rows, int columns) {
		double[][] contents = new double[rows][columns];

		int lead = 0;
		for (int i = 0; i < rows && lead < columns; i++) {
			if (lead < columns) {
				contents[i][lead] = 1.0;

				for (int j = lead + 1; j < columns; j++) {
					contents[i][j] = (i + 1) * (j + 1);
				}

				lead++;
			}
		}

		return new OriginalMatrix(contents);
	}

	/**
	 * Returns common test matrices by name
	 */
	public static Matrix getNamedMatrix(String name) {
		switch (name.toLowerCase()) {
			case "simple2x2":
				return new OriginalMatrix(new double[][] { { 1, 2 }, { 3, 4 } });
			case "simple3x3":
				return new OriginalMatrix(new double[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
			case "invertible3x3":
				return new OriginalMatrix(new double[][] { { 4, 2, 1 }, { 3, 1, 2 }, { 2, 5, 3 } });
			case "singular3x3":
				return new OriginalMatrix(new double[][] { { 1, 2, 3 }, { 2, 4, 6 }, { 3, 6, 9 } });
			case "magic3x3":
				return new OriginalMatrix(new double[][] { { 8, 1, 6 }, { 3, 5, 7 }, { 4, 9, 2 } });
			default:
				throw new IllegalArgumentException("Unknown matrix name: " + name);
		}
	}

	/**
	 * Returns a coefficient matrix and right-hand side for a linear system with
	 * known solution.
	 */
	public static Matrix[] linearSystem(int size) {
		// Create coefficient matrix A
		double[][] coefficients = new double[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				coefficients[i][j] = (i + 1) * (j + 1);
			}
			// Make diagonally dominant to ensure invertibility
			coefficients[i][i] += size * 2;
		}

		// Create solution vector x
		double[] solution = new double[size];
		for (int i = 0; i < size; i++) {
			solution[i] = i + 1;
		}

		// Calculate b = Ax
		double[] rhs = new double[size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				rhs[i] += coefficients[i][j] * solution[j];
			}
		}

		// Create the right-hand side matrix
		double[][] rhsMatrix = new double[size][1];
		for (int i = 0; i < size; i++) {
			rhsMatrix[i][0] = rhs[i];
		}

		return new Matrix[] {
				new OriginalMatrix(coefficients),
				new OriginalMatrix(rhsMatrix)
		};
	}
}
