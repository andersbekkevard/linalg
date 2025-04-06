package calculation;

import representations.MyVector;

public class VectorCalculator {
	public double innerProduct(MyVector u, MyVector v) {
		if (v.size() != u.size())
			throw new IllegalArgumentException("Can't take inner product of vectors of different dimensions");

		double result = 0;
		for (int i = 0; i < v.size(); i++) {
			result += v.get(i) * u.get(i);
		}
		return result;
	}

	/**
	 * Finds the angle between two vectors
	 * If one lives in a higher dimensional vectorspace, the other one is considered
	 * as being in said vectorspace but with other values set to 0
	 * 
	 * @param u
	 * @param v
	 * @return Theta: The angle between u and v
	 */
	public double angleBetween(MyVector u, MyVector v) {
		// cos(theta) * ||u|| * ||v|| = <u, v>
		// ==> theta = arccos(<u, v> / (||u|| * ||v||))
		int sizeOfU = u.size();
		int sizeOfV = v.size();

		if (sizeOfU == sizeOfV) {
			return Math.acos(innerProduct(u, v) / (u.length() * v.length()));
		}

		else if (sizeOfU > sizeOfV) {
			double[] contentsOfNewV = new double[sizeOfU];
			for (int i = 0; i < sizeOfV; i++) {
				contentsOfNewV[i] = v.get(i);
			}
			MyVector newV = new MyVector(contentsOfNewV);
			return Math.acos(innerProduct(u, newV) / (u.length() * newV.length()));
		}

		else {
			double[] contentsOfNewU = new double[sizeOfV];
			for (int i = 0; i < sizeOfU; i++) {
				contentsOfNewU[i] = u.get(i);
			}
			MyVector newU = new MyVector(contentsOfNewU);
			return Math.acos(innerProduct(v, newU) / (v.length() * newU.length()));
		}
	}

	/**
	 * Cross product of three dimensional vectors
	 * Does not transfer incompatible vectors to the desired vectorspace as
	 * angleBetween does
	 * 
	 * @param u
	 * @param v
	 * @return
	 */
	public MyVector crossProduct(MyVector u, MyVector v) {
		if (u.size() != 3 || v.size() != 3) {
			throw new IllegalArgumentException("Can only perform cross product on three diminsional vectors");
		}

		double[] contents = new double[3];
		contents[0] = u.get(1) * v.get(2) - u.get(2) * v.get(1);
		contents[1] = u.get(2) * v.get(0) - u.get(0) * v.get(2);
		contents[2] = u.get(0) * v.get(1) - u.get(1) * v.get(0);
		return new MyVector(contents);
	}

}
