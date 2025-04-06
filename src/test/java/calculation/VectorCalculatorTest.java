package calculation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import representations.MyVector;

public class VectorCalculatorTest {

	private VectorCalculator calculator;

	@Before
	public void setUp() {
		calculator = new VectorCalculator();
	}

	// Inner Product Tests

	@Test
	public void testInnerProductNormalCase() {
		MyVector u = new MyVector(new double[] { 1, 2, 3 });
		MyVector v = new MyVector(new double[] { 4, 5, 6 });
		assertEquals(32, calculator.innerProduct(u, v), 0.0001);
	}

	@Test
	public void testInnerProductZeroVectors() {
		MyVector u = new MyVector(new double[] { 0, 0, 0 });
		MyVector v = new MyVector(new double[] { 0, 0, 0 });
		assertEquals(0, calculator.innerProduct(u, v), 0.0001);
	}

	@Test
	public void testInnerProductNegativeNumbers() {
		MyVector u = new MyVector(new double[] { -1, -2, -3 });
		MyVector v = new MyVector(new double[] { 4, 5, 6 });
		assertEquals(-32, calculator.innerProduct(u, v), 0.0001);
	}

	@Test
	public void testInnerProductSingleElement() {
		MyVector u = new MyVector(new double[] { 5 });
		MyVector v = new MyVector(new double[] { 7 });
		assertEquals(35, calculator.innerProduct(u, v), 0.0001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInnerProductDifferentDimensions() {
		MyVector u = new MyVector(new double[] { 1, 2, 3 });
		MyVector v = new MyVector(new double[] { 4, 5 });
		calculator.innerProduct(u, v);
	}

	// Angle Between Tests

	@Test
	public void testAngleBetweenSameDirection() {
		MyVector u = new MyVector(new double[] { 1, 0, 0 });
		MyVector v = new MyVector(new double[] { 2, 0, 0 });
		assertEquals(0, calculator.angleBetween(u, v), 0.0001);
	}

	@Test
	public void testAngleBetweenOppositeDirection() {
		MyVector u = new MyVector(new double[] { 1, 0, 0 });
		MyVector v = new MyVector(new double[] { -1, 0, 0 });
		assertEquals(Math.PI, calculator.angleBetween(u, v), 0.0001);
	}

	@Test
	public void testAngleBetweenOrthogonal() {
		MyVector u = new MyVector(new double[] { 1, 0, 0 });
		MyVector v = new MyVector(new double[] { 0, 1, 0 });
		assertEquals(Math.PI / 2, calculator.angleBetween(u, v), 0.0001);
	}

	@Test
	public void testAngleBetweenDifferentDimensionsULarger() {
		MyVector u = new MyVector(new double[] { 1, 2, 3 });
		MyVector v = new MyVector(new double[] { 4, 5 });
		double expected = Math.acos(14 / (Math.sqrt(14) * Math.sqrt(41)));
		assertEquals(expected, calculator.angleBetween(u, v), 0.0001);
	}

	@Test
	public void testAngleBetweenDifferentDimensionsVLarger() {
		MyVector u = new MyVector(new double[] { 1, 2 });
		MyVector v = new MyVector(new double[] { 4, 5, 6 });
		double expected = Math.acos(14 / (Math.sqrt(5) * Math.sqrt(77)));
		assertEquals(expected, calculator.angleBetween(u, v), 0.0001);
	}

	@Test
	public void testAngleBetweenWithZeroVector() {
		MyVector u = new MyVector(new double[] { 0, 0, 0 });
		MyVector v = new MyVector(new double[] { 1, 2, 3 });
		assertTrue(Double.isNaN(calculator.angleBetween(u, v)));
	}

	// Cross Product Tests

	@Test
	public void testCrossProductStandardVectors() {
		MyVector u = new MyVector(new double[] { 1, 0, 0 });
		MyVector v = new MyVector(new double[] { 0, 1, 0 });
		MyVector result = calculator.crossProduct(u, v);
		assertEquals(0, result.get(0), 0.0001);
		assertEquals(0, result.get(1), 0.0001);
		assertEquals(1, result.get(2), 0.0001);
	}

	@Test
	public void testCrossProductOrthogonality() {
		MyVector u = new MyVector(new double[] { 1, 2, 3 });
		MyVector v = new MyVector(new double[] { 4, 5, 6 });
		MyVector result = calculator.crossProduct(u, v);
		assertEquals(0, calculator.innerProduct(result, u), 0.0001);
		assertEquals(0, calculator.innerProduct(result, v), 0.0001);
	}

	@Test
	public void testCrossProductParallelVectors() {
		MyVector u = new MyVector(new double[] { 1, 2, 3 });
		MyVector v = new MyVector(new double[] { 2, 4, 6 });
		MyVector result = calculator.crossProduct(u, v);
		assertEquals(0, result.get(0), 0.0001);
		assertEquals(0, result.get(1), 0.0001);
		assertEquals(0, result.get(2), 0.0001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCrossProductNon3DVectors() {
		MyVector u = new MyVector(new double[] { 1, 2 });
		MyVector v = new MyVector(new double[] { 3, 4, 5 });
		calculator.crossProduct(u, v);
	}
}
