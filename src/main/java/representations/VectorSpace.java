package representations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import calculation.RowReducer;

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
		int dimension = potentialBasis.get(0).getSize();
		if (potentialBasis.stream().anyMatch(v -> v.getSize() != dimension))
			throw new IllegalArgumentException("All vectors must be of same dimension");
		this.dimensionOfVectors = dimension;
	}

	/* ================================= Methods ================================ */
	public boolean add(MyVector vector) {
		if (basis.isEmpty()) {
			basis.add(vector);
			this.dimensionOfVectors = vector.getSize();
			return true;
		}

		if (vector.getSize() != dimensionOfVectors)
			throw new IllegalArgumentException("Can't add vector of different dimension");

		return true;
	}

	public boolean contains(MyVector vector) {
		Matrix columnSpan = new OriginalMatrix(basis, true);
		Matrix reducedMatrix = reducer.reduce(columnSpan).reducedMatrix();

		return true;
	}

}
