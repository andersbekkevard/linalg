package app;

import java.util.Arrays;

import calculation.MatrixCalculator;
import calculation.RowReducer;
import representations.Matrix;
import utils.MatrixBank;

public class App {

	public static int sumInts(int... ints) {
		return Arrays.stream(ints).sum();
	}

	public static void main(String[] args) {
		System.out.print("\033[2J\033[1;1H");
		MatrixBank bank = new MatrixBank();
		RowReducer reducer = new RowReducer();
		MatrixCalculator calc = new MatrixCalculator();

		Matrix m = bank.invertible(3);
		System.out.println(m);
		Matrix n = reducer.inverse(m);
		reducer.reduce(m);
		System.out.println(m);
		Matrix I = calc.multiply(n, m);
		System.out.println(m);

	}
}
