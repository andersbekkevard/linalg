package calculation;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import representations.Matrix;
import representations.OriginalMatrix;
import utils.MatrixBank;

public class MatrixCalculatorTest {

	private MatrixCalculator calculator;

	@Before
	public void setUp() {
		calculator = new MatrixCalculator();
	}

	@Test
	public void testMultiplySquareMatrices() {
		Matrix a = new OriginalMatrix(new double[][] { { 1, 2 }, { 3, 4 } });
		Matrix b = new OriginalMatrix(new double[][] { { 5, 6 }, { 7, 8 } });

		Matrix result = calculator.multiply(a, b);

		assertEquals(2, result.rows());
		assertEquals(2, result.columns());
		assertEquals(19, result.get(0, 0), 0.0001);
		assertEquals(22, result.get(0, 1), 0.0001);
		assertEquals(43, result.get(1, 0), 0.0001);
		assertEquals(50, result.get(1, 1), 0.0001);
	}

	@Test
	public void testMultiplyRectangularMatrices() {
		Matrix a = new OriginalMatrix(new double[][] { { 1, 2, 3 }, { 4, 5, 6 } });
		Matrix b = new OriginalMatrix(new double[][] { { 7, 8 }, { 9, 10 }, { 11, 12 } });

		Matrix result = calculator.multiply(a, b);

		assertEquals(2, result.rows());
		assertEquals(2, result.columns());
		assertEquals(58, result.get(0, 0), 0.0001);
		assertEquals(64, result.get(0, 1), 0.0001);
		assertEquals(139, result.get(1, 0), 0.0001);
		assertEquals(154, result.get(1, 1), 0.0001);
	}

	@Test
	public void testMultiplyWithIdentity() {
		Matrix a = MatrixBank.getNamedMatrix("simple3x3");
		Matrix identity = MatrixBank.identity(3);

		Matrix result = calculator.multiply(a, identity);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				assertEquals(a.get(i, j), result.get(i, j), 0.0001);
			}
		}
	}

	@Test
	public void testMultiplyWithZeros() {
		Matrix a = MatrixBank.getNamedMatrix("simple3x3");
		Matrix zeros = MatrixBank.zeros(3, 3);

		Matrix result = calculator.multiply(a, zeros);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				assertEquals(0, result.get(i, j), 0.0001);
			}
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMultiplyIncompatibleDimensions() {
		Matrix a = new OriginalMatrix(new double[][] { { 1, 2 }, { 3, 4 } });
		Matrix b = new OriginalMatrix(new double[][] { { 5, 6, 7 }, { 8, 9, 10 }, { 11, 12, 13 } });

		calculator.multiply(a, b); // Should throw exception
	}

	@Test
	public void testDeterminant2x2() {
		Matrix m = new OriginalMatrix(new double[][] {
				{ 1, 2 },
				{ 3, 4 }
		});
		double det = calculator.determinant(m);
		assertEquals(-2.0, det, 0.0001);
	}

	@Test
	public void testDeterminant3x3() {
		Matrix m = new OriginalMatrix(new double[][] {
				{ 1, 2, 3 },
				{ 4, 5, 6 },
				{ 7, 8, 0 }
		});
		double det = calculator.determinant(m);
		assertEquals(27.0, det, 0.0001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDeterminantNonSquare() {
		Matrix m = new OriginalMatrix(2, 3);
		calculator.determinant(m);
	}

	@Test
	public void testGetSubmatrix() {
		Matrix original = new OriginalMatrix(new double[][] {
				{ 1, 2, 3 },
				{ 4, 5, 6 },
				{ 7, 8, 9 }
		});

		Matrix sub = calculator.getSubmatrix(0, 0, original);
		assertEquals(2, sub.rows());
		assertEquals(2, sub.columns());
		assertEquals(5, sub.get(0, 0), 0.0001);
		assertEquals(6, sub.get(0, 1), 0.0001);
		assertEquals(8, sub.get(1, 0), 0.0001);
		assertEquals(9, sub.get(1, 1), 0.0001);
	}

}
