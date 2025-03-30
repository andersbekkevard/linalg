package utils.records;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import org.junit.Test;

import representations.ElementaryMatrix;
import representations.Matrix;
import representations.OriginalMatrix;

public class ReductionResultTest {

	@Test
	public void testConstructorAndAccessors() {
		Matrix reducedMatrix = new OriginalMatrix(2, 2);
		List<ElementaryMatrix> operations = new ArrayList<>();
		operations.add(new ElementaryMatrix(2));

		ReductionResult result = new ReductionResult(reducedMatrix, operations);

		assertSame(reducedMatrix, result.reducedMatrix());
		assertSame(operations, result.operations());
		assertEquals(1, result.operations().size());
	}

	@Test
	public void testEquality() {
		Matrix matrix1 = new OriginalMatrix(2, 2);
		List<ElementaryMatrix> operations1 = new ArrayList<>();
		operations1.add(new ElementaryMatrix(2));

		Matrix matrix2 = new OriginalMatrix(2, 2);
		List<ElementaryMatrix> operations2 = new ArrayList<>();
		operations2.add(new ElementaryMatrix(2));

		ReductionResult result1 = new ReductionResult(matrix1, operations1);
		ReductionResult result2 = new ReductionResult(matrix2, operations2);
		ReductionResult result3 = new ReductionResult(matrix1, operations1);

		assertNotEquals(result1, result2);
		assertEquals(result1, result3);
	}
}
