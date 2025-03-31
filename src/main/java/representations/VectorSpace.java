package representations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import calculation.RowReducer;
import utils.records.ReductionResult;

public class VectorSpace {
	/* ================================= Fields ================================= */
	private List<MyVector> basis;
	private int dimensionOfVectors;

	// For now the VectorSpace has it's own rowreducer object
	// This might be exchanged for a singleton design pattern in the future
	private RowReducer reducer = new RowReducer();

	/* ============================== Constructors ============================== */
	/*
	 * Constructor for the empty vectorspace
	 */
	public VectorSpace() {
		this.basis = new ArrayList<>();
	}

	public VectorSpace(MyVector... vectors) {
		ArrayList<MyVector> potentialBasis = new ArrayList<>(Arrays.asList(vectors));
		int dimension = potentialBasis.get(0).size();
		if (potentialBasis.stream().anyMatch(v -> v.size() != dimension))
			throw new IllegalArgumentException("All vectors must be of same dimension");
		this.basis = potentialBasis;
		this.dimensionOfVectors = dimension;
	}

	public VectorSpace(Matrix matrix) {
		this.basis = new ArrayList<>();
		matrix.getColumnVectors().stream().forEach(c -> basis.addLast(c));
		this.dimensionOfVectors = matrix.rows();

	}

	/* ================================= Methods ================================ */
	/**
	 * Extends the vector space to contain the vector.
	 * If the vector already is in the span of the basisvectors, it does not add the
	 * vector, and returns false
	 * If it is not contained the vector is added to the basis and the method
	 * returns true
	 * 
	 * @param boolean Whether or not the vector was added to the basis
	 */
	public boolean add(MyVector vector) {
		if (basis.isEmpty()) {
			basis.add(vector);
			this.dimensionOfVectors = vector.size();
			return true;
		}

		if (vector.size() != dimensionOfVectors)
			throw new IllegalArgumentException("Can't add vector of different dimension");

		if (contains(vector))
			return false;

		basis.add(vector);
		return true;
	}

	public boolean contains(MyVector vector) {
		// The empty space should always return false
		if (basis.isEmpty())
			return false;

		Matrix columnSpan = new OriginalMatrix(basis, true);
		ReductionResult result = reducer.reduce(columnSpan);
		Matrix aggregatedOperations = reducer.aggregateOperations(result.operations(), columnSpan.columns());
		List<MyVector> resultRows = result.reducedMatrix().getRowVectors();
		MyVector rightHandVector = aggregatedOperations.multiply(vector);

		/*
		 * We iterate through each row of our augmented matrix
		 * If the rightHandVector has a non zero element, the resultRow can not be all
		 * zeros. Checking the length is valid in this case as any inner product must
		 * abide by positivity
		 */
		for (int i = 0; i < rightHandVector.size(); i++) {
			if (Math.abs(rightHandVector.get(i)) > 1e-10 && Math.abs(resultRows.get(i).length()) < 1e-10) {
				return false;
			}
		}
		return true;

	}
}
