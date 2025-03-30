package representations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import utils.MatrixBank;

public class MatrixTest {
	@Test
	public void testIsIdentityMatrix() {
		// True for identity matrix
		Matrix identity = MatrixBank.identity(3);
		assertTrue(Matrix.isIdentityMatrix(identity));

		// False for non-identity matrix
		Matrix nonIdentity = new OriginalMatrix(new double[][] {
				{ 1, 0, 0 },
				{ 0, 2, 0 },
				{ 0, 0, 1 }
		});
		assertFalse(Matrix.isIdentityMatrix(nonIdentity));

		// False for non-square matrix
		Matrix nonSquare = new OriginalMatrix(2, 3);
		assertFalse(Matrix.isIdentityMatrix(nonSquare));

		// True for almost-identity (floating point errors)
		Matrix almostIdentity = new OriginalMatrix(new double[][] {
				{ 1.0000001, 0, 0 },
				{ 0, 1, 0 },
				{ 0, 0, 0.9999999 }
		});
		assertTrue(Matrix.isIdentityMatrix(almostIdentity));
	}
}
