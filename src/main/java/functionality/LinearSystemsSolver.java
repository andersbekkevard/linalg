package functionality;

import java.util.Optional;

import representations.Matrix;
import representations.MyVector;
import utils.records.ReductionResult;

public class LinearSystemsSolver {
	private RowReducer reducer = new RowReducer();

	public boolean isSolvable(Matrix matrix, MyVector vector) {
		if (matrix.columns() != vector.size())
			return false;

		return new VectorSpace(matrix).contains(vector);
	}

	/**
	 * Method to solve systems of linear equations
	 * 
	 * This method currently solves the system two times, both through the
	 * VectorSpace.contains() in the isSolvable method and then one more
	 * time later. This is inefficient, but i haven't bothered fixing it
	 * yet
	 * 
	 * @param matrix that represents the systems coefficients
	 * @param vector that represents what each expression should be equal to
	 * 
	 * @return Optional of a MyVector where the elements are the unknowns values
	 * 
	 */
	public Optional<MyVector> solve(Matrix matrix, MyVector vector) {
		if (matrix.rows() == matrix.columns()) {
			Optional<MyVector> possibleSolution = solveSquareMatrixSystem(matrix, vector);
			if (possibleSolution.isPresent())
				return possibleSolution;
		}

		if (!isSolvable(matrix, vector))
			return Optional.empty();

		ReductionResult result = reducer.reduce(matrix);
		Matrix aggregatedOperations = reducer.aggregateOperations(result.operations());
		return Optional.ofNullable(aggregatedOperations.multiply(vector));
	}

	private Optional<MyVector> solveSquareMatrixSystem(Matrix matrix, MyVector vector) {
		Optional<Matrix> inverse = reducer.inverse(matrix);
		if (inverse.isPresent())
			return Optional.ofNullable(inverse.get().multiply(vector));
		return Optional.empty();
	}
}