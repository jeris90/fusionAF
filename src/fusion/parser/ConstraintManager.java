package fusion.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.InstanceReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.ModelIterator;

public class ConstraintManager {


	/**
	 * Read the integrity constraint stored in a dimacs file, and returns its models
	 * 
	 * @param cnfFile the dimacs file representing the constraint
	 * @return the models of the constraint
	 */
	public static Vector<int[]> readConstraint(String cnfFile) {
		ISolver solver = SolverFactory.newDefault();
		ModelIterator mi = new ModelIterator(solver);
		solver.setTimeout(3600); // 1 hour timeout
		Reader reader = new InstanceReader(mi);
		Vector<int[]> models = new Vector<int[]>();
		try {
			//boolean unsat = true;
			IProblem problem = reader.parseInstance(cnfFile);
			while (problem.isSatisfiable()) {
				//unsat = false;
				int[] model = problem.model();
				models.add(model);
			}
//			if (unsat) {
//				// do something for unsat case
//			}
		} catch (FileNotFoundException e) {
			System.out.println("File " + cnfFile + " not found.");
			System.exit(1);
		} catch (ParseFormatException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (ContradictionException e) {
			// Nothing to do: the constraint has no model.
		} catch (TimeoutException e) {
			System.out.println("Timeout for reading the constraint.");
		}
		return models;
	}

	/**
	 * Copy the content of a file into another one
	 * 
	 * @param file1 the source file
	 * @param file2 the target file
	 */
	public static void copyOfFile(String file1, String file2) {
		FileInputStream instream = null;
		FileOutputStream outstream = null;
		try {
			File infile = new File(file1);
			File outfile = new File(file2);
			instream = new FileInputStream(infile);
			outstream = new FileOutputStream(outfile);
			byte[] buffer = new byte[1024];
			int length;

			while ((length = instream.read(buffer)) > 0) {
				outstream.write(buffer, 0, length);
			}
			// Closing the input/output file streams
			instream.close();
			outstream.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Method that allows to obtain one model of a constraint
	 * 
	 * @param cnfFile the dimacs file that represents the constraint
	 * @return one model of the constraint
	 * @throws IOException
	 */
	public static int[] findModel(String cnfFile) throws IOException {
		int[] solution = {};
		ISolver solver = SolverFactory.newDefault();
		solver.setTimeout(3600); // 1 hour timeout
		Reader reader = new DimacsReader(solver);
		try {
			IProblem problem = reader.parseInstance(cnfFile);
			if (problem.isSatisfiable()) {
				// System.out.println("Satisfiable !");
				solution = problem.findModel();

				// Complete the model if needed
				if (solution.length < problem.nVars())
					solution = fillModel(solution, problem.nVars());

			}
			/*
			 * else { System.out.println("Unsatisfiable !"); }
			 */
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		} catch (ParseFormatException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} catch (ContradictionException e) {
			System.out.println("Unsatisfiable!");
		} catch (TimeoutException e) {
			System.out.println("Timeout, sorry!");
		}

		return solution;
	}

	/**
	 * Provides a textual representation of a clause, using the dimacs format.
	 * 
	 * @param clause the clause
	 * @return the dimacs representation of the clause
	 */
	public static String formatCNF(int[] clause) {
		String dimacsClause = "";
		for (int i = 0; i < clause.length; i++) {
			dimacsClause = dimacsClause + String.valueOf(clause[i] * (-1) + " ");
		}

		return dimacsClause + "0\n";
	}

	/**
	 * Adds a clause to a dimacs file
	 * 
	 * @param clause  the clause
	 * @param cnfFile the dimacs file
	 */
	public static void addClause(String clause, String cnfFile) {
		List<String> lines = new ArrayList<String>();
		String line = null;
		int nb_clause;
		try {
			// Reading the file to replace the number of clauses
			File f1 = new File(cnfFile);
			FileReader fr = new FileReader(f1);
			BufferedReader br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				if (line.contains("p cnf")) {
					nb_clause = Integer.parseInt(line.substring(line.indexOf(' ', 6) + 1));
					nb_clause++;
					line = line.substring(0, line.indexOf(' ', 6) + 1);
					line = line.concat(String.valueOf(nb_clause));
				}
				lines.add(line);
			}
			fr.close();
			br.close();
			// Writing the new clause
			FileWriter fw = new FileWriter(f1);
			BufferedWriter bw = new BufferedWriter(fw);
			for (String s : lines) {
				bw.write(s + "\n");
			}

			bw.write(clause);

			bw.flush();

			bw.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/***
	 * Function for completing the clause by adding the missing literals For
	 * instance, with 4 variables, the clause [-1,3] becomes [–1,-2,3,-4].
	 * 
	 * @param clause : the clause to be completed (if necessary)
	 * @param nbVar  : the total number of variables
	 * @return the completed clause
	 */
	public static int[] fillModel(int[] clause, int nbVar) {
		int[] res = new int[nbVar];

		// We instantiate the clause by setting the negation of all literals
		for (int i = 0; i < res.length; i++) {
			res[i] = i + 1;
		}

		// And we add the fixed literals (positive or negative) in the clause
		for (int j = 0; j < clause.length; j++) {
			int litt = Math.abs(clause[j]) - 1;
			res[litt] = clause[j];
		}
		return res;
	}

	/**
	 * Transforms the models of the integrity constraints. The input is a
	 * Vector<int[]>, where each int[] is one model of the constraint. The output is
	 * a Set<Collection<String>>, where each Collection<String> is a model of the
	 * contraint.
	 * 
	 * @param modeles
	 * @return
	 */
	public static Collection<Collection<String>> transformModels(Vector<int[]> modeles) {
		Collection<Collection<String>> formatModels = new HashSet<Collection<String>>();
		String line = "";
		for (int[] mod : modeles) {
			line = intToStr(mod);
			formatModels.add(stringToHashset(line));
		}
		return formatModels;
	}

	public static Collection<Collection<String>> getModels(String cnfFile) throws IOException {
		return transformModels(readConstraint(cnfFile));
	}

	/***
	 * Return the power set of a set of arguments. This method is used if no
	 * integrity constraint is provided.
	 * 
	 * @param setArguments A set of arguments
	 * @return the power set of setArguments
	 */
	public static Collection<Collection<String>> fullModels(Collection<String> setArguments) {
		Collection<Collection<String>> models = new HashSet<Collection<String>>();

		if (setArguments.isEmpty()) {
			models.add(new HashSet<String>());
			return models;
		}
		List<String> list = new ArrayList<String>(setArguments);

		String head = list.get(0);
		Collection<String> rest = new HashSet<String>(list.subList(1, list.size()));
		for (Collection<String> set : fullModels(rest)) {
			Set<String> newSet = new HashSet<String>();
			newSet.add(head);
			newSet.addAll(set);
			models.add(newSet);
			models.add(set);
		}
		return models;
	}

	/**
	 * Transforms one model of the integrity constraint (given as a int[]) into a
	 * textual representation
	 * 
	 * @param vec the model
	 * @return the String representation of the model
	 */
	public static String intToStr(int[] vec) {
		String chaine = "[";
		for (int i = 0; i < vec.length; i++) {
			if (vec[i] > 0) {
				chaine = chaine + "a" + String.valueOf(vec[i]) + ", ";
			}
		}
		return chaine + "]";
	}

	/**
	 * Splits the textual representation of a list of arguments/literals
	 * 
	 * @param line
	 * @return
	 */
	public static List<String> stringToHashset(String line) {
		return Arrays.asList((line.replaceAll("[\\[\\] ]", "").split(",")));
	}

}
