package representations;

import java.util.Arrays;

public class MyVector {

	/*
	 * Fundamental object in linear algebra
	 * Contents are stored in double array
	 * 
	 */

	/* ================================= Fields ================================= */
	private boolean isTransposed = false;
	private final int size;
	private final double[] contents;

	/* ============================== Constructors ============================== */
	public MyVector(double[] contents) {
		this.contents = contents;
		this.size = contents.length;
	}

	public MyVector(int size) {
		this.contents = new double[size];
		this.size = size;
	}

	/* ================================= Methods ================================ */
	public void transpose() {
		isTransposed = !isTransposed;
	}

	private boolean sharesDimension(MyVector vector) {
		return size == vector.getSize();
	}

	public void scale(double c) {
		for (int i = 0; i < size; i++) {
			contents[i] = c * contents[i];
		}
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

	/* ================================= Getters ================================ */
	public boolean isTransposed() {
		return isTransposed;
	}

	public double[] getContents() {
		return contents.clone();
	}

	public int getSize() {
		return size;
	}

	/* ================================== Other ================================= */
	@Override
	public String toString() {
		String s = Arrays.toString(contents);
		if (isTransposed)
			s = s + "T";
		return s;
	}

	public static void main(String[] args) {
		System.out.print("\033[2J\033[1;1H");
		int size = 6;
		double[] array = new double[size];

		for (int i = 1; i < size; i++)
			array[i] = i;

		MyVector vector = new MyVector(array);
		System.out.println(vector);

	}

}
