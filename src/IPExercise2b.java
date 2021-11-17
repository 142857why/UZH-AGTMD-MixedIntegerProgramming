
import edu.harvard.econcs.jopt.solver.IMIP;
import edu.harvard.econcs.jopt.solver.IMIPResult;
import edu.harvard.econcs.jopt.solver.client.SolverClient;
import edu.harvard.econcs.jopt.solver.mip.*;

public class IPExercise2b {

	int[] ITEM_WEIGHTS = new int[] { 5, 3, 8, 3, 2, 4, 5, 5, 1, 5, 6 };

	int CAPACITY = 10;

	private IMIP mip;

	public IPExercise2b() {
		this.buildMIP();
	}

	public IMIP getMIP() {
		return this.mip;
	}
	private void buildMIP() {
		mip = new MIP();

		// TODO: Implement MIP.
		int n = this.ITEM_WEIGHTS.length;
		Variable[] x = new Variable[n];
		Variable[][] p = new Variable[n][n];
		mip.setObjectiveMax(false);
		for (int i = 0; i < n; ++i) {
			x[i] = new Variable("suitcase_" + i + "_used", VarType.INT, 0, 1);
			mip.add(x[i]);
			mip.addObjectiveTerm(1, x[i]);
			for (int j = 0; j < n; ++j) {
				p[i][j] = new Variable("item_" + i + " in suitcase_" + j, VarType.INT, 0, 1);
				mip.add(p[i][j]);
			}
		}

		for (int i = 0; i < n; ++i) {
			Constraint c_eq1 = new Constraint(CompareType.EQ, 1);
			Constraint c_weight = new Constraint(CompareType.LEQ, 0);
			for (int j = 0; j < n; ++j) {
				c_eq1.addTerm(1, p[i][j]);
				c_weight.addTerm(this.ITEM_WEIGHTS[j], p[j][i]);
			}
			c_weight.addTerm(-this.CAPACITY, x[i]);
			mip.add(c_eq1);
			mip.add(c_weight);
		}
	}

	public IMIPResult solve() {
		SolverClient solverClient = new SolverClient();
		IMIPResult result = solverClient.solve(mip);
		return result;
	}

	public static void main(String[] argv) {
		IPExercise2b exercise = new IPExercise2b();
		IMIPResult result = exercise.solve();
//		System.out.println(exercise.getMIP());
//		System.out.println(result);
		if (result != null && result.getObjectiveValue() == 5.0) {
			System.out.println("Congratulations. You obtained the correct objective value.");
		} else {
			System.out.println("The obtained solution is not correct. ");
		}
	}
}
