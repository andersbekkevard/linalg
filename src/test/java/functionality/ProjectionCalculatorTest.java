package functionality;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import calculation.VectorCalculator;
import representations.Matrix;
import representations.MyVector;

public class ProjectionCalculatorTest {

	private ProjectionCalculator projectionCalculator;
	private VectorCalculator vectorCalculator;
	private final double DELTA = 0.0001;

	@Before
	public void setUp() {
		projectionCalculator = new ProjectionCalculator();
		vectorCalculator = new VectorCalculator();
	}

	// Tests for projectOnto(MyVector, MyVector)

	@Test
	public void testProjectOntoVectorBasic() {
		MyVector vectorToProject = new MyVector(new double[] { 3, 4 });
		MyVector baseVector = new MyVector(new double[] { 1, 0 });

		MyVector projection = projectionCalculator.projectOnto(vectorToProject, baseVector);

		assertEquals(3, projection.get(0), DELTA);
		assertEquals(0, projection.get(1), DELTA);
	}

	@Test
	public void testProjectOntoVectorOrthogonal() {
		MyVector vectorToProject = new MyVector(new double[] { 0, 4 });
		MyVector baseVector = new MyVector(new double[] { 1, 0 });

		MyVector projection = projectionCalculator.projectOnto(vectorToProject, baseVector);

		assertEquals(0, projection.get(0), DELTA);
		assertEquals(0, projection.get(1), DELTA);
	}

	@Test
	public void testProjectOntoVectorParallel() {
		MyVector vectorToProject = new MyVector(new double[] { 3, 0 });
		MyVector baseVector = new MyVector(new double[] { 1, 0 });

		MyVector projection = projectionCalculator.projectOnto(vectorToProject, baseVector);

		assertEquals(3, projection.get(0), DELTA);
		assertEquals(0, projection.get(1), DELTA);
	}

	@Test
	public void testProjectOntoVectorHighDimension() {
		MyVector vectorToProject = new MyVector(new double[] { 1, 2, 3 });
		MyVector baseVector = new MyVector(new double[] { 2, 3, 4 });

		MyVector projection = projectionCalculator.projectOnto(vectorToProject, baseVector);

		// Expected: (20/29) * (2, 3, 4) = (40/29, 60/29, 80/29)
		assertEquals(40.0 / 29.0, projection.get(0), DELTA);
		assertEquals(60.0 / 29.0, projection.get(1), DELTA);
		assertEquals(80.0 / 29.0, projection.get(2), DELTA);
	}

	@Test
	public void testProjectOntoZeroVector() {
		MyVector vectorToProject = new MyVector(new double[] { 3, 4 });
		MyVector baseVector = new MyVector(new double[] { 0, 0 });

		MyVector projection = projectionCalculator.projectOnto(vectorToProject, baseVector);

		assertEquals(0, projection.get(0), DELTA);
		assertEquals(0, projection.get(1), DELTA);
	}

	// Tests for orthogonalBasisOf(List<MyVector>)

	@Test
	public void testOrthogonalBasisOfEmptyList() {
		List<MyVector> basis = new ArrayList<>();

		List<MyVector> orthogonalBasis = projectionCalculator.orthogonalBasisOf(basis);

		assertTrue(orthogonalBasis.isEmpty());
	}

	@Test
	public void testOrthogonalBasisOfSingleVector() {
		List<MyVector> basis = new ArrayList<>();
		basis.add(new MyVector(new double[] { 1, 2, 3 }));

		List<MyVector> orthogonalBasis = projectionCalculator.orthogonalBasisOf(basis);

		assertEquals(1, orthogonalBasis.size());
		assertEquals(1, orthogonalBasis.get(0).get(0), DELTA);
		assertEquals(2, orthogonalBasis.get(0).get(1), DELTA);
		assertEquals(3, orthogonalBasis.get(0).get(2), DELTA);
	}

	@Test
	public void testOrthogonalBasisOfStandardBasis() {
		List<MyVector> basis = new ArrayList<>();
		basis.add(new MyVector(new double[] { 1, 0, 0 }));
		basis.add(new MyVector(new double[] { 0, 1, 0 }));
		basis.add(new MyVector(new double[] { 0, 0, 1 }));

		List<MyVector> orthogonalBasis = projectionCalculator.orthogonalBasisOf(basis);

		assertEquals(3, orthogonalBasis.size());

		// Check if standard basis remains unchanged
		assertEquals(1, orthogonalBasis.get(0).get(0), DELTA);
		assertEquals(0, orthogonalBasis.get(0).get(1), DELTA);
		assertEquals(0, orthogonalBasis.get(0).get(2), DELTA);

		assertEquals(0, orthogonalBasis.get(1).get(0), DELTA);
		assertEquals(1, orthogonalBasis.get(1).get(1), DELTA);
		assertEquals(0, orthogonalBasis.get(1).get(2), DELTA);

		assertEquals(0, orthogonalBasis.get(2).get(0), DELTA);
		assertEquals(0, orthogonalBasis.get(2).get(1), DELTA);
		assertEquals(1, orthogonalBasis.get(2).get(2), DELTA);
	}

	@Test
	public void testOrthogonalBasisOfNonOrthogonalVectors() {
		List<MyVector> basis = new ArrayList<>();
		basis.add(new MyVector(new double[] { 1, 1, 0 }));
		basis.add(new MyVector(new double[] { 1, 0, 1 }));

		List<MyVector> orthogonalBasis = projectionCalculator.orthogonalBasisOf(basis);

		assertEquals(2, orthogonalBasis.size());

		// Check vectors are orthogonal
		double innerProduct = vectorCalculator.innerProduct(orthogonalBasis.get(0), orthogonalBasis.get(1));
		assertEquals(0, innerProduct, DELTA);
	}

	@Test
	public void testOrthogonalBasisOfLinearlyDependentVectors() {
		List<MyVector> basis = new ArrayList<>();
		basis.add(new MyVector(new double[] { 1, 2, 3 }));
		basis.add(new MyVector(new double[] { 2, 4, 6 })); // 2 * first vector

		List<MyVector> orthogonalBasis = projectionCalculator.orthogonalBasisOf(basis);

		// Only one vector should remain
		assertEquals(1, orthogonalBasis.size());
	}

	// Tests for orthoNormalBasisOf(List<MyVector>)

	@Test
	public void testOrthoNormalBasisOfStandardBasis() {
		List<MyVector> basis = new ArrayList<>();
		basis.add(new MyVector(new double[] { 1, 0, 0 }));
		basis.add(new MyVector(new double[] { 0, 1, 0 }));
		basis.add(new MyVector(new double[] { 0, 0, 1 }));

		List<MyVector> orthonormalBasis = projectionCalculator.orthoNormalBasisOf(basis);

		assertEquals(3, orthonormalBasis.size());

		// Check if standard basis remains unchanged
		assertEquals(1, orthonormalBasis.get(0).get(0), DELTA);
		assertEquals(0, orthonormalBasis.get(0).get(1), DELTA);
		assertEquals(0, orthonormalBasis.get(0).get(2), DELTA);

		// Verify all vectors have unit length
		for (MyVector vector : orthonormalBasis) {
			assertEquals(1.0, vector.length(), DELTA);
		}
	}

	@Test
	public void testOrthoNormalBasisOfNonOrthogonalVectors() {
		List<MyVector> basis = new ArrayList<>();
		basis.add(new MyVector(new double[] { 1, 1, 0 }));
		basis.add(new MyVector(new double[] { 1, 0, 1 }));

		List<MyVector> orthonormalBasis = projectionCalculator.orthoNormalBasisOf(basis);

		assertEquals(2, orthonormalBasis.size());

		// Check vectors are orthogonal
		double innerProduct = vectorCalculator.innerProduct(orthonormalBasis.get(0), orthonormalBasis.get(1));
		assertEquals(0, innerProduct, DELTA);

		// Verify all vectors have unit length
		for (MyVector vector : orthonormalBasis) {
			assertEquals(1.0, vector.length(), DELTA);
		}
	}

	// Tests for methods that use VectorSpace

	@Test
	public void testProjectOntoVectorSpace() {
		MyVector vector = new MyVector(new double[] { 1, 1, 1 });

		// Create a VectorSpace with the xy-plane basis
		VectorSpace space = new VectorSpace();
		space.add(new MyVector(new double[] { 1, 0, 0 }));
		space.add(new MyVector(new double[] { 0, 1, 0 }));

		MyVector projection = projectionCalculator.projectOnto(vector, space);

		// Projection should be [1, 1, 0]
		assertEquals(1, projection.get(0), DELTA);
		assertEquals(1, projection.get(1), DELTA);
		assertEquals(0, projection.get(2), DELTA);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testProjectOntoVectorSpaceIncompatibleDimensions() {
		// Vector of dimension 2
		MyVector vector = new MyVector(new double[] { 1, 1 });

		// Space with vectors of dimension 3
		VectorSpace space = new VectorSpace();
		space.add(new MyVector(new double[] { 1, 0, 0 }));

		projectionCalculator.projectOnto(vector, space);
	}

	@Test
	public void testOrthogonalBasisOfVectorSpace() {
		VectorSpace space = new VectorSpace();
		space.add(new MyVector(new double[] { 1, 1, 0 }));
		space.add(new MyVector(new double[] { 1, 0, 1 }));

		List<MyVector> basis = projectionCalculator.orthogonalBasisOf(space);

		assertEquals(2, basis.size());
		assertEquals(0, vectorCalculator.innerProduct(basis.get(0), basis.get(1)), DELTA);
	}

	@Test
	public void testOrthoNormalBasisOfVectorSpace() {
		VectorSpace space = new VectorSpace();
		space.add(new MyVector(new double[] { 2, 0, 0 }));
		space.add(new MyVector(new double[] { 0, 3, 0 }));

		List<MyVector> basis = projectionCalculator.orthoNormalBasisOf(space);

		assertEquals(2, basis.size());
		assertEquals(1.0, basis.get(0).length(), DELTA);
		assertEquals(1.0, basis.get(1).length(), DELTA);
		assertEquals(0, vectorCalculator.innerProduct(basis.get(0), basis.get(1)), DELTA);
	}

	@Test
	public void testProjectionMatrix() {
		VectorSpace space = new VectorSpace();
		space.add(new MyVector(new double[] { 1, 0, 0 }));
		space.add(new MyVector(new double[] { 0, 1, 0 }));

		Matrix projMatrix = projectionCalculator.projectionMatrix(space);

		// For xy-plane, projection matrix should be:
		// [1 0 0]
		// [0 1 0]
		// [0 0 0]
		assertEquals(3, projMatrix.rows());
		assertEquals(3, projMatrix.columns());
		assertEquals(1, projMatrix.get(0, 0), DELTA);
		assertEquals(0, projMatrix.get(0, 1), DELTA);
		assertEquals(0, projMatrix.get(0, 2), DELTA);
		assertEquals(0, projMatrix.get(1, 0), DELTA);
		assertEquals(1, projMatrix.get(1, 1), DELTA);
		assertEquals(0, projMatrix.get(1, 2), DELTA);
		assertEquals(0, projMatrix.get(2, 0), DELTA);
		assertEquals(0, projMatrix.get(2, 1), DELTA);
		assertEquals(0, projMatrix.get(2, 2), DELTA);
	}

	@Test
	public void testProjectOntoUsingProjectionMatrix() {
		VectorSpace space = new VectorSpace();
		space.add(new MyVector(new double[] { 1, 0, 0 }));
		space.add(new MyVector(new double[] { 0, 1, 0 }));

		MyVector vector = new MyVector(new double[] { 1, 1, 1 });

		MyVector projection = projectionCalculator.projectOntoUsingProjectionMatrix(vector, space);

		// Projection should be [1, 1, 0]
		assertEquals(1, projection.get(0), DELTA);
		assertEquals(1, projection.get(1), DELTA);
		assertEquals(0, projection.get(2), DELTA);
	}

	@Test
	public void testProjectionMatrixForFullSpace() {
		VectorSpace space = new VectorSpace();
		space.add(new MyVector(new double[] { 1, 0, 0 }));
		space.add(new MyVector(new double[] { 0, 1, 0 }));
		space.add(new MyVector(new double[] { 0, 0, 1 }));

		Matrix projMatrix = projectionCalculator.projectionMatrix(space);

		// For full space, projection matrix should be identity
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (i == j) {
					assertEquals(1, projMatrix.get(i, j), DELTA);
				} else {
					assertEquals(0, projMatrix.get(i, j), DELTA);
				}
			}
		}
	}
}
