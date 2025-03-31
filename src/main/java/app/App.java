package app;

import calculation.MatrixCalculator;
import calculation.RowReducer;
import representations.Matrix;
import utils.MatrixBank;

public class App {
	// TODO

	/*
	 * Implement a solver for systems of linear equations
	 */

	public static void main(String[] args) {
		System.out.print("\033[2J\033[1;1H");
		RowReducer reducer = new RowReducer();
		MatrixCalculator calc = new MatrixCalculator();

		Matrix m = MatrixBank.rectangular(3, 2);
		System.out.println(m);
		System.out.println(m.transposed());

	}
}
