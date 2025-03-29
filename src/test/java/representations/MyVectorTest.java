package representations;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class MyVectorTest {

	private MyVector vector;
	private MyVector zeroVector;

	@Before
	public void setUp() {
		vector = new MyVector(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 });
		zeroVector = new MyVector(new double[5]);
	}

	@Test
	public void testConstructorWithArray() {
		double[] values = { 1.0, 2.0, 3.0 };
		MyVector v = new MyVector(values);

		assertEquals(3, v.getSize());
		assertEquals(1.0, v.get(0), 0.0001);
		assertEquals(2.0, v.get(1), 0.0001);
		assertEquals(3.0, v.get(2), 0.0001);
	}

	@Test
	public void testConstructorWithSize() {
		MyVector v = new MyVector(5);

		assertEquals(5, v.getSize());
		for (int i = 0; i < 5; i++) {
			assertEquals(0.0, v.get(i), 0.0001);
		}
	}

	@Test
	public void testTranspose() {
		assertFalse(vector.isTransposed());

		MyVector transposed = vector.transpose();

		assertTrue(transposed.isTransposed());
		assertSame(vector, transposed); // Should return itself

		transposed.transpose(); // Double transpose
		assertFalse(transposed.isTransposed());
	}

	@Test
	public void testScale() {
		vector.scale(2.0);

		assertEquals(2.0, vector.get(0), 0.0001);
		assertEquals(4.0, vector.get(1), 0.0001);
		assertEquals(6.0, vector.get(2), 0.0001);
		assertEquals(8.0, vector.get(3), 0.0001);
		assertEquals(10.0, vector.get(4), 0.0001);
	}

	@Test
	public void testNormalize() {
		vector.normalize();

		double length = vector.getLength();
		assertEquals(1.0, length, 0.0001);
	}

	@Test(expected = ArithmeticException.class)
	public void testNormalizeZeroVector() {
		zeroVector.normalize(); // Should throw exception
	}

	@Test
	public void testAdd() {
		MyVector other = new MyVector(new double[] { 5.0, 4.0, 3.0, 2.0, 1.0 });

		vector.add(other);

		assertEquals(6.0, vector.get(0), 0.0001);
		assertEquals(6.0, vector.get(1), 0.0001);
		assertEquals(6.0, vector.get(2), 0.0001);
		assertEquals(6.0, vector.get(3), 0.0001);
		assertEquals(6.0, vector.get(4), 0.0001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddIncompatibleSizes() {
		MyVector other = new MyVector(new double[] { 1.0, 2.0 });
		vector.add(other); // Should throw exception
	}

	@Test
	public void testSubtract() {
		MyVector other = new MyVector(new double[] { 0.5, 1.0, 1.5, 2.0, 2.5 });

		vector.subtract(other);

		assertEquals(0.5, vector.get(0), 0.0001);
		assertEquals(1.0, vector.get(1), 0.0001);
		assertEquals(1.5, vector.get(2), 0.0001);
		assertEquals(2.0, vector.get(3), 0.0001);
		assertEquals(2.5, vector.get(4), 0.0001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSubtractIncompatibleSizes() {
		MyVector other = new MyVector(new double[] { 1.0, 2.0, 3.0 });
		vector.subtract(other); // Should throw exception
	}

	@Test
	public void testGetLength() {
		MyVector v = new MyVector(new double[] { 3.0, 4.0 });
		assertEquals(5.0, v.getLength(), 0.0001);
	}

	@Test
	public void testGetContents() {
		double[] contents = vector.getContents();

		assertEquals(5, contents.length);
		assertEquals(1.0, contents[0], 0.0001);

		// Verify it's a clone
		contents[0] = 99.0;
		assertEquals(1.0, vector.get(0), 0.0001);
	}

	@Test
	public void testToString() {
		String expected = "[1.0, 2.0, 3.0, 4.0, 5.0]";
		assertEquals(expected, vector.toString());

		vector.transpose();
		assertEquals(expected + "T", vector.toString());
	}

	@Test
	public void testIterator() {
		Iterator<Double> it = vector.iterator();
		assertTrue(it.hasNext());
		assertEquals(1.0, it.next(), 0.0001);
		assertEquals(2.0, it.next(), 0.0001);
		assertEquals(3.0, it.next(), 0.0001);
		assertEquals(4.0, it.next(), 0.0001);
		assertEquals(5.0, it.next(), 0.0001);
		assertFalse(it.hasNext());
	}
}
