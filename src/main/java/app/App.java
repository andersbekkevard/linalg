package app;

import calculation.MatrixCalculator;
import calculation.RowReducer;
import representations.Matrix;
import utils.MatrixBank;

public class App {

	public static void main(String[] args) {
		System.out.print("\033[2J\033[1;1H");
		RowReducer reducer = new RowReducer();
		MatrixCalculator calc = new MatrixCalculator();

		Matrix m = MatrixBank.invertible(3);
		Matrix n = reducer.reduce(m).reducedMatrix();
		System.out.println(m);
		System.out.println(n);
		System.out.println(calc.determinant(m));

	}
}
