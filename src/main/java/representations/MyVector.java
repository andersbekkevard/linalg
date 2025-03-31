package representations;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 * Fundamental object in linear algebra
 * Contents are stored in double[]
 */
public class MyVector implements Iterable<Double>, Comparable<MyVector> {

	/* ================================= Fields ================================= */
	private final int size;
	private final double[] contents;

	/* ================================= Helpers ================================ */
	private boolean sharesDimension(MyVector vector) {
		return size == vector.size();
	}

	/*
	 * Params:
	 * int vectorSpace: The mathematical dimension (R1, R2 etc)
	 * int dimension: The java index of the "1" element (0, 1 etc)
	 * 
	 * This may be a bit illogical, but doing it like this for now
	 */
	public static MyVector unitVector(int vectorSpace, int dimension) {
		if (vectorSpace <= 0)
			throw new IllegalArgumentException("Can not have vector space with negative or zero dimension");
		// if (dimension < 0 || dimension >= vectorSpace)
		// throw new IllegalArgumentException("Dimension is not contained in the
		// vectorspace");

		return new MyVector(
				IntStream.range(0, vectorSpace).mapToDouble(i -> i == dimension ? 1.0 : 0.0).toArray());
	}

	/* ============================== Constructors ============================== */
	public MyVector(double[] contents) {
		this.contents = contents.clone();
		this.size = contents.length;
	}

	public MyVector(int size) {
		this.contents = new double[size];
		this.size = size;
	}

	/* ================================= Methods ================================ */

	public void scale(double c) {
		for (int i = 0; i < size; i++) {
			contents[i] = c * contents[i];
		}
	}

	public MyVector scaled(double c) {
		MyVector newVector = new MyVector(contents);
		newVector.scale(c);
		return newVector;
	}

	public void normalize() {
		double length = length();
		if (length == 0)
			throw new ArithmeticException("Cannot normalize zero vector");
		scale(1 / length);
	}

	public void add(MyVector vector) {
		if (!(sharesDimension(vector)))
			throw new IllegalArgumentException("Cannot add vectors with different sizes");
		for (int i = 0; i < size; i++) {
			contents[i] += vector.contents()[i];
		}
	}

	public void subtract(MyVector vector) {
		if (!(sharesDimension(vector)))
			throw new IllegalArgumentException("Cannot subtract vectors with different sizes");

		for (int i = 0; i < size; i++) {
			contents[i] -= vector.contents()[i];
		}
	}

	public DoubleStream stream() {
		return Arrays.stream(contents);
	}

	/* ================================= Getters ================================ */

	public double get(int index) {
		return contents[index];
	}

	public int size() {
		return size;
	}

	public double length() {
		return Math.sqrt(Arrays.stream(contents).map(a -> a * a).sum());
	}

	public double[] contents() {
		return contents.clone();
	}

	/* ================================== Other ================================= */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int size = size();

		// Determine maximum width needed
		int maxWidth = 0;
		String[] formattedValues = new String[size];

		// Format all values with one decimal place
		DecimalFormat df = new DecimalFormat("0.0");
		df.setRoundingMode(RoundingMode.HALF_UP);

		// First pass: format all values and find maximum width
		for (int i = 0; i < size; i++) {
			formattedValues[i] = df.format(get(i));
			maxWidth = Math.max(maxWidth, formattedValues[i].length());
		}

		// Build the string with proper alignment
		sb.append("[ ");
		for (int i = 0; i < size; i++) {
			if (i > 0) {
				sb.append("[ ");
			}

			// Right-align each value
			String padding = " ".repeat(maxWidth - formattedValues[i].length());
			sb.append(padding).append(formattedValues[i]);

			// End of element
			if (i < size - 1) {
				sb.append(" ]\n");
			}
		}
		sb.append(" ]\n");

		return sb.toString();
	}

	/*
	 * I have declared MyVector iterable for convenience in debugging
	 */
	@Override
	public Iterator<Double> iterator() {
		return Arrays.stream(contents).boxed().toList().iterator();
	}

	/*
	 * compareTo method to be able to sort List<MyVector> for row reducion
	 * Use is only intended for vectors of same size
	 */
	@Override
	public int compareTo(MyVector otherVector) {
		if (this.size > otherVector.size())
			return 1;
		if (this.size < otherVector.size())
			return -1;

		List<Boolean> thisNonZeroPlacements = Arrays.stream(contents).mapToObj((e) -> e != 0)
				.collect(Collectors.toList());

		List<Boolean> otherNonZeroPlacements = Arrays.stream(otherVector.contents()).mapToObj((e) -> e != 0)
				.collect(Collectors.toList());

		// At this point we can assume the vectors are of the same size
		for (int i = 0; i < thisNonZeroPlacements.size(); i++) {
			if (thisNonZeroPlacements.get(i) && otherNonZeroPlacements.get(i))
				continue;
			return thisNonZeroPlacements.get(i) ? -1 : 1;
		}
		return 0;
	}

}
