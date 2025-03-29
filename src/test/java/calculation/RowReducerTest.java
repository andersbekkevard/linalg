package calculation;

import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

import utils.MatrixBank;

public class RowReducerTest {

	private RowReducer rowReducer;
	private MatrixBank bank;

	@Before
	public void setUp() {
		bank = new MatrixBank();
	}

	@Test
	public void testInstantiation() {
		assertNotNull(rowReducer);
	}

	// Additional tests will be needed when this class is implemented
}
