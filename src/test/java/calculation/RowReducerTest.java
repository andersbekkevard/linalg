package calculation;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import representations.ElementaryMatrix;
import representations.Matrix;
import representations.OriginalMatrix;
import utils.MatrixBank;
import utils.records.ReductionResult;

public class RowReducerTest {
	private RowReducer rowReducer;
	private MatrixCalculator calculator;

	@Before
	public void setUp() {
		rowReducer = new RowReducer();
		calculator = new MatrixCalculator();
	}

	@Test
	public void testInstantiation() {
		assertNotNull(rowReducer);
	}

	// Basic tests for reduce() method

	@Test
	public void testReduceIdentityMatrix() {
		Matrix identity = MatrixBank.identity(3);
		ReductionResult result = rowReducer.reduce(identity);

		// Reduced form should still be identity
		assertTrue(Matrix.isIdentityMatrix(result.reducedMatrix()));

		// No operations should be needed for an already reduced matrix
		assertEquals(0, result.operations().size());
	}

	@Test
	public void testReduceMatrix() {
		Matrix matrix = new OriginalMatrix(new double[][] {
				{ 2, 4, 6 },
				{ 1, 3, 5 },
				{ 7, 8, 9 }
		});

		ReductionResult result = rowReducer.reduce(matrix);
		Matrix reduced = result.reducedMatrix();

		// First column should be reduced
		assertEquals(1.0, reduced.get(0, 0), 1e-10);
		assertEquals(0.0, reduced.get(1, 0), 1e-10);
		assertEquals(0.0, reduced.get(2, 0), 1e-10);

		// Operations should not be empty
		assertFalse(result.operations().isEmpty());
	}

	// Tests for rectangular matrices

	@Test
	public void testReduceWideMatrix() {
		// More columns than rows
		Matrix wide = MatrixBank.rectangular(2, 4);
		ReductionResult result = rowReducer.reduce(wide);

		// Check dimensions are preserved
		Matrix reduced = result.reducedMatrix();
		assertEquals(wide.rows(), reduced.rows());
		assertEquals(wide.columns(), reduced.columns());

		// Should have at most rows-many pivots
		int pivots = 0;
		for (int i = 0; i < reduced.rows(); i++) {
			for (int j = 0; j < reduced.columns(); j++) {
				if (Math.abs(reduced.get(i, j) - 1.0) < 1e-10) {
					pivots++;
					break;
				}
			}
		}
		assertTrue(pivots <= reduced.rows());
	}

	@Test
	public void testReduceTallMatrix() {
		// More rows than columns
		Matrix tall = MatrixBank.rectangular(4, 2);
		ReductionResult result = rowReducer.reduce(tall);

		// Check dimensions are preserved
		Matrix reduced = result.reducedMatrix();
		assertEquals(tall.rows(), reduced.rows());
		assertEquals(tall.columns(), reduced.columns());

		// Should have at most columns-many pivots
		int pivots = 0;
		for (int i = 0; i < reduced.rows(); i++) {
			for (int j = 0; j < reduced.columns(); j++) {
				if (Math.abs(reduced.get(i, j) - 1.0) < 1e-10) {
					pivots++;
					break;
				}
			}
		}
		assertTrue(pivots <= reduced.columns());
	}

	// Edge cases for reduce()

	@Test
	public void testReduce1x1Matrix() {
		Matrix matrix = new OriginalMatrix(new double[][] { { 5 } });
		ReductionResult result = rowReducer.reduce(matrix);

		// Should be normalized to 1
		assertEquals(1.0, result.reducedMatrix().get(0, 0), 1e-10);
	}

	@Test
	public void testReduceMatrixWithAllZeros() {
		Matrix zeros = MatrixBank.zeros(3, 3);
		ReductionResult result = rowReducer.reduce(zeros);

		// All entries should still be zero
		Matrix reduced = result.reducedMatrix();
		for (int i = 0; i < reduced.rows(); i++) {
			for (int j = 0; j < reduced.columns(); j++) {
				assertEquals(0.0, reduced.get(i, j), 1e-10);
			}
		}
	}

	@Test
	public void testReduceMatrixWithZeroRow() {
		Matrix matrix = new OriginalMatrix(new double[][] {
				{ 0, 0, 0 },
				{ 1, 2, 3 },
				{ 4, 5, 6 }
		});

		ReductionResult result = rowReducer.reduce(matrix);

		// Zero row should be moved to the bottom
		Matrix reduced = result.reducedMatrix();
		assertNotEquals(0.0, reduced.get(0, 0), 1e-10);
	}

	@Test
	public void testReduceWithMultipleZeroRows() {
		Matrix matrix = new OriginalMatrix(new double[][] {
				{ 0, 0, 0 },
				{ 0, 0, 0 },
				{ 1, 2, 3 }
		});

		ReductionResult result = rowReducer.reduce(matrix);

		// Check that the non-zero row is moved to the top
		Matrix reduced = result.reducedMatrix();
		assertNotEquals(0.0, reduced.get(0, 0), 1e-10);
		assertEquals(0.0, reduced.get(1, 0), 1e-10);
		assertEquals(0.0, reduced.get(1, 1), 1e-10);
		assertEquals(0.0, reduced.get(1, 2), 1e-10);
		assertEquals(0.0, reduced.get(2, 0), 1e-10);
		assertEquals(0.0, reduced.get(2, 1), 1e-10);
		assertEquals(0.0, reduced.get(2, 2), 1e-10);
	}

	@Test
	public void testReduceNearlySingularMatrix() {
		// Create a nearly singular matrix
		Matrix matrix = new OriginalMatrix(new double[][] {
				{ 1, 2 },
				{ 1.000001, 2.000001 }
		});

		ReductionResult result = rowReducer.reduce(matrix);

		// Check that the result is still meaningful
		Matrix reduced = result.reducedMatrix();
		assertNotNull(reduced);
	}

	@Test
	public void testReduceMatrixWithLargeRange() {
		// Create a matrix with a large range of values
		Matrix matrix = new OriginalMatrix(new double[][] {
				{ 1e-6, 1e6 },
				{ 1e6, 1e-6 }
		});

		ReductionResult result = rowReducer.reduce(matrix);

		// Check that the reduction is still sensible
		Matrix reduced = result.reducedMatrix();
		assertNotNull(reduced);
	}

	@Test
	public void testReduceRankDeficientMatrix() {
		Matrix matrix = MatrixBank.withRank(4, 4, 2);
		ReductionResult result = rowReducer.reduce(matrix);

		// The reduced matrix should have exactly 2 leading ones
		Matrix reduced = result.reducedMatrix();
		int leadingOnes = 0;
		for (int i = 0; i < reduced.rows(); i++) {
			for (int j = 0; j < reduced.columns(); j++) {
				if (Math.abs(reduced.get(i, j) - 1.0) < 1e-10) {
					leadingOnes++;
					break;
				}
			}
		}
		assertEquals(2, leadingOnes);
	}

	// Tests for specific matrices from MatrixBank

	@Test
	public void testReduceMagic3x3() {
		Matrix magic = MatrixBank.getNamedMatrix("magic3x3");
		ReductionResult result = rowReducer.reduce(magic);

		// Check that the result is in row echelon form
		Matrix reduced = result.reducedMatrix();
		for (int i = 0; i < reduced.rows(); i++) {
			assertEquals(1.0, reduced.get(i, i), 1e-10);
			for (int j = 0; j < i; j++) {
				assertEquals(0.0, reduced.get(i, j), 1e-10);
			}
		}
	}

	// Tests for inverse() method

	@Test
	public void testInverseIdentityMatrix() {
		Matrix identity = MatrixBank.identity(3);
		Optional<Matrix> inverse = rowReducer.inverse(identity);

		assertTrue(inverse.isPresent());

		// The inverse of the identity matrix is the identity matrix
		Matrix inverseMatrix = inverse.get();
		assertTrue(Matrix.isIdentityMatrix(inverseMatrix));
	}

	@Test
	public void testInverseInvertibleMatrix() {
		Matrix invertible = MatrixBank.getNamedMatrix("invertible3x3");
		Optional<Matrix> inverse = rowReducer.inverse(invertible);

		assertTrue(inverse.isPresent());

		// Check that A * A^-1 = I
		Matrix product = calculator.multiply(invertible, inverse.get());
		assertTrue(Matrix.isIdentityMatrix(product));
	}

	@Test
	public void testInverseSingularMatrix() {
		Matrix singular = MatrixBank.getNamedMatrix("singular3x3");
		Optional<Matrix> inverse = rowReducer.inverse(singular);

		assertFalse(inverse.isPresent());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInverseNonSquareMatrix() {
		Matrix nonSquare = MatrixBank.rectangular(2, 3);
		rowReducer.inverse(nonSquare);
	}

	// Edge cases for inverse()

	@Test
	public void testInverse1x1Matrix() {
		Matrix matrix = new OriginalMatrix(new double[][] { { 2 } });
		Optional<Matrix> inverse = rowReducer.inverse(matrix);

		assertTrue(inverse.isPresent());
		assertEquals(0.5, inverse.get().get(0, 0), 1e-10);
	}

	@Test
	public void testInverseDiagonalMatrix() {
		Matrix diagonal = MatrixBank.diagonal(2, 3, 4);
		Optional<Matrix> inverse = rowReducer.inverse(diagonal);

		assertTrue(inverse.isPresent());
		assertEquals(0.5, inverse.get().get(0, 0), 1e-10);
		assertEquals(1.0 / 3.0, inverse.get().get(1, 1), 1e-10);
		assertEquals(0.25, inverse.get().get(2, 2), 1e-10);
	}

	@Test
	public void testInverseNearlySingularMatrix() {
		// Create a nearly singular matrix
		Matrix matrix = new OriginalMatrix(new double[][] {
				{ 1, 2 },
				{ 1.000001, 2.000001 }
		});

		Optional<Matrix> inverse = rowReducer.inverse(matrix);

		// It might or might not return an inverse, depending on numerical tolerance
		if (inverse.isPresent()) {
			// If an inverse is returned, check that A * A^-1 is approximately identity
			Matrix product = calculator.multiply(matrix, inverse.get());

			for (int i = 0; i < matrix.rows(); i++) {
				for (int j = 0; j < matrix.columns(); j++) {
					if (i == j) {
						assertTrue(Math.abs(product.get(i, j) - 1.0) < 1e-5);
					} else {
						assertTrue(Math.abs(product.get(i, j)) < 1e-5);
					}
				}
			}
		}
	}

	@Test
	public void testInverseMatrixWithLargeRange() {
		// Create a matrix with a large range of values
		Matrix matrix = new OriginalMatrix(new double[][] {
				{ 1e-6, 1e6 },
				{ 1e6, 1e-6 }
		});

		Optional<Matrix> inverse = rowReducer.inverse(matrix);

		assertTrue(inverse.isPresent());

		// Check that A * A^-1 is approximately identity
		Matrix product = calculator.multiply(matrix, inverse.get());
		assertTrue(Matrix.isIdentityMatrix(product));
	}

	@Test
	public void testInverseRankDeficientMatrix() {
		Matrix matrix = MatrixBank.withRank(3, 3, 2);
		Optional<Matrix> inverse = rowReducer.inverse(matrix);

		// Rank deficient square matrices are not invertible
		assertFalse(inverse.isPresent());
	}

	// Test for verifying operations

	@Test
	public void testOperationsCorrectness() {
		Matrix matrix = MatrixBank.getNamedMatrix("simple3x3");
		ReductionResult result = rowReducer.reduce(matrix);

		List<ElementaryMatrix> operations = result.operations();
		Matrix reducedMatrix = result.reducedMatrix();

		// Apply all operations to the original matrix
		Matrix transformed = matrix;
		for (ElementaryMatrix op : operations) {
			transformed = calculator.multiply(op, transformed);
		}

		// The transformed matrix should be close to the reduced matrix
		for (int i = 0; i < matrix.rows(); i++) {
			for (int j = 0; j < matrix.columns(); j++) {
				assertEquals(reducedMatrix.get(i, j), transformed.get(i, j), 1e-10);
			}
		}
	}

	@Test
	public void testInverseCommutative() {
		Matrix matrix = MatrixBank.getNamedMatrix("invertible3x3");
		Optional<Matrix> inverse = rowReducer.inverse(matrix);

		assertTrue(inverse.isPresent());

		// Check that A^-1 * A = I (commutative property)
		Matrix product = calculator.multiply(inverse.get(), matrix);
		assertTrue(Matrix.isIdentityMatrix(product));
	}
}
