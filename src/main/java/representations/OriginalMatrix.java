package representations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
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

	/*
	 * Adjuster for floating point arithmetic so we avoid -0.0 in the toStrig()
	 */
	private static final Function<Double, Double> floatingPointAdjuster = (d) -> {
		if (Math.abs(d) < 1e-10)
			return 0.0;
		return d;
	};

	// Swap .0f to .1f to show decimal
	private static final Function<Double, String> doubleToRoundedString = (d) -> String.format("%.1f", d);

	private static final Function<double[], String> arrayToString = (row) -> "["
			+ Arrays.stream(row).boxed().map(floatingPointAdjuster)
					.map(doubleToRoundedString).collect(Collectors.joining(", "))
			+ "]";

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
		int vectorSize = vectors.get(0).getSize();
		for (MyVector vector : vectors) {
			if (vector.getSize() != vectorSize)
				throw new IllegalArgumentException("All vectors must be of same length");
		}

		if (!considerVectorsAsColumns) {
			double[][] contents = vectors.stream().map(MyVector::getContents).toArray(double[][]::new);

			this.contents = contents;
			this.rows = contents.length;
			this.columns = contents[0].length;
		} else {
			int numberOfColumns = vectors.size();
			double[][] contents = IntStream.range(0, numberOfColumns)
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
	@Override
	public String toString() {
		return "[\n" + Arrays.stream(contents).map(arrayToString).collect(Collectors.joining("\n")) + "\n]";
	}
}
