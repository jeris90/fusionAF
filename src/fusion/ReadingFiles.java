package fusion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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

import net.sf.jargsemsat.jargsemsat.datastructures.DungAF;
public abstract class ReadingFiles {

	public static List<String> string_to_hashset(String line) {
		return Arrays.asList((line.replaceAll("[\\[\\] ]", "").split(",")));
	}

	/***
	 * Function for completing the clause by adding the missing literals
	 * For instance, with 4 variables, the clause [-1,3] becomes [–1,-2,3,-4].
	 * @param clause : the clause to be completed (if necessary)
	 * @param nbVar : the total number of variables
	 * @return the completed clause
	 */
	public static int[] full_model(int[] clause, int nbVar) {
		int[] res = new int[nbVar];
		
		// We instantiate the clause by setting the negation of all literals 
		for(int i=0 ; i<res.length ; i++) {
			res[i] = i+1;
		}
		
		// And we add the fixed literals (positive or negative) in the clause
		for(int j=0 ; j<clause.length ; j++) {
			int litt = Math.abs(clause[j]) - 1;
			res[litt] = clause[j];
		}
		return res;
	}	
	
	public static String format_cnf(int[] clause) {
		String chaine = "";
		for (int i = 0; i < clause.length; i++) {
			chaine = chaine + String.valueOf(clause[i] * (-1) + " ");
		}
		//return "\n" + chaine + "0";
		return chaine + "0\n";
	}
	
	//Function for reading the Afs
	public static void readingAF(Vector<String> argument, Vector<String[]> atts, String af_file) {
		argument.clear();
		atts.clear();
		String file_arguments = new String();
		//path to the file to be read
		String link = new String(af_file);
		String[] splited = new String[2];
		Boolean attaque = false;
		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader(link));
			String line = reader.readLine();
			while (line != null) {
				try {
					if (line.equals("#")) {
						attaque = true;
					} else {
						file_arguments = line;
						if (attaque == false) {
							argument.add(file_arguments);
						} else {
							splited = file_arguments.split(" ");
							atts.add(splited);
						}
					}
				} catch (NumberFormatException e) {
					System.err.println("Erreur dans le try : " + line + ".");
				}
				line = reader.readLine();
			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage() + "Excep 2");
		}

	}

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

	public static void addClause(String clause, String cnf_file) {
		List<String> lines = new ArrayList<String>();
		String line = null;
		int nb_clause;
		try {
			// Reading the file to replace the number of clauses
			File f1 = new File(cnf_file);
			FileReader fr = new FileReader(f1);
			BufferedReader br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				if (line.contains("p cnf")) {
					nb_clause = Integer.parseInt(line.substring(line.indexOf(' ', 6)+1));
                    nb_clause++;
                    line = line.substring(0, line.indexOf(' ', 6)+1);
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

	/*
	public static Vector<int[]> readingConstrainte(String cnf_file) throws IOException {
		String cnf_use_file = "Sat_run.txt";
		copyOfFile(cnf_file, cnf_use_file);
		Vector<int[]> modeles = new Vector<>();
		ISolver solver = SolverFactory.newDefault();
		ModelIterator mi = new ModelIterator(solver); // new
		solver.setTimeout(3600); // 1 hour timeout
		Reader reader = new InstanceReader(mi); // new
		
		try {
			boolean unsat = true;
			IProblem problem = reader.parseInstance(cnf_use_file);
			while (problem.isSatisfiable()) {
				unsat = false;
				int [] solution = problem.model();
				for(int i=0 ; i<solution.length ; i++)
					System.out.println("\t" + i + ") " + solution[i]);
			}
			if (unsat)
				System.out.println("UNSAT");
		} catch (FileNotFoundException e) {
			System.out.println("Constrainte File Not Found");
		} catch (ParseFormatException e) {
			System.out.println("Wrong Format on the CNF file ");
		} catch (IOException e) {
			System.out.println("Input/Output Exception");
		} catch (ContradictionException e) {
			System.out.println("Unsatisfiable (trivial)!");
		} catch (TimeoutException e) {
			System.out.println("Timeout, sorry!");
		}
		
		return modeles;
	}
	*/
	
	public static void affiche_tab(int[] tab) {
		System.out.print("\t[");
		for(int i=0 ; i<tab.length ; i++) {
			System.out.print(" " + tab[i]);
		}
		System.out.println("]");
	}
	
	public static int[] find_model(String cnf_file) throws IOException {
		int [] solution = {};
		ISolver solver = SolverFactory.newDefault();
		solver.setTimeout(3600); // 1 hour timeout
		Reader reader = new DimacsReader(solver);
		try {
            IProblem problem = reader.parseInstance(cnf_file);
            if (problem.isSatisfiable()) {
                //System.out.println("Satisfiable !");
                solution = problem.findModel();
                
                // Complete the model if needed
                if(solution.length < problem.nVars())
                	solution = full_model(solution, problem.nVars());
                
            } 
            /*else {
                System.out.println("Unsatisfiable !");
            }*/
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
	
	public static Vector<int[]> readingConstrainte(String cnf_file) throws IOException {
		String cnf_use_file = "Sat_run.txt";
		copyOfFile(cnf_file, cnf_use_file);
		Vector<int[]> modeles = new Vector<>();
		
		int[] solution = find_model(cnf_use_file);
		while(solution.length > 0) {
			modeles.add(solution);
			String clause = format_cnf(solution);
			addClause(clause, cnf_use_file);
			solution = find_model(cnf_use_file);
		}
		
		
		return modeles;
	}
	
	/*
	public static Vector<int[]> readingConstrainte(String cnf_file) throws IOException {
		String cnf_use_file = "Sat_run.txt";
		copyOfFile(cnf_file, cnf_use_file);
		Vector<int[]> modeles = new Vector<>();
		ISolver solver = SolverFactory.newDefault();
		solver.setTimeout(3600); // 1 hour timeout
		Reader reader = new DimacsReader(solver);
		//PrintWriter out = new PrintWriter(System.out, true);
		int unSat = 0;
		try {
			while (unSat == 0) {
				IProblem problem = reader.parseInstance(cnf_use_file);
				if (problem.isSatisfiable()) {
					int[] solution = problem.findModel();
					//reader.decode(problem.model(), out);
					//String clause = format_cnf(solution);
					solution = full_model(solution, problem.nVars());
					modeles.add(solution);
					String clause = format_cnf(solution);
					for(int i=0 ; i<solution.length ; i++)
						System.out.println("\t" + i + ") " + solution[i]);
					System.out.println("[" + clause + "]");
					addClause(clause, cnf_use_file);
				} else {
					unSat = 1;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Constrainte File Not Found");
		} catch (ParseFormatException e) {
			System.out.println("Wrong Format on the CNF file ");
		} catch (IOException e) {
			System.out.println("Input/Output Exception");
		} catch (ContradictionException e) {
			System.out.println("Unsatisfiable (trivial)!");
		} catch (TimeoutException e) {
			System.out.println("Timeout, sorry!");
		}
		
		return modeles;
	}
	*/
	

	public static Collection<Collection<String>> transformeModeles(Vector<int[]> modeles) {
		Collection<Collection<String>> format_modeles = new HashSet<Collection<String>>();
		String line = "";
		for (int[] mod : modeles) {
			line = int_to_str(mod);
			format_modeles.add(string_to_hashset(line));
		}
		return format_modeles;
	}

	public static String int_to_str(int[] vec) {
		String chaine = "[";
		for (int i = 0; i < vec.length; i++) {
			if (vec[i] > 0) {
				chaine = chaine + "a" + String.valueOf(vec[i]) + ", ";
			}
		}
		return chaine + "]";
	}

	// read all afs files in our folder
	public static Vector<DungAF> Lectures(String af_folder) {
		Vector<String> argument = new Vector<String>();
		Vector<String[]> atts = new Vector<String[]>();
		Vector<DungAF> afs = new Vector<DungAF>();
		// Reading Af's files
		File dir = new File(af_folder);
		File[] liste = dir.listFiles();
		for (File item : liste) {
			if (item.isFile()) {
				readingAF(argument, atts, item.getAbsolutePath());
				// Cr&ate de l'af
				DungAF af = new DungAF(argument, atts);
				// add the Af to the set of Afs
				afs.add(af);
			}
		}
		return afs;
	}
	//Retrieve file names 
	public static Vector<String> nameFile(String af_folder) {
		Vector<String> listFile = new Vector<String>();
		String it = new String();
		File dir = new File(af_folder);
		File[] liste = dir.listFiles();
		for (File item : liste) {
			if (item.isFile()) {
				it = item.toString();
				listFile.add(it);
			}

		}
		return listFile;
	}
}
