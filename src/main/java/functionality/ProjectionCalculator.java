package functionality;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import calculation.MatrixCalculator;
import calculation.VectorCalculator;
import representations.Matrix;
import representations.MyVector;
import representations.OriginalMatrix;
import utils.Utils;

public class ProjectionCalculator {
	private final VectorCalculator vectorCalculator = new VectorCalculator();

	/**
	 * Projects the vectorToProject (first) onto the base vector (second)
	 * 
	 * @param vectorToProject
	 * @param baseVector
	 * @return
	 */
	public MyVector projectOnto(MyVector vectorToProject, MyVector baseVector) {
		if (vectorToProject.size() != baseVector.size())
			throw new IllegalArgumentException("Cannot project onto a vector with different dimension");

		if (Utils.isZero(baseVector.length()))
			return MyVector.zeroVector(vectorToProject.size());

		double scalar = vectorCalculator.innerProduct(vectorToProject, baseVector)
				/ vectorCalculator.innerProduct(baseVector, baseVector);

		return baseVector.scaled(scalar);
	}

	public MyVector projectOnto(MyVector vector, VectorSpace space) {
		if (space.getDimensionOfVectors() != vector.size()) {
			throw new IllegalArgumentException("Vector dimension does not match basis vectors in VectorSpace");
		}

		List<MyVector> orthogonalBasis = orthogonalBasisOf(space);

		List<MyVector> projectedResiduals = orthogonalBasis.stream()
				.map(basisVector -> projectOnto(vector, basisVector))
				.collect(Collectors.toCollection(ArrayList::new));

		MyVector projectedVector = new MyVector(vector.size());
		for (MyVector residualVector : projectedResiduals) {
			projectedVector.add(residualVector);
		}
		return projectedVector;
	}

	/**
	 * Method that produces an orthogonal basis of a list of vectors using Graham
	 * Smiths method
	 * 
	 * @param basis
	 * @return
	 */
	public List<MyVector> orthogonalBasisOf(List<MyVector> basis) {
		if (basis.isEmpty())
			return new ArrayList<>();

		List<MyVector> orthogonalBasisVectors = new ArrayList<>();
		orthogonalBasisVectors.add(basis.get(0));

		for (int i = 1; i < basis.size(); i++) {
			MyVector currentWorkingVector = basis.get(i).clone();
			for (MyVector vector : orthogonalBasisVectors) {
				/*
				 * This next line doesn't follow the regular algorithm, as it is normal to
				 * subtract the original vector's projection onto the orthogonal vectors, not
				 * the partially orthogonalized workingvector. However, this must necessarily
				 * give the same result, as the vectors we project onto are orthogonal
				 */
				currentWorkingVector.subtract(projectOnto(currentWorkingVector, vector));
			}
			if (!(Utils.isZero(currentWorkingVector.length())))
				orthogonalBasisVectors.add(currentWorkingVector);
		}
		return orthogonalBasisVectors;
	}

	public List<MyVector> orthogonalBasisOf(VectorSpace vectorSpace) {
		return orthogonalBasisOf(vectorSpace.getBasisVectors());
	}

	public List<MyVector> orthoNormalBasisOf(List<MyVector> basis) {
		List<MyVector> orthogonalBasisVectors = orthogonalBasisOf(basis);
		for (MyVector vector : orthogonalBasisVectors) {
			vector.normalize();
		}
		return orthogonalBasisVectors;
	}

	public List<MyVector> orthoNormalBasisOf(VectorSpace vectorSpace) {
		return orthoNormalBasisOf(vectorSpace.getBasisVectors());
	}

	/**
	 * Leverages the fact that for an orthonormal basis of a vectorspace,
	 * [P_u] = A*AT
	 * 
	 * @param space
	 * @return The matrix representation of the linear transformation that is
	 *         projection onto the space
	 */
	public Matrix projectionMatrix(VectorSpace space) {
		MatrixCalculator matrixCalculator = new MatrixCalculator();
		Matrix orthoNormalMatrix = new OriginalMatrix(orthoNormalBasisOf(space), true);
		return matrixCalculator.multiply(orthoNormalMatrix, orthoNormalMatrix.transposed());
	}

	public MyVector projectOntoUsingProjectionMatrix(MyVector vector, VectorSpace space) {
		return projectionMatrix(space).multiply(vector);
	}
}