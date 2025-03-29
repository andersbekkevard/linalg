package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import representations.Matrix;

public class MatrixBankTest {

	@Test
	public void testIdentity() {
		Matrix identity = MatrixBank.identity(3);

		assertEquals(3, identity.rows());
		assertEquals(3, identity.columns());

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (i == j) {
					assertEquals(1.0, identity.get(i, j), 0.0001);
				} else {
					assertEquals(0.0, identity.get(i, j), 0.0001);
				}
			}
		}
	}

	@Test
	public void testZeros() {
		Matrix zeros = MatrixBank.zeros(2, 4);

		assertEquals(2, zeros.rows());
		assertEquals(4, zeros.columns());

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 4; j++) {
				assertEquals(0.0, zeros.get(i, j), 0.0001);
			}
		}
	}

	@Test
	public void testOnes() {
		Matrix ones = MatrixBank.ones(3, 2);

		assertEquals(3, ones.rows());
		assertEquals(2, ones.columns());

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 2; j++) {
				assertEquals(1.0, ones.get(i, j), 0.0001);
			}
		}
	}

	@Test
	public void testDiagonal() {
		double[] diagonalValues = { 1.0, 2.0, 3.0 };
		Matrix diagonal = MatrixBank.diagonal(diagonalValues);

		assertEquals(3, diagonal.rows());
		assertEquals(3, diagonal.columns());

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (i == j) {
					assertEquals(diagonalValues[i], diagonal.get(i, j), 0.0001);
				} else {
					assertEquals(0.0, diagonal.get(i, j), 0.0001);
				}
			}
		}
	}

	@Test
	public void testInvertible() {
		int size = 3;
		Matrix invertible = MatrixBank.invertible(size);

		assertEquals(size, invertible.rows());
		assertEquals(size, invertible.columns());

		// Check diagonal dominance (indication of invertibility)
		for (int i = 0; i < size; i++) {
			assertTrue(invertible.get(i, i) >= size);
		}
	}

	@Test
	public void testNonInvertible() {
		int size = 3;
		Matrix nonInvertible = MatrixBank.nonInvertible(size);

		assertEquals(size, nonInvertible.rows());
		assertEquals(size, nonInvertible.columns());

		// Check that the last row is a linear combination of the first two
		for (int j = 0; j < size; j++) {
			assertEquals(nonInvertible.get(0, j) * 2 + nonInvertible.get(1, j),
					nonInvertible.get(size - 1, j), 0.0001);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNonInvertibleWithInvalidSize() {
		MatrixBank.nonInvertible(1); // Should throw exception
	}

	@Test
	public void testSymmetric() {
		int size = 4;
		Matrix symmetric = MatrixBank.symmetric(size);

		assertEquals(size, symmetric.rows());
		assertEquals(size, symmetric.columns());

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				assertEquals(symmetric.get(i, j), symmetric.get(j, i), 0.0001);
			}
		}
	}

	@Test
	public void testRectangular() {
		int rows = 2;
		int cols = 3;
		Matrix rectangular = MatrixBank.rectangular(rows, cols);

		assertEquals(rows, rectangular.rows());
		assertEquals(cols, rectangular.columns());

		// Check values
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				assertEquals(i * 10 + j + 1, rectangular.get(i, j), 0.0001);
			}
		}
	}

	@Test
	public void testWithRank() {
		int rows = 4;
		int cols = 3;
		int rank = 2;
		Matrix withRank = MatrixBank.withRank(rows, cols, rank);

		assertEquals(rows, withRank.rows());
		assertEquals(cols, withRank.columns());

		// Check that first rank diagonal elements are non-zero
		for (int i = 0; i < rank; i++) {
			assertNotEquals(0.0, withRank.get(i, i), 0.0001);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithRankInvalidRank() {
		MatrixBank.withRank(3, 4, 5); // Rank can't exceed min dimension
	}

	@Test
	public void testSparse() {
		int rows = 5;
		int cols = 5;
		double density = 0.2;
		Matrix sparse = MatrixBank.sparse(rows, cols, density);

		assertEquals(rows, sparse.rows());
		assertEquals(cols, sparse.columns());

		// Count non-zero elements
		int nonZeroCount = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (sparse.get(i, j) != 0.0) {
					nonZeroCount++;
				}
			}
		}

		assertTrue(nonZeroCount > 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSparseInvalidDensity() {
		MatrixBank.sparse(3, 3, 2.0); // Density > 1.0
	}

	@Test
	public void testRowEchelonForm() {
		int rows = 3;
		int cols = 4;
		Matrix echelon = MatrixBank.rowEchelonForm(rows, cols);

		assertEquals(rows, echelon.rows());
		assertEquals(cols, echelon.columns());

		// Check for staircase pattern
		for (int i = 0; i < Math.min(rows, cols); i++) {
			assertEquals(1.0, echelon.get(i, i), 0.0001);
		}
	}

	@Test
	public void testGetNamedMatrices() {
		// Test simple2x2
		Matrix simple2x2 = MatrixBank.getNamedMatrix("simple2x2");
		assertEquals(2, simple2x2.rows());
		assertEquals(2, simple2x2.columns());
		assertEquals(1.0, simple2x2.get(0, 0), 0.0001);
		assertEquals(4.0, simple2x2.get(1, 1), 0.0001);

		// Test simple3x3
		Matrix simple3x3 = MatrixBank.getNamedMatrix("simple3x3");
		assertEquals(3, simple3x3.rows());
		assertEquals(3, simple3x3.columns());
		assertEquals(1.0, simple3x3.get(0, 0), 0.0001);
		assertEquals(9.0, simple3x3.get(2, 2), 0.0001);

		// Test invertible3x3
		Matrix invertible = MatrixBank.getNamedMatrix("invertible3x3");
		assertEquals(3, invertible.rows());
		assertEquals(3, invertible.columns());
		assertEquals(4.0, invertible.get(0, 0), 0.0001);

		// Test singular3x3
		Matrix singular = MatrixBank.getNamedMatrix("singular3x3");
		assertEquals(3, singular.rows());
		assertEquals(3, singular.columns());

		// Test magic3x3
		Matrix magic = MatrixBank.getNamedMatrix("magic3x3");
		assertEquals(15, magic.get(0, 0) + magic.get(1, 1) + magic.get(2, 2), 0.0001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetNamedMatrixUnknown() {
		MatrixBank.getNamedMatrix("nonExistentMatrix"); // Should throw exception
	}

	@Test
	public void testLinearSystem() {
		int size = 3;
		Matrix[] system = MatrixBank.linearSystem(size);

		assertEquals(2, system.length);
		Matrix A = system[0];
		Matrix b = system[1];

		assertEquals(size, A.rows());
		assertEquals(size, A.columns());
		assertEquals(size, b.rows());
		assertEquals(1, b.columns());
	}
}
