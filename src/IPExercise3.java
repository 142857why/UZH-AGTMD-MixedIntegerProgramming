import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import edu.harvard.econcs.jopt.solver.IMIP;
import edu.harvard.econcs.jopt.solver.IMIPResult;
import edu.harvard.econcs.jopt.solver.client.SolverClient;
import edu.harvard.econcs.jopt.solver.mip.*;

public class IPExercise3 {

	/*
	 * You might have to change this variable depending on the location of the
	 * distances.txt file.
	 */
	private static final String PATH_TO_DISTANCES_TXT_FILE = "src/distances.txt";

	private static final int NUMBER_OF_CITIES = 17;

	private String[] cities;
	private double[][] distances;
	private int numberOfCities;
	private IMIPResult result = null;

	private IMIP mip;

	public IPExercise3(String[] cities, double[][] distances) {
		this.cities = cities;
		this.numberOfCities = cities.length;

		if (distances.length != numberOfCities || distances[0].length != numberOfCities) {
			throw new IllegalArgumentException(
					String.format("Matrix dimension should be %s x %s", numberOfCities, numberOfCities));
		}

		this.distances = distances;
		this.createMIP();
	}

	private void createMIP() {
		/*
		 * TODO: Implement MIP.
		 */
		mip = new MIP();
		int n = this.numberOfCities;
		int S = n - 1;
		Variable[][] x = new Variable[n][n];
		Variable[][] y = new Variable[n][n];
		mip.setObjectiveMax(false);
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < n; ++j) {
				x[i][j] = new Variable("x_" + (i+1) + "_" + (j+1), VarType.INT, 0, 1);
				y[i][j] = new Variable("y_" + (i+1) + "_" + (j+1), VarType.INT, 0, Integer.MAX_VALUE);
				mip.add(x[i][j]);
				mip.add(y[i][j]);
				mip.addObjectiveTerm(distances[i][j], x[i][j]);
			}
		}
		for (int j = 0; j < n; ++j) {
			Constraint c_eq1 = new Constraint(CompareType.EQ, 1);
			for (int i = 0; i < n; ++i) {
				c_eq1.addTerm(1, x[i][j]);
			}
			mip.add(c_eq1);
		}

		for (int i = 0; i < n; ++i) {
			Constraint c_eq1 = new Constraint(CompareType.EQ, 1);
			for (int j = 0; j < n; ++j) {
				c_eq1.addTerm(1, x[i][j]);
			}
			mip.add(c_eq1);
		}

		for (int i = 1; i < n; ++i) {
			Constraint c_flow_eq1 = new Constraint(CompareType.EQ, 1);

			for (int j = 0; j < n; ++j) {
				if (j == i) continue;
				c_flow_eq1.addTerm(1, y[i][j]);
				Constraint c_flow_upperbound = new Constraint(CompareType.LEQ, 0);
				c_flow_upperbound.addTerm(1, y[i][j]);
				c_flow_upperbound.addTerm(-S, x[i][j]);
				mip.add(c_flow_upperbound);
			}
			for (int j = 1; j < n; ++j) {
				if (j == i) continue;
				c_flow_eq1.addTerm(-1, y[j][i]);
			}
			mip.add(c_flow_eq1);
		}
//		System.out.println(mip);
	}

	private void solve() {
		SolverClient solverClient = new SolverClient();
		result = solverClient.solve(mip);
//		System.out.println(result);
	}

	public String toString() {
		if (result == null) {
			return "Unsolved TSP for cities: " + cities;
		}

		String returnString = "TSP solved in " + result.getSolveTime() / 1000 + " seconds:\n";

		// TODO: Print your result as illustrated in example_output.txt. (From City X to City Y...)
		Map<String, Double> entry = result.getValues();
		int[] nextCities = new int[this.numberOfCities+1]; // this array stores the index of the next cities
		for (String s : entry.keySet()) {
			if (s.charAt(0) == 'y') continue;
			if (entry.get(s) == 1.0) {
				String[] s_list = s.split("_");
				nextCities[Integer.parseInt(s_list[1])] = Integer.parseInt(s_list[2]);
			}
		}

		int startCityIndex = 1, endCityIndex = -1;
		while (endCityIndex != 1) {
			endCityIndex = nextCities[startCityIndex];
			returnString += "From City " + startCityIndex + " to City " + endCityIndex + "\n";
			startCityIndex = endCityIndex;
		}

		returnString += "Total distance: " + result.getObjectiveValue() + "\n";
		return returnString;
	}

	/**
	 * Method to run your code and store solution in text file. Do not modify.
	 */
	public static void main(String args[]) throws IOException {
		String[] cities = createCities();
		double[][] distances = loadDistancesFromFile(PATH_TO_DISTANCES_TXT_FILE);

		IPExercise3 exercise = new IPExercise3(cities, distances);
		exercise.solve();

		File file = new File("output.txt");

		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(exercise.toString());
		bw.close();
	}

	/**
	 * Method to read distances from text file. Do not modify.
	 */
	private static double[][] loadDistancesFromFile(String fileName) {
		double[][] distances = new double[NUMBER_OF_CITIES][NUMBER_OF_CITIES];
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String sCurrentLine = null;
			int currentRow = 0;
			while ((sCurrentLine = reader.readLine()) != null) {
				String[] numbersInLine = sCurrentLine.split(" +");
				int currentColumn = 0;
				for (String number : numbersInLine) {
					if (number.matches("-?\\d+(\\.\\d+)?")) {
						distances[currentRow][currentColumn] = Double.valueOf(number);
						currentColumn++;
					}
				}
				currentRow++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return distances;
	}

	/**
	 * Method to create cities of the instance. Do not modify.
	 */
	private static String[] createCities() {
		String[] cities = new String[NUMBER_OF_CITIES];
		for (int i = 1; i <= NUMBER_OF_CITIES; i++) {
			cities[i - 1] = "City " + String.valueOf(i);
		}
		return cities;
	}

}