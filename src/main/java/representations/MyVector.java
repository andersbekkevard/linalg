package representations;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class MyVector implements Iterable<Double>, Comparable<MyVector> {

	/*
	 * Fundamental object in linear algebra
	 * Contents are stored in double[]
	 */

	/* ================================= Fields ================================= */
	// the field isTransposed is not in use at this point in time
	private boolean isTransposed = false;
	private final int size;
	private final double[] contents;

	/* ================================= Helpers ================================ */
	private boolean sharesDimension(MyVector vector) {
		return size == vector.getSize();
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
	public MyVector transpose() {
		isTransposed = !isTransposed;
		return this;
	}

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
		double length = getLength();
		if (length == 0)
			throw new ArithmeticException("Cannot normalize zero vector");
		scale(1 / length);
	}

	public void add(MyVector vector) {
		if (!(sharesDimension(vector)))
			throw new IllegalArgumentException("Cannot add vectors with different sizes");
		for (int i = 0; i < size; i++) {
			contents[i] += vector.getContents()[i];
		}
	}

	public void subtract(MyVector vector) {
		if (!(sharesDimension(vector)))
			throw new IllegalArgumentException("Cannot subtract vectors with different sizes");

		for (int i = 0; i < size; i++) {
			contents[i] -= vector.getContents()[i];
		}
	}

	public DoubleStream stream() {
		return Arrays.stream(contents);
	}

	/* ================================= Getters ================================ */
	public boolean isTransposed() {
		return isTransposed;
	}

	public double get(int index) {
		return contents[index];
	}

	public int getSize() {
		return size;
	}

	public double getLength() {
		return Math.sqrt(Arrays.stream(contents).map(a -> a * a).sum());
	}

	public double[] getContents() {
		return contents.clone();
	}

	/* ================================== Other ================================= */
	@Override
	public String toString() {
		String s = Arrays.toString(contents);
		if (isTransposed)
			s = s + "T";
		return s;
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
		if (this.size > otherVector.getSize())
			return 1;
		if (this.size < otherVector.getSize())
			return -1;

		List<Boolean> thisNonZeroPlacements = Arrays.stream(contents).mapToObj((e) -> e != 0)
				.collect(Collectors.toList());

		List<Boolean> otherNonZeroPlacements = Arrays.stream(otherVector.getContents()).mapToObj((e) -> e != 0)
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
