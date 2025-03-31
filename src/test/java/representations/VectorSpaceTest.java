package representations;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class VectorSpaceTest {

	private VectorSpace emptySpace;
	private MyVector v1, v2, v3, v4, differentDimVector, zeroVector;

	@Before
	public void setUp() {
		emptySpace = new VectorSpace();

		v1 = new MyVector(new double[] { 1, 0, 0 });
		v2 = new MyVector(new double[] { 0, 1, 0 });
		v3 = new MyVector(new double[] { 1, 0, 1 });
		v4 = new MyVector(new double[] { 1, 1, 1 });
		differentDimVector = new MyVector(new double[] { 1, 2 });
		zeroVector = new MyVector(new double[] { 0, 0, 0 });

		Matrix matrix = new OriginalMatrix(Arrays.asList(v1, v2), true);
	}

	@Test
	public void testEmptyConstructor() {
		assertNotNull(emptySpace);
	}

	@Test
	public void testVectorConstructor() {
		VectorSpace space = new VectorSpace(v1, v2, v3);

		space.add(v1);
		space.add(v2);
		space.add(v3);

		assertTrue(space.contains(v1));
		assertTrue(space.contains(v2));
		assertTrue(space.contains(v3));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testVectorConstructorWithDifferentDimensions() {
		new VectorSpace(v1, differentDimVector);
	}

	@Test
	public void testMatrixConstructor() {
		Matrix matrix = new OriginalMatrix(Arrays.asList(v1, v2), true);
		VectorSpace space = new VectorSpace(matrix);

		assertTrue(space.contains(v1));
		assertTrue(space.contains(v2));
	}

	@Test
	public void testAddToEmptySpace() {
		assertTrue(emptySpace.add(v1));
		assertTrue(emptySpace.contains(v1));
	}

	@Test
	public void testAddIndependentVector() {
		VectorSpace space = new VectorSpace();
		space.add(v1);

		assertTrue(space.add(v2));
		assertTrue(space.contains(v2));
	}

	@Test
	public void testAddDependentVector() {
		VectorSpace space = new VectorSpace();
		space.add(v1);
		space.add(v2);
		space.add(v3);

		assertFalse(space.add(v4));
	}

	@Test
	public void testAddZeroVector() {
		VectorSpace space = new VectorSpace();
		space.add(v1);

		assertFalse(space.add(zeroVector));
		assertTrue(space.contains(zeroVector));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddDifferentDimensionVector() {
		VectorSpace space = new VectorSpace();
		space.add(v1);

		space.add(differentDimVector);
	}

	@Test
	public void testContainsInSpan() {
		VectorSpace space = new VectorSpace();
		space.add(v1);
		space.add(v2);
		space.add(v3);

		assertTrue(space.contains(v4));
	}

	@Test
	public void testContainsScaledVector() {
		VectorSpace space = new VectorSpace();
		space.add(v1);

		MyVector scaledV1 = v1.scaled(2);
		assertTrue(space.contains(scaledV1));
	}

	@Test
	public void testNotContainsInSpan() {
		VectorSpace space = new VectorSpace();
		space.add(v1);
		space.add(v2);

		assertFalse(space.contains(v3));
	}

	@Test
	public void testContainsInEmptySpace() {
		assertFalse(emptySpace.contains(v1));
	}

	@Test
	public void testContainsZeroVector() {
		VectorSpace space = new VectorSpace();
		space.add(v1);

		assertTrue(space.contains(zeroVector));
	}

	@Test
	public void testAddingVectorsIncreasesSpan() {
		VectorSpace space = new VectorSpace();
		space.add(v1);
		space.add(v2);

		MyVector v5 = new MyVector(new double[] { 2, 3, 4 });
		assertFalse(space.contains(v5));

		space.add(v3);
		assertTrue(space.contains(v5));
	}

	@Test
	public void testAddingRedundantVectors() {
		VectorSpace space = new VectorSpace();
		space.add(v1);

		MyVector v1Multiple = new MyVector(new double[] { 2, 0, 0 });
		assertFalse(space.add(v1Multiple));
	}

	@Test
	public void testNumericalStability() {
		VectorSpace space = new VectorSpace();
		space.add(v1);
		space.add(v2);

		MyVector almostInSpan = new MyVector(new double[] { 1e-11, 1.0, 0.0 });
		assertTrue(space.contains(almostInSpan));

		MyVector notInSpan = new MyVector(new double[] { 0.1, 1, 40 });
		assertFalse(space.contains(notInSpan));
	}
}
