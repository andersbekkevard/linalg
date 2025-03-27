package representations;

public interface Matrix {
	public Matrix transpose();

	public void scale(double c);

	public void add(Matrix m);

	public void subtract(Matrix m);

	public int rows();

	public int columns();

	public double get(int row, int column);

}
