
import edu.harvard.econcs.jopt.solver.IMIP;
import edu.harvard.econcs.jopt.solver.IMIPResult;
import edu.harvard.econcs.jopt.solver.client.SolverClient;
import edu.harvard.econcs.jopt.solver.mip.*;

import java.util.ArrayList;

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
		// TODO: Implement MIP;
		int n = this.ITEM_VALUES.length;
		Variable[] a = new Variable[n];
		Variable[] b = new Variable[n];
		mip.setObjectiveMax(true);
		for (int i = 0; i < n; ++i) {
			a[i] = new Variable("a" + i, VarType.INT, 0, 1);
			b[i] = new Variable("b" + i, VarType.INT, 0, 1);
			mip.add(a[i]);
			mip.add(b[i]);
			mip.addObjectiveTerm(this.ITEM_VALUES[i], a[i]);
			mip.addObjectiveTerm(this.ITEM_VALUES[i], b[i]);
		}
//		ArrayList<Constraint> c = new ArrayList<>();
		Constraint c_a = new Constraint(CompareType.LEQ, this.CAPACITY_A);
		Constraint c_b = new Constraint(CompareType.LEQ, this.CAPACITY_B);
		for (int i = 0; i < n; ++i) {
			c_a.addTerm(this.ITEM_WEIGHTS[i], a[i]);
			c_b.addTerm(this.ITEM_WEIGHTS[i], b[i]);
			Constraint c_leq1 = new Constraint(CompareType.LEQ, 1);
			c_leq1.addTerm(1, a[i]);
			c_leq1.addTerm(1, b[i]);
			mip.add(c_leq1);
		}
		mip.add(c_a);
		mip.add(c_b);

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
