package representations;

import java.util.Arrays;
import java.util.stream.Collectors;

public class OriginalMatrix implements Matrix {

	/*
	 * Matricies are the centre of this project
	 * They could have been built up of vectors, but it is more decoupled and
	 * efficient to store the info in arrays
	 */

	/* ================================= Fields ================================= */
	private boolean isTransposed = false;
	private final int rows;
	private final int columns;
	private final double[][] contents;

	/* ============================== Constructors ============================== */
	public OriginalMatrix(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		contents = new double[rows][columns];
	}

	public OriginalMatrix(double[][] contents) {
		this(contents[0].length, contents.length);

		// This could be done with
		// System.arraycopy
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				this.contents[i][j] = contents[i][j];
			}
		}
	}

	/* ================================= Methods ================================ */
	public Matrix transpose() {
		return new TransposedMatrix(this);
	}

	public void scale(double c) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				contents[i][j] = c * contents[i][j];
			}
		}
	}

	@Override
	public void add(Matrix m) {
		if (!(rows == m.rows() && columns == m.columns()))
			throw new IllegalArgumentException("Can not add matricies of different dimensions");

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				contents[i][j] += m.get(i, j);
			}
		}
	}

	@Override
	public void subtract(Matrix m) {
		if (!(rows == m.rows() && columns == m.columns()))
			throw new IllegalArgumentException("Can not subtractmatricies of different dimensions");

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				contents[i][j] -= m.get(i, j);
			}
		}
	}

	/* ================================= Getters ================================ */
	@Override
	public int rows() {
		return rows;
	}

	@Override
	public int columns() {
		return columns;
	}

	@Override
	public double get(int row, int column) {
		return contents[row][column];
	}

	public double[][] getContents() {
		double[][] copy = new double[rows][columns];
		for (int i = 0; i < rows; i++) {
			copy[i] = contents[i].clone();
		}
		return copy;
	}

	@Override
	public String toString() {
		return '[' + Arrays.stream(contents).map(Arrays::toString).collect(Collectors.joining("\n")) + ']';
	}
}
