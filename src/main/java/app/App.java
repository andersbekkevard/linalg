package app;

import representations.Matrix;
import representations.OriginalMatrix;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("=== MATRIX TEST RUNNER ===");

		// Create a 2x3 matrix
		double[][] dataA = {
				{ 1.0, 2.0, 3.0 },
				{ 4.0, 5.0, 6.0 }
		};
		Matrix A = new OriginalMatrix(dataA);
		System.out.println("Original matrix A:");
		printMatrix(A);

		// Transpose A
		Matrix A_T = A.transpose();
		System.out.println("Transposed A:");
		printMatrix(A_T);

		// Scale A by 2
		A.scale(2.0);
		System.out.println("Scaled A by 2:");
		printMatrix(A);

		// Create another matrix B (same size as A)
		double[][] dataB = {
				{ 6.0, 5.0, 4.0 },
				{ 3.0, 2.0, 1.0 }
		};
		Matrix B = new OriginalMatrix(dataB);
		System.out.println("Matrix B:");
		printMatrix(B);

		// Add B to A
		A.add(B);
		System.out.println("A + B:");
		printMatrix(A);

		// Subtract B from A
		A.subtract(B);
		System.out.println("A - B (should match scaled A):");
		printMatrix(A);

		// Check get, rows, columns
		System.out.println("A[0][2] = " + A.get(0, 2));
		System.out.println("A has " + A.rows() + " rows and " + A.columns() + " columns");

		// Transpose B and add to A_T
		Matrix B_T = B.transpose();
		System.out.println("Transposed B:");
		printMatrix(B_T);

		System.out.println("Adding B_T to A_T (should mutate A):");
		A_T.add(B_T); // This mutates A since A_T is a view
		printMatrix(A);

		// Verify double transpose returns original
		Matrix A_TT = A_T.transpose();
		System.out.println("A_TT (should be A again):");
		printMatrix(A_TT);

		System.out.println("All tests ran successfully (manually check the output above).");
	}

	private static void printMatrix(Matrix m) {
		for (int i = 0; i < m.rows(); i++) {
			for (int j = 0; j < m.columns(); j++) {
				System.out.print(m.get(i, j) + "\t");
			}
			System.out.println();
		}
		System.out.println();
	}
}
