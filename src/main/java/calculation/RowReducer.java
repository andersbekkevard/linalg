package calculation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import representations.ElementaryMatrix;
import representations.Matrix;
import representations.MyVector;
import representations.OriginalMatrix;
import utils.MatrixBank;
import utils.records.ReductionResult;

public class RowReducer {
	private final MatrixCalculator calc = new MatrixCalculator();

	/*
	 * Algorithm:
	 * 
	 * 1. Start from the leftmost column and work through until we find the first
	 * pivot element
	 * 2. Move this row up to the top if not there already
	 * 3. Scale it so the first element in the row becomes 1
	 * 4. Subtract the necessary amount from the other rows
	 * 4. Move to the next column and repeat. The first row is now not considered
	 * for pivots, but should still be subtracted if needed
	 * 
	 * 
	 * Returns a ReductionResult record that holds both the reduced form and the
	 * operations
	 */

	/* ============================= Primary methods ============================ */
	public ReductionResult reduce(Matrix matrix) {
		if (Matrix.isIdentityMatrix(matrix))
			return new ReductionResult(matrix, new ArrayList<>());

		List<ElementaryMatrix> operationsPerformed = new ArrayList<>();

		List<MyVector> rowVectors = matrix.getRowVectors();
		int numberOfVectors = rowVectors.size();
		int lengthOfVectors = rowVectors.get(0).getSize();

		for (int c = 0; c < lengthOfVectors; c++) {
			int pivotIndex = -1;
			// We are now working with column number c.
			// We should have c pivots already established and moved up
			for (int r = c; r < numberOfVectors; r++) {
				// Checking if non zero (have to allow for a floating point rounding error)
				if (Math.abs(rowVectors.get(r).get(c)) > 1e-10) {
					pivotIndex = r;
				}
			}

			// If no pivotvector was found we move to the next column
			if (pivotIndex == -1) {
				continue;
			}

			// Else we move our pivotrow up to it's designated spot
			MyVector pivotVector = rowVectors.get(pivotIndex);
			if (pivotIndex != c) {
				rowVectors.set(pivotIndex, rowVectors.get(c));
				rowVectors.set(c, pivotVector);
				operationsPerformed.add(new ElementaryMatrix(numberOfVectors).swapRows(pivotIndex, c));
				pivotIndex = c;
			}

			// Then we scale it so the first element is 1
			double ratio = 1 / pivotVector.get(c);
			pivotVector.scale(ratio);
			operationsPerformed.add(new ElementaryMatrix(numberOfVectors).scaleRow(c, ratio));

			// Lastly we subtract the required amount of pivotVector from the others
			for (int i = 0; i < numberOfVectors; i++) {
				if (i == pivotIndex)
					continue;

				double scalar = rowVectors.get(i).get(c);
				if (Math.abs(scalar) < 1e-10)
					continue;

				rowVectors.get(i).subtract(pivotVector.scaled(scalar));
				operationsPerformed.add(new ElementaryMatrix(numberOfVectors).subtractScaledRow(i, pivotIndex, scalar));
			}
		}
		return new ReductionResult(new OriginalMatrix(rowVectors), operationsPerformed);
	}

	public Optional<Matrix> inverse(Matrix matrix) {
		if (matrix.rows() != matrix.columns())
			throw new IllegalArgumentException("Can only invert square matricies");

		ReductionResult result = reduce(matrix);
		Matrix inverse = aggregateOperations(result.operations());
		return Matrix.isIdentityMatrix(result.reducedMatrix()) ? Optional.ofNullable(inverse) : Optional.empty();

	}

	/* ============================= Helper methods ============================= */
	private Matrix aggregateOperations(List<ElementaryMatrix> operations) {
		if (operations.isEmpty())
			throw new IllegalArgumentException("Cannot aggregate an empty list");

		Matrix aggregateMatrix = MatrixBank.identity(operations.get(0).rows());
		for (ElementaryMatrix e : operations) {
			aggregateMatrix = calc.multiply(e, aggregateMatrix);
		}
		return aggregateMatrix;
	}
}