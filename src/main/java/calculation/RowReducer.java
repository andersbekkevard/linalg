package calculation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import representations.ElementaryMatrix;
import representations.Matrix;
import representations.MyVector;
import representations.OriginalMatrix;

public class RowReducer {
	/*
	 * We try again.
	 * 
	 * Plan:
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
	 */
	public List<ElementaryMatrix> reduce(Matrix matrix) {
		List<ElementaryMatrix> operationsPerformed = new ArrayList<>();

		List<MyVector> rowVectors = matrix.getRowVectors();
		int numberOfVectors = rowVectors.size();
		int lengthOfVectors = rowVectors.get(0).getSize();

		for (int c = 0; c < lengthOfVectors; c++) {
			// We are now working with column number c.
			// We should have c pivots already established and moved up
			int pivotIndex = -1;
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
			}

			// Then we scale it so the first element is 1
			double ratio = 1 / pivotVector.get(c);
			pivotVector.scale(ratio);
			operationsPerformed.add(new ElementaryMatrix(numberOfVectors).scaleRow(c, ratio));

			// Lastly we subtract the required amount of pivotVector from the others
			for (int i = 0; i < numberOfVectors; i++) {
				if (i == c)
					continue;

				double scalar = rowVectors.get(i).get(c);
				if (scalar == 0)
					continue;

				rowVectors.get(i).subtract(pivotVector.scaled(scalar));
				operationsPerformed.add(new ElementaryMatrix(numberOfVectors).subtractRow(i, c));
			}
		}

		matrix = new OriginalMatrix(rowVectors);
		return operationsPerformed;
	}

	public Matrix inverse(Matrix matrix) {
		if (matrix.rows() != matrix.columns())
			throw new IllegalArgumentException("Can only invert square matricies");

		MatrixCalculator calc = new MatrixCalculator();

		// We start off with an identity matrix. We will store all the row operations in
		// it, and return it as the inverse
		int dimension = matrix.rows();
		Matrix identity = new OriginalMatrix(dimension, dimension);
		IntStream.range(0, dimension).forEach(i -> identity.set(i, i, 1));

		List<ElementaryMatrix> operationsPerformed = reduce(matrix);
		Matrix inverse = calc.multiply(identity,
				operationsPerformed.stream().reduce((o1, o2) -> new ElementaryMatrix(calc.multiply(o1, o2)))
						.get());

		// Non invertible matricies aren't handled that sustainably, but this exception
		// can be removed in the future
		// if (!Matrix.isIdentityMatrix(calc.multiply(inverse, matrix)))
		// throw new IllegalArgumentException("Matrix is not invertible");

		return inverse;
	}

	// region graveyard
	/*
	 * This is a horrible method, but it is my first attempt
	 * It reduces invertible matricies, but not very effectively
	 * It is not compatible with rectangular or non invertible matricies
	 * 
	 */
	public Matrix OLD_RECUTION_METHOD(Matrix matrix) {
		List<MyVector> rowVectors = matrix.getRowVectors();
		int numberOfVectors = rowVectors.size();
		int lengthOfVectors = rowVectors.get(0).getSize();
		int[] solvedColumns = new int[lengthOfVectors];
		int iterations = 0;

		for (int i = 0; i < lengthOfVectors; i++) {
			Collections.sort(rowVectors);
			int pivotVectorIndex = -1;
			boolean pivotFound = false;

			// We need to find our first pivot
			// We start at index i, because after i passes with the loops below there are i
			// vectors with pivots we cant reduce further
			for (int j = i; j < numberOfVectors; j++) {
				if (rowVectors.get(j).get(i) != 0 && !pivotFound) {
					pivotVectorIndex = j;
					pivotFound = true;
					break;
				}
			}

			// Loop through the rows untill all are zero
			boolean performingOperations = true;
			while (performingOperations) {
				performingOperations = false;
				for (int j = pivotVectorIndex + 1; j < numberOfVectors; j++) {
					if (rowVectors.get(j).get(i) != 0.0) {
						double ratio = rowVectors.get(j).get(i) / rowVectors.get(pivotVectorIndex).get(i);
						rowVectors.get(j).subtract(rowVectors.get(pivotVectorIndex).scaled(ratio));
						performingOperations = true;
					}
				}
			}
		}
		Collections.sort(rowVectors);
		for (int i = numberOfVectors - 1; i >= 0; i--) {
			if (i == numberOfVectors) {
				rowVectors.get(i).normalize();
			}

			MyVector subtractingVector = rowVectors.get(i);
			int indexForRatioCalculation = IntStream.range(0, lengthOfVectors)
					.filter(index -> subtractingVector.get(index) != 0).findFirst().orElseThrow();
			subtractingVector.normalize();
			for (int j = i - 1; j >= 0; j--) {
				double ratio = rowVectors.get(j).get(indexForRatioCalculation)
						/ subtractingVector.get(indexForRatioCalculation);
				rowVectors.get(j).subtract(subtractingVector.scaled(ratio));
			}
		}
		return new OriginalMatrix(rowVectors);
	}

	// endregion

}