package representations;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class OriginalMatrixTest {

	private OriginalMatrix matrix;
	private double[][] testData;

	@Before
	public void setUp() {
		testData = new double[][] {
				{ 1.0, 2.0, 3.0 },
				{ 4.0, 5.0, 6.0 },
				{ 7.0, 8.0, 9.0 }
		};
		matrix = new OriginalMatrix(testData);
	}

	@Test
	public void testConstructorWithDimensions() {
		OriginalMatrix m = new OriginalMatrix(2, 3);

		assertEquals(2, m.rows());
		assertEquals(3, m.columns());

		// Verify all values are 0
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 3; j++) {
				assertEquals(0.0, m.get(i, j), 0.0001);
			}
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWithInvalidDimensions() {
		new OriginalMatrix(0, 5); // Should throw exception
	}

	@Test
	public void testConstructorWithContents() {
		assertEquals(3, matrix.rows());
		assertEquals(3, matrix.columns());

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				assertEquals(testData[i][j], matrix.get(i, j), 0.0001);
			}
		}
	}

	@Test
	public void testSet() {
		matrix.set(1, 1, 99.0);
		assertEquals(99.0, matrix.get(1, 1), 0.0001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetWithInvalidRow() {
		matrix.set(-1, 1, 10.0); // Should throw exception
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetWithInvalidColumn() {
		matrix.set(1, 3, 10.0); // Should throw exception
	}

	@Test
	public void testTranspose() {
		Matrix transposed = matrix.transpose();

		assertEquals(3, transposed.rows());
		assertEquals(3, transposed.columns());

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				assertEquals(matrix.get(j, i), transposed.get(i, j), 0.0001);
			}
		}
	}

	@Test
	public void testScale() {
		matrix.scale(2.0);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				assertEquals(testData[i][j] * 2.0, matrix.get(i, j), 0.0001);
			}
		}
	}

	@Test
	public void testAdd() {
		OriginalMatrix other = new OriginalMatrix(testData);
		matrix.add(other);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				assertEquals(testData[i][j] * 2.0, matrix.get(i, j), 0.0001);
			}
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddWithIncompatibleDimensions() {
		OriginalMatrix other = new OriginalMatrix(2, 2);
		matrix.add(other); // Should throw exception
	}

	@Test
	public void testSubtract() {
		OriginalMatrix other = new OriginalMatrix(testData);
		matrix.subtract(other);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				assertEquals(0.0, matrix.get(i, j), 0.0001);
			}
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSubtractWithIncompatibleDimensions() {
		OriginalMatrix other = new OriginalMatrix(4, 3);
		matrix.subtract(other); // Should throw exception
	}

	@Test
	public void testGetContents() {
		double[][] contents = matrix.getContents();

		assertEquals(3, contents.length);
		assertEquals(3, contents[0].length);

		// Verify it's a clone
		contents[0][0] = 99.0;
		assertEquals(1.0, matrix.get(0, 0), 0.0001);
	}

	@Test
	public void testGetRowVectors() {
		List<MyVector> rows = matrix.getRowVectors();

		assertEquals(3, rows.size());

		for (int i = 0; i < 3; i++) {
			MyVector row = rows.get(i);
			assertEquals(3, row.getSize());

			for (int j = 0; j < 3; j++) {
				assertEquals(testData[i][j], row.get(j), 0.0001);
			}
		}
	}

	@Test
	public void testGetColumnVectors() {
		List<MyVector> columns = matrix.getColumnVectors();

		assertEquals(3, columns.size());

		for (int j = 0; j < 3; j++) {
			MyVector column = columns.get(j);
			assertEquals(3, column.getSize());

			for (int i = 0; i < 3; i++) {
				assertEquals(testData[i][j], column.get(i), 0.0001);
			}
		}
	}

	@Test
	public void testToString() {
		String result = matrix.toString();
		assertTrue(result.contains("[1.0, 2.0, 3.0]"));
		assertTrue(result.contains("[4.0, 5.0, 6.0]"));
		assertTrue(result.contains("[7.0, 8.0, 9.0]"));
	}
}
