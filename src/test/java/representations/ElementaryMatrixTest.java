package representations;

import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class ElementaryMatrixTest {
	private ElementaryMatrix matrix;
	private final int dimension = 3;

	@Before
	public void setUp() {
		matrix = new ElementaryMatrix(dimension);
	}

	@Test
	public void testConstructor() {
		assertEquals(dimension, matrix.rows());
		assertEquals(dimension, matrix.columns());

		List<MyVector> rowVectors = matrix.getRowVectors();
		assertEquals(dimension, rowVectors.size());

		// Should be identity matrix initially
		for (int i = 0; i < dimension; i++) {
			MyVector row = rowVectors.get(i);
			for (int j = 0; j < dimension; j++) {
				assertEquals(i == j ? 1.0 : 0.0, row.get(j), 0.0001);
			}
		}
	}

	@Test
	public void testSwapRows() {
		matrix.swapRows(0, 2);

		List<MyVector> rowVectors = matrix.getRowVectors();

		// Check rows 0 and 2 were swapped
		assertEquals(0.0, rowVectors.get(0).get(0), 0.0001);
		assertEquals(0.0, rowVectors.get(0).get(1), 0.0001);
		assertEquals(1.0, rowVectors.get(0).get(2), 0.0001);

		assertEquals(1.0, rowVectors.get(2).get(0), 0.0001);
		assertEquals(0.0, rowVectors.get(2).get(1), 0.0001);
		assertEquals(0.0, rowVectors.get(2).get(2), 0.0001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSwapSameRow() {
		matrix.swapRows(1, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSwapInvalidRows() {
		matrix.swapRows(-1, 4);
	}

	@Test
	public void testScaleRow() {
		matrix.scaleRow(1, 2.5);

		List<MyVector> rowVectors = matrix.getRowVectors();

		assertEquals(0.0, rowVectors.get(1).get(0), 0.0001);
		assertEquals(2.5, rowVectors.get(1).get(1), 0.0001);
		assertEquals(0.0, rowVectors.get(1).get(2), 0.0001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testScaleInvalidRow() {
		matrix.scaleRow(5, 2.0);
	}

	@Test
	public void testSubtractRow() {
		matrix.subtractRow(1, 0);

		List<MyVector> rowVectors = matrix.getRowVectors();

		// Row 1 should be [0.0, 1.0, 0.0] - [1.0, 0.0, 0.0] = [-1.0, 1.0, 0.0]
		assertEquals(-1.0, rowVectors.get(1).get(0), 0.0001);
		assertEquals(1.0, rowVectors.get(1).get(1), 0.0001);
		assertEquals(0.0, rowVectors.get(1).get(2), 0.0001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSubtractSameRow() {
		matrix.subtractRow(2, 2);
	}

	@Test
	public void testSubtractScaledRow() {
		matrix.subtractScaledRow(2, 0, 2.5);

		List<MyVector> rowVectors = matrix.getRowVectors();

		// Row 2 should be [0.0, 0.0, 1.0] - 2.5*[1.0, 0.0, 0.0] = [-2.5, 0.0, 1.0]
		assertEquals(-2.5, rowVectors.get(2).get(0), 0.0001);
		assertEquals(0.0, rowVectors.get(2).get(1), 0.0001);
		assertEquals(1.0, rowVectors.get(2).get(2), 0.0001);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testUnsupportedGet() {
		matrix.get(0, 0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testUnsupportedSet() {
		matrix.set(0, 0, 5.0);
	}
}
