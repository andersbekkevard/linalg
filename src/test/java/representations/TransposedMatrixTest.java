package representations;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class TransposedMatrixTest {

	private Matrix originalMatrix;
	private Matrix transposedMatrix;
	private static final double MARGIN_OF_ERROR = 0.0001;

	@Before
	public void setUp() {
		double[][] data = new double[][] {
				{ 1.0, 2.0, 3.0 },
				{ 4.0, 5.0, 6.0 }
		};
		originalMatrix = new OriginalMatrix(data);
		transposedMatrix = originalMatrix.transpose();
	}

	@Test
	public void testDimensions() {
		assertEquals(2, originalMatrix.rows());
		assertEquals(3, originalMatrix.columns());

		assertEquals(3, transposedMatrix.rows());
		assertEquals(2, transposedMatrix.columns());
	}

	@Test
	public void testGet() {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 3; j++) {
				assertEquals(originalMatrix.get(i, j), transposedMatrix.get(j, i), MARGIN_OF_ERROR);
			}
		}
	}

	@Test
	public void testSet() {
		transposedMatrix.set(0, 1, 99.0);
		assertEquals(99.0, originalMatrix.get(1, 0), MARGIN_OF_ERROR);
	}

	@Test
	public void testTranspose() {
		Matrix doubleTransposed = transposedMatrix.transpose();
		assertSame(originalMatrix, doubleTransposed);
	}

	@Test
	public void testScale() {
		transposedMatrix.scale(2.0);

		// Check that the original matrix was scaled
		assertEquals(2.0, originalMatrix.get(0, 0), MARGIN_OF_ERROR);
		assertEquals(4.0, originalMatrix.get(0, 1), MARGIN_OF_ERROR);
		assertEquals(6.0, originalMatrix.get(0, 2), MARGIN_OF_ERROR);
		assertEquals(8.0, originalMatrix.get(1, 0), MARGIN_OF_ERROR);

		// Check that the transposed view reflects the scaling
		assertEquals(2.0, transposedMatrix.get(0, 0), MARGIN_OF_ERROR);
		assertEquals(4.0, transposedMatrix.get(1, 0), MARGIN_OF_ERROR);
	}

	@Test
	public void testGetRowVectors() {
		List<MyVector> rows = transposedMatrix.getRowVectors();
		List<MyVector> originalColumns = originalMatrix.getColumnVectors();

		assertEquals(3, rows.size());

		for (int i = 0; i < rows.size(); i++) {
			MyVector row = rows.get(i);
			MyVector originalColumn = originalColumns.get(i);

			assertEquals(originalColumn.getSize(), row.getSize());

			for (int j = 0; j < row.getSize(); j++) {
				assertEquals(originalColumn.get(j), row.get(j), MARGIN_OF_ERROR);
			}
		}
	}

	@Test
	public void testGetColumnVectors() {
		List<MyVector> columns = transposedMatrix.getColumnVectors();
		List<MyVector> originalRows = originalMatrix.getRowVectors();

		assertEquals(2, columns.size());

		for (int i = 0; i < columns.size(); i++) {
			MyVector column = columns.get(i);
			MyVector originalRow = originalRows.get(i);

			assertEquals(originalRow.getSize(), column.getSize());

			for (int j = 0; j < column.getSize(); j++) {
				assertEquals(originalRow.get(j), column.get(j), MARGIN_OF_ERROR);
			}
		}
	}

	@Test
	public void testAdd() {
		// Create a compatible matrix to add (3x2)
		double[][] data = new double[][] {
				{ 1.0, 2.0 },
				{ 3.0, 4.0 },
				{ 5.0, 6.0 }
		};
		Matrix other = new OriginalMatrix(data);

		transposedMatrix.add(other);

		// Check the original matrix was updated correctly
		assertEquals(2.0, originalMatrix.get(0, 0), MARGIN_OF_ERROR);
		assertEquals(5.0, originalMatrix.get(0, 1), MARGIN_OF_ERROR);
		assertEquals(8.0, originalMatrix.get(0, 2), MARGIN_OF_ERROR);
		assertEquals(6.0, originalMatrix.get(1, 0), MARGIN_OF_ERROR);
		assertEquals(9.0, originalMatrix.get(1, 1), MARGIN_OF_ERROR);
		assertEquals(12.0, originalMatrix.get(1, 2), MARGIN_OF_ERROR);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddIncompatibleDimensions() {
		Matrix other = new OriginalMatrix(2, 2);
		transposedMatrix.add(other); // Should throw exception
	}

	@Test
	public void testToString() {
		String result = transposedMatrix.toString();
		assertTrue(result.contains("T"));
	}
}
