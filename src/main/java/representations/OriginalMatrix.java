package representations;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class OriginalMatrix implements Matrix {

	/*
	 * Matricies are the centre of this project
	 * They could have been built up of vectors, but it is more decoupled and
	 * efficient to store the info in arrays
	 */

	/* ================================= Fields ================================= */
	private final int rows;
	private final int columns;
	private final double[][] contents;

	/* ================================= Helpers ================================ */

	private boolean rowIndexIsValid(int rowIndex) {
		return 0 <= rowIndex && rowIndex < rows;
	}

	private boolean columnIndexIsValid(int columnIndex) {
		return 0 <= columnIndex && columnIndex < columns;
	}

	/* ============================== Constructors ============================== */
	public OriginalMatrix(int rows, int columns) {
		if (rows <= 0 || columns <= 0)
			throw new IllegalArgumentException("Can not have dimension=0");
		this.rows = rows;
		this.columns = columns;
		contents = new double[rows][columns];
	}

	public OriginalMatrix(double[][] contents) {
		this(contents.length, contents[0].length);

		// This could be done with
		// System.arraycopy
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				this.contents[i][j] = contents[i][j];
			}
		}
	}

	/*
	 * Constructur that takes a number of vectors and combines them into a matrix
	 * The default is that the vectors are interpreted as rows
	 */
	public OriginalMatrix(List<MyVector> rowVectors) {
		this(rowVectors, false);
	}

	public OriginalMatrix(List<MyVector> vectors, boolean considerVectorsAsColumns) {
		int vectorSize = vectors.get(0).size();
		for (MyVector vector : vectors) {
			if (vector.size() != vectorSize)
				throw new IllegalArgumentException("All vectors must be of same length");
		}

		if (!considerVectorsAsColumns) {
			double[][] contents = vectors.stream().map(MyVector::contents).toArray(double[][]::new);

			this.contents = contents;
			this.rows = contents.length;
			this.columns = contents[0].length;
		} else {
			double[][] contents = IntStream.range(0, vectorSize)
					.mapToObj(i -> vectors.stream().mapToDouble(vector -> vector.get(i)).toArray())
					.toArray(double[][]::new);

			this.contents = contents;
			this.rows = contents.length;
			this.columns = contents[0].length;
		}
	}

	/* ================================= Methods ================================ */
	public void set(int row, int column, double value) {
		if (!(rowIndexIsValid(row) && columnIndexIsValid(column))) {
			throw new IllegalArgumentException("Illegal indicies");
		}

		contents[row][column] = value;
	}

	@Override
	public MyVector multiply(MyVector vector) {
		if (vector.size() != this.contents[0].length)
			throw new IllegalArgumentException("Vector has wrong dimensions");

		MyVector resultVector = new MyVector(this.contents.length);
		List<MyVector> columns = getColumnVectors();

		for (int i = 0; i < columns.size(); i++) {
			resultVector.add(columns.get(i).scaled(vector.get(i)));
		}
		return resultVector;

	}

	public Matrix transposed() {
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

	public void sortRows() {

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
	public List<MyVector> getRowVectors() {
		return new ArrayList<>(Arrays.stream(contents).map(a -> new MyVector(a)).toList());
	}

	/*
	 * This one is a bit tricky to debug, but works as intended
	 * - We start with the stream of all the column indicies
	 * - Then for each of the indicicies we create a new stream of the contents
	 * and map each row to the element at our index
	 * - We gather this stream in an array, and cast it to a vector
	 * - Finally we collect all vectors to an ArrayList
	 * 
	 */

	@Override
	public List<MyVector> getColumnVectors() {
		return new ArrayList<>(IntStream.range(0, columns)
				.mapToObj(i -> new MyVector(Arrays.stream(contents).mapToDouble(row -> row[i]).toArray()))
				.toList());
	}

	/* ================================== Other ================================= */
	// @Override
	// public String toString() {
	// return "[\n" +
	// Arrays.stream(contents).map(arrayToString).collect(Collectors.joining("\n"))
	// + "\n]";
	// }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int rows = rows();
		int cols = columns();

		// Determine maximum width needed for each column
		int[] colWidths = new int[cols];
		String[][] formattedValues = new String[rows][cols];

		// Format all values with one decimal place
		DecimalFormat df = new DecimalFormat("0.0");
		df.setRoundingMode(RoundingMode.HALF_UP);

		// First pass: format all values and find maximum width per column
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				formattedValues[i][j] = df.format(get(i, j));
				colWidths[j] = Math.max(colWidths[j], formattedValues[i][j].length());
			}
		}

		// Build the string with proper alignment
		sb.append("[ ");
		for (int i = 0; i < rows; i++) {
			if (i > 0) {
				sb.append("[ ");
			}

			for (int j = 0; j < cols; j++) {
				// Right-align each value within its column width
				String padding = " ".repeat(colWidths[j] - formattedValues[i][j].length());
				sb.append(padding).append(formattedValues[i][j]);

				// Add spacing between columns
				if (j < cols - 1) {
					sb.append("  ");
				}
			}

			// End of row
			if (i < rows - 1) {
				sb.append(" ]\n");
			}
		}
		sb.append(" ]\n");

		return sb.toString();
	}
}
