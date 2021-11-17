
import edu.harvard.econcs.jopt.solver.IMIP;
import edu.harvard.econcs.jopt.solver.IMIPResult;
import edu.harvard.econcs.jopt.solver.client.SolverClient;
import edu.harvard.econcs.jopt.solver.mip.MIP;

public class IPExercise2a {

	int[] ITEM_VALUES = new int[] { 2, 4, 6, 3, 5, 7, 5, 8, 5, 6, 3 };
	int[] ITEM_WEIGHTS = new int[] { 5, 3, 8, 3, 2, 4, 5, 5, 1, 5, 6 };

	int CAPACITY_A = 12;
	int CAPACITY_B = 10;

	private IMIP mip;

	public IPExercise2a() {
		this.buildMIP();
	}

	private void buildMIP() {
		mip = new MIP();

		/*
		 * TODO: Implement MIP.
		 */
	}

	public IMIPResult solve() {
		SolverClient solverClient = new SolverClient();
		IMIPResult result = solverClient.solve(mip);
		return result;
	}

	public static void main(String[] argv) {
		IPExercise2a exercise = new IPExercise2a();
		IMIPResult result = exercise.solve();

		if (result != null && result.getObjectiveValue() == 36.0) {
			System.out.println("Congratulations. You obtained the correct objective value.");
		} else {
			System.out.println("The obtained solution is not correct.");
		}
	}
}
